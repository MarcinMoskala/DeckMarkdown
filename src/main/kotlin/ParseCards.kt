package parse

import Card
import java.lang.Exception

val BRACKET_REGEX = "\\{([^}]+)}".toRegex()
const val BASIC_PATTERN = "q: {front}\na: {back}"
val BASIC_REGEX = "q: ([^\\n]+)\\na: ([^\\n]+)".toRegex()
const val BASIC_AND_REVERSED_PATTERN = "qa: {front}\naq: {back}"
val BASIC_AND_REVERSED_REGEX = "qa: ([^\\n]+)\\naq: ([^\\n]+)".toRegex()

fun parseCards(markdown: String): List<Card> {
    val slitted = markdown.split("\n\n")
    return slitted.map { paragraph ->
        when {
            BASIC_REGEX in paragraph -> {
                val (question, answer) = parseQA(paragraph, BASIC_REGEX)
                Card.Basic(question, answer)
            }
            BASIC_AND_REVERSED_REGEX in paragraph -> {
                val (question, answer) = parseQA(paragraph, BASIC_AND_REVERSED_REGEX)
                Card.BasicAndReverse(question, answer)
            }
            BRACKET_REGEX in paragraph -> Card.Cloze(paragraph.processCloze())
            else -> Card.Text(paragraph)
        }
    }
}

class IncorrectFormatException(paragraph: String) : Exception("Incorrect format of the paragraph: \n$paragraph")

private fun String.processCloze(): String {
    var num = 0
    return this.replace(BRACKET_REGEX) { matchResult: MatchResult ->
        val content = matchResult.groupValues[1]
        num++
        "{{c$num::$content}}"
    }
}

private fun parseQA(text: String, regex: Regex): Pair<String, String> =
    regex
        .matchEntire(text)
        ?.groupValues
        ?.drop(1)
        ?.map { it.trim() }
        ?.let { (f, s) -> f to s }
        ?: throw IncorrectFormatException(text)