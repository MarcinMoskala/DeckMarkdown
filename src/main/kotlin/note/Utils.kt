package note

fun String.removeMultipleBreaks() = replace("\\n+".toRegex(), "\n")