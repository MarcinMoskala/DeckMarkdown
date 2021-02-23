import kotlinx.coroutines.coroutineScope
import java.io.File

suspend fun main() = coroutineScope<Unit> {
    val ankiMarkup = AnkiConnector()
//    ankiMarkup.syncFolder("notes")
    ankiMarkup.syncFile(File("notes/Wiedza::Biologia::Biologia_na_czasie_1"))
//    ankiMarkup.syncFile(File("notes/Wiedza::Techniczne::Modele_Predykcyjne"))
//    ankiMarkup.syncFile(File("notes/Wiedza::Techniczne::Allegro::Yunkai"))
    print("Done")
}