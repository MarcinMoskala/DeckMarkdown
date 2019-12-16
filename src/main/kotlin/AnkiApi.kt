package parse

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.client.HttpClient
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.post
import kotlinx.coroutines.coroutineScope

interface CardsApi {
    suspend fun addNote(note: NoteDataApi): NoteDataApi
    suspend fun updateNoteFields(note: NoteDataApi): NoteDataApi
    suspend fun getNotesInDeck(deckName: String): List<NoteDataApi>
    suspend fun createDeck(name: String)
    suspend fun removeDeck(name: String)
    suspend fun getDecks(): List<String>
}

data class ResultWrapper<T>(val result: T? = null, val error: String? = null)

data class NoteDataApi(
    val noteId: Long = NO_ID, // For creation, API do not care
    val deckName: String,
    val modelName: String,
    val fields: Map<String, String>,
    val tags: List<String> = emptyList()
) {
    val hasId get() = noteId != NO_ID

    companion object {
        const val NO_ID = -1L
    }
}

data class NoteReceiveDataApi(
    val noteId: Long,
    val modelName: String,
    val fields: Map<String, OrderedField>,
    val tags: List<String> = emptyList()
) {
    fun toNoteData(deckName: String): NoteDataApi = NoteDataApi(
        noteId = noteId,
        deckName = deckName,
        modelName = modelName,
        fields = fields.mapValues { it.value.value },
        tags = tags
    )
}

data class OrderedField(
    val value: String,
    val order: Int
)

class AnkiApi : CardsApi {
    private val url = "http://localhost:8765"
    private val client = HttpClient() {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
    }

    override suspend fun getNotesInDeck(deckName: String): List<NoteDataApi> {
        val text = client.post<String>(url) {
            body = "{\"action\": \"findNotes\", \"version\": 6, \"params\": {\"query\": \"deck:$deckName\"}}"
        }
        val res = text.readObject<ResultWrapper<List<String>>>()
        val notesIds = res.result ?: throw Error(res.error)
        val idsAsString = notesIds.joinToString(prefix = "[", postfix = "]", separator = ", ")

        val text2 = client.post<String>(url) {
            body = "{\"action\": \"notesInfo\", \"version\": 6, \"params\": {\"notes\": $idsAsString}}"
        }
        val res2 = text2.readObject<ResultWrapper<List<NoteReceiveDataApi>>>()
        return res2.result
            ?.map { it.toNoteData(deckName) }
            ?: throw Error(res2.error)
    }

    override suspend fun addNote(note: NoteDataApi): NoteDataApi {
        val noteStr = note.toJson()
        val text = client.post<String>(url) {
            val bodyText = "{\"action\": \"addNote\", \"version\": 6, \"params\": {\"note\": $noteStr}}"
            body = bodyText
        }
        val res = text.readObject<ResultWrapper<Long>>()
        val id = res.result ?: throw Error(res.error)
        return note.copy(noteId = id)
    }

    override suspend fun updateNoteFields(note: NoteDataApi): NoteDataApi {
        val fieldsStr = note.fields.toJson()
        val text = client.post<String>(url) {
            val bodyText = "{\"action\": \"updateNoteFields\", \"version\": 6, \"params\": {\"id\": ${note.noteId}, \"fields\": $fieldsStr}}"
            body = bodyText
        }
        val res = text.readObject<ResultWrapper<Any?>>()
        if (res.error != null) throw Error(res.error)
        return note
    }

    override suspend fun createDeck(name: String) {
        val text = client.post<String>(url) {
            body = "{\"action\": \"createDeck\", \"version\": 6, \"params\": {\"deck\": \"$name\"}}"
        }
        val res = text.readObject<ResultWrapper<Long>>()
        if (res.error != null) throw Error(res.error)
    }

    override suspend fun removeDeck(name: String) {
        val text = client.post<String>(url) {
            body =
                "{\"action\": \"deleteDecks\", \"version\": 6, \"params\": {\"decks\": [\"$name\"], \"cardsToo\": true}}"
        }
        val res = text.readObject<ResultWrapper<Long>>()
        if (res.error != null) throw Error(res.error)
    }

    override suspend fun getDecks(): List<String> {
        val text = client.post<String>(url) {
            body = "{\"action\": \"deckNames\", \"version\": 6}"
        }
        val res = text.readObject<ResultWrapper<List<String>>>()
        return res.result ?: throw Error(res.error)
    }
}

suspend fun main() = coroutineScope {
    val api = AnkiApi()
    val deckName = "MyNewDeck"
    api.createDeck(deckName)
    """
This is text {1}

qa: My question
aq: My answer

q: Question 2 
a: Answer 2

And this {text} number is {2}
    """.trimMargin()
        .let { markdown -> parseCards(markdown) }
        .map { it.toApiNote(deckName, "My comment") }
        .filterNotNull()
        .forEach { api.addNote(it) }
}

inline fun <reified T> String.readObject(): T =
    Gson().fromJson<T>(this, object : TypeToken<T>() {}.type) ?: error("Cannot parse $this")

fun Any.toJson(): String = Gson().toJson(this)