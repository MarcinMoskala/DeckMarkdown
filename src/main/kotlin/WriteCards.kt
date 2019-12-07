fun writeCards(cards: List<Card>): String = cards.joinToString(separator = "\n\n") {
    when (it) {
        is Card.Basic -> TODO()
        is Card.BasicAndReverse -> TODO()
        is Card.Cloze -> it.text
        is Card.Text -> TODO()
    }
}