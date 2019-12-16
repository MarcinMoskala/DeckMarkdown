import io.writeCards
import org.junit.Test
import kotlin.test.assertEquals

class WriteTests {

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
        val text = writeCards(cards)
        val expected = """
@1
This is text {{c1::1}}

@2
And this {{c1::text}} number is {{c2::2}}
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
        val text = writeCards(cards)
        val expected = """
@1
q: AAA
a: BBB

@2
qa: EEE
aq: FFF

@3
qa: GGG
aq: HHH HHH HHH ;,!@#${'$'}%^&

@4
q: CCC
a: DDD
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
        val text = writeCards(cards)
        val expected = """
@1
q: AAA
a: BBB

@2
And this {{c1::text}} number is {{c2::2}}

@3
qa: GGG
aq: HHH HHH
        """.trimIndent()
        assertEquals(expected, text)
    }
}
