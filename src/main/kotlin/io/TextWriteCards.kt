package io

import Card

const val TEXT_BASIC_PATTERN = "Q: {front}\nA: {back}"
const val TEXT_BASIC_AND_REVERSED_PATTERN = "Q/A: {front}\nA/Q: {back}"

fun textWriteCards(cards: List<Card>): String = cards.joinToString(separator = "\n\n") {
    return@joinToString when (it) {
        is Card.Basic -> TEXT_BASIC_PATTERN
            .replace("{front}", it.front)
            .replace("{back}", it.back)
        is Card.BasicAndReverse -> TEXT_BASIC_AND_REVERSED_PATTERN
            .replace("{front}", it.front)
            .replace("{back}", it.back)
        is Card.Cloze -> it.text.replace(CLOZE_REGEX) { it.groupValues[2] }
        is Card.Text -> it.text
    }
}