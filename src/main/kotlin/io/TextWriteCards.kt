package io

import Note

const val TEXT_BASIC_PATTERN = "Q: {front}\nA: {back}"
const val TEXT_BASIC_AND_REVERSED_PATTERN = "Q/A: {front}\nA/Q: {back}"

fun textWriteNotes(notes: List<Note>): String = notes.joinToString(separator = "\n\n") {
    return@joinToString when (it) {
        is Note.Basic -> TEXT_BASIC_PATTERN
            .replace("{front}", it.front)
            .replace("{back}", it.back)
        is Note.BasicAndReverse -> TEXT_BASIC_AND_REVERSED_PATTERN
            .replace("{front}", it.front)
            .replace("{back}", it.back)
        is Note.Cloze -> it.text.replace(CLOZE_REGEX) { it.groupValues[2] }
        is Note.Text -> it.text
    }
}