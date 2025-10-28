package de.visualdigits.kaudiotagger.model.id3.types

import de.visualdigits.kaudiotagger.model.common.types.GenericFieldKey

enum class ID3V23Frame(
    override val id: String,
    override val genericFieldKey: GenericFieldKey?,
    override val fieldKey: ID3v23FieldKey?,
    override val friendlyName: String,
    override val isCommon: Boolean,
    override val isBinary: Boolean,
    override val isMultipleAllowed: Boolean,
    override val isSupported: Boolean,
    override val isExtension: Boolean,
    override val isDiscardedIfFileAltered: Boolean
) : Frame {

        ACCOMPANIMENT("TPE2", GenericFieldKey.ALBUM_ARTIST, ID3v23FieldKey.ALBUM_ARTIST, "Text: Band/Orchestra/Accompaniment", false, false, false, true, false, false),
        ALBUM("TALB", GenericFieldKey.ALBUM, ID3v23FieldKey.ALBUM, "Text: Album/Movie/Show title", true, false, false, true, false, false),
        ARTIST("TPE1", GenericFieldKey.ARTIST, ID3v23FieldKey.ARTIST, "Text: Lead artist(s)/Lead performer(s)/Soloist(s)/Performing group", true, false, false, true, false, false),
        ATTACHED_PICTURE("APIC", GenericFieldKey.COVER_ART, ID3v23FieldKey.COVER_ART, "Attached picture", false, true, true, true, false, false),
        AUDIO_ENCRYPTION("AENC", null, null, "Audio encryption", false, true, false, true, false, false),
        BPM("TBPM", GenericFieldKey.BPM, ID3v23FieldKey.BPM, "Text: BPM (Beats Per Minute)", false, false, false, true, false, false),
        COMMENT("COMM", GenericFieldKey.COMMENT, ID3v23FieldKey.COMMENT, "Comments", true, false, true, true, false, false),
        COMMERCIAL_FRAME("COMR", null, null, "", false, false, false, true, false, false),
        COMPOSER("TCOM", GenericFieldKey.COMPOSER, ID3v23FieldKey.COMPOSER, "Text: Composer", false, false, false, true, false, false),
        CONDUCTOR("TPE3", GenericFieldKey.CONDUCTOR, ID3v23FieldKey.CONDUCTOR, "Text: Conductor/Performer refinement", false, false, false, true, false, false),
        CONTENT_GROUP_DESC("TIT1", GenericFieldKey.GROUPING, ID3v23FieldKey.GROUPING, "Text: Content group description", false, false, false, true, false, false),
        COPYRIGHTINFO("TCOP", null, null, "Text: Copyright message", false, false, false, true, false, false),
        ENCODEDBY("TENC", GenericFieldKey.ENCODER, ID3v23FieldKey.ENCODER, "Text: Encoded by", false, false, false, true, false, true),
        ENCRYPTION("ENCR", null, null, "Encryption method registration", false, true, false, true, false, false),
        EQUALISATION("EQUA", null, null, "Equalization", false, true, false, true, false, true),
        EVENT_TIMING_CODES("ETCO", null, null, "Event timing codes", false, true, false, true, false, true),
        FILE_OWNER("TOWN", null, null, "", false, false, false, true, false, false),
        FILE_TYPE("TFLT", null, null, "Text: File type", false, false, false, true, false, false),
        GENERAL_ENCAPS_OBJECT("GEOB", null, null, "General encapsulated datatype", false, true, true, true, false, false),
        GENRE("TCON", GenericFieldKey.GENRE, ID3v23FieldKey.GENRE, "Text: Content type", true, false, false, true, false, false),
        GROUP_ID_REG("GRID", null, null, "", false, false, false, true, false, false),
        HW_SW_SETTINGS("TSSE", null, null, "Text: Software/hardware and settings used for encoding", false, false, false, true, false, false),
        INITIAL_KEY("TKEY", GenericFieldKey.KEY, ID3v23FieldKey.KEY, "Text: Initial key", false, false, false, true, false, false),
        INVOLVED_PEOPLE("IPLS", GenericFieldKey.ARRANGER, ID3v23FieldKey.ARRANGER, "Involved people list", false, false, false, true, false, false),
        ISRC("TSRC", GenericFieldKey.ISRC, ID3v23FieldKey.ISRC, "Text: ISRC (International Standard Recording Code)", false, false, false, true, false, false),
        ITUNES_GROUPING("GRP1", GenericFieldKey.ITUNES_GROUPING, ID3v23FieldKey.ITUNES_GROUPING, "Text: iTunes Grouping", false, false, false, true, false, false),
        LANGUAGE("TLAN", GenericFieldKey.LANGUAGE, ID3v23FieldKey.LANGUAGE, "Text: Language(s)", false, false, false, true, false, false),
        LENGTH("TLEN", null, null, "Text: Length", false, false, false, true, false, true),
        LINKED_INFO("LINK", null, null, "Linked information", false, false, false, true, false, false),
        LYRICIST("TEXT", GenericFieldKey.LYRICIST, ID3v23FieldKey.LYRICIST, "Text: Lyricist/text writer", false, false, false, true, false, false),
        MEDIA_TYPE("TMED", GenericFieldKey.MEDIA, ID3v23FieldKey.MEDIA, "Text: Media type", false, false, false, true, false, false),
        MOVEMENT("MVNM", GenericFieldKey.MOVEMENT, ID3v23FieldKey.MOVEMENT, "Text: Movement", false, false, false, true, false, false),
        MOVEMENT_NO("MVIN", GenericFieldKey.MOVEMENT_NO, ID3v23FieldKey.MOVEMENT_NO, "Text: Movement No", false, false, false, true, false, false),
        MPEG_LOCATION_LOOKUP_TABLE("MLLT", null, null, "MPEG location lookup table", false, false, false, true, false, true),
        MUSIC_CD_ID("MCDI", null, null, "Music CD Identifier", false, false, false, true, false, false),
        ORIGARTIST("TOPE", GenericFieldKey.ORIGINAL_ARTIST, ID3v23FieldKey.ORIGINAL_ARTIST, "Text: Original artist(s)/performer(s)", false, false, false, true, false, false),
        ORIG_FILENAME("TOFN", null, null, "Text: Original filename", false, false, false, true, false, false),
        ORIG_LYRICIST("TOLY", GenericFieldKey.ORIGINAL_LYRICIST, ID3v23FieldKey.ORIGINAL_LYRICIST, "Text: Original Lyricist(s)/text writer(s)", false, false, false, true, false, false),
        ORIG_TITLE("TOAL", GenericFieldKey.ORIGINAL_ALBUM, ID3v23FieldKey.ORIGINAL_ALBUM, "Text: Original album/Movie/Show title", false, false, false, true, false, false),
        OWNERSHIP("OWNE", null, null, "", false, false, false, true, false, false),
        PLAYLIST_DELAY("TDLY", null, null, "Text: Playlist delay", false, false, false, true, false, false),
        PLAY_COUNTER("PCNT", null, null, "Play counter", false, false, false, true, false, false),
        POPULARIMETER("POPM", GenericFieldKey.RATING, ID3v23FieldKey.RATING, "Popularimeter", false, false, true, true, false, false),
        POSITION_SYNC("POSS", null, null, "Position Sync", false, false, false, true, false, true),
        PRIVATE("PRIV", null, null, "Private frame", false, false, true, true, false, false),
        PUBLISHER("TPUB", GenericFieldKey.RECORD_LABEL, ID3v23FieldKey.RECORD_LABEL, "Text: Publisher", false, false, false, true, false, false),
        RADIO_NAME("TRSN", null, null, "", false, false, false, true, false, false),
        RADIO_OWNER("TRSO", null, null, "", false, false, false, true, false, false),
        RECOMMENDED_BUFFER_SIZE("RBUF", null, null, "Recommended buffer size", false, true, false, true, false, false),
        RELATIVE_VOLUME_ADJUSTMENT("RVAD", null, null, "Relative volume adjustment", false, true, false, true, false, true),
        REMIXED("TPE4", GenericFieldKey.REMIXER, ID3v23FieldKey.REMIXER, "Text: Interpreted, remixed, or otherwise modified by", false, false, false, true, false, false),
        REVERB("RVRB", null, null, "Reverb", false, false, false, true, false, false),
        SET("TPOS", GenericFieldKey.DISC_NO, ID3v23FieldKey.DISC_NO, "Text: Part of a setField", false, false, false, true, false, false),
        SET_SUBTITLE("TSST", GenericFieldKey.DISC_SUBTITLE, ID3v23FieldKey.DISC_SUBTITLE, "Text: SubTitle", false, false, false, true, false, false),
        SYNC_LYRIC("SYLT", null, null, "Synchronized lyric/text", false, false, false, true, false, true),
        SYNC_TEMPO("SYTC", null, null, "Synced tempo codes", false, false, false, true, false, true),
        TDAT("TDAT", null, null, "Text: Date", false, false, false, true, false, false),
        TERMS_OF_USE("USER", null, null, "", false, false, false, true, false, false),
        TIME("TIME", null, null, "Text: Time", false, false, false, true, false, false),
        TITLE("TIT2", GenericFieldKey.TITLE, ID3v23FieldKey.TITLE, "Text: Title/Songname/Content description", true, false, false, true, false, false),
        TITLE_REFINEMENT("TIT3", GenericFieldKey.SUBTITLE, ID3v23FieldKey.SUBTITLE, "Text: Subtitle/Description refinement", false, false, false, true, false, false),
        TORY("TORY", GenericFieldKey.ORIGINAL_YEAR, ID3v23FieldKey.ORIGINAL_YEAR, "Text: Original release year", false, false, false, true, false, false),
        TRACK("TRCK", GenericFieldKey.TRACK, ID3v23FieldKey.TRACK, "Text: Track number/Position in setField", true, false, false, true, false, false),
        TRDA("TRDA", null, null, "Text: Recording dates", false, false, false, true, false, false),
        TSIZ("TSIZ", null, null, "Text: Size", false, false, false, true, false, true),
        TYER("TYER", GenericFieldKey.YEAR, ID3v23FieldKey.YEAR, "Text: Year", true, false, false, true, false, false),
        UNIQUE_FILE_ID("UFID", GenericFieldKey.MUSICBRAINZ_TRACK_ID, ID3v23FieldKey.MUSICBRAINZ_TRACK_ID, "Unique file identifier", false, true, true, true, false, false),
        UNSYNC_LYRICS("USLT", GenericFieldKey.LYRICS, ID3v23FieldKey.LYRICS, "Unsychronized lyric/text transcription", false, false, true, true, false, false),
        URL_ARTIST_WEB("WOAR", GenericFieldKey.URL_OFFICIAL_ARTIST_SITE, ID3v23FieldKey.URL_OFFICIAL_ARTIST_SITE, "URL: Official artist/performer webpage", false, false, true, true, false, false),
        URL_COMMERCIAL("WCOM", null, null, "URL: Commercial information", false, false, false, true, false, false),
        URL_COPYRIGHT("WCOP", null, null, "URL: Copyright/Legal information", false, false, false, true, false, false),
        URL_FILE_WEB("WOAF", null, null, "URL: Official audio file webpage", false, false, false, true, false, false),
        URL_OFFICIAL_RADIO("WORS", null, null, "Official Radio", false, false, false, true, false, false),
        URL_PAYMENT("WPAY", null, null, "URL: Payment", false, false, false, true, false, false),
        URL_PUBLISHERS("WPUB", null, null, "URL: Publishers official webpage", false, false, false, true, false, false),
        URL_SOURCE_WEB("WOAS", null, null, "URL: Official audio source webpage", false, false, false, true, false, false),
        USER_DEFINED_INFO("TXXX", GenericFieldKey.ACOUSTID_FINGERPRINT, ID3v23FieldKey.ACOUSTID_FINGERPRINT, "User defined text information frame", false, false, true, true, false, false),
        USER_DEFINED_URL("WXXX", GenericFieldKey.URL_DISCOGS_ARTIST_SITE, ID3v23FieldKey.URL_DISCOGS_ARTIST_SITE, "User defined URL link frame", false, false, true, true, false, false),
        IS_COMPILATION("TCMP", GenericFieldKey.IS_COMPILATION, ID3v23FieldKey.IS_COMPILATION, "Is Compilation", false, false, false, false, true, false),
        TITLE_SORT_ORDER_ITUNES("TSOT", GenericFieldKey.TITLE_SORT, ID3v23FieldKey.TITLE_SORT, "Text: title sort order", false, false, false, false, true, false),
        ARTIST_SORT_ORDER_ITUNES("TSOP", GenericFieldKey.ARTIST_SORT, ID3v23FieldKey.ARTIST_SORT, "Text: artist sort order", false, false, false, false, true, false),
        ALBUM_SORT_ORDER_ITUNES("TSOA", GenericFieldKey.ALBUM_SORT, ID3v23FieldKey.ALBUM_SORT, "Text: album sort order", false, false, false, false, true, false),
        TITLE_SORT_ORDER_MUSICBRAINZ("XSOT", null, null, "Text: title sort order", false, false, false, false, true, false),
        ARTIST_SORT_ORDER_MUSICBRAINZ("XSOP", null, null, "Text: artist sort order", false, false, false, false, true, false),
        ALBUM_SORT_ORDER_MUSICBRAINZ("XSOA", null, null, "Text: album sort order", false, false, false, false, true, false),
        ALBUM_ARTIST_SORT_ORDER_ITUNES("TSO2", GenericFieldKey.ALBUM_ARTIST_SORT, ID3v23FieldKey.ALBUM_ARTIST_SORT, "Text:Album Artist Sort Order Frame", false, false, false, false, true, false),
        COMPOSER_SORT_ORDER_ITUNES("TSOC", GenericFieldKey.COMPOSER_SORT, ID3v23FieldKey.COMPOSER_SORT, "Text:Composer Sort Order Frame", false, false, false, false, true, false),
    ;

    companion object {

        fun contains(id: String?): Boolean = entries.any { e -> id == e.id }

        fun fromId(id: String?): ID3V23Frame? = entries.find { e -> id == e.id }

        fun fromFieldKey(fieldKey: GenericFieldKey): ID3V23Frame? = entries.find { e -> fieldKey == e.genericFieldKey }

        fun commonFrames(): List<ID3V23Frame> = entries.filter { e -> e.isCommon }

        fun binaryFrames(): List<ID3V23Frame> = entries.filter { e -> e.isBinary }

        fun multipleFrames(): List<ID3V23Frame> = entries.filter { e -> e.isMultipleAllowed }

        fun supprtedFrames(): List<ID3V23Frame> = entries.filter { e -> e.isSupported }

        fun extensionFrames(): List<ID3V23Frame> = entries.filter { e -> e.isExtension }

        fun discardIfFileAltered(): List<ID3V23Frame> = entries.filter { e -> e.isDiscardedIfFileAltered }

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