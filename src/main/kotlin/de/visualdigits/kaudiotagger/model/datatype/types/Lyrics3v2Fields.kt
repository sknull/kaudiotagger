package de.visualdigits.kaudiotagger.model.datatype.types

enum class Lyrics3v2Fields(
    val id: String
) {

    INDICATIONS("IND"),
    LYRICS_MULTI_LINE_TEXT("LYR"),
    ADDITIONAL_MULTI_LINE_TEXT("INF"),
    AUTHOR("AUT"),
    ALBUM("EAL"),
    ARTIST("EAR"),
    TRACK("ETT"),
    IMAGE("IMG"),
    ;

    companion object {
        /**
         * CRLF int set
         */
        val crlfByte = byteArrayOf(13, 10)

        /**
         * CRLF int set
         */
        val CRLF = String(crlfByte);

        fun fromId(id: String): Lyrics3v2Fields? = entries.find { e -> id == e.id }

        fun isLyrics3v2FieldIdentifier(id: String): Boolean = entries.any { e -> id == e.id }
    }
}