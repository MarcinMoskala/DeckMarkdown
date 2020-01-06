//import io.writeNotes
//import kotlinx.coroutines.coroutineScope
//
//suspend fun main() = coroutineScope<Unit> {
//    with(AnkiConnector()) {
//        readNotesFromDeck(deckName = "Wiedza::Techniczne::ETH")
//            .let(::writeNotes)
//            .let(::print)
//    }
//}