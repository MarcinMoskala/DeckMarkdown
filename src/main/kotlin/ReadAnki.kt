import io.writeNotes
import kotlinx.coroutines.coroutineScope

suspend fun main() = coroutineScope<Unit> {
    with(AnkiConnector()) {
        readNotesFromDeck(deckName = "Wiedza::Biologia::Ciało_instrukcja")
            .let(::writeNotes)
            .let(::print)
    }
}