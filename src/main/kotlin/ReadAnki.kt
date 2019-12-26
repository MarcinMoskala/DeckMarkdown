import io.writeNotes
import kotlinx.coroutines.coroutineScope

suspend fun main() = coroutineScope<Unit> {
    with(AnkiMarkup()) {
        readNotesFromDeck(deckName = "Wiedza::Baza_wiedzy::Zasady")
            .let(::writeNotes)
            .let(::print)
    }
}