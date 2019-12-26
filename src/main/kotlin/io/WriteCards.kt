package io

import Note

const val BASIC_PATTERN = "q: {front}\na: {back}"
const val BASIC_AND_REVERSED_PATTERN = "qa: {front}\naq: {back}"
const val LIST_PATTERN_BEGINNING = "L: {question}"
const val Set_PATTERN_BEGINNING = "S: {question}"

fun writeNotes(notes: List<Note>): String = notes.joinToString(separator = "\n\n") {
    val head = if (it.id == null) "" else "@${it.id}\n"
    val noteText = when (it) {
        is Note.Basic -> BASIC_PATTERN
            .replace("{front}", it.front)
            .replace("{back}", it.back)
        is Note.BasicAndReverse -> BASIC_AND_REVERSED_PATTERN
            .replace("{front}", it.front)
            .replace("{back}", it.back)
        is Note.Reminder -> "Reminder: " + it.text
        is Note.ListDeletion -> (if (it.type == Note.ListDeletion.ListType.List) LIST_PATTERN_BEGINNING else Set_PATTERN_BEGINNING)
            .replace("{question}", it.title)
            .plus(it.items.joinToString(separator = "\n", prefix = "\n") {
                if (it.comment.isBlank()) "* ${it.value}" else "* ${it.value}\n${it.comment}"
            })
        is Note.Cloze -> it.text
        is Note.Text -> it.text
    }
    return@joinToString "$head$noteText"
}