package de.visualdigits.kaudiotagger.model.id3.types

import de.visualdigits.kaudiotagger.model.common.types.Frames
import de.visualdigits.kaudiotagger.model.common.types.GenericFieldKey

enum class ID3v22Frames(
    override val id: String,
    override val genericFieldKey: GenericFieldKey?,
    override val fieldKey: ID3v22FieldKey?,
    override val friendlyName: String,
    override val isCommon: Boolean,
    override val isBinary: Boolean,
    override val isMultipleAllowed: Boolean,
    override val isSupported: Boolean,
    override val isExtension: Boolean,
    override val isDiscardedIfFileAltered: Boolean
) : Frames {

        ACCOMPANIMENT("TP2", GenericFieldKey.ALBUM_ARTIST, ID3v22FieldKey.ALBUM_ARTIST, "Text: Band/Orchestra/Accompaniment", false, false, false, true, false, false),
        ALBUM("TAL", GenericFieldKey.ALBUM, ID3v22FieldKey.ALBUM, "Text: Album/Movie/Show title", true, false, false, true, false, false),
        ARTIST("TP1", GenericFieldKey.ARTIST, ID3v22FieldKey.ARTIST, "Text: Lead artist(s)/Lead performer(s)/Soloist(s)/Performing group", true, false, false, true, false, false),
        ATTACHED_PICTURE("PIC", GenericFieldKey.COVER_ART, ID3v22FieldKey.COVER_ART, "Attached picture", false, true, true, true, false, false),
        AUDIO_ENCRYPTION("CRA", null, null, "Audio encryption", false, true, false, true, false, false),
        BPM("TBP", GenericFieldKey.BPM, ID3v22FieldKey.BPM, "Text: BPM (Beats Per Minute)", false, false, false, true, false, false),
        COMMENT("COM", GenericFieldKey.COMMENT, ID3v22FieldKey.COMMENT, "Comments", true, false, true, true, false, false),
        COMPOSER("TCM", GenericFieldKey.COMPOSER, ID3v22FieldKey.COMPOSER, "Text: Composer", false, false, false, true, false, false),
        CONDUCTOR("TPE", GenericFieldKey.CONDUCTOR, ID3v22FieldKey.CONDUCTOR, "Text: Conductor/Performer refinement", false, false, false, true, false, false),
        CONTENT_GROUP_DESC("TT1", GenericFieldKey.GROUPING, ID3v22FieldKey.GROUPING, "Text: Content group description", false, false, false, true, false, false),
        COPYRIGHTINFO("TCR", null, null, "Text: Copyright message", false, false, false, true, false, false),
        ENCODEDBY("TEN", GenericFieldKey.ENCODER, ID3v22FieldKey.ENCODER, "Text: Encoded by", false, false, false, true, false, false),
        ENCRYPTED_FRAME("CRM", null, null, "Encrypted meta frame", false, true, false, true, false, false),
        EQUALISATION("EQU", null, null, "Equalization", false, true, false, true, false, false),
        EVENT_TIMING_CODES("ETC", null, null, "Event timing codes", false, true, false, true, false, false),
        FILE_TYPE("TFT", null, null, "Text: File type", false, false, false, true, false, false),
        GENERAL_ENCAPS_OBJECT("GEO", null, null, "General encapsulated datatype", false, true, true, true, false, false),
        GENRE("TCO", GenericFieldKey.GENRE, ID3v22FieldKey.GENRE, "Text: Content type", true, false, false, true, false, false),
        HW_SW_SETTINGS("TSS", null, null, "Text: Software/hardware and settings used for encoding", false, false, false, true, false, false),
        INITIAL_KEY("TKE", GenericFieldKey.KEY, ID3v22FieldKey.KEY, "Text: Initial key", false, false, false, true, false, false),
        IPLS("IPL", GenericFieldKey.ARRANGER, ID3v22FieldKey.ARRANGER, "Involved people list", false, false, false, true, false, false),
        ISRC("TRC", GenericFieldKey.ISRC, ID3v22FieldKey.ISRC, "Text: ISRC (International Standard Recording Code)", false, false, false, true, false, false),
        ITUNES_GROUPING("GP1", GenericFieldKey.ITUNES_GROUPING, ID3v22FieldKey.ITUNES_GROUPING, "iTunes Grouping", false, false, false, true, false, false),
        LANGUAGE("TLA", GenericFieldKey.LANGUAGE, ID3v22FieldKey.LANGUAGE, "Text: Language(s)", false, false, false, true, false, false),
        LENGTH("TLE", null, null, "Text: Length", false, false, false, true, false, false),
        LINKED_INFO("LNK", null, null, "Linked information", false, false, false, true, false, false),
        LYRICIST("TXT", GenericFieldKey.LYRICIST, ID3v22FieldKey.LYRICIST, "Text: Lyricist/text writer", false, false, false, true, false, false),
        MEDIA_TYPE("TMT", GenericFieldKey.MEDIA, ID3v22FieldKey.MEDIA, "Text: Media type", false, false, false, true, false, false),
        MOVEMENT("MVN", GenericFieldKey.MOVEMENT, ID3v22FieldKey.MOVEMENT, "Text: Movement", false, false, false, true, false, false),
        MOVEMENT_NO("MVI", GenericFieldKey.MOVEMENT_NO, ID3v22FieldKey.MOVEMENT_NO, "Text: Movement No", false, false, false, true, false, false),
        MPEG_LOCATION_LOOKUP_TABLE("MLL", null, null, "MPEG location lookup table", false, false, false, true, false, false),
        MUSIC_CD_ID("MCI", null, null, "Music CD Identifier", false, false, false, true, false, false),
        ORIGARTIST("TOA", GenericFieldKey.ORIGINAL_ARTIST, ID3v22FieldKey.ORIGINAL_ARTIST, "Text: Original artist(s)/performer(s)", false, false, false, true, false, false),
        ORIG_FILENAME("TOF", null, null, "Text: Original filename", false, false, false, true, false, false),
        ORIG_LYRICIST("TOL", GenericFieldKey.ORIGINAL_LYRICIST, ID3v22FieldKey.ORIGINAL_LYRICIST, "Text: Original Lyricist(s)/text writer(s)", false, false, false, true, false, false),
        ORIG_TITLE("TOT", GenericFieldKey.ORIGINAL_ALBUM, ID3v22FieldKey.ORIGINAL_ALBUM, "Text: Original album/Movie/Show title", false, false, false, true, false, false),
        PLAYLIST_DELAY("TDY", null, null, "Text: Playlist delay", false, false, false, true, false, false),
        PLAY_COUNTER("CNT", null, null, "Play counter", false, false, false, true, false, false),
        POPULARIMETER("POP", GenericFieldKey.RATING, ID3v22FieldKey.RATING, "Popularimeter", false, false, true, true, false, false),
        PUBLISHER("TPB", GenericFieldKey.RECORD_LABEL, ID3v22FieldKey.RECORD_LABEL, "Text: Publisher", false, false, false, true, false, false),
        RECOMMENDED_BUFFER_SIZE("BUF", null, null, "Recommended buffer size", false, true, false, true, false, false),
        RELATIVE_VOLUME_ADJUSTMENT("RVA", null, null, "Relative volume adjustment", false, true, false, true, false, false),
        REMIXED("TP4", GenericFieldKey.REMIXER, ID3v22FieldKey.REMIXER, "Text: Interpreted, remixed, or otherwise modified by", false, false, false, true, false, false),
        REVERB("REV", null, null, "Reverb", false, false, false, true, false, false),
        SET("TPA", GenericFieldKey.DISC_NO, ID3v22FieldKey.DISC_NO, "Text: Part of a setField", false, false, false, true, false, false),
        SET_SUBTITLE("TPS", GenericFieldKey.DISC_SUBTITLE, ID3v22FieldKey.DISC_SUBTITLE, "Text: Set subtitle", false, false, false, false, false, false),
        SYNC_LYRIC("SLT", null, null, "Synchronized lyric/text", false, false, false, true, false, false),
        SYNC_TEMPO("STC", null, null, "Synced tempo codes", false, false, false, true, false, false),
        TDAT("TDA", null, null, "Text: Date", false, false, false, true, false, false),
        TIME("TIM", null, null, "Text: Time", false, false, false, true, false, false),
        TITLE("TT2", GenericFieldKey.TITLE, ID3v22FieldKey.TITLE, "Text: Title/Songname/Content description", true, false, false, true, false, false),
        TITLE_REFINEMENT("TT3", GenericFieldKey.SUBTITLE, ID3v22FieldKey.SUBTITLE, "Text: Subtitle/Description refinement", false, false, false, true, false, false),
        TORY("TOR", GenericFieldKey.ORIGINAL_YEAR, ID3v22FieldKey.ORIGINAL_YEAR, "Text: Original release year", false, false, false, true, false, false),
        TRACK("TRK", GenericFieldKey.TRACK, ID3v22FieldKey.TRACK, "Text: Track number/Position in setField", true, false, false, true, false, false),
        TRDA("TRD", null, null, "Text: Recording dates", false, false, false, true, false, false),
        TSIZ("TSI", null, null, "Text: Size", false, false, false, true, false, false),
        TYER("TYE", GenericFieldKey.YEAR, ID3v22FieldKey.YEAR, "Text: Year", true, false, false, true, false, false),
        UNIQUE_FILE_ID("UFI", GenericFieldKey.MUSICBRAINZ_TRACK_ID, ID3v22FieldKey.MUSICBRAINZ_TRACK_ID, "Unique file identifier", false, true, true, true, false, false),
        UNSYNC_LYRICS("ULT", GenericFieldKey.LYRICS, ID3v22FieldKey.LYRICS, "Unsychronized lyric/text transcription", false, false, true, true, false, false),
        URL_ARTIST_WEB("WAR", GenericFieldKey.URL_OFFICIAL_ARTIST_SITE, ID3v22FieldKey.URL_OFFICIAL_ARTIST_SITE, "URL: Official artist/performer webpage", false, false, true, true, false, false),
        URL_COMMERCIAL("WCM", null, null, "URL: Commercial information", false, false, false, true, false, false),
        URL_COPYRIGHT("WCP", null, null, "URL: Copyright/Legal information", false, false, false, true, false, false),
        URL_FILE_WEB("WAF", null, null, "URL: Official audio file webpage", false, false, false, true, false, false),
        URL_OFFICIAL_RADIO("WRS", null, null, "URL: Official radio station", false, false, false, true, false, false),
        URL_PAYMENT("WPAY", null, null, "URL: Official payment site", false, false, false, true, false, false),
        URL_PUBLISHERS("WPB", null, null, "URL: Publishers official webpage", false, false, false, true, false, false),
        URL_SOURCE_WEB("WAS", null, null, "URL: Official audio source webpage", false, false, false, true, false, false),
        USER_DEFINED_INFO("TXX", GenericFieldKey.ACOUSTID_FINGERPRINT, ID3v22FieldKey.ACOUSTID_FINGERPRINT, "User defined text information frame", false, false, true, true, false, false),
        USER_DEFINED_URL("WXX", GenericFieldKey.URL_DISCOGS_ARTIST_SITE, ID3v22FieldKey.URL_DISCOGS_ARTIST_SITE, "User defined URL link frame", false, false, true, true, false, false),
        IS_COMPILATION("TCP", GenericFieldKey.IS_COMPILATION, ID3v22FieldKey.IS_COMPILATION, "Is Compilation", false, false, false, false, true, false),
        TITLE_SORT_ORDER_ITUNES("TST", GenericFieldKey.TITLE_SORT, ID3v22FieldKey.TITLE_SORT, "Text: title sort order", false, false, false, false, true, false),
        ARTIST_SORT_ORDER_ITUNES("TSP", GenericFieldKey.ARTIST_SORT, ID3v22FieldKey.ARTIST_SORT, "Text: artist sort order", false, false, false, false, true, false),
        ALBUM_SORT_ORDER_ITUNES("TSA", GenericFieldKey.ALBUM_SORT, ID3v22FieldKey.ALBUM_SORT, "Text: album sort order", false, false, false, false, true, false),
        ALBUM_ARTIST_SORT_ORDER_ITUNES("TS2", GenericFieldKey.ALBUM_ARTIST_SORT, ID3v22FieldKey.ALBUM_ARTIST_SORT, "Text:Album Artist Sort Order Frame", false, false, false, false, true, false),
        COMPOSER_SORT_ORDER_ITUNES("TSC", GenericFieldKey.COMPOSER_SORT, ID3v22FieldKey.COMPOSER_SORT, "Text:Composer Sort Order Frame", false, false, false, false, true, false),
    ;

    companion object {

        fun contains(id: String?): Boolean = entries.any { e -> id == e.id }

        fun fromId(id: String?): ID3v22Frames? = entries.find { e -> id == e.id }

        fun fromFieldKey(fieldKey: GenericFieldKey): ID3v22Frames? = entries.find { e -> fieldKey == e.genericFieldKey }

        fun commonFrames(): List<ID3v22Frames> = entries.filter { e -> e.isCommon }

        fun binaryFrames(): List<ID3v22Frames> = entries.filter { e -> e.isBinary }

        fun multipleFrames(): List<ID3v22Frames> = entries.filter { e -> e.isMultipleAllowed }

        fun supprtedFrames(): List<ID3v22Frames> = entries.filter { e -> e.isSupported }

        fun extensionFrames(): List<ID3v22Frames> = entries.filter { e -> e.isExtension }

        fun discardIfFileAltered(): List<ID3v22Frames> = entries.filter { e -> e.isDiscardedIfFileAltered }

        fun isSupported(id: String?): Boolean = supprtedFrames().any { e -> e.id == id }

        fun isExtension(id: String?): Boolean = extensionFrames().any { e -> e.id == id }

        fun isCommon(id: String?): Boolean = commonFrames().any { e -> e.id == id }

        fun isBinary(id: String?): Boolean = binaryFrames().any { e -> e.id == id }

        fun isMultipleAllowed(id: String?): Boolean = multipleFrames().any { e -> e.id == id }

        fun isDiscardedIfFileAltered(id: String?): Boolean = discardIfFileAltered().any { e -> e.id == id }

        fun getValueToIdMap(): Map<String, String> = entries.associate { e -> Pair(e.friendlyName, e.id) }

        fun getIdToValueMap(): Map<String, String> = entries.associate { e -> Pair(e.id, e.friendlyName) }
    }
}