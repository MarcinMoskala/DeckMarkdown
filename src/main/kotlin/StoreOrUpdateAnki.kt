import io.htmlWriteNotes
import io.parseNotes
import io.textWriteNotes
import io.writeNotes
import kotlinx.coroutines.coroutineScope
import parse.*
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

//    val htmlNotesFile = File("notesHtml")

    val files = notesFile.listFiles()!!
    for (file in files) {
        val name = file.name
        val body = file.readText()
        val (notes, processedText) = storeOrUpdateNote(api = api, deckName = name, noteContent = body, comment = "")
        file.writeText(processedText)

//        if(htmlNotesFile.exists() && htmlNotesFile.isDirectory) {
//            writeHtml(htmlNotesFile, name, notes)
//            writeFormattedText(htmlNotesFile, name, notes)
//        }
    }

    print("Done")
}

data class UpdateResult(val notes: List<Note>, val processedText: String)

suspend fun storeOrUpdateNote(api: AnkiApi, deckName: String, noteContent: String, comment: String): UpdateResult {
    api.createDeck(deckName)
    val notes = parseNotes(noteContent)

    val processedNotes = notes
        .map { it.toApiNoteOrText(deckName, comment) }
        .let { apiNoteOrText -> storeOrUpdateCards(api, deckName, apiNoteOrText) }
        .map(ApiNoteOrText::toNote)
        .let(::writeNotes)

    return UpdateResult(notes, processedNotes)
}

suspend fun storeOrUpdateCards(api: AnkiApi, deckName: String, apiNote: List<ApiNoteOrText>): List<ApiNoteOrText> {
    val currentCards = api.getNotesInDeck(deckName)
    val currentIds = currentCards.map { it.noteId }

    val removedCardIds = currentIds - apiNote.filterIsInstance<ApiNote>().map { it.noteId }
    api.deleteNotes(removedCardIds)

    val removedCount = removedCardIds.size
    var addedCount = 0
    var updatedCount = 0
    val newCards = apiNote.map {
        when(it) {
            is ApiNote -> {
                if (it.hasId && it.noteId in currentIds) {
                    updatedCount++
                    api.updateNoteFields(it)
                } else {
                    addedCount++
                    api.addNote(it)
                }
            }
            is Text -> it
        }
    }

    println("In deck $deckName added $addedCount, updated $updatedCount, removed $removedCount")
    return newCards
}

private fun writeHtml(formattedNotesFile: File, name: String, notes: List<Note>) {
    val expectedFile = File("${formattedNotesFile.absolutePath}/$name.html")
    val htmlFile =
        if (expectedFile.exists()) expectedFile
        else Files.createFile(expectedFile.toPath()).toFile()

    val html = htmlWriteNotes(notes)
    htmlFile.writeText(html)
}

private fun writeFormattedText(
    formattedNotesFile: File,
    name: String,
    notes: List<Note>
) {
    val expectedFile = File("${formattedNotesFile.absolutePath}/$name")
    val htmlFile =
        if (expectedFile.exists()) expectedFile
        else Files.createFile(expectedFile.toPath()).toFile()

    val text = textWriteNotes(notes)
    htmlFile.writeText(text)
}