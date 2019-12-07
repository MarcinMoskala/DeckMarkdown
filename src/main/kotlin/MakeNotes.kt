package parse

import Card

fun Card.toNote(deckName: String, comment: String): NoteApi? = when(this) {
    is Card.Basic -> NoteApi(deckName = deckName, modelName = "Basic", fields = mapOf("Front" to this.front, "Back" to this.back, "Comment" to comment))
    is Card.BasicAndReverse -> NoteApi(deckName = deckName, modelName = "Basic (and reversed card)", fields = mapOf("Front" to this.front, "Back" to this.back, "Comment" to comment))
    is Card.Cloze -> NoteApi(deckName = deckName, modelName = "Cloze", fields = mapOf("Text" to this.text, "Comment" to comment))
    is Card.Text -> null
}