import io.parseNotes
import io.writeNotes
import parse.AnkiApi
import parse.ApiNoteOrText
import parse.toApiNoteOrText
import parse.toNote
import java.io.File

//import Note.Basic
//import parse.AnkiApi
//import parse.ApiNote
//
//val client = flashcardsMarkup {
//    addNoteProcessor(BasicParser)
//
//    install(AnkiSync) {
//        api = LocalAnkiApi()
//    }
//    install(GenerateHtml) {
//        location = "notesHtml"
//    }
//    install(GenerateMarkdown) {
//        location = "notesHtml"
//    }
//    install(GenerateA) {
//        location = "notesHtml"
//    }
//    install(FileSync)
//    install()
//}
//
//fun main() {
//    client[FolderSync].syncFolder("notes")
//    client[FolderSync].syncFolder("notes")
//}
//
//object BasicParser: CardParser<Basic>, AnkiParser<Basic>, HtmlSerializer<Basic>, MarkdownSerializer<Basic> {
//
//    override fun recognize(text: String): Boolean = TODO()
//    override fun parse(text: String): Basic = TODO()
//    override fun render(note: Basic): String = TODO()
//
//    override fun cardToAnkiNote(note: Basic): ApiNote = TODO()
//    override fun ankiNoteToCard(note: ApiNote): Basic = TODO()
//
//    override fun toHtml(note: Basic): String = TODO()
//    override fun toMarkdown(note: Basic): String = TODO()
//}
//
//interface NoteProcessor<T: Note>
//
//interface CardParser<T: Note>: NoteProcessor<T> {
//    fun recognize(text: String): Boolean
//    fun parse(text: String): T
//    fun render(note: T): String
//}
//
//interface AnkiParser<T: Note>: NoteProcessor<T> {
//    fun cardToAnkiNote(note: T): ApiNote
//    fun ankiNoteToCard(note: ApiNote): T
//}
//
//interface HtmlSerializer<T: Note>: NoteProcessor<T> {
//    fun toHtml(note: T): String
//}
//
//interface MarkdownSerializer<T: Note>: NoteProcessor<T> {
//    fun toMarkdown(note: T): String
//}