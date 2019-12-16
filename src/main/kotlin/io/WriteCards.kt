package io

import Card

fun writeCards(cards: List<Card>): String = cards.joinToString(separator = "\n\n") {
    val head = if(it.id == null) "" else "@${it.id}\n"
    val cardText = when (it) {
        is Card.Basic -> BASIC_PATTERN
            .replace("{front}", it.front.removeMultipleBreaks())
            .replace("{back}", it.back.removeMultipleBreaks())
        is Card.BasicAndReverse -> BASIC_AND_REVERSED_PATTERN
            .replace("{front}", it.front.removeMultipleBreaks())
            .replace("{back}", it.back.removeMultipleBreaks())
        is Card.Cloze -> it.text
        is Card.Text -> it.text
    }
    return@joinToString "$head$cardText"
}

fun String.removeMultipleBreaks() = replace("\\n+".toRegex(), "\n")