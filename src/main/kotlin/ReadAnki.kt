import io.writeCards
import kotlinx.coroutines.coroutineScope
import parse.AnkiApi
import parse.toNote

suspend fun main() = coroutineScope<Unit> {
    val api = AnkiApi()
    val deckName = "AAA"
    val text = api.getNotesInDeck(deckName)
        .map { it.toNote() }
        .let(::writeCards)

    print(text)
}