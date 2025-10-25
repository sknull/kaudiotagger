package de.visualdigits.kaudiotagger.model.datatype.types

enum class SynchronisedLyricsContentType(
    val id: Int,
    val friendlyName: String
) {

    OTHER(0x00, "other"),
    LYRICS(0x01, "lyrics3"),
    TEXT_TRANSCRIPTION(0x02, "text transcription"),
    MOVEMENT_PART_NAME(0x03, "movement/part name"),
    EVENTS(0x04, "events"),
    CHORD(0x05, "chord"),
    TRIVIA(0x06, "trivia"),
    URLS_TO_WEBPAGES(0x07, "URLs to webpages"),
    URLS_TO_IMAGES(0x08, "URLs to images"),
    ;

    companion object {

        const val CONTENT_KEY_FIELD_SIZE: Int = 1

        fun getValueToIdMap(): Map<String, Int> = entries.associate { e -> Pair(e.friendlyName, e.id) }

        fun getIdToValueMap(): Map<Int, String> = entries.associate { e -> Pair(e.id, e.friendlyName) }

    }
}