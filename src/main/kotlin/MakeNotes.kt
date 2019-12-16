package parse

import Card

fun Card.toNote(deckName: String, comment: String): NoteDataApi? = when(this) {
    is Card.Basic -> NoteDataApi(deckName = deckName, modelName = "Basic", fields = mapOf("Front" to this.front, "Back" to this.back, "Comment" to comment))
    is Card.BasicAndReverse -> NoteDataApi(deckName = deckName, modelName = "Basic (and reversed card)", fields = mapOf("Front" to this.front, "Back" to this.back, "Comment" to comment))
    is Card.Cloze -> NoteDataApi(deckName = deckName, modelName = "Cloze", fields = mapOf("Text" to this.text, "Comment" to comment))
    is Card.Text -> null
}