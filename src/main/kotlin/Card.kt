sealed class Card {
    abstract val id: Long?

    data class Basic(
        override val id: Long? = null,
        val front: String,
        val back: String
    ) : Card()

    data class BasicAndReverse(
        override val id: Long? = null,
        val front: String,
        val back: String
    ) : Card()

    data class Cloze(
        override val id: Long? = null,
        val text: String
    ) : Card()

    data class Text(
        override val id: Long? = null,
        val text: String
    ) : Card()
}