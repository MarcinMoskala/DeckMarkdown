import io.parseCards
import io.writeCards
import kotlinx.coroutines.coroutineScope
import parse.*
import java.io.File

suspend fun main() = coroutineScope<Unit> {
    val api = AnkiApi()
    val deckName = "AAAA"
    val comment = ""
    api.createDeck(deckName)
//    File("DeckData")
//        .readText()
    """
This is text {{c1::1}}

qa: My question
aq: My answer

q: Question 2
a: Answer 2

And this {{c1::text}} number is {{c2::2}}
    """.trimMargin()
        .let { markdown -> parseCards(markdown) }
        .map { it.toApiNote(deckName, comment) }
        .let { cards -> storeOrUpdateCards(api, deckName, cards) }
        .map { it.toNote() }
        .let(::writeCards)
        .let(::print)
}

suspend fun storeOrUpdateCards(api: AnkiApi, deckName: String, cards: List<NoteDataApi>): List<NoteDataApi> {
    val currentCards = api.getNotesInDeck(deckName)
    val currentNoteIds = currentCards.map { it.noteId }.toMutableList()
    val newCards = cards.map {
        if (it.hasId && it.noteId in currentNoteIds) {
            currentNoteIds -= it.noteId
            api.updateNoteFields(it)
        } else {
            api.addNote(it)
        }
    }
    // Delete cards not present
    api.deleteNotes(currentNoteIds)
    return newCards
}