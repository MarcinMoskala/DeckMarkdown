import kotlinx.coroutines.coroutineScope
import parse.AnkiApi
import parse.parseCards
import parse.toApiNote
import parse.toNote
import write.writeCards

suspend fun main() = coroutineScope<Unit> {
    val api = AnkiApi()
    val deckName = "MyNewDeck"
    val comment = "Full note under: "
    api.createDeck(deckName)
    """
This is text {{c1::1}}

qa: My question
aq: My answer

q: Question 2
a: Answer 2

And this {{c1::text}} number is {{c2::2}}
    """.trimMargin()
        .let { markdown -> parseCards(markdown) }
        .map { it.toApiNote(deckName, comment) }
        .map { if (it.hasId) api.updateNoteFields(it) else api.addNote(it) }
        .map { it.toNote() }
        .let(::writeCards)
        .let(::print)
}
