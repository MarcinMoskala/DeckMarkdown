package parse

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.ktor.client.HttpClient
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.post
import java.io.File
import java.net.ConnectException
import java.util.*

interface RepositoryApi {
    suspend fun connected(): Boolean
    suspend fun addNote(apiNote: ApiNote): ApiNote
    suspend fun updateNoteFields(apiNote: ApiNote): ApiNote
    suspend fun getNotesInDeck(deckName: String): List<ApiNote>
    suspend fun createDeck(name: String)
    suspend fun removeDeck(name: String)
    suspend fun deleteNotes(ids: List<Long>)
    suspend fun getDecks(): List<String>
    suspend fun getModelsNames(): List<String>
    suspend fun addModel(model: ApiNoteModel)
    suspend fun storeMediaFile(file: File)
}

data class ResultWrapper<T>(val result: T? = null, val error: String? = null)

sealed class ApiNoteOrText
data class Text(val text: String) : ApiNoteOrText()
data class ApiNote(
    val noteId: Long = NO_ID, // For creation, API do not care
    val deckName: String,
    val modelName: String,
    val fields: Map<String, String>,
    val tags: List<String> = emptyList()
) : ApiNoteOrText() {
    val hasId get() = noteId != NO_ID

    companion object {
        const val NO_ID = -1L
    }
}

data class ApiNoteModel(
    val modelName: String,
    val inOrderFields: List<String>,
    val cardTemplates: List<CardTemplate>
)

data class CardTemplate(
    val Front: String,
    val Back: String
)

data class NoteReceiveDataApi(
    val noteId: Long,
    val modelName: String,
    val fields: Map<String, OrderedField>,
    val tags: List<String> = emptyList()
) {
    fun toNoteData(deckName: String): ApiNote = ApiNote(
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

class AnkiApi : RepositoryApi {
    private val url = "http://localhost:8765"
    private val client = HttpClient() {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
    }

    override suspend fun connected(): Boolean = try {
        client.post<Any?>(url)
        true
    } catch (connectException: ConnectException) {
        false
    }

    override suspend fun getNotesInDeck(deckName: String): List<ApiNote> {
        val text = client.post<String>(url) {
            body = """{"action": "findNotes", "version": 6, "params": {"query": "deck:$deckName"}}"""
        }
        val res = text.readObject<ResultWrapper<List<String>>>()
        val notesIds = res.result ?: throw Error(res.error)
        val idsAsString = notesIds.joinToString(prefix = "[", postfix = "]", separator = ", ")

        val text2 = client.post<String>(url) {
            body = """{"action": "notesInfo", "version": 6, "params": {"notes": $idsAsString}}"""
        }
        val res2 = text2.readObject<ResultWrapper<List<NoteReceiveDataApi>>>()
        return res2.result
            ?.map { it.toNoteData(deckName) }
            ?: throw Error("${res2.error} for $deckName")
    }

    override suspend fun addNote(apiNote: ApiNote): ApiNote {
        val noteStr = apiNote.toJson()
        val text = client.post<String>(url) {
            val bodyText = """{"action": "addNote", "version": 6, "params": {"note": $noteStr}}"""
            body = bodyText
        }
        val res = text.readObject<ResultWrapper<Long>>()
        val id = res.result ?: throw Error("${res.error} for $apiNote")
        return apiNote.copy(noteId = id)
    }

    override suspend fun updateNoteFields(apiNote: ApiNote): ApiNote {
        val fieldsStr = apiNote.fields.toJson()
        val text = client.post<String>(url) {
            val bodyText =
                """{"action": "updateNoteFields", "version": 6, "params": { "note": { "id": ${apiNote.noteId}, "fields": $fieldsStr}}}"""
            body = bodyText
        }
        val res = text.readObjectOrNull<ResultWrapper<Any?>>()
        if (res?.error != null) throw Error("${res.error} for $apiNote")
        return apiNote
    }

    override suspend fun createDeck(name: String) {
        val text = client.post<String>(url) {
            body = """{"action": "createDeck", "version": 6, "params": {"deck": "$name"}}"""
        }
        val res = text.readObject<ResultWrapper<Long>>()
        if (res.error != null) throw Error("${res.error} for $name")
    }

    override suspend fun removeDeck(name: String) {
        val text = client.post<String>(url) {
            body =
                """{"action": "deleteDecks", "version": 6, "params": {"decks": ["$name"], "cardsToo": true}}"""
        }
        val res = text.readObject<ResultWrapper<Long>>()
        if (res.error != null) throw Error("${res.error} for $name")
    }

    override suspend fun deleteNotes(ids: List<Long>) {
        val idsAsString = ids.joinToString(prefix = "[", postfix = "]", separator = ", ")
        val text = client.post<String>(url) {
            val bodyText = """{"action": "deleteNotes", "version": 6, "params": {"notes": $idsAsString}}"""
            body = bodyText
        }
        val res = text.readObjectOrNull<ResultWrapper<Any?>>()
        if (res?.error != null) throw Error("${res.error} for $ids")
    }

    override suspend fun getDecks(): List<String> {
        val text = client.post<String>(url) {
            body = """{"action": "deckNames", "version": 6}"""
        }
        val res = text.readObject<ResultWrapper<List<String>>>()
        return res.result ?: throw Error(res.error)
    }

    override suspend fun getModelsNames(): List<String> {
        val text = client.post<String>(url) {
            body = """{"action": "modelNames", "version": 6}"""
        }
        val res = text.readObject<ResultWrapper<List<String>>>()
        return res.result ?: throw Error(res.error)
    }

    override suspend fun addModel(model: ApiNoteModel) {
        val modelStr = model.toJson()
        val text = client.post<String>(url) {
            val bodyText = """{"action": "createModel", "version": 6, "params": $modelStr}"""
            body = bodyText
        }
        val res = text.readObjectOrNull<ResultWrapper<Any?>>()
        if (res?.error != null) throw Error("${res.error} for $model (str $modelStr)")
    }

    override suspend fun storeMediaFile(file: File) {
        val filename = file.name
        val text = client.post<String>(url) {
            val bodyText =
                """{"action": "storeMediaFile", "version": 6, "params": { "filename": "$filename", "data": "${file.readInBase64()}" }}"""
            body = bodyText
        }
        val res = text.readObjectOrNull<ResultWrapper<Any?>>()
        if (res?.error != null) throw Error("${res.error}")
    }
}

fun File.readInBase64(): String {
    val bytes = this.readBytes()
    val base64 = Base64.getEncoder().encodeToString(bytes)
    return base64
}

inline fun <reified T> String.readObjectOrNull(): T? =
    gson.fromJson<T>(this, object : TypeToken<T>() {}.type)

inline fun <reified T> String.readObject(): T =
    gson.fromJson<T>(this, object : TypeToken<T>() {}.type) ?: error("Cannot parse $this")

val gson = GsonBuilder().serializeNulls().create()

fun Any.toJson(): String = Gson().toJson(this)