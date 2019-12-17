import io.htmlWriteCards
import io.parseCards
import io.textWriteCards
import io.writeCards
import kotlinx.coroutines.coroutineScope
import parse.AnkiApi
import parse.NoteDataApi
import parse.toApiNote
import java.io.File
import java.nio.file.Files

suspend fun main() = coroutineScope<Unit> {
    val api = AnkiApi()

    if(!api.connected()) {
        print("This function requires opened Anki with installed Anki Connect plugin. Details in ReadMe.md")
        return@coroutineScope
    }

    val notesFile = File("notes")
    check(notesFile.exists())
    check(notesFile.isDirectory)

    val htmlNotesFile = File("notesHtml")

    val files = notesFile.listFiles()!!
    for (file in files) {
        val name = file.name
        val body = file.readText()
        val (cards, processedText) = storeOrUpdateNote(api = api, deckName = name, noteContent = body, comment = "")
        file.writeText(processedText)

        if(htmlNotesFile.exists() && htmlNotesFile.isDirectory) {
            writeHtml(htmlNotesFile, name, cards)
            writeFormattedText(htmlNotesFile, name, cards)
        }
    }

    print("Done")
}

data class UpdateResult(val cards: List<Card>, val processedText: String)

suspend fun storeOrUpdateNote(api: AnkiApi, deckName: String, noteContent: String, comment: String): UpdateResult {
    api.createDeck(deckName)
    val cards = parseCards(noteContent)

    val processedCards = cards
        .map { it.toApiNote(deckName, comment) }
        .let { cards -> storeOrUpdateCards(api, deckName, cards) }
        .map { it.toApiNote() }
        .let(::writeCards)

    return UpdateResult(cards, processedCards)
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

private fun writeHtml(formattedNotesFile: File, name: String, cards: List<Card>) {
    val expectedFile = File("${formattedNotesFile.absolutePath}/$name.html")
    val htmlFile =
        if (expectedFile.exists()) expectedFile
        else Files.createFile(expectedFile.toPath()).toFile()

    val html = htmlWriteCards(cards)
    htmlFile.writeText(html)
}

private fun writeFormattedText(
    formattedNotesFile: File,
    name: String,
    cards: List<Card>
) {
    val expectedFile = File("${formattedNotesFile.absolutePath}/$name")
    val htmlFile =
        if (expectedFile.exists()) expectedFile
        else Files.createFile(expectedFile.toPath()).toFile()

    val text = textWriteCards(cards)
    htmlFile.writeText(text)
}