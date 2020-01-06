package parse

import Note
import Note.ListDeletion.Item
import Note.ListDeletion.ListType
import parse.ApiNote.Companion.NO_ID
import java.lang.IllegalArgumentException

fun Note.toApiNote(deckName: String, comment: String): ApiNote =
    this.toApiNoteOrText(deckName, comment) as? ApiNote
        ?: throw IllegalArgumentException("Note $this parsed to a text which is not ApiNote")

fun Note.toApiNoteOrText(deckName: String, comment: String): ApiNoteOrText = when (this) {
    is Note.Text -> Text(text)
    is Note.Basic -> ApiNote(
        noteId = id ?: NO_ID,
        deckName = deckName,
        modelName = "Basic",
        fields = mapOf("Front" to this.front, "Back" to this.back, "Extra" to comment)
    )
    is Note.BasicAndReverse -> ApiNote(
        noteId = id ?: NO_ID,
        deckName = deckName,
        modelName = "Basic (and reversed card)",
        fields = mapOf("Front" to this.front, "Back" to this.back, "Extra" to comment)
    )
    is Note.Cloze -> ApiNote(
        noteId = id ?: NO_ID,
        deckName = deckName,
        modelName = "Cloze",
        fields = mapOf("Text" to this.text, "Extra" to comment)
    )
    is Note.Reminder -> ApiNote(
        noteId = id ?: NO_ID,
        deckName = deckName,
        modelName = "Reminder",
        fields = mapOf("Front" to text, "Extra" to comment)
    )
    is Note.ListDeletion -> ApiNote(
        noteId = id ?: NO_ID,
        deckName = deckName,
        modelName = if (type == ListType.List) "ListDeletion" else "SetDeletion",
        fields = mapOf("Title" to title, "General Comment" to generalComment, "Extra" to comment) +
                items.withIndex()
                    .flatMap { (index, item) ->
                        val positionStr = "${index + 1}"
                        listOfNotNull(positionStr to item.value, "$positionStr comment" to item.comment)
                    }
                    .toMap()
    )
    is Note.General -> ApiNote(
        noteId = id ?: NO_ID,
        deckName = deckName,
        modelName = modelName,
        fields = fields
    )
}

fun ApiNoteOrText.toNote(): Note = when (this) {
    is Text -> Note.Text(this.text)
    is ApiNote -> this.toNote()
}

fun ApiNote.toNote(): Note = toNoteOrNull() ?: error("Unknown notes type $this")

fun ApiNote.toNoteOrNull(): Note? = when (modelName) {
    "Basic" -> Note.Basic(
        noteId,
        fields.getValue("Front").removeMultipleBreaks(),
        fields.getValue("Back").removeMultipleBreaks()
    )
    "Basic (and reversed card)" -> Note.BasicAndReverse(
        noteId,
        fields.getValue("Front").removeMultipleBreaks(),
        fields.getValue("Back").removeMultipleBreaks()
    )
    "Cloze" -> Note.Cloze(noteId, fields.getValue("Text").removeMultipleBreaks())
    "Reminder" -> Note.Reminder(noteId, fields.getValue("Front").removeMultipleBreaks())
    "ListDeletion" -> Note.ListDeletion(
        noteId,
        ListType.List,
        fields.getValue("Title").removeMultipleBreaks(),
        makeItemsList(),
        fields.getValue("General Comment")
    )
    "SetDeletion" -> Note.ListDeletion(
        noteId,
        ListType.Set,
        fields.getValue("Title").removeMultipleBreaks(),
        makeItemsList(),
        fields.getValue("General Comment")
    )
    else -> null
}

private fun ApiNote.makeItemsList(): List<Item> {
    return (1..20).mapNotNull { i ->
        val value = fields["$i"].takeUnless { it.isNullOrBlank() } ?: return@mapNotNull null
        val comment = fields["${i} comment"].orEmpty()
        Item(value, comment)
    }
}

private fun String.removeMultipleBreaks() = replace("\\n+".toRegex(), "\n")