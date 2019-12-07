sealed class Card {
    data class Basic(val front: String, val back: String) : Card()
    data class BasicAndReverse(val front: String, val back: String) : Card()
    data class Cloze(val text: String) : Card()
    data class Text(val text: String) : Card()
}