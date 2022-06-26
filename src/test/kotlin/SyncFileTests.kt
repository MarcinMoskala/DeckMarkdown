import fakes.FakeAnkiApi
import kotlinx.coroutines.runBlocking
import note.DefaultParser
import org.junit.Before
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class SyncFileTests {

    private lateinit var apiFake: FakeAnkiApi
    private lateinit var ankiMarkup: AnkiConnector
    private val FAKE_FILE_NAME = "some::deck"

    @Before
    fun setup() {
        apiFake = FakeAnkiApi()
        ankiMarkup = AnkiConnector(
            api = apiFake, parser = DefaultParser
        )
    }

    @Test
    fun `should not update when changes not needed`() = fakeFileTest { file ->
        // given
        file.writeText(
            """
@1
qa: Some question
aq: Some answer

@2
A {{c1::B}} C {{c2::D}} E
        """.trimIndent()
        )

        // when synced for the first time
        ankiMarkup.syncFile(file)

        // then added two noted
        assertEquals(apiFake.addUsedCount, 2)
        assertEquals(apiFake.removeUsedCount, 0)
        assertEquals(apiFake.updateUsedCount, 0)

        // when synced again without changes
        ankiMarkup.syncFile(file)

        // then no additional operations were made
        assertEquals(apiFake.addUsedCount, 2)
        assertEquals(apiFake.removeUsedCount, 0)
        assertEquals(apiFake.updateUsedCount, 0)
    }

    @Test
    fun `should update when changes needed`() = fakeFileTest { file ->
        // given
        file.writeText(
            """
@1
qa: Some question
aq: Some answer

@2
A {{c1::B}} C {{c2::D}} E
        """.trimIndent()
        )

        // when synced for the first time
        ankiMarkup.syncFile(file)

        // then added two noted
        assertEquals(apiFake.addUsedCount, 2)
        assertEquals(apiFake.removeUsedCount, 0)
        assertEquals(apiFake.updateUsedCount, 0)

        // when synced with changed file
        file.writeText(
            """
@1
qa: Some queXXXstion
aq: Some answer

@2
A {{c1::B}} C {{c2::D}} E
        """.trimIndent()
        )
        ankiMarkup.syncFile(file)

        // then added two noted
        assertEquals(apiFake.addUsedCount, 2)
        assertEquals(apiFake.removeUsedCount, 0)
        assertEquals(apiFake.updateUsedCount, 1)
    }

    private fun fakeFileTest(body: suspend (File) -> Unit) = runBlocking {
        val file = File("$FAKE_FILE_NAME.md")
        @Suppress("BlockingMethodInNonBlockingContext") file.createNewFile()
        try {
            body(file)
        } finally {
            file.delete()
        }
    }
}
