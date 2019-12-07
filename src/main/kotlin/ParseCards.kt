package parse

import Card
import java.lang.Exception

fun parseCards(markdown: String): List<Card> {
    val slitted = markdown.split("\n\n")
    return slitted.flatMap { paragraph ->
        when {
            paragraph.startsWith("q:") -> {
                val (question, answer) = parseQA(paragraph, "q: ", "a: ")
                listOf(Card.Basic(question, answer))
            }
            paragraph.startsWith("qa:") -> {
                val (question, answer) = parseQA(paragraph, "qa: ", "aq: ")
                listOf(Card.BasicAndReverse(question, answer))
            }
            paragraph.contains(BRACKET_REGEX) -> {
                listOf(Card.Cloze(paragraph.processCloze()))
            }
            else -> emptyList()
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

private val BRACKET_REGEX = "\\{([^}]+)}".toRegex()

private fun parseQA(text: String, qStart: String, aStart: String): Pair<String, String> =
    text.regexGroups("$qStart([^\\n]+)\\n$aStart([^\\n]+)")
        ?.map { it.trim() }
        ?.let { (f, s) -> f to s }
        ?: throw IncorrectFormatException(text)

private fun String.regexGroups(regex: String): List<String>? =
    regex.toRegex()
        .matchEntire(this)
        ?.groupValues
        ?.drop(1)