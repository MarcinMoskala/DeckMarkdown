import org.junit.Test
import io.parseCards
import kotlin.test.assertEquals

class ParseTests {

    @Test
    fun `Text string with no special elements produces Text cards`() {
        val texts = listOf("@1\nK", "@1\nLorem ipsum", "@1\nA\nB")
        for (wholeText in texts) {
            val text = wholeText.substringAfter("\n")
            val cards = parseCards(wholeText)
            assertEquals(listOf(Card.Text(1, text)), cards)
        }
    }

    @Test
    fun `IncorrectFormatException is thrown when only id and not card`() {
        assertThrows<IllegalArgumentException> { parseCards("@1") }
        assertThrows<IllegalArgumentException> { parseCards("@1\n") }
    }

    @Test
    fun `Text string with incorrect special syntax elements produces no cards`() {
        val texts = listOf("@1\nLorem {ipsum", "@1\nLorem }ipsum", "@1\nLorem }{ipsum")
        for (wholeText in texts) {
            val text = wholeText.substringAfter("\n")
            val cards = parseCards(wholeText)
            assertEquals(listOf(Card.Text(1, text)), cards)
        }
    }

    @Test
    fun `Text string with cloze produces a cloze card`() {
        val cards = parseCards("Lorem {{c1::ipsum}} est")
        assertEquals(listOf(Card.Cloze(text = "Lorem {{c1::ipsum}} est")), cards)
    }

    @Test
    fun `We can have multiple clozes and they are separated by an empty line`() {
        val text = """
            This is text {{c1::1}}

            And this {{c1::text}} number is {{c2::2}}
        """.trimIndent()
        val cards = parseCards(text)
        assertEquals(
            listOf(
                Card.Cloze(text = "This is text {{c1::1}}"),
                Card.Cloze(text = "And this {{c1::text}} number is {{c2::2}}")
            ), cards
        )
    }

    @Test
    fun `Questions and answers works fine`() {
        val text = """
            q: My question
            a: My answer
        """.trimIndent()
        val cards = parseCards(text)
        assertEquals(listOf(Card.Basic(front = "My question", back = "My answer")), cards)
    }

    @Test
    fun `Multiline questions and answers works fine`() {
        val text = """
            q: My question
            Line 2
            a: My answer
            line 2
        """.trimIndent()
        val cards = parseCards(text)
        assertEquals(listOf(Card.Basic(front = "My question\nLine 2", back = "My answer\nline 2")), cards)
    }

    @Test
    fun `Questions and answers with reverse works fine`() {
        val text = """
            qa: My question
            aq: My answer
        """.trimIndent()
        val cards = parseCards(text)
        assertEquals(listOf(Card.BasicAndReverse(front = "My question", back = "My answer")), cards)
    }

    @Test
    fun `Both cloze and qa answers work`() {
        val text = """
            This is text {{c1::1}}

            qa: My question
            aq: My answer

            q: Question 2
            a: Answer 2

            And this {{c1::text}} number is {{c2::2}}
        """.trimIndent()
        val cards = parseCards(text)
        val expected = listOf(
            Card.Cloze(text = "This is text {{c1::1}}"),
            Card.BasicAndReverse(front = "My question", back = "My answer"),
            Card.Basic(front = "Question 2", back = "Answer 2"),
            Card.Cloze(text = "And this {{c1::text}} number is {{c2::2}}")
        )
        assertEquals(expected, cards)
    }
}