

//import Note.Basic
//import parse.AnkiApi
//import parse.ApiNote
//
//val client = flashcardsMarkup {
//    addNoteProcessor(BasicParser)
//
//    install(AnkiSync) {
//        api = LocalAnkiApi()
//    }
//    install(GenerateHtml) {
//        location = "notesHtml"
//    }
//    install(GenerateMarkdown) {
//        location = "notesHtml"
//    }
//    install(GenerateA) {
//        location = "notesHtml"
//    }
//    install(FileSync)
//    install()
//}
//
//fun main() {
//    client[FolderSync].syncFolder("notes")
//    client[FolderSync].syncFolder("notes")
//}
//
