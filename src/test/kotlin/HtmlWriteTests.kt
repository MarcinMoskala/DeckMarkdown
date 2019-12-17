import io.htmlWriteCards
import io.writeCards
import org.junit.Test
import kotlin.test.assertEquals

class HtmlWriteTests {

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
        val text = htmlWriteCards(cards)
        val expected = """
<div>This is text <b>1</b></div>
<div>And this <b>text</b> number is <b>2</b></div>
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
        val text = htmlWriteCards(cards)
        val expected = """
<div><i>Q:</i> AAA<br><i>A:</i> BBB</div>
<div><i>Q/A:</i> EEE<br><i>A/Q:</i> FFF</div>
<div><i>Q/A:</i> GGG<br><i>A/Q:</i> HHH HHH HHH ;,!@#${'$'}%^&</div>
<div><i>Q:</i> CCC<br><i>A:</i> DDD</div>
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
        val text = htmlWriteCards(cards)
        val expected = """
<div><i>Q:</i> AAA<br><i>A:</i> BBB</div>
<div>And this <b>text</b> number is <b>2</b></div>
<div><i>Q/A:</i> GGG<br><i>A/Q:</i> HHH HHH</div>
        """.trimIndent()
        assertEquals(expected, text)
    }
}
