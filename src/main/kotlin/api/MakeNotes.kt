package parse

import Note
import parse.NoteDataApi.Companion.NO_ID

fun Note.toApiNote(deckName: String, comment: String): NoteDataApi = when (this) {
    is Note.Basic -> NoteDataApi(
        noteId = id ?: NO_ID,
        deckName = deckName,
        modelName = "Basic",
        fields = mapOf("Front" to this.front, "Back" to this.back, "Comment" to comment)
    )
    is Note.BasicAndReverse -> NoteDataApi(
        noteId = id ?: NO_ID,
        deckName = deckName,
        modelName = "Basic (and reversed card)",
        fields = mapOf("Front" to this.front, "Back" to this.back, "Comment" to comment)
    )
    is Note.Cloze -> NoteDataApi(
        noteId = id ?: NO_ID,
        deckName = deckName,
        modelName = "Cloze",
        fields = mapOf("Text" to this.text, "Comment" to comment)
    )
    is Note.Text -> NoteDataApi(
        noteId = id ?: NO_ID,
        deckName = deckName,
        modelName = "Text",
        fields = mapOf("Text" to text, "Comment" to comment)
    )
}

fun NoteDataApi.toApiNote() = when (modelName) {
    "Basic" -> Note.Basic(noteId, fields.getValue("Front").removeMultipleBreaks(), fields.getValue("Back").removeMultipleBreaks())
    "Basic (and reversed card)" -> Note.BasicAndReverse(noteId, fields.getValue("Front").removeMultipleBreaks(), fields.getValue("Back").removeMultipleBreaks())
    "Cloze" -> Note.Cloze(noteId, fields.getValue("Text").removeMultipleBreaks())
    "Text" -> Note.Text(noteId, fields.getValue("Text").removeMultipleBreaks())
    else -> error("Unknown notes type $this")
}

fun String.removeMultipleBreaks() = replace("\\n+".toRegex(), "\n")