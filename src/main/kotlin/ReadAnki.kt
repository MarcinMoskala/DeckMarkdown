import io.writeCards
import kotlinx.coroutines.coroutineScope
import parse.AnkiApi
import parse.toNote

suspend fun main() = coroutineScope<Unit> {
    val api = AnkiApi()
    val text = api.getNotesInDeck("Wiedza::Techniczne::GoogleInterview")
        .map { it.toNote() }
        .let(::writeCards)

    print(text)
}