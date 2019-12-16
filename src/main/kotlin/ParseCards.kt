package parse

import Card
import java.lang.Exception
import java.lang.IllegalArgumentException
import kotlin.contracts.contract
import kotlin.random.Random

val BRACKET_REGEX = "\\{([^}]+)}".toRegex()
const val BASIC_PATTERN = "q: {front}\na: {back}"
val BASIC_REGEX = "q: ([^\\n]+)\\na: ([^\\n]+)".toRegex()
const val BASIC_AND_REVERSED_PATTERN = "qa: {front}\naq: {back}"
val BASIC_AND_REVERSED_REGEX = "qa: ([^\\n]+)\\naq: ([^\\n]+)".toRegex()

fun parseCards(markdown: String, random: Random = Random): List<Card> {
    val slitted = markdown.split("\n\n")
    return slitted.asSequence()
        .map { it.trimStart().trimEnd() }
        .map { paragraph ->
            if (paragraph.startsWith("@")) {
                require(paragraph.contains("\n")) { "Nothing after id in the paragraph $paragraph" }
                val (idLine, cardText) = paragraph.split("\n", limit = 2)
                CardTextWithId(idLine.substringAfter("@"), cardText)
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
                BRACKET_REGEX in cardText -> Card.Cloze(id, cardText.processCloze())
                else -> Card.Text(id, cardText)
            }
        }
        .toList()
}

private data class CardTextWithId(val id: String?, val cardText: String)

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
        ?: throw IllegalArgumentException("Incorrect format of the paragraph: \n$text")