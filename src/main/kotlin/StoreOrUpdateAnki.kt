import io.parseCards
import io.writeCards
import kotlinx.coroutines.coroutineScope
import parse.*
import java.io.File

suspend fun main() = coroutineScope<Unit> {
    val api = AnkiApi()
    val notesFile = File("notes")
    check(notesFile.exists())
    check(notesFile.isDirectory)

    val files = notesFile.listFiles()!!
    for (file in files) {
        val name = file.name
        val body = file.readText()
        val processed = storeOrUpdateNote(api = api, deckName = name, noteContent = body, comment = "")
        file.writeText(processed)
    }

    print("Done")
}

suspend fun storeOrUpdateNote(api: AnkiApi, deckName: String, noteContent: String, comment: String): String {
    api.createDeck(deckName)
    return noteContent
        .let { markdown -> parseCards(markdown) }
        .map { it.toApiNote(deckName, comment) }
        .let { cards -> storeOrUpdateCards(api, deckName, cards) }
        .map { it.toNote() }
        .let(::writeCards)
}

suspend fun storeOrUpdateCards(api: AnkiApi, deckName: String, cards: List<NoteDataApi>): List<NoteDataApi> {
    val currentCards = api.getNotesInDeck(deckName)
    val currentIds = currentCards.map { it.noteId }

    val removedCardIds = currentIds - cards.map { it.noteId }
    api.deleteNotes(removedCardIds)

    val newCards = cards.map {
        if (it.hasId && it.noteId in currentIds) {
            api.updateNoteFields(it)
        } else {
            api.addNote(it)
        }
    }
    return newCards
}