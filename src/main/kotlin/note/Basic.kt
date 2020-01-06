package note

import io.CLOZE_REGEX
import io.HTML_BASIC_AND_REVERSED_PATTERN
import io.HTML_BASIC_PATTERN
import parse.ApiNote

abstract class NNote {
    abstract val id: Long?
}

data class Basic(
    override val id: Long? = null,
    val front: String,
    val back: String
) : NNote()

data class BasicAndReversed(
    override val id: Long? = null,
    val front: String,
    val back: String
) : NNote()

class DeckMarkdown(val processors: List<NoteProcessor<*>>) {
//    fun htmlWriteNotes(notes: List<NNote>): String = notes.joinToString(separator = "\n") { note ->
//        val serializer = processors.filterIsInstance<HtmlSerializer<*>>()
//            .first { it.handlesNote(note) }
//        return@joinToString "<div>${serializer.toHtml(note)}</div>"
//    }
}

object BasicParser : FullNoteProcessor<Basic> {
    private val PATTERN = "[Qq]:([\\s\\S]+)\\n[Aa]:([\\s\\S]+)".toRegex()
    private const val API_NOTE_NAME = "Basic"

    override fun handlesNote(note: NNote): Boolean = note is Basic

    override fun recognize(text: String): Boolean = PATTERN in text
    override fun parse(id: Long, noteText: String): Basic {
        val (question, answer) = parseQA(noteText, PATTERN)
        return Basic(id, question, answer)
    }

    override fun render(note: Basic): String = "q: {front}\na: {back}"
        .replace("{front}", note.front)
        .replace("{back}", note.back)

    override fun recognizeApiNote(apiNote: ApiNote): Boolean = apiNote.modelName == API_NOTE_NAME
    override fun cardToAnkiNote(note: Basic, deckName: String, comment: String): ApiNote = ApiNote(
        noteId = note.id ?: ApiNote.NO_ID,
        deckName = deckName,
        modelName = "Basic",
        fields = mapOf("Front" to note.front, "Back" to note.back, "Extra" to comment)
    )

    override fun ankiNoteToCard(apiNote: ApiNote): Basic = Basic(
        apiNote.noteId,
        apiNote.fields.getValue("Front").removeMultipleBreaks(),
        apiNote.fields.getValue("Back").removeMultipleBreaks()
    )

    override fun toHtml(note: Basic): String = "<i>Q:</i> {front}<br><i>A:</i> {back}"
        .replace("{front}", note.front)
        .replace("{back}", note.back)

    override fun toMarkdown(note: Basic): String = "Q: {front}\nA: {back}"
        .replace("{front}", note.front)
        .replace("{back}", note.back)
}

object BasicAndReversedParser : FullNoteProcessor<BasicAndReversed> {
    private val PATTERN = "[Qq][Aa]:([\\s\\S]+)\\n[Aa][Qq]:([\\s\\S]+)".toRegex()
    private const val API_NOTE_NAME = "Basic (and reversed card)"

    override fun handlesNote(note: NNote): Boolean = note is BasicAndReversed

    override fun recognize(text: String): Boolean = PATTERN in text
    override fun parse(id: Long, noteText: String): BasicAndReversed {
        val (question, answer) = parseQA(noteText, PATTERN)
        return BasicAndReversed(id, question, answer)
    }

    override fun render(note: BasicAndReversed): String = "qa: {front}\naq: {back}"
        .replace("{front}", note.front)
        .replace("{back}", note.back)

    override fun recognizeApiNote(apiNote: ApiNote): Boolean = apiNote.modelName == API_NOTE_NAME
    override fun cardToAnkiNote(note: BasicAndReversed, deckName: String, comment: String): ApiNote = ApiNote(
        noteId = note.id ?: ApiNote.NO_ID,
        deckName = deckName,
        modelName = API_NOTE_NAME,
        fields = mapOf("Front" to note.front, "Back" to note.back, "Extra" to comment)
    )

    override fun ankiNoteToCard(apiNote: ApiNote): BasicAndReversed = BasicAndReversed(
        apiNote.noteId,
        apiNote.fields.getValue("Front").removeMultipleBreaks(),
        apiNote.fields.getValue("Back").removeMultipleBreaks()
    )

    override fun toHtml(note: BasicAndReversed): String = "<i>Q/A:</i> {front}<br><i>A/Q:</i> {back}"
        .replace("{front}", note.front)
        .replace("{back}", note.back)

    override fun toMarkdown(note: BasicAndReversed): String = "Q/A: {front}\nA/Q: {back}"
        .replace("{front}", note.front)
        .replace("{back}", note.back)
}

private fun parseQA(text: String, regex: Regex): Pair<String, String> =
    regex
        .matchEntire(text)
        ?.groupValues
        ?.drop(1)
        ?.map { it.trim() }
        ?.let { (f, s) -> f to s }
        ?: throw IllegalArgumentException("Incorrect format of the paragraph: \n$text")

private fun String.removeMultipleBreaks() = replace("\\n+".toRegex(), "\n")

interface FullNoteProcessor<T : NNote> : CardParser<T>, AnkiParser<T>, HtmlSerializer<T>, MarkdownSerializer<T>

interface NoteProcessor<T : NNote> {
    fun handlesNote(note: NNote): Boolean
}

interface CardParser<T : NNote> : NoteProcessor<T> {
    fun recognize(text: String): Boolean
    fun parse(id: Long, noteText: String): T
    fun render(note: T): String
}

interface AnkiParser<T : NNote> : NoteProcessor<T> {
    fun recognizeApiNote(apiNote: ApiNote): Boolean
    fun cardToAnkiNote(note: T, deckName: String, comment: String): ApiNote
    fun ankiNoteToCard(apiNote: ApiNote): T
}

interface HtmlSerializer<T : NNote> : NoteProcessor<T> {
    fun toHtml(note: T): String
}

interface MarkdownSerializer<T : NNote> : NoteProcessor<T> {
    fun toMarkdown(note: T): String
}