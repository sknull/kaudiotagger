package de.visualdigits.kaudiotagger.model.kframe

import de.visualdigits.kaudiotagger.model.kfield.KFieldKey

enum class ID3v24KFrame(
    override val id: String,
    override val fieldKey: KFieldKey?,
    override val friendlyName: String,
    override val isCommon: Boolean,
    override val isBinary: Boolean,
    override val isMultipleAllowed: Boolean,
    override val isSupported: Boolean,
    override val isExtension: Boolean,
    override val isDiscardedIfFileAltered: Boolean
) : KFrame<ID3v24KFrame> {

        ACCOMPANIMENT("TPE2", KFieldKey.ALBUM_ARTIST, "Text: Band/Orchestra/Accompaniment", false, false, false, true, false, false),
        ALBUM("TALB", KFieldKey.ALBUM, "Text: Album/Movie/Show title", true, false, false, true, false, false),
        ALBUM_SORT_ORDER("TSOA", KFieldKey.ALBUM_SORT, "Album sort order", false, false, false, true, false, false),
        ARTIST("TPE1", KFieldKey.ARTIST, "Text: Lead artist(s)/Lead performer(s)/Soloist(s)/Performing group", true, false, false, true, false, false),
        ATTACHED_PICTURE("APIC", KFieldKey.COVER_ART, "Attached picture", false, true, true, true, false, false),
        AUDIO_ENCRYPTION("AENC", null, "Audio encryption", false, true, false, true, false, false),
        AUDIO_SEEK_POINT_INDEX("ASPI", null, "Audio seek point index", false, false, false, true, false, false),
        BPM("TBPM", KFieldKey.BPM, "Text: BPM (Beats Per Minute)", false, false, false, true, false, false),
        COMMENT("COMM", KFieldKey.COMMENT, "Comments", true, false, true, true, false, false),
        COMMERCIAL_FRAME("COMR", null, "Commercial Frame", false, false, false, true, false, false),
        COMPOSER("TCOM", KFieldKey.COMPOSER, "Text: Composer", false, false, false, true, false, false),
        CONDUCTOR("TPE3", KFieldKey.CONDUCTOR, "Text: Conductor/Performer refinement", false, false, false, true, false, false),
        CONTENT_GROUP_DESC("TIT1", KFieldKey.GROUPING, "Text: Content group description", false, false, false, true, false, false),
        COPYRIGHTINFO("TCOP", null, "Text: Copyright message", false, false, false, true, false, false),
        ENCODEDBY("TENC", KFieldKey.ENCODER, "Text: Encoded by", false, false, false, true, false, true),
        ENCODING_TIME("TDEN", null, "Text: Encoding time", false, false, false, true, false, false),
        ENCRYPTION("ENCR", null, "Encryption method registration", false, true, false, true, false, false),
        EQUALISATION2("EQU2", null, "Equalization (2)", false, true, false, true, false, false),
        EVENT_TIMING_CODES("ETCO", null, "Event timing codes", false, true, false, true, false, true),
        FILE_OWNER("TOWN", null, "Text:File Owner", false, false, false, true, false, false),
        FILE_TYPE("TFLT", null, "Text: File type", false, false, false, true, false, false),
        GENERAL_ENCAPS_OBJECT("GEOB", null, "General encapsulated datatype", false, true, true, true, false, false),
        GENRE("TCON", KFieldKey.GENRE, "Text: Content type", true, false, false, true, false, false),
        GROUP_ID_REG("GRID", null, "Group ID Registration", false, false, false, true, false, false),
        HW_SW_SETTINGS("TSSE", null, "Text: Software/hardware and settings used for encoding", false, false, false, true, false, false),
        INITIAL_KEY("TKEY", KFieldKey.KEY, "Text: Initial key", false, false, false, true, false, false),
        INVOLVED_PEOPLE("TIPL", KFieldKey.ARRANGER, "Involved people list", false, false, false, true, false, false),
        ISRC("TSRC", KFieldKey.ISRC, "Text: ISRC (International Standard Recording Code)", false, false, false, true, false, false),
        ITUNES_GROUPING("GRP1", KFieldKey.ITUNES_GROUPING, "iTunes Grouping", false, false, false, true, false, false),
        LANGUAGE("TLAN", KFieldKey.LANGUAGE, "Text: Language(s)", false, false, false, true, false, false),
        LENGTH("TLEN", null, "Text: Length", false, false, false, true, false, true),
        LINKED_INFO("LINK", null, "Linked information", false, false, false, true, false, false),
        LYRICIST("TEXT", KFieldKey.LYRICIST, "Text: Lyricist/text writer", false, false, false, true, false, false),
        MEDIA_TYPE("TMED", KFieldKey.MEDIA, "Text: Media type", false, false, false, true, false, false),
        MOOD("TMOO", KFieldKey.MOOD, "Text: Mood", false, false, false, true, false, false),
        MOVEMENT("MVNM", KFieldKey.MOVEMENT, "Text: Movement", false, false, false, true, false, false),
        MOVEMENT_NO("MVIN", KFieldKey.MOVEMENT_NO, "Text: Movement No", false, false, false, true, false, false),
        MPEG_LOCATION_LOOKUP_TABLE("MLLT", null, "MPEG location lookup table", false, false, false, true, false, true),
        MUSICIAN_CREDITS("TMCL", null, "Musical Credits", false, false, false, false, false, false),
        MUSIC_CD_ID("MCDI", null, "Music CD Identifier", false, false, false, true, false, false),
        ORIGARTIST("TOPE", KFieldKey.ORIGINAL_ARTIST, "Text: Original artist(s)/performer(s)", false, false, false, true, false, false),
        ORIGINAL_RELEASE_TIME("TDOR", KFieldKey.ORIGINAL_YEAR, "Text: Original release time", false, false, false, true, false, false),
        ORIG_FILENAME("TOFN", null, "Text: Original filename", false, false, false, true, false, false),
        ORIG_LYRICIST("TOLY", KFieldKey.ORIGINAL_LYRICIST, "Text: Original Lyricist(s)/text writer(s)", false, false, false, true, false, false),
        ORIG_TITLE("TOAL", KFieldKey.ORIGINAL_ALBUM, "Text: Original album/Movie/Show title", false, false, false, true, false, false),
        OWNERSHIP("OWNE", null, "Ownership", false, false, false, true, false, false),
        ARTIST_SORT_ORDER("TSOP", KFieldKey.ARTIST_SORT, "Performance Sort Order", false, false, false, true, false, false),
        PLAYLIST_DELAY("TDLY", null, "Text: Playlist delay", false, false, false, true, false, false),
        PLAY_COUNTER("PCNT", null, "Play counter", false, false, false, true, false, false),
        POPULARIMETER("POPM", KFieldKey.RATING, "Popularimeter", false, false, true, true, false, false),
        POSITION_SYNC("POSS", null, "Position Sync", false, false, false, true, false, true),
        PRIVATE("PRIV", null, "Private frame", false, false, true, true, false, false),
        PRODUCED_NOTICE("TPRO", null, "Produced Notice", false, false, false, true, false, false),
        PUBLISHER("TPUB", KFieldKey.RECORD_LABEL, "Text: Publisher", false, false, false, true, false, false),
        RADIO_NAME("TRSN", null, "Text: Radio Name", false, false, false, true, false, false),
        RADIO_OWNER("TRSO", null, "Text: Radio Owner", false, false, false, true, false, false),
        RECOMMENDED_BUFFER_SIZE("RBUF", null, "Recommended buffer size", false, true, false, true, false, false),
        RELATIVE_VOLUME_ADJUSTMENT2("RVA2", null, "Relative volume adjustment(2)", false, true, false, true, false, false),
        RELEASE_TIME("TDRL", null, "Release Time", false, false, false, true, false, false),
        REMIXED("TPE4", KFieldKey.REMIXER, "Text: Interpreted, remixed, or otherwise modified by", false, false, false, true, false, false),
        REVERB("RVRB", null, "Reverb", false, false, false, true, false, false),
        SEEK("SEEK", null, "Seek", false, false, false, true, false, false),
        SET("TPOS", KFieldKey.DISC_NO, "Text: Part of a setField", false, false, false, true, false, false),
        SET_SUBTITLE("TSST", KFieldKey.DISC_SUBTITLE, "Text: Set subtitle", false, false, false, true, false, false),
        SIGNATURE("SIGN", null, "Signature", false, false, false, true, false, false),
        SYNC_LYRIC("SYLT", null, "Synchronized lyric/text", false, false, false, true, false, true),
        SYNC_TEMPO("SYTC", null, "Synced tempo codes", false, false, false, true, false, true),
        TAGGING_TIME("TDTG", null, "Text: Tagging time", false, false, false, true, false, false),
        TERMS_OF_USE("USER", null, "Terms of Use", false, false, false, true, false, false),
        TITLE("TIT2", KFieldKey.TITLE, "Text: title", true, false, false, true, false, false),
        TITLE_REFINEMENT("TIT3", KFieldKey.SUBTITLE, "Text: Subtitle/Description refinement", false, false, false, true, false, false),
        TITLE_SORT_ORDER("TSOT", KFieldKey.TITLE_SORT, "Text: title sort order", false, false, false, true, false, false),
        TRACK("TRCK", KFieldKey.TRACK, "Text: Track number/Position in setField", true, false, false, true, false, false),
        UNIQUE_FILE_ID("UFID", KFieldKey.MUSICBRAINZ_TRACK_ID, "Unique file identifier", false, true, true, true, false, false),
        UNSYNC_LYRICS("USLT", KFieldKey.LYRICS, "Unsychronized lyric/text transcription", false, false, true, true, false, false),
        URL_ARTIST_WEB("WOAR", KFieldKey.URL_OFFICIAL_ARTIST_SITE, "URL: Official artist/performer webpage", false, false, true, true, false, false),
        URL_COMMERCIAL("WCOM", null, "URL: Commercial information", false, false, false, true, false, false),
        URL_COPYRIGHT("WCOP", null, "URL: Copyright/Legal information", false, false, false, true, false, false),
        URL_FILE_WEB("WOAF", null, "URL: Official audio file webpage", false, false, false, true, false, false),
        URL_OFFICIAL_RADIO("WORS", null, "URL: Official Radio website", false, false, false, true, false, false),
        URL_PAYMENT("WPAY", null, "URL: Payment for this recording ", false, false, false, true, false, false),
        URL_PUBLISHERS("WPUB", null, "URL: Publishers official webpage", false, false, false, true, false, false),
        URL_SOURCE_WEB("WOAS", null, "URL: Official audio source webpage", false, false, false, true, false, false),
        USER_DEFINED_INFO("TXXX", KFieldKey.ACOUSTID_FINGERPRINT, "User defined text information frame", false, false, true, true, false, false),
        USER_DEFINED_URL("WXXX", KFieldKey.URL_DISCOGS_ARTIST_SITE, "User defined URL link frame", false, false, true, true, false, false),
        YEAR("TDRC", KFieldKey.YEAR, "Text:Year", true, false, false, true, false, false),
        IS_COMPILATION("TCMP", KFieldKey.IS_COMPILATION, "Is Compilation", false, false, false, false, true, false),
        ALBUM_ARTIST_SORT_ORDER_ITUNES("TSO2", KFieldKey.ALBUM_ARTIST_SORT, "Text:Album Artist Sort Order Frame", false, false, false, false, true, false),
        COMPOSER_SORT_ORDER_ITUNES("TSOC", KFieldKey.COMPOSER_SORT, "Text:Composer Sort Order Frame", false, false, false, false, true, false),
    ;

    companion object {

        fun contains(id: String?): Boolean = ID3v24KFrame.entries.any { e -> id == e.id }

        fun fromId(id: String?): ID3v24KFrame? = entries.find { e -> id == e.id }

        fun fromFieldKey(fieldKey: KFieldKey): ID3v24KFrame? = entries.find { e -> fieldKey == e.fieldKey }

        fun commonFrames(): List<ID3v24KFrame> = entries.filter { e -> e.isCommon }

        fun binaryFrames(): List<ID3v24KFrame> = entries.filter { e -> e.isBinary }

        fun multipleFrames(): List<ID3v24KFrame> = entries.filter { e -> e.isMultipleAllowed }

        fun supprtedFrames(): List<ID3v24KFrame> = entries.filter { e -> e.isSupported }

        fun extensionFrames(): List<ID3v24KFrame> = entries.filter { e -> e.isExtension }

        fun discardIfFileAltered(): List<ID3v24KFrame> = entries.filter { e -> e.isDiscardedIfFileAltered }

        fun isSupported(id: String?): Boolean = supprtedFrames().any { e -> e.id == id }

        fun isExtension(id: String?): Boolean = extensionFrames().any { e -> e.id == id }

        fun isCommon(id: String?): Boolean = commonFrames().any { e -> e.id == id }

        fun isBinary(id: String?): Boolean = binaryFrames().any { e -> e.id == id }

        fun isMultipleAllowed(id: String?): Boolean = multipleFrames().any { e -> e.id == id }

        fun isDiscardedIfFileAltered(id: String?): Boolean = discardIfFileAltered().any { e -> e.id == id }
    }
}