import io.parseNotes
import org.junit.Test
import kotlin.test.assertEquals

class ParseBasicTests {

    @Test
    fun `Simple and multiline Basic is parsed correctly`() {
        val text = """
q: AAA
a: BBB

q: QLine 1
QLine 2
a: Line 1
Line 2
Line 3
        """.trimIndent()
        val expected = listOf<Note>(
            Note.Basic(front = "AAA", back = "BBB"),
            Note.Basic(front = "QLine 1\nQLine 2", back = "Line 1\nLine 2\nLine 3")
        )
        assertEquals(expected, parseNotes(text))
    }

    @Test
    fun `Uppercase q and a text produce Basic as well`() {
        val text = """
Q: AAA
A: BBB

q: AAA
A: BBB

Q: AAA
a: BBB
        """.trimIndent()
        val expected = listOf<Note>(
            Note.Basic(front = "AAA", back = "BBB"),
            Note.Basic(front = "AAA", back = "BBB"),
            Note.Basic(front = "AAA", back = "BBB")
        )
        assertEquals(expected, parseNotes(text))
    }

    @Test
    fun `White spaces before content are trimmed`() {
        val text = """
q:AAA
a:BBB

q:
AAA
a:
BBB

q:     AAA
a:          BBB

q:
AAA
a:     
     BBB
        """.trimIndent()
        val expected = listOf<Note>(
            Note.Basic(front = "AAA", back = "BBB"),
            Note.Basic(front = "AAA", back = "BBB"),
            Note.Basic(front = "AAA", back = "BBB"),
            Note.Basic(front = "AAA", back = "BBB")
        )
        assertEquals(expected, parseNotes(text))
    }
}