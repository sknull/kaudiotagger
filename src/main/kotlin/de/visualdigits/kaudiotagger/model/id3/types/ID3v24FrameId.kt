package de.visualdigits.kaudiotagger.model.id3.types

import de.visualdigits.kaudiotagger.model.common.types.GenericFieldKey

enum class ID3v24FrameId(
    override val id: String,
    override val genericFieldKey: GenericFieldKey?,
    override val fieldKey: ID3v24FieldKey?,
    override val friendlyName: String,
    override val isCommon: Boolean,
    override val isBinary: Boolean,
    override val isMultipleAllowed: Boolean,
    override val isSupported: Boolean,
    override val isExtension: Boolean,
    override val isDiscardedIfFileAltered: Boolean
) : FrameId {

        ACCOMPANIMENT("TPE2", GenericFieldKey.ALBUM_ARTIST, ID3v24FieldKey.ALBUM_ARTIST, "Text: Band/Orchestra/Accompaniment", false, false, false, true, false, false),
        ALBUM("TALB", GenericFieldKey.ALBUM, ID3v24FieldKey.ALBUM, "Text: Album/Movie/Show title", true, false, false, true, false, false),
        ALBUM_SORT_ORDER("TSOA", GenericFieldKey.ALBUM_SORT, ID3v24FieldKey.ALBUM_SORT, "Album sort order", false, false, false, true, false, false),
        ARTIST("TPE1", GenericFieldKey.ARTIST, ID3v24FieldKey.ARTIST, "Text: Lead artist(s)/Lead performer(s)/Soloist(s)/Performing group", true, false, false, true, false, false),
        ATTACHED_PICTURE("APIC", GenericFieldKey.COVER_ART, ID3v24FieldKey.COVER_ART, "Attached picture", false, true, true, true, false, false),
        AUDIO_ENCRYPTION("AENC", null, null, "Audio encryption", false, true, false, true, false, false),
        AUDIO_SEEK_POINT_INDEX("ASPI", null, null, "Audio seek point index", false, false, false, true, false, false),
        BPM("TBPM", GenericFieldKey.BPM, ID3v24FieldKey.BPM, "Text: BPM (Beats Per Minute)", false, false, false, true, false, false),
        COMMENT("COMM", GenericFieldKey.COMMENT, ID3v24FieldKey.COMMENT, "Comments", true, false, true, true, false, false),
        COMMERCIAL_FRAME("COMR", null, null, "Commercial Frame", false, false, false, true, false, false),
        COMPOSER("TCOM", GenericFieldKey.COMPOSER, ID3v24FieldKey.COMPOSER, "Text: Composer", false, false, false, true, false, false),
        CONDUCTOR("TPE3", GenericFieldKey.CONDUCTOR, ID3v24FieldKey.CONDUCTOR, "Text: Conductor/Performer refinement", false, false, false, true, false, false),
        CONTENT_GROUP_DESC("TIT1", GenericFieldKey.GROUPING, ID3v24FieldKey.GROUPING, "Text: Content group description", false, false, false, true, false, false),
        COPYRIGHTINFO("TCOP", null, null, "Text: Copyright message", false, false, false, true, false, false),
        DISC_TOTAL("TPOS", GenericFieldKey.DISC_TOTAL, ID3v24FieldKey.DISC_TOTAL, "Musicbrainz Disc Total", false, false, false, true, false, false),
        ENCODEDBY("TENC", GenericFieldKey.ENCODER, ID3v24FieldKey.ENCODER, "Text: Encoded by", false, false, false, true, false, true),
        ENCODING_TIME("TDEN", null, null, "Text: Encoding time", false, false, false, true, false, false),
        ENCRYPTION("ENCR", null, null, "Encryption method registration", false, true, false, true, false, false),
        EQUALISATION2("EQU2", null, null, "Equalization (2)", false, true, false, true, false, false),
        EVENT_TIMING_CODES("ETCO", null, null, "Event timing codes", false, true, false, true, false, true),
        FILE_OWNER("TOWN", null, null, "Text:File Owner", false, false, false, true, false, false),
        FILE_TYPE("TFLT", null, null, "Text: File type", false, false, false, true, false, false),
        GENERAL_ENCAPS_OBJECT("GEOB", null, null, "General encapsulated datatype", false, true, true, true, false, false),
        GENRE("TCON", GenericFieldKey.GENRE, ID3v24FieldKey.GENRE, "Text: Content type", true, false, false, true, false, false),
        GROUP_ID_REG("GRID", null, null, "Group ID Registration", false, false, false, true, false, false),
        HW_SW_SETTINGS("TSSE", null, null, "Text: Software/hardware and settings used for encoding", false, false, false, true, false, false),
        INITIAL_KEY("TKEY", GenericFieldKey.KEY, ID3v24FieldKey.KEY, "Text: Initial key", false, false, false, true, false, false),
        INVOLVED_PEOPLE("TIPL", GenericFieldKey.ARRANGER, ID3v24FieldKey.ARRANGER, "Involved people list", false, false, false, true, false, false),
        ISRC("TSRC", GenericFieldKey.ISRC, ID3v24FieldKey.ISRC, "Text: ISRC (International Standard Recording Code)", false, false, false, true, false, false),
        ITUNES_GROUPING("GRP1", GenericFieldKey.ITUNES_GROUPING, ID3v24FieldKey.ITUNES_GROUPING, "iTunes Grouping", false, false, false, true, false, false),
        LANGUAGE("TLAN", GenericFieldKey.LANGUAGE, ID3v24FieldKey.LANGUAGE, "Text: Language(s)", false, false, false, true, false, false),
        LENGTH("TLEN", null, null, "Text: Length", false, false, false, true, false, true),
        LINKED_INFO("LINK", null, null, "Linked information", false, false, false, true, false, false),
        LYRICIST("TEXT", GenericFieldKey.LYRICIST, ID3v24FieldKey.LYRICIST, "Text: Lyricist/text writer", false, false, false, true, false, false),
        MEDIA_TYPE("TMED", GenericFieldKey.MEDIA, ID3v24FieldKey.MEDIA, "Text: Media type", false, false, false, true, false, false),
        MOOD("TMOO", GenericFieldKey.MOOD, ID3v24FieldKey.MOOD, "Text: Mood", false, false, false, true, false, false),
        MOVEMENT("MVNM", GenericFieldKey.MOVEMENT, ID3v24FieldKey.MOVEMENT, "Text: Movement", false, false, false, true, false, false),
        MOVEMENT_NO("MVIN", GenericFieldKey.MOVEMENT_NO, ID3v24FieldKey.MOVEMENT_NO, "Text: Movement No", false, false, false, true, false, false),
        MPEG_LOCATION_LOOKUP_TABLE("MLLT", null, null, "MPEG location lookup table", false, false, false, true, false, true),
        MUSICIAN_CREDITS("TMCL", null, null, "Musical Credits", false, false, false, false, false, false),
        MUSIC_CD_ID("MCDI", null, null, "Music CD Identifier", false, false, false, true, false, false),
        ORIGARTIST("TOPE", GenericFieldKey.ORIGINAL_ARTIST, ID3v24FieldKey.ORIGINAL_ARTIST, "Text: Original artist(s)/performer(s)", false, false, false, true, false, false),
        ORIGINAL_RELEASE_TIME("TDOR", GenericFieldKey.ORIGINAL_YEAR, ID3v24FieldKey.ORIGINAL_YEAR, "Text: Original release time", false, false, false, true, false, false),
        ORIG_FILENAME("TOFN", null, null, "Text: Original filename", false, false, false, true, false, false),
        ORIG_LYRICIST("TOLY", GenericFieldKey.ORIGINAL_LYRICIST, ID3v24FieldKey.ORIGINAL_LYRICIST, "Text: Original Lyricist(s)/text writer(s)", false, false, false, true, false, false),
        ORIG_TITLE("TOAL", GenericFieldKey.ORIGINAL_ALBUM, ID3v24FieldKey.ORIGINAL_ALBUM, "Text: Original album/Movie/Show title", false, false, false, true, false, false),
        OWNERSHIP("OWNE", null, null, "Ownership", false, false, false, true, false, false),
        ARTIST_SORT_ORDER("TSOP", GenericFieldKey.ARTIST_SORT, ID3v24FieldKey.ARTIST_SORT, "Performance Sort Order", false, false, false, true, false, false),
        PLAYLIST_DELAY("TDLY", null, null, "Text: Playlist delay", false, false, false, true, false, false),
        PLAY_COUNTER("PCNT", null, null, "Play counter", false, false, false, true, false, false),
        POPULARIMETER("POPM", GenericFieldKey.RATING, ID3v24FieldKey.RATING, "Popularimeter", false, false, true, true, false, false),
        POSITION_SYNC("POSS", null, null, "Position Sync", false, false, false, true, false, true),
        PRIVATE("PRIV", null, null, "Private frame", false, false, true, true, false, false),
        PRODUCED_NOTICE("TPRO", null, null, "Produced Notice", false, false, false, true, false, false),
        PUBLISHER("TPUB", GenericFieldKey.RECORD_LABEL, ID3v24FieldKey.RECORD_LABEL, "Text: Publisher", false, false, false, true, false, false),
        RADIO_NAME("TRSN", null, null, "Text: Radio Name", false, false, false, true, false, false),
        RADIO_OWNER("TRSO", null, null, "Text: Radio Owner", false, false, false, true, false, false),
        RECOMMENDED_BUFFER_SIZE("RBUF", null, null, "Recommended buffer size", false, true, false, true, false, false),
        RELATIVE_VOLUME_ADJUSTMENT2("RVA2", null, null, "Relative volume adjustment(2)", false, true, false, true, false, false),
        RELEASE_TIME("TDRL", null, null, "Release Time", false, false, false, true, false, false),
        REMIXED("TPE4", GenericFieldKey.REMIXER, ID3v24FieldKey.REMIXER, "Text: Interpreted, remixed, or otherwise modified by", false, false, false, true, false, false),
        REVERB("RVRB", null, null, "Reverb", false, false, false, true, false, false),
        SEEK("SEEK", null, null, "Seek", false, false, false, true, false, false),
        SET("TPOS", GenericFieldKey.DISC_NO, ID3v24FieldKey.DISC_NO, "Text: Part of a setField", false, false, false, true, false, false),
        SET_SUBTITLE("TSST", GenericFieldKey.DISC_SUBTITLE, ID3v24FieldKey.DISC_SUBTITLE, "Text: Set subtitle", false, false, false, true, false, false),
        SIGNATURE("SIGN", null, null, "Signature", false, false, false, true, false, false),
        SYNC_LYRIC("SYLT", null, null, "Synchronized lyric/text", false, false, false, true, false, true),
        SYNC_TEMPO("SYTC", null, null, "Synced tempo codes", false, false, false, true, false, true),
        TAGGING_TIME("TDTG", null, null, "Text: Tagging time", false, false, false, true, false, false),
        TERMS_OF_USE("USER", null, null, "Terms of Use", false, false, false, true, false, false),
        TITLE("TIT2", GenericFieldKey.TITLE, ID3v24FieldKey.TITLE, "Text: title", true, false, false, true, false, false),
        TITLE_REFINEMENT("TIT3", GenericFieldKey.SUBTITLE, ID3v24FieldKey.SUBTITLE, "Text: Subtitle/Description refinement", false, false, false, true, false, false),
        TITLE_SORT_ORDER("TSOT", GenericFieldKey.TITLE_SORT, ID3v24FieldKey.TITLE_SORT, "Text: title sort order", false, false, false, true, false, false),
        TRACK("TRCK", GenericFieldKey.TRACK, ID3v24FieldKey.TRACK, "Text: Track number/Position in setField", true, false, false, true, false, false),
        TRACK_TOTAL("TRCK", GenericFieldKey.TRACK_TOTAL, ID3v24FieldKey.TRACK_TOTAL, "Text: Track number/Position in setField", true, false, false, true, false, false),
        UNIQUE_FILE_ID("UFID", GenericFieldKey.MUSICBRAINZ_TRACK_ID, ID3v24FieldKey.MUSICBRAINZ_TRACK_ID, "Unique file identifier", false, true, true, true, false, false),
        UNSYNC_LYRICS("USLT", GenericFieldKey.LYRICS, ID3v24FieldKey.LYRICS, "Unsychronized lyric/text transcription", false, false, true, true, false, false),
        URL_ARTIST_WEB("WOAR", GenericFieldKey.URL_OFFICIAL_ARTIST_SITE, ID3v24FieldKey.URL_OFFICIAL_ARTIST_SITE, "URL: Official artist/performer webpage", false, false, true, true, false, false),
        URL_COMMERCIAL("WCOM", null, null, "URL: Commercial information", false, false, false, true, false, false),
        URL_COPYRIGHT("WCOP", null, null, "URL: Copyright/Legal information", false, false, false, true, false, false),
        URL_FILE_WEB("WOAF", null, null, "URL: Official audio file webpage", false, false, false, true, false, false),
        URL_OFFICIAL_RADIO("WORS", null, null, "URL: Official Radio website", false, false, false, true, false, false),
        URL_PAYMENT("WPAY", null, null, "URL: Payment for this recording ", false, false, false, true, false, false),
        URL_PUBLISHERS("WPUB", null, null, "URL: Publishers official webpage", false, false, false, true, false, false),
        URL_SOURCE_WEB("WOAS", null, null, "URL: Official audio source webpage", false, false, false, true, false, false),
        USER_DEFINED_INFO("TXXX", GenericFieldKey.ACOUSTID_FINGERPRINT, ID3v24FieldKey.ACOUSTID_FINGERPRINT, "User defined text information frame", false, false, true, true, false, false),
        USER_DEFINED_URL("WXXX", GenericFieldKey.URL_DISCOGS_ARTIST_SITE, ID3v24FieldKey.URL_DISCOGS_ARTIST_SITE, "User defined URL link frame", false, false, true, true, false, false),
        YEAR("TDRC", GenericFieldKey.YEAR, ID3v24FieldKey.YEAR, "Text:Year", true, false, false, true, false, false),
        IS_COMPILATION("TCMP", GenericFieldKey.IS_COMPILATION, ID3v24FieldKey.IS_COMPILATION, "Is Compilation", false, false, false, false, true, false),
        ALBUM_ARTIST_SORT_ORDER_ITUNES("TSO2", GenericFieldKey.ALBUM_ARTIST_SORT, ID3v24FieldKey.ALBUM_ARTIST_SORT, "Text:Album Artist Sort Order Frame", false, false, false, false, true, false),
        COMPOSER_SORT_ORDER_ITUNES("TSOC", GenericFieldKey.COMPOSER_SORT, ID3v24FieldKey.COMPOSER_SORT, "Text:Composer Sort Order Frame", false, false, false, false, true, false),
    ;

    companion object {

        fun contains(id: String?): Boolean = entries.any { e -> id == e.id }

        fun fromId(id: String?): ID3v24FrameId? = entries.find { e -> id == e.id }

        fun fromFieldKey(fieldKey: GenericFieldKey?): ID3v24FrameId? = entries.find { e -> fieldKey == e.genericFieldKey }

        fun commonFrames(): List<ID3v24FrameId> = entries.filter { e -> e.isCommon }

        fun binaryFrames(): List<ID3v24FrameId> = entries.filter { e -> e.isBinary }

        fun multipleFrames(): List<ID3v24FrameId> = entries.filter { e -> e.isMultipleAllowed }

        fun supprtedFrames(): List<ID3v24FrameId> = entries.filter { e -> e.isSupported }

        fun extensionFrames(): List<ID3v24FrameId> = entries.filter { e -> e.isExtension }

        fun discardIfFileAltered(): List<ID3v24FrameId> = entries.filter { e -> e.isDiscardedIfFileAltered }

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