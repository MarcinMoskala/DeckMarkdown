import org.junit.Test
import parse.IncorrectFormatException
import parse.parseCards
import kotlin.test.assertEquals

class ParseTests {

    @Test
    fun `Text string with no special elements produces no cards`() {
        val texts = listOf("", "Lorem ipsum", "A\n\nB")
        for (text in texts) {
            val cards = parseCards(text)
            assertEquals(listOf(Card.Text(text)), cards)
        }
    }

    @Test
    fun `Text string with incorrect special syntax elements produces no cards`() {
        val texts = listOf("Lorem {ipsum", "Lorem }ipsum", "Lorem }{ipsum")
        for (text in texts) {
            val cards = parseCards(text)
            assertEquals(listOf(Card.Text(text)), cards)
        }
    }

    @Test
    fun `Text string with cloze produces a cloze card`() {
        val cards = parseCards("Lorem {ipsum} est")
        assertEquals(listOf(Card.Cloze("Lorem {{c1::ipsum}} est")), cards)
    }

    @Test
    fun `We can have multiple clozes and they are separated by an empty line`() {
        val text = """
            This is text {1}
            
            And this {text} number is {2}
        """.trimIndent()
        val cards = parseCards(text)
        assertEquals(listOf(Card.Cloze("This is text {{c1::1}}"), Card.Cloze("And this {{c1::text}} number is {{c2::2}}")), cards)
    }

    @Test
    fun `Questions and answers works fine`() {
        val text = """
            q: My question
            a: My answer
        """.trimIndent()
        val cards = parseCards(text)
        assertEquals(listOf(Card.Basic("My question", "My answer")), cards)
    }

    @Test
    fun `Questions and answers with reverse works fine`() {
        val text = """
            qa: My question
            aq: My answer
        """.trimIndent()
        val cards = parseCards(text)
        assertEquals(listOf(Card.BasicAndReverse("My question", "My answer")), cards)
    }

    @Test
    fun `Incorrect texts throw errors correctly`() {
        val texts = listOf("q:Lorem ipsum", "qa: Lorem }ipsum")
        for (text in texts) {
            assertThrows<IncorrectFormatException> { parseCards(text) }
        }
    }

    @Test
    fun `Both cloze and qa answers work`() {
        val text = """
            This is text {1}
            
            qa: My question
            aq: My answer
            
            q: Question 2
            a: Answer 2
            
            And this {text} number is {2}
        """.trimIndent()
        val cards = parseCards(text)
        val expected = listOf(
            Card.Cloze("This is text {{c1::1}}"),
            Card.BasicAndReverse("My question", "My answer"),
            Card.Basic("Question 2", "Answer 2"),
            Card.Cloze("And this {{c1::text}} number is {{c2::2}}")
        )
        assertEquals(expected, cards)
    }
}