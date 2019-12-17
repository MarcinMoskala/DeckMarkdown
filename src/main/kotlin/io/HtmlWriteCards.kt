package io

import Card

const val HTML_BASIC_PATTERN = "<i>Q:</i> {front}<br><i>A:</i> {back}"
const val HTML_BASIC_AND_REVERSED_PATTERN = "<i>Q/A:</i> {front}<br><i>A/Q:</i> {back}"

fun htmlWriteCards(cards: List<Card>): String = cards.joinToString(separator = "\n") {
    val cardText = when (it) {
        is Card.Basic -> HTML_BASIC_PATTERN
            .replace("{front}", it.front)
            .replace("{back}", it.back)
        is Card.BasicAndReverse -> HTML_BASIC_AND_REVERSED_PATTERN
            .replace("{front}", it.front)
            .replace("{back}", it.back)
        is Card.Cloze -> it.text.replace(CLOZE_REGEX) { "<b>${it.groupValues[2]}</b>" }
        is Card.Text -> it.text
    }
    return@joinToString "<div>$cardText</div>"
}