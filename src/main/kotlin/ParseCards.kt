package parse

import Card
import java.lang.IllegalArgumentException

val BRACKET_REGEX = "\\{\\{([^:]+::[^}]+)}}".toRegex()
const val BASIC_PATTERN = "q: {front}\na: {back}"
val BASIC_REGEX = "q: ([^\\n]+)\\na: ([^\\n]+)".toRegex()
const val BASIC_AND_REVERSED_PATTERN = "qa: {front}\naq: {back}"
val BASIC_AND_REVERSED_REGEX = "qa: ([^\\n]+)\\naq: ([^\\n]+)".toRegex()

fun parseCards(markdown: String): List<Card> {
    val slitted = markdown.split("\n\n")
    return slitted.asSequence()
        .map { it.trimStart().trimEnd() }
        .map { paragraph ->
            if (paragraph.startsWith("@")) {
                require(paragraph.contains("\n")) { "Nothing after id in the paragraph $paragraph" }
                val (idLine, cardText) = paragraph.split("\n", limit = 2)
                CardTextWithId(idLine.substringAfter("@").toLongOrNull(), cardText)
            } else {
                CardTextWithId(null, paragraph)
            }
        }
        .map { (id, cardText) ->
            when {
                BASIC_REGEX in cardText -> {
                    val (question, answer) = parseQA(cardText, BASIC_REGEX)
                    Card.Basic(id, question, answer)
                }
                BASIC_AND_REVERSED_REGEX in cardText -> {
                    val (question, answer) = parseQA(cardText, BASIC_AND_REVERSED_REGEX)
                    Card.BasicAndReverse(id, question, answer)
                }
                BRACKET_REGEX in cardText -> Card.Cloze(id, cardText)
                else -> Card.Text(id, cardText)
            }
        }
        .toList()
}

private data class CardTextWithId(val id: Long?, val cardText: String)

private fun parseQA(text: String, regex: Regex): Pair<String, String> =
    regex
        .matchEntire(text)
        ?.groupValues
        ?.drop(1)
        ?.map { it.trim() }
        ?.let { (f, s) -> f to s }
        ?: throw IllegalArgumentException("Incorrect format of the paragraph: \n$text")