import io.writeNotes
import kotlinx.coroutines.coroutineScope
import parse.*
import java.io.File

suspend fun main() = coroutineScope<Unit> {
    val api = AnkiApi()
    val ankiMarkup = AnkiMarkup()
    ankiMarkup.syncFolder("notes")
    print("Done")
}