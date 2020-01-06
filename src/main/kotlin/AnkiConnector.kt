import note.DeckParser
import note.DefaultParser
import parse.AnkiApi
import parse.RepositoryApi
import java.io.File

class AnkiConnector(
    private val api: RepositoryApi = AnkiApi(),
    private val parser: DeckParser = DefaultParser
) {
    suspend fun syncFolder(folderName: String) {
        val notesFile = File(folderName)
        require(notesFile.exists())
        require(notesFile.isDirectory)

        syncMedia("$folderName/media")

//    val htmlNotesFile = File("notesHtml")

        notesFile.listFiles()!!.filterNot { it.isDirectory }.forEach { file ->
            val name = file.name
            val body = file.readText().dropMediaFolderPrefix()
            val comment = body.substringBefore("\n")
            val notes = storeOrUpdateNoteText(deckName = name, noteContent = body, comment = comment)
            val text = parser.writeNotes(notes)
            file.writeText(text.addMediaFolderPrefix())

//        if(htmlNotesFile.exists() && htmlNotesFile.isDirectory) {
//            writeHtml(htmlNotesFile, name, notes)
//            writeFormattedText(htmlNotesFile, name, notes)
//        }
        }
    }

    private suspend fun syncMedia(folderName: String) {
        val notesFile = File(folderName)
        if (!notesFile.exists() || !notesFile.isDirectory) return

        notesFile.listFiles()!!.forEach { file ->
            api.storeMediaFile(file)
        }
    }

    suspend fun readNotesFromDeck(deckName: String): List<Note> =
        api.getNotesInDeck(deckName)
            .map(parser::apiNoteToNote)

    suspend fun storeOrUpdateNoteText(deckName: String, noteContent: String, comment: String): List<Note> {
        if (!api.connected()) {
            error("This function requires opened Anki with installed Anki Connect plugin. Details in ReadMe.md")
        }

        api.createDeck(deckName)
        val notes = parser.parseNotes(noteContent)

        if (!api.connected()) {
            error("This function requires opened Anki with installed Anki Connect plugin. Details in ReadMe.md")
        }

        val currentCards = api.getNotesInDeck(deckName)
        val currentIds = currentCards.map { it.noteId }

        val removedCardIds = currentIds - notes.mapNotNull { it.id }
        api.deleteNotes(removedCardIds)

        val removedCount = removedCardIds.size
        var addedCount = 0
        var updatedCount = 0
        val newCards = notes.map(fun(note: Note): Note {
            if (note is Note.Text) return note
            val apiNote = parser.noteToApiNote(note, deckName, comment)
            val newApiNote = if (apiNote.hasId && apiNote.noteId in currentIds) {
                updatedCount++
                api.updateNoteFields(apiNote)
            } else {
                addedCount++
                api.addNote(apiNote)
            }
            return parser.apiNoteToNote(newApiNote)
        })

        println("In deck $deckName added $addedCount, updated $updatedCount, removed $removedCount")
        return newCards
    }
}

/**
 * This is done because all media files needs to be located in "media", but later in Anki
 * they are all by default in the folder containing all media
 */
private fun String.dropMediaFolderPrefix(): String = this.replace("\"media/", "\"")

private fun String.addMediaFolderPrefix(): String = this.replace("<img src=\"([\\w.]*)\"".toRegex()) {
    "<img src=\"media/${it.groupValues[1]}\""
}
