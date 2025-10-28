package de.visualdigits.kaudiotagger.model.id3.types

enum class EventTimingTimestampTypes(
    val id: Int,
    val friendlyName: String
) {

    ABSOLUTE_TIME_USING_MPEG_MPEG_FRAMES_AS_UNIT(1, "Absolute time using MPEG [MPEG] frames as unit"),
    ABSOLUTE_TIME_USING_MILLISECONDS_AS_UNIT(2, "Absolute time using milliseconds as unit"),
    ;

    companion object {

        const val TIMESTAMP_KEY_FIELD_SIZE: Int = 1

        fun fromId(id: Int): EventTimingTimestampTypes? = entries.find { e -> id == e.id }

        fun getValueToIdMap(): Map<String, Long> = entries.associate { e -> Pair(e.friendlyName, e.id.toLong()) }

        fun getIdToValueMap(): Map<Long, String> = entries.associate { e -> Pair(e.id.toLong(), e.friendlyName) }

    }
}