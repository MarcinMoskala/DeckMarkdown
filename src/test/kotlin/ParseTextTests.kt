import io.parseNotes
import org.junit.Test
import kotlin.test.assertEquals

class ParseTextTests {

    @Test
    fun `Pure text with no markup is a text`() {
        val text = """
AAA BBB

Line 1
Line 2
Line 3

Lorem ipsum
        """.trimIndent()
        val expected = listOf<Note>(
            Note.Text(text = "AAA BBB"),
            Note.Text(text = "Line 1\nLine 2\nLine 3"),
            Note.Text(text = "Lorem ipsum")
        )
        assertEquals(expected, parseNotes(text))
    }
}