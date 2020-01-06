package note.toanki

import Note
import Note.ListDeletion.Item
import note.DeckParser
import note.ListDeletionParser
import org.junit.Test
import parse.ApiNote
import kotlin.test.assertEquals

class AnkiNoteFromListTest {

    val parser = DeckParser(listOf(ListDeletionParser))

    @Test
    fun `Example ListDeletion is correctly transformed`() {
        val id = 123L
        val deckName = "DeckName"
        val comment = "Comment"
        val note = Note.ListDeletion(
            id = id,
            title = "AAA",
            generalComment = "General Comment",
            items = listOf(Item("a"), Item("b", "comment"))
        )
        val actual = parser.noteToApiNote(note, deckName, comment)
        val expected = ApiNote(
            noteId = id,
            deckName = deckName,
            modelName = "ListDeletion",
            fields = mapOf(
                "Title" to "AAA",
                "General Comment" to "General Comment",
                "Extra" to comment,
                "1" to "a",
                "1 comment" to "",
                "2" to "b",
                "2 comment" to "comment"
            )
        )
        assertEquals(expected, actual)
    }
}