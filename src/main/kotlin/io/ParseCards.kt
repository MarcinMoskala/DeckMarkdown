package io

import Note
import Note.ListDeletion.ListType

val CLOZE_REGEX = "\\{\\{([^:]+::([^}]+))}}".toRegex()
val STANDALONE_BRACKET_REGEX = "\\{([^}{]+)}".toRegex()

val BASIC_REGEX = "[Qq]:([\\s\\S]+)\\n[Aa]:([\\s\\S]+)".toRegex()
val BASIC_AND_REVERSED_REGEX = "[Qq][Aa]:([\\s\\S]+)\\n[Aa][Qq]:([\\s\\S]+)".toRegex()
val REMINDER_REGEX = "[Rr](eminder)?:([\\s\\S]+)".toRegex()

val LIST_QUESTION_REGEX = "([LlSs]):([^*]+)\\*".toRegex()
val LIST_ITEM_REGEX = "\\*\\s*([^\\n]*)(\\n([^*]*))?".toRegex()

fun parseNotes(markdown: String): List<Note> {
    val slitted = markdown.split("\n\n")
    return slitted.asSequence()
        .map { it.trimStart().trimEnd() }
        .map { paragraph ->
            if (paragraph.startsWith("@")) {
                require(paragraph.contains("\n")) { "Nothing after id in the paragraph $paragraph" }
                val (idLine, notesText) = paragraph.split("\n", limit = 2)
                NotesTextWithId(idLine.substringAfter("@").toLongOrNull(), notesText)
            } else {
                NotesTextWithId(null, paragraph)
            }
        }
        .map { (id, noteText) ->
            when {
                BASIC_REGEX in noteText -> {
                    val (question, answer) = parseQA(noteText, BASIC_REGEX)
                    Note.Basic(id, question, answer)
                }
                BASIC_AND_REVERSED_REGEX in noteText -> {
                    val (question, answer) = parseQA(
                        noteText,
                        BASIC_AND_REVERSED_REGEX
                    )
                    Note.BasicAndReverse(id, question, answer)
                }
                REMINDER_REGEX in noteText -> {
                    Note.Reminder(id, noteText.substringAfter(":").trim())
                }
                LIST_QUESTION_REGEX in noteText -> {
                    val (prefix, question) = checkNotNull(LIST_QUESTION_REGEX.find(noteText))
                        .let { it.groupValues[1] to it.groupValues[2].trim().trimEnd() }

                    val listType = when(prefix.toLowerCase()) {
                        "s" -> ListType.Set
                        else -> ListType.List
                    }

                    val items = LIST_ITEM_REGEX.findAll(noteText)
                        .map {
                            val value = it.groupValues[1].trim().trimEnd()
                            val comment = it.groupValues[3].trim().trimEnd()
                            Note.ListDeletion.Item(value, comment)
                        }
                        .toList()

                    Note.ListDeletion(id, listType, question, items)
                }
                CLOZE_REGEX in noteText || STANDALONE_BRACKET_REGEX in noteText -> Note.Cloze(
                    id,
                    noteText.processToCloze()
                )
                else -> Note.Text(noteText)
            }
        }
        .toList()
}

private data class NotesTextWithId(val id: Long?, val notesText: String)

private fun String.processToCloze(): String {
    if (CLOZE_REGEX in this) return this
    var num = 0
    return this.replace(STANDALONE_BRACKET_REGEX) { matchResult: MatchResult ->
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