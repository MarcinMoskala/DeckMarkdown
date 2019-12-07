package parse

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.client.HttpClient
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.post
import kotlinx.coroutines.coroutineScope

interface CardsApi {
    suspend fun addNote(note: NoteApi)
    suspend fun createDeck(name: String)
    suspend fun removeDeck(name: String)
    suspend fun getDecks(): List<String>
}

data class ResultWrapper<T>(val result: T? = null, val error: String? = null)
data class NoteApi(
    val deckName: String,
    val modelName: String,
    val fields: Map<String, String>,
    val tags: List<String> = emptyList()
)

class AnkiApi : CardsApi {
    private val url = "http://localhost:8765"
    private val client = HttpClient() {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
//        install(Logging) {
//            logger = Logger.DEFAULT
//            level = LogLevel.HEADERS
//        }
    }

    override suspend fun addNote(note: NoteApi) {
        val noteStr = note.toJson()
        val text = client.post<String>(url) {
            val bodyText = "{\"action\": \"addNote\", \"version\": 6, \"params\": {\"note\": $noteStr}}"
            body = bodyText
        }
        val res = text.readObject<ResultWrapper<Long>>()
        if (res.error != null) throw Error(res.error)
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
        .let(::parseCards)
        .map { it.toNote(deckName, "My comment") }
        .filterNotNull()
        .forEach { api.addNote(it) }
}

inline fun <reified T> String.readObject(): T =
    Gson().fromJson<T>(this, object : TypeToken<T>() {}.type) ?: error("Cannot parse $this")

fun Any.toJson(): String = Gson().toJson(this)