package de.visualdigits.kaudiotagger.model.datatype.types

enum class EventTimingTimestampTypes(
    val id: Int,
    val friendlyName: String
) {

    ABSOLUTE_TIME_USING_MPEG_MPEG_FRAMES_AS_UNIT(1, "Absolute time using MPEG [MPEG] frames as unit"),
    ABSOLUTE_TIME_USING_MILLISECONDS_AS_UNIT(2, "Absolute time using milliseconds as unit"),
    ;

    companion object {

        const val TIMESTAMP_KEY_FIELD_SIZE: Int = 1

        fun getValueToIdMap(): Map<String, Int> = entries.associate { e -> Pair(e.friendlyName, e.id) }

        fun getIdToValueMap(): Map<Int, String> = entries.associate { e -> Pair(e.id, e.friendlyName) }

    }
}