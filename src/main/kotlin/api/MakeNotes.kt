package parse

import Card
import parse.NoteDataApi.Companion.NO_ID

fun Card.toApiNote(deckName: String, comment: String): NoteDataApi = when (this) {
    is Card.Basic -> NoteDataApi(
        noteId = id ?: NO_ID,
        deckName = deckName,
        modelName = "Basic",
        fields = mapOf("Front" to this.front, "Back" to this.back, "Comment" to comment)
    )
    is Card.BasicAndReverse -> NoteDataApi(
        noteId = id ?: NO_ID,
        deckName = deckName,
        modelName = "Basic (and reversed card)",
        fields = mapOf("Front" to this.front, "Back" to this.back, "Comment" to comment)
    )
    is Card.Cloze -> NoteDataApi(
        noteId = id ?: NO_ID,
        deckName = deckName,
        modelName = "Cloze",
        fields = mapOf("Text" to this.text, "Comment" to comment)
    )
    is Card.Text -> NoteDataApi(
        noteId = id ?: NO_ID,
        deckName = deckName,
        modelName = "Text",
        fields = mapOf("Text" to text, "Comment" to comment)
    )
}

fun NoteDataApi.toApiNote() = when (modelName) {
    "Basic" -> Card.Basic(noteId, fields.getValue("Front").removeMultipleBreaks(), fields.getValue("Back").removeMultipleBreaks())
    "Basic (and reversed card)" -> Card.BasicAndReverse(noteId, fields.getValue("Front").removeMultipleBreaks(), fields.getValue("Back").removeMultipleBreaks())
    "Cloze" -> Card.Cloze(noteId, fields.getValue("Text").removeMultipleBreaks())
    "Text" -> Card.Text(noteId, fields.getValue("Text").removeMultipleBreaks())
    else -> error("Unknown card type $this")
}

fun String.removeMultipleBreaks() = replace("\\n+".toRegex(), "\n")