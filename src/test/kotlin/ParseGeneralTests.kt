import io.parseNotes
import org.junit.Test
import kotlin.test.assertEquals

class ParseGeneralTests {

    @Test
    fun `Uppercase Reminder text produces Reminder note`() {
        val text = """
Reminder: AAA BBB

Reminder: Line 1
Line 2
Line 3

Reminder: Lorem ipsum
        """.trimIndent()
        val expected = listOf<Note>(
            Note.Reminder(text = "AAA BBB"),
            Note.Reminder(text = "Line 1\nLine 2\nLine 3"),
            Note.Reminder(text = "Lorem ipsum")
        )
        assertEquals(expected, parseNotes(text))
    }

    @Test
    fun `Lowercase Reminder text produces Reminder note`() {
        val text = """
reminder: AAA BBB

reminder: Line 1
Line 2
Line 3

reminder: Lorem ipsum
        """.trimIndent()
        val expected = listOf<Note>(
            Note.Reminder(text = "AAA BBB"),
            Note.Reminder(text = "Line 1\nLine 2\nLine 3"),
            Note.Reminder(text = "Lorem ipsum")
        )
        assertEquals(expected, parseNotes(text))
    }

    @Test
    fun `Just "r" is accepred too`() {
        val text = """
r: AAA BBB

R: Line 1
Line 2
Line 3

r: Lorem ipsum
        """.trimIndent()
        val expected = listOf<Note>(
            Note.Reminder(text = "AAA BBB"),
            Note.Reminder(text = "Line 1\nLine 2\nLine 3"),
            Note.Reminder(text = "Lorem ipsum")
        )
        assertEquals(expected, parseNotes(text))
    }

    @Test
    fun `Too many white spaces are trimmed`() {
        val text = """
Reminder:    AAA BBB

Reminder:  
    AAA BBB

Reminder: 
AAA BBB
        """.trimIndent()
        val expected = listOf<Note>(
            Note.Reminder(text = "AAA BBB"),
            Note.Reminder(text = "AAA BBB"),
            Note.Reminder(text = "AAA BBB")
        )
        assertEquals(expected, parseNotes(text))
    }
}