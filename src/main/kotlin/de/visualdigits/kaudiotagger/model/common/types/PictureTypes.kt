package de.visualdigits.kaudiotagger.model.common.types

enum class PictureTypes(
    val id: Int,
    val friendlyName: String
) {

    OTHER(0, "Other"),
    X32_PIXELS_FILE_ICON_PNG_ONLY(1, "32x32 pixels 'file icon' (PNG only)"),
    OTHER_FILE_ICON(2, "Other file icon"),
    COVER_FRONT(3, "Cover (front)"),
    COVER_BACK(4, "Cover (back)"),
    LEAFLET_PAGE(5, "Leaflet page"),
    MEDIA_E_G_LABEL_SIDE_OF_CD(6, "Media (e.g. label side of CD)"),
    LEAD_ARTIST_LEAD_PERFORMER_SOLOIST(7, "Lead artist/lead performer/soloist"),
    ARTIST_PERFORMER(8, "Artist/performer"),
    CONDUCTOR(9, "Conductor"),
    BAND_ORCHESTRA(10, "Band/Orchestra"),
    COMPOSER(11, "Composer"),
    LYRICIST_TEXT_WRITER(12, "Lyricist/text writer"),
    RECORDING_LOCATION(13, "Recording Location"),
    DURING_RECORDING(14, "During recording"),
    DURING_PERFORMANCE(15, "During performance"),
    MOVIE_VIDEO_SCREEN_CAPTURE(16, "Movie/video screen capture"),
    A_BRIGHT_COLOURED_FISH(17, "A bright coloured fish"),
    ILLUSTRATION(18, "Illustration"),
    BAND_ARTIST_LOGOTYPE(19, "Band/artist logotype"),
    PUBLISHER_STUDIO_LOGOTYPE(20, "Publisher/Studio logotype"),
    ;

    companion object {

        const val PICTURE_TYPE_FIELD_SIZE: Int = 1
        const val DEFAULT_VALUE: String = "Cover (front)"
        const val DEFAULT_ID: Int = 3

        fun getSize(): Int = entries.size

        fun fromId(id: Int?): PictureTypes? = entries.find { e -> e.id == id }

        fun getValueToIdMap(): Map<String, Int> = entries.associate { e -> Pair(e.friendlyName, e.id) }

        fun getIdToValueMap(): Map<Int, String> = entries.associate { e -> Pair(e.id, e.friendlyName) }

    }
}