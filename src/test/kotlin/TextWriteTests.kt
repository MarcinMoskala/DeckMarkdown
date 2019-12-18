import io.textWriteNotes
import io.writeNotes
import org.junit.Test
import kotlin.test.assertEquals

class TextWriteTests {

    @Test
    fun `No notes produce empty`() {
        val text = textWriteNotes(listOf())
        val expected = ""
        assertEquals(expected, text)
    }

    @Test
    fun `Cloze is stored correctly`() {
        val notes = listOf(
            Note.Cloze(1, "This is text {{c1::1}}"),
            Note.Cloze(2, "And this {{c1::text}} number is {{c2::2}}")
        )
        val text = textWriteNotes(notes)
        val expected = """
This is text 1

And this text number is 2
        """.trimIndent()
        assertEquals(expected, text)
    }

    @Test
    fun `Basic and BasisAndReversed are stored correctly`() {
        val notes = listOf(
            Note.Basic(1, "AAA", "BBB"),
            Note.BasicAndReverse(2, "EEE", "FFF"),
            Note.BasicAndReverse(3, "GGG", "HHH HHH HHH ;,!@#$%^&"),
            Note.Basic(4, "CCC", "DDD")
        )
        val text = textWriteNotes(notes)
        val expected = """
Q: AAA
A: BBB

Q/A: EEE
A/Q: FFF

Q/A: GGG
A/Q: HHH HHH HHH ;,!@#$%^&

Q: CCC
A: DDD
        """.trimIndent()
        assertEquals(expected, text)
    }

    @Test
    fun `All are stored correctly`() {
        val notes = listOf(
            Note.Basic(1, "AAA", "BBB"),
            Note.Cloze(2, "And this {{c1::text}} number is {{c2::2}}"),
            Note.BasicAndReverse(3, "GGG", "HHH HHH")
        )
        val text = textWriteNotes(notes)
        val expected = """
Q: AAA
A: BBB

And this text number is 2

Q/A: GGG
A/Q: HHH HHH
        """.trimIndent()
        assertEquals(expected, text)
    }
}
