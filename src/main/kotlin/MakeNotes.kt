package parse

import Card

fun Card.toApiNote(deckName: String, comment: String): NoteDataApi = when (this) {
    is Card.Basic -> NoteDataApi(
        deckName = deckName,
        modelName = "Basic",
        fields = mapOf("Front" to this.front, "Back" to this.back, "Comment" to comment)
    )
    is Card.BasicAndReverse -> NoteDataApi(
        deckName = deckName,
        modelName = "Basic (and reversed card)",
        fields = mapOf("Front" to this.front, "Back" to this.back, "Comment" to comment)
    )
    is Card.Cloze -> NoteDataApi(
        deckName = deckName,
        modelName = "Cloze",
        fields = mapOf("Text" to this.text, "Comment" to comment)
    )
    is Card.Text -> NoteDataApi(
        deckName = deckName,
        modelName = "Text",
        fields = mapOf("Front" to "$text<\\br><\\br>$comment")
    )
}

fun NoteDataApi.toNote() = when (modelName) {
    "Basic" -> Card.Basic(noteId, fields.getValue("Front"), fields.getValue("Back"))
    "Basic (and reversed card)" -> Card.BasicAndReverse(noteId, fields.getValue("Front"), fields.getValue("Back"))
    "Cloze" -> Card.Cloze(noteId, fields.getValue("Text"))
    "Text" -> Card.Text(noteId, fields.getValue("Text"))
    else -> error("Unknown card type $this")
}