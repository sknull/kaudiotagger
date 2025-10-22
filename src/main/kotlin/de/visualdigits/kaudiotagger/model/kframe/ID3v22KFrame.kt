package de.visualdigits.kaudiotagger.model.kframe

import de.visualdigits.kaudiotagger.model.kfield.KFieldKey

enum class ID3v22KFrame(
    override val id: String,
    override val fieldKey: KFieldKey?,
    override val friendlyName: String,
    override val isCommon: Boolean,
    override val isBinary: Boolean,
    override val isMultipleAllowed: Boolean,
    override val isSupported: Boolean,
    override val isExtension: Boolean,
    override val isDiscardedIfFileAltered: Boolean
) : KFrame<ID3v22KFrame> {

        ACCOMPANIMENT("TP2", KFieldKey.ALBUM_ARTIST, "Text: Band/Orchestra/Accompaniment", false, false, false, true, false, false),
        ALBUM("TAL", KFieldKey.ALBUM, "Text: Album/Movie/Show title", true, false, false, true, false, false),
        ARTIST("TP1", KFieldKey.ARTIST, "Text: Lead artist(s)/Lead performer(s)/Soloist(s)/Performing group", true, false, false, true, false, false),
        ATTACHED_PICTURE("PIC", KFieldKey.COVER_ART, "Attached picture", false, true, true, true, false, false),
        AUDIO_ENCRYPTION("CRA", null, "Audio encryption", false, true, false, true, false, false),
        BPM("TBP", KFieldKey.BPM, "Text: BPM (Beats Per Minute)", false, false, false, true, false, false),
        COMMENT("COM", KFieldKey.COMMENT, "Comments", true, false, true, true, false, false),
        COMPOSER("TCM", KFieldKey.COMPOSER, "Text: Composer", false, false, false, true, false, false),
        CONDUCTOR("TPE", KFieldKey.CONDUCTOR, "Text: Conductor/Performer refinement", false, false, false, true, false, false),
        CONTENT_GROUP_DESC("TT1", KFieldKey.GROUPING, "Text: Content group description", false, false, false, true, false, false),
        COPYRIGHTINFO("TCR", null, "Text: Copyright message", false, false, false, true, false, false),
        ENCODEDBY("TEN", KFieldKey.ENCODER, "Text: Encoded by", false, false, false, true, false, false),
        ENCRYPTED_FRAME("CRM", null, "Encrypted meta frame", false, true, false, true, false, false),
        EQUALISATION("EQU", null, "Equalization", false, true, false, true, false, false),
        EVENT_TIMING_CODES("ETC", null, "Event timing codes", false, true, false, true, false, false),
        FILE_TYPE("TFT", null, "Text: File type", false, false, false, true, false, false),
        GENERAL_ENCAPS_OBJECT("GEO", null, "General encapsulated datatype", false, true, true, true, false, false),
        GENRE("TCO", KFieldKey.GENRE, "Text: Content type", true, false, false, true, false, false),
        HW_SW_SETTINGS("TSS", null, "Text: Software/hardware and settings used for encoding", false, false, false, true, false, false),
        INITIAL_KEY("TKE", KFieldKey.KEY, "Text: Initial key", false, false, false, true, false, false),
        IPLS("IPL", KFieldKey.ARRANGER, "Involved people list", false, false, false, true, false, false),
        ISRC("TRC", KFieldKey.ISRC, "Text: ISRC (International Standard Recording Code)", false, false, false, true, false, false),
        ITUNES_GROUPING("GP1", KFieldKey.ITUNES_GROUPING, "iTunes Grouping", false, false, false, true, false, false),
        LANGUAGE("TLA", KFieldKey.LANGUAGE, "Text: Language(s)", false, false, false, true, false, false),
        LENGTH("TLE", null, "Text: Length", false, false, false, true, false, false),
        LINKED_INFO("LNK", null, "Linked information", false, false, false, true, false, false),
        LYRICIST("TXT", KFieldKey.LYRICIST, "Text: Lyricist/text writer", false, false, false, true, false, false),
        MEDIA_TYPE("TMT", KFieldKey.MEDIA, "Text: Media type", false, false, false, true, false, false),
        MOVEMENT("MVN", KFieldKey.MOVEMENT, "Text: Movement", false, false, false, true, false, false),
        MOVEMENT_NO("MVI", KFieldKey.MOVEMENT_NO, "Text: Movement No", false, false, false, true, false, false),
        MPEG_LOCATION_LOOKUP_TABLE("MLL", null, "MPEG location lookup table", false, false, false, true, false, false),
        MUSIC_CD_ID("MCI", null, "Music CD Identifier", false, false, false, true, false, false),
        ORIGARTIST("TOA", KFieldKey.ORIGINAL_ARTIST, "Text: Original artist(s)/performer(s)", false, false, false, true, false, false),
        ORIG_FILENAME("TOF", null, "Text: Original filename", false, false, false, true, false, false),
        ORIG_LYRICIST("TOL", KFieldKey.ORIGINAL_LYRICIST, "Text: Original Lyricist(s)/text writer(s)", false, false, false, true, false, false),
        ORIG_TITLE("TOT", KFieldKey.ORIGINAL_ALBUM, "Text: Original album/Movie/Show title", false, false, false, true, false, false),
        PLAYLIST_DELAY("TDY", null, "Text: Playlist delay", false, false, false, true, false, false),
        PLAY_COUNTER("CNT", null, "Play counter", false, false, false, true, false, false),
        POPULARIMETER("POP", KFieldKey.RATING, "Popularimeter", false, false, true, true, false, false),
        PUBLISHER("TPB", KFieldKey.RECORD_LABEL, "Text: Publisher", false, false, false, true, false, false),
        RECOMMENDED_BUFFER_SIZE("BUF", null, "Recommended buffer size", false, true, false, true, false, false),
        RELATIVE_VOLUME_ADJUSTMENT("RVA", null, "Relative volume adjustment", false, true, false, true, false, false),
        REMIXED("TP4", KFieldKey.REMIXER, "Text: Interpreted, remixed, or otherwise modified by", false, false, false, true, false, false),
        REVERB("REV", null, "Reverb", false, false, false, true, false, false),
        SET("TPA", KFieldKey.DISC_NO, "Text: Part of a setField", false, false, false, true, false, false),
        SET_SUBTITLE("TPS", KFieldKey.DISC_SUBTITLE, "Text: Set subtitle", false, false, false, false, false, false),
        SYNC_LYRIC("SLT", null, "Synchronized lyric/text", false, false, false, true, false, false),
        SYNC_TEMPO("STC", null, "Synced tempo codes", false, false, false, true, false, false),
        TDAT("TDA", null, "Text: Date", false, false, false, true, false, false),
        TIME("TIM", null, "Text: Time", false, false, false, true, false, false),
        TITLE("TT2", KFieldKey.TITLE, "Text: Title/Songname/Content description", true, false, false, true, false, false),
        TITLE_REFINEMENT("TT3", KFieldKey.SUBTITLE, "Text: Subtitle/Description refinement", false, false, false, true, false, false),
        TORY("TOR", KFieldKey.ORIGINAL_YEAR, "Text: Original release year", false, false, false, true, false, false),
        TRACK("TRK", KFieldKey.TRACK, "Text: Track number/Position in setField", true, false, false, true, false, false),
        TRDA("TRD", null, "Text: Recording dates", false, false, false, true, false, false),
        TSIZ("TSI", null, "Text: Size", false, false, false, true, false, false),
        TYER("TYE", KFieldKey.YEAR, "Text: Year", true, false, false, true, false, false),
        UNIQUE_FILE_ID("UFI", KFieldKey.MUSICBRAINZ_TRACK_ID, "Unique file identifier", false, true, true, true, false, false),
        UNSYNC_LYRICS("ULT", KFieldKey.LYRICS, "Unsychronized lyric/text transcription", false, false, true, true, false, false),
        URL_ARTIST_WEB("WAR", KFieldKey.URL_OFFICIAL_ARTIST_SITE, "URL: Official artist/performer webpage", false, false, true, true, false, false),
        URL_COMMERCIAL("WCM", null, "URL: Commercial information", false, false, false, true, false, false),
        URL_COPYRIGHT("WCP", null, "URL: Copyright/Legal information", false, false, false, true, false, false),
        URL_FILE_WEB("WAF", null, "URL: Official audio file webpage", false, false, false, true, false, false),
        URL_OFFICIAL_RADIO("WRS", null, "URL: Official radio station", false, false, false, true, false, false),
        URL_PAYMENT("WPAY", null, "URL: Official payment site", false, false, false, true, false, false),
        URL_PUBLISHERS("WPB", null, "URL: Publishers official webpage", false, false, false, true, false, false),
        URL_SOURCE_WEB("WAS", null, "URL: Official audio source webpage", false, false, false, true, false, false),
        USER_DEFINED_INFO("TXX", KFieldKey.ACOUSTID_FINGERPRINT, "User defined text information frame", false, false, true, true, false, false),
        USER_DEFINED_URL("WXX", KFieldKey.URL_DISCOGS_ARTIST_SITE, "User defined URL link frame", false, false, true, true, false, false),
        IS_COMPILATION("TCP", KFieldKey.IS_COMPILATION, "Is Compilation", false, false, false, false, true, false),
        TITLE_SORT_ORDER_ITUNES("TST", KFieldKey.TITLE_SORT, "Text: title sort order", false, false, false, false, true, false),
        ARTIST_SORT_ORDER_ITUNES("TSP", KFieldKey.ARTIST_SORT, "Text: artist sort order", false, false, false, false, true, false),
        ALBUM_SORT_ORDER_ITUNES("TSA", KFieldKey.ALBUM_SORT, "Text: album sort order", false, false, false, false, true, false),
        ALBUM_ARTIST_SORT_ORDER_ITUNES("TS2", KFieldKey.ALBUM_ARTIST_SORT, "Text:Album Artist Sort Order Frame", false, false, false, false, true, false),
        COMPOSER_SORT_ORDER_ITUNES("TSC", KFieldKey.COMPOSER_SORT, "Text:Composer Sort Order Frame", false, false, false, false, true, false),
    ;

    companion object {

        fun contains(id: String?): Boolean = entries.any { e -> id == e.id }

        fun fromId(id: String?): ID3v22KFrame? = entries.find { e -> id == e.id }

        fun fromFieldKey(fieldKey: KFieldKey): ID3v22KFrame? = entries.find { e -> fieldKey == e.fieldKey }

        fun commonFrames(): List<ID3v22KFrame> = entries.filter { e -> e.isCommon }

        fun binaryFrames(): List<ID3v22KFrame> = entries.filter { e -> e.isBinary }

        fun multipleFrames(): List<ID3v22KFrame> = entries.filter { e -> e.isMultipleAllowed }

        fun supprtedFrames(): List<ID3v22KFrame> = entries.filter { e -> e.isSupported }

        fun extensionFrames(): List<ID3v22KFrame> = entries.filter { e -> e.isExtension }

        fun discardIfFileAltered(): List<ID3v22KFrame> = entries.filter { e -> e.isDiscardedIfFileAltered }

        fun isSupported(id: String?): Boolean = supprtedFrames().any { e -> e.id == id }

        fun isExtension(id: String?): Boolean = extensionFrames().any { e -> e.id == id }

        fun isCommon(id: String?): Boolean = commonFrames().any { e -> e.id == id }

        fun isBinary(id: String?): Boolean = binaryFrames().any { e -> e.id == id }

        fun isMultipleAllowed(id: String?): Boolean = multipleFrames().any { e -> e.id == id }

        fun isDiscardedIfFileAltered(id: String?): Boolean = discardIfFileAltered().any { e -> e.id == id }
    }
}