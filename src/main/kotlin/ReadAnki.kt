import io.writeCards
import kotlinx.coroutines.coroutineScope
import parse.AnkiApi
import parse.toApiNote

suspend fun main() = coroutineScope<Unit> {
    val api = AnkiApi()
    val deckName = "Wiedza::Techniczne::GoogleInterview"
    val text = api.getNotesInDeck(deckName)
        .map { it.toApiNote() }
        .let(::writeCards)

    print(text)
}