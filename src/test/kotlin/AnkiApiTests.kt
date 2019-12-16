import kotlinx.coroutines.runBlocking
import org.junit.Test
import parse.AnkiApi
import parse.toNote
import kotlin.random.Random
import kotlin.test.assertEquals

class AnkiApiTests {

    val api = AnkiApi()

    @Test
    fun `After deck added it exists, after removed, not anymore`() = runBlocking {
        val deckName = "MyName"
        val decksBefore = api.getDecks()
        assert(deckName !in decksBefore) { "Deck with the name $deckName should not be present before this test" }

        try {
            api.createDeck(deckName)
            assertDeckAdded(deckName)
        } finally {
            api.removeDeck(deckName)
        }
        assertDeckRemoved(deckName)
    }

    @Test
    fun `After notes added they exists, after removed, not anymore`() = runBlocking {
        val deckName = "MyName"
        val notes = listOf(Card.BasicAndReverse("1", "AAA", "BBB"))
            .map { it.toNote(deckName, "") }
        try {
            api.createDeck(deckName)
            assertDeckAdded(deckName)

            val addedCards = notes.filterNotNull().map { api.addNote(it) }
            val notesInDeck = api.getNotesInDeck(deckName)
            assertEquals(addedCards, notesInDeck)
        } finally {
            api.removeDeck(deckName)
        }
        assertDeckRemoved(deckName)
    }

    private suspend fun assertDeckAdded(deckName: String) {
        val decksAfter = api.getDecks()
        assert(deckName in decksAfter) { "Deck with the name $deckName should be present after addition. Decks are #" }
    }

    private suspend fun assertDeckRemoved(deckName: String) {
        val decksAfterEverything = api.getDecks()
        assert(deckName !in decksAfterEverything) { "Deck with the name $deckName should not be present after this test" }
    }

    @Test // Same for incorrect names
    fun `Adding to a deck that does not exist causes error throw`() = runBlocking {
        val deckName = "MyNameKOKOKOKOKO" + Random.nextInt()
        assertThrows<Error> { api.addNote(Card.Cloze("1", "AAA").toNote(deckName, "")!!) }
    }
}