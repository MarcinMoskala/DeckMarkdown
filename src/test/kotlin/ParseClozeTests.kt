import io.parseNotes
import org.junit.Test
import kotlin.test.assertEquals

class ParseClozeTests {

    @Test
    fun `Simple and multiline Cloze is parsed correctly`() {
        val text = """
Lorem {{c1::ipsum}} dolor sit amet

Lorem {{c1::ipsum}}
dolor sit amet
        """.trimIndent()
        val expected = listOf<Note>(
            Note.Cloze(text = "Lorem {{c1::ipsum}} dolor sit amet"),
            Note.Cloze(text = "Lorem {{c1::ipsum}}\ndolor sit amet")
        )
        assertEquals(expected, parseNotes(text))
    }

    @Test
    fun `Both standard and shortened format for cloze produce the same result`() {
        val text = """
Lorem {ipsum} dolor sit {amet}

Lorem {{c1::ipsum}} dolor sit {{c2::amet}}
        """.trimIndent()
        val (first, second) = parseNotes(text)
        assertEquals(second, first)
    }
}