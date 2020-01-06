package io

import Note

const val HTML_BASIC_PATTERN = "<i>Q:</i> {front}<br><i>A:</i> {back}"
const val HTML_BASIC_AND_REVERSED_PATTERN = "<i>Q/A:</i> {front}<br><i>A/Q:</i> {back}"

fun htmlWriteNotes(notes: List<Note>): String = notes.joinToString(separator = "\n") {
    val notesText = when (it) {
        is Note.Basic -> HTML_BASIC_PATTERN
            .replace("{front}", it.front)
            .replace("{back}", it.back)
        is Note.BasicAndReverse -> HTML_BASIC_AND_REVERSED_PATTERN
            .replace("{front}", it.front)
            .replace("{back}", it.back)
        is Note.Cloze -> it.text.replace(CLOZE_REGEX) { "<b>${it.groupValues[2]}</b>" }
        is Note.Reminder -> it.text
        is Note.Text -> it.text
        is Note.ListDeletion -> TODO()
        is Note.General -> TODO()
    }
    return@joinToString "<div>$notesText</div>"
}