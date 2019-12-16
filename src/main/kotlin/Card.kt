sealed class Card {
    abstract val id: String?

    data class Basic(
        override val id: String? = null,
        val front: String,
        val back: String
    ) : Card()

    data class BasicAndReverse(
        override val id: String? = null,
        val front: String,
        val back: String
    ) : Card()

    data class Cloze(
        override val id: String? = null,
        val text: String
    ) : Card()

    data class Text(
        override val id: String? = null,
        val text: String
    ) : Card()
}