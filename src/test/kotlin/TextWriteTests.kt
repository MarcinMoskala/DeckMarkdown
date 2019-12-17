import io.textWriteCards
import io.writeCards
import org.junit.Test
import kotlin.test.assertEquals

class TextWriteTests {

    @Test
    fun `No cards produce empty`() {
        val text = writeCards(listOf())
        val expected = ""
        assertEquals(expected, text)
    }

    @Test
    fun `Cloze is stored correctly`() {
        val cards = listOf(
            Card.Cloze(1, "This is text {{c1::1}}"),
            Card.Cloze(2, "And this {{c1::text}} number is {{c2::2}}")
        )
        val text = textWriteCards(cards)
        val expected = """
This is text 1

And this text number is 2
        """.trimIndent()
        assertEquals(expected, text)
    }

    @Test
    fun `Basic and BasisAndReversed are stored correctly`() {
        val cards = listOf(
            Card.Basic(1, "AAA", "BBB"),
            Card.BasicAndReverse(2, "EEE", "FFF"),
            Card.BasicAndReverse(3, "GGG", "HHH HHH HHH ;,!@#$%^&"),
            Card.Basic(4, "CCC", "DDD")
        )
        val text = textWriteCards(cards)
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
        val cards = listOf(
            Card.Basic(1, "AAA", "BBB"),
            Card.Cloze(2, "And this {{c1::text}} number is {{c2::2}}"),
            Card.BasicAndReverse(3, "GGG", "HHH HHH")
        )
        val text = textWriteCards(cards)
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
