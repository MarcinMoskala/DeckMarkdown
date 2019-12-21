import Note.ListDeletion.Item
import io.parseNotes
import org.junit.Test
import kotlin.test.assertEquals

class ParseListDeletionTests {

    @Test
    fun `Simple and multiline List is parsed correctly`() {
        val text = """
L: First 3 letters
* a
* b
* c

L: First 3 letters
In the English alphabet
* a
* b
* c
        """.trimIndent()
        val expected = listOf<Note>(
            Note.ListDeletion(title = "First 3 letters", items = listOf(Item("a"), Item("b"), Item("c"))),
            Note.ListDeletion(
                title = "First 3 letters\nIn the English alphabet",
                items = listOf(Item("a"), Item("b"), Item("c"))
            )
        )
        assertEquals(expected, parseNotes(text))
    }

    @Test
    fun `A lines below item is understood as a comment`() {
        val text = """
L: First 3 letters
* a
aaaaa
next
and more
* b
bbbbb
* c
ccccc

L: First 3 letters
In the English alphabet
* a
aaaaa
* b
bbbbb
* c
ccccc
        """.trimIndent()
        val expected = listOf<Note>(
            Note.ListDeletion(
                title = "First 3 letters",
                items = listOf(Item("a", "aaaaa\nnext\nand more"), Item("b", "bbbbb"), Item("c", "ccccc"))
            ),
            Note.ListDeletion(
                title = "First 3 letters\nIn the English alphabet",
                items = listOf(Item("a", "aaaaa"), Item("b", "bbbbb"), Item("c", "ccccc"))
            )
        )
        assertEquals(expected, parseNotes(text))
    }

    @Test
    fun `List can be both uppercase and lowercase`() {
        val text = """
L: First 3 letters
* a
* b
* c

l: First 3 letters
* a
* b
* c
        """.trimIndent()
        val (upper, lower) = parseNotes(text)
        assertEquals(upper, lower)
    }

    @Test
    fun `White spaces before content are trimmed`() {
        val text = """
L:     First 3 letters
*     a
*              b
*c
        """.trimIndent()
        val expected = listOf<Note>(
            Note.ListDeletion(title = "First 3 letters", items = listOf(Item("a"), Item("b"), Item("c")))
        )
        assertEquals(expected, parseNotes(text))
    }
}