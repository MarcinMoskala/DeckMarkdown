import kotlinx.coroutines.coroutineScope
import parse.*

suspend fun main() = coroutineScope<Unit> {
    val api = AnkiApi()
    api.addModel(
        ApiNoteModel(
            modelName = "Triple",
            inOrderFields = listOf("First", "Second", "Third"),
            cardTemplates = listOf(
                CardTemplate("{{First}}", "{{FrontSide}}\n\n<hr id=answer>\n\n{{Second}}<br>{{Third}}"),
                CardTemplate("{{Second}}", "{{FrontSide}}\n\n<hr id=answer>\n\n{{First}}<br>{{Third}}"),
                CardTemplate("{{Third}}", "{{FrontSide}}\n\n<hr id=answer>\n\n{{First}}<br>{{Second}}")
            )
        )
    )

    val name = "Test"
    api.createDeck(name)
    api.addNote(
        ApiNote(
            deckName = name,
            modelName = "Triple",
            fields = mapOf("First" to "A", "Second" to "B", "Third" to "C")
        )
    )
}