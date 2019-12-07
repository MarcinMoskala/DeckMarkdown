import org.junit.Test
import parse.IncorrectFormatException
import parse.parseCards
import write.writeCards
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

    @Test
    fun `Basic and BasisAndReversed are stored correctly`() {
        val cards = listOf(
            Card.Basic("AAA", "BBB"),
            Card.BasicAndReverse("EEE", "FFF"),
            Card.BasicAndReverse("GGG", "HHH HHH HHH ;,!@#$%^&"),
            Card.Basic("CCC", "DDD")
        )
        val text = writeCards(cards)
        val expected = """
q: AAA
a: BBB

qa: EEE
aq: FFF

qa: GGG
aq: HHH HHH HHH ;,!@#${'$'}%^&

q: CCC
a: DDD
        """.trimIndent()
        assertEquals(expected, text)
    }
}