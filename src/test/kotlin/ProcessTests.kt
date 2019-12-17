import org.junit.Test
import io.parseCards
import io.writeCards
import kotlin.test.assertEquals

class ProcessTests {

    @Test
    fun `Text stays the same after reading and writing`() {
        val texts = listOf(
            """
                Lorem {{c1::ipsum}} est
            """,
            """
                This is text {{c1::1}}
            
                qa: My question
                aq: My answer
            
                q: Question 2
                a: Answer 2
            
                And this {{c1::text}} number is {{c2::2}}
            """
        ).map { it.trimIndent() }

        for (text in texts) {
            val cards = parseCards(text)
            val processed = writeCards(cards)
            assertEquals(text, processed)
        }
    }
}