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
}