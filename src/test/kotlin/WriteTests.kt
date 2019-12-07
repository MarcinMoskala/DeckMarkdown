import org.junit.Test
import parse.IncorrectFormatException
import parse.parseCards
import kotlin.test.assertEquals

class WriteTests {

    @Test
    fun `No cards produce empty`() {
        val text = writeCards(listOf())
        val expected = ""
        assertEquals(text, text)
    }

    @Test
    fun `Cloze is stored correctly`() {
        val cards = listOf(
            Card.Cloze("This is text {{c1::1}}"),
            Card.Cloze("And this {{c1::text}} number is {{c2::2}}")
        )
        val text = writeCards(cards)
        val expected = """
This is text {{c1::1}}

And this {{c1::text}} number is {{c2::2}}
        """.trimIndent()
        assertEquals(expected, text)
    }
}