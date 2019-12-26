import Note.ListDeletion.Item
import org.junit.Test

import parse.ApiNote
import parse.toApiNoteOrText
import kotlin.test.assertEquals

class MakeNotesTest {

    @Test
    fun `Example ListDeletion is correctly transformed`() {
        val id = 123L
        val deckName = "DeckName"
        val comment = "Comment"
        val actual = Note.ListDeletion(
            id = id,
            title = "AAA",
            generalComment = "General Comment",
            items = listOf(Item("a"), Item("b", "comment"))
        )
            .toApiNoteOrText(deckName, comment)
        val expected = ApiNote(
            noteId = id,
            deckName = deckName,
            modelName = "ListDeletion",
            fields = mapOf(
                "Title" to "AAA",
                "General Comment" to "General Comment",
                "Comment" to comment,
                "1" to "a",
                "1 comment" to "",
                "2" to "b",
                "2 comment" to "comment"
            )
        )
        assertEquals(expected, actual)
    }
}