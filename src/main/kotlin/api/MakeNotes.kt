package parse

import Note
import parse.ApiNote.Companion.NO_ID
import java.lang.IllegalArgumentException

fun Note.toApiNote(deckName: String, comment: String): ApiNote =
    this.toApiNoteOrText(deckName, comment) as? ApiNote
        ?: throw IllegalArgumentException("Note $this parsed to a text which is not ApiNote")

fun Note.toApiNoteOrText(deckName: String, comment: String): ApiNoteOrText = when (this) {
    is Note.Text -> Text(text)
    is Note.Basic -> ApiNote(
        noteId = id ?: NO_ID,
        deckName = deckName,
        modelName = "Basic",
        fields = mapOf("Front" to this.front, "Back" to this.back, "Comment" to comment)
    )
    is Note.BasicAndReverse -> ApiNote(
        noteId = id ?: NO_ID,
        deckName = deckName,
        modelName = "Basic (and reversed card)",
        fields = mapOf("Front" to this.front, "Back" to this.back, "Comment" to comment)
    )
    is Note.Cloze -> ApiNote(
        noteId = id ?: NO_ID,
        deckName = deckName,
        modelName = "Cloze",
        fields = mapOf("Text" to this.text, "Comment" to comment)
    )
    is Note.Reminder -> ApiNote(
        noteId = id ?: NO_ID,
        deckName = deckName,
        modelName = "Text",
        fields = mapOf("Text" to text, "Comment" to comment)
    )
}

fun ApiNoteOrText.toNote(): Note = when(this) {
    is Text -> Note.Text(this.text)
    is ApiNote -> this.toNote()
}

fun ApiNote.toNote(): Note = when (modelName) {
    "Basic" -> Note.Basic(noteId, fields.getValue("Front").removeMultipleBreaks(), fields.getValue("Back").removeMultipleBreaks())
    "Basic (and reversed card)" -> Note.BasicAndReverse(noteId, fields.getValue("Front").removeMultipleBreaks(), fields.getValue("Back").removeMultipleBreaks())
    "Cloze" -> Note.Cloze(noteId, fields.getValue("Text").removeMultipleBreaks())
    "Text" -> Note.Reminder(noteId, fields.getValue("Text").removeMultipleBreaks())
    else -> error("Unknown notes type $this")
}

fun String.removeMultipleBreaks() = replace("\\n+".toRegex(), "\n")