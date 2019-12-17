package io

import Card

const val BASIC_PATTERN = "q: {front}\na: {back}"
const val BASIC_AND_REVERSED_PATTERN = "qa: {front}\naq: {back}"

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