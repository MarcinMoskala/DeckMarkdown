import kotlinx.coroutines.coroutineScope

suspend fun main() = coroutineScope<Unit> {
    val ankiMarkup = AnkiConnector()
    ankiMarkup.syncFolder("notes")
    print("Done")
}