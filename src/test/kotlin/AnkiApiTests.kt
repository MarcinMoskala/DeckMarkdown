import kotlinx.coroutines.runBlocking
import org.junit.Test
import parse.AnkiApi
import parse.toNote
import kotlin.random.Random

class AnkiApiTests {

    val api = AnkiApi()

    @Test
    fun `After deck added it exists, after removed, not anymore`() = runBlocking {
        val deckName = "MyName"
        val decksBefore = api.getDecks()
        assert(deckName !in decksBefore) { "Deck with the name $deckName should not be present before this test" }

        try {
            api.createDeck(deckName)
            val decksAfter = api.getDecks()
            assert(deckName in decksAfter) { "Deck with the name $deckName should be present after addition. Decks are #" }
        } finally {
            api.removeDeck(deckName)
        }

        val decksAfterEverything = api.getDecks()
        assert(deckName !in decksAfterEverything) { "Deck with the name $deckName should not be present after this test" }
    }

    @Test
    fun `After notes added they exists, after removed, not anymore`() = runBlocking {
        val deckName = "MyName"
        val notes = listOf(
            Card.BasicAndReverse("AAA", "BBB")
        ).map { it.toNote(deckName, "") }
        try {
            api.createDeck(deckName)
            // Do not exist

            notes.filterNotNull().forEach { api.addNote(it) }
            // Do exist

            // Delete
            // Do not exist
        } finally {
            api.removeDeck(deckName)
            // Do not exist
        }
    }

    @Test // Same for incorrect names
    fun `Adding to a deck that does not exist causes error throw`() = runBlocking {
        val deckName = "MyNameKOKOKOKOKO" + Random.nextInt()
        assertThrows<Error> { api.addNote(Card.Cloze("AAA").toNote(deckName, "")!!) }
    }
}