sealed class Note {
    abstract val id: Long?

    data class Basic(
        override val id: Long? = null,
        val front: String,
        val back: String
    ) : Note()

    data class BasicAndReverse(
        override val id: Long? = null,
        val front: String,
        val back: String
    ) : Note()

    data class Cloze(
        override val id: Long? = null,
        val text: String
    ) : Note()

    data class Reminder(
        override val id: Long? = null,
        val text: String
    ) : Note()

    data class Text(
        val text: String
    ) : Note() {
        override val id: Long? = null
    }
}