package de.visualdigits.kaudiotagger.model.field

enum class Lyrics3v2KField(
    val id: String
) : KField {
    INDICATIONS("IND"),
    LYRICS_MULTI_LINE_TEXT("LYR"),
    ADDITIONAL_MULTI_LINE_TEXT("INF"),
    AUTHOR("AUT"),
    ALBUM("EAL"),
    ARTIST("EAR"),
    TRACK("ETT"),
    IMAGE("IMG")
    ;

    companion object {
        fun fromId(id: String): Lyrics3v2KField? = entries.find { e -> e.id == id }
    }
}