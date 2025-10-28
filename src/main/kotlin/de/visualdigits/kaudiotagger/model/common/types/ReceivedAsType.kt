package de.visualdigits.kaudiotagger.model.common.types

enum class ReceivedAsType(
    val id: Int,
    val friendlyName: String
) {

    OTHER(0x00, "Other"),
    STANDARD_CD_ALBUM_WITH_OTHER_SONGS(0x01, "Standard CD album with other songs"),
    COMPRESSED_AUDIO_ON_CD(0x02, "Compressed audio on CD"),
    FILE_OVER_THE_INTERNET(0x03, "File over the Internet"),
    STREAM_OVER_THE_INTERNET(0x04, "Stream over the Internet"),
    AS_NOTE_SHEETS(0x05, "As note sheets"),
    AS_NOTE_SHEETS_IN_A_BOOK_WITH_OTHER_SHEETS(0x06, "As note sheets in a book with other sheets"),
    MUSIC_ON_OTHER_MEDIA(0x07, "Music on other media"),
    NON_MUSICAL_MERCHANDISE(0x08, "Non-musical merchandise"),
    ;

    companion object {

        //The number of bytes used to hold the text encoding field size
        const val RECEIVED_AS_FIELD_SIZE: Int = 1

        fun getValueToIdMap(): Map<String, Long> = entries.associate { e -> Pair(e.friendlyName, e.id.toLong()) }

        fun getIdToValueMap(): Map<Long, String> = entries.associate { e -> Pair(e.id.toLong(), e.friendlyName) }

    }
}