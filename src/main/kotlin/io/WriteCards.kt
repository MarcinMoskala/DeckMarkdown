package io

import Note

const val BASIC_PATTERN = "q: {front}\na: {back}"
const val BASIC_AND_REVERSED_PATTERN = "qa: {front}\naq: {back}"

fun writeNotes(notes: List<Note>): String = notes.joinToString(separator = "\n\n") {
    val head = if(it.id == null) "" else "@${it.id}\n"
    val noteText = when (it) {
        is Note.Basic -> BASIC_PATTERN
            .replace("{front}", it.front)
            .replace("{back}", it.back)
        is Note.BasicAndReverse -> BASIC_AND_REVERSED_PATTERN
            .replace("{front}", it.front)
            .replace("{back}", it.back)
        is Note.Cloze -> it.text
        is Note.Reminder -> "Reminder: " + it.text
        is Note.Text -> it.text
    }
    return@joinToString "$head$noteText"
}