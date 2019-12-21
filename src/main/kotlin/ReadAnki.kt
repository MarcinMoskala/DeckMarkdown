import io.writeNotes
import kotlinx.coroutines.coroutineScope
import parse.AnkiApi
import parse.toNote

suspend fun main() = coroutineScope<Unit> {
    val api = AnkiApi()
    val deckName = "Wiedza::Baza_wiedzy::Zasady"
    val text = api.getNotesInDeck(deckName)
        .map { it.toNote() }
        .let(::writeNotes)

    print(text)
}