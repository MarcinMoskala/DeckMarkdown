package write

import Card
import parse.BASIC_AND_REVERSED_PATTERN
import parse.BASIC_PATTERN

fun writeCards(cards: List<Card>): String = cards.joinToString(separator = "\n\n") {
    val head = if(it.id == null) "" else "@${it.id}\n"
    val cardText = when (it) {
        is Card.Basic -> BASIC_PATTERN
            .replace("{front}", it.front)
            .replace("{back}", it.back)
        is Card.BasicAndReverse -> BASIC_AND_REVERSED_PATTERN
            .replace("{front}", it.front)
            .replace("{back}", it.back)
        is Card.Cloze -> it.text
        is Card.Text -> it.text
    }
    return@joinToString "$head$cardText"
}