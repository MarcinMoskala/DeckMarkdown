import org.junit.Test
import io.parseNotes
import io.writeNotes
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
            val notes = parseNotes(text)
            val processed = writeNotes(notes)
            assertEquals(text, processed)
        }
    }
}