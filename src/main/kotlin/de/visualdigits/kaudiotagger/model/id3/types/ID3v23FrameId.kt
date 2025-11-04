package de.visualdigits.kaudiotagger.model.id3.types

import de.visualdigits.kaudiotagger.model.common.types.GenericFieldKey

enum class ID3v23FrameId(
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
) : FrameId {

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
        CUSTOM1("COMM", GenericFieldKey.CUSTOM1, ID3v23FieldKey.CUSTOM1, "Text: Custom 1", false, false, false, true, false, false),
        CUSTOM2("COMM", GenericFieldKey.CUSTOM2, ID3v23FieldKey.CUSTOM2, "Text: Custom 2", false, false, false, true, false, false),
        CUSTOM3("COMM", GenericFieldKey.CUSTOM3, ID3v23FieldKey.CUSTOM3, "Text: Custom 3", false, false, false, true, false, false),
        CUSTOM4("COMM", GenericFieldKey.CUSTOM4, ID3v23FieldKey.CUSTOM4, "Text: Custom 4", false, false, false, true, false, false),
        CUSTOM5("COMM", GenericFieldKey.CUSTOM5, ID3v23FieldKey.CUSTOM5, "Text: Custom 5", false, false, false, true, false, false),
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
        URL_OFFICIAL_RELEASE_SITE("WXXX", GenericFieldKey.URL_OFFICIAL_RELEASE_SITE, ID3v23FieldKey.URL_OFFICIAL_RELEASE_SITE, "URL: Official release site", false, false, false, true, false, false),
        URL_PAYMENT("WPAY", null, null, "URL: Payment", false, false, false, true, false, false),
        URL_PUBLISHERS("WPUB", null, null, "URL: Publishers official webpage", false, false, false, true, false, false),
        URL_SOURCE_WEB("WOAS", null, null, "URL: Official audio source webpage", false, false, false, true, false, false),
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

        USER_DEFINED_INFO("TXXX", GenericFieldKey.ACOUSTID_FINGERPRINT, ID3v23FieldKey.ACOUSTID_FINGERPRINT, "User defined text information frame", false, false, true, true, false, false),
        ACOUSTID_FINGERPRINT("TXXX", GenericFieldKey.ACOUSTID_FINGERPRINT, ID3v23FieldKey.ACOUSTID_FINGERPRINT, "Text: Acoustid Fingerprint", false, false, false, false, false, false),
        ACOUSTID_ID("TXXX", GenericFieldKey.ACOUSTID_ID, ID3v23FieldKey.ACOUSTID_ID, "Text: Acoustid Id", false, false, false, false, false, false),
        ALBUM_ARTISTS("TXXX", GenericFieldKey.ALBUM_ARTISTS, ID3v23FieldKey.ALBUM_ARTISTS, "Text: ALBUM_ARTISTS", false, false, false, false, false, false),
        ALBUM_ARTISTS_SORT("TXXX", GenericFieldKey.ALBUM_ARTISTS_SORT, ID3v23FieldKey.ALBUM_ARTISTS_SORT, "Text: ALBUM_ARTISTS_SORT", false, false, false, false, false, false),
        AMAZON_ID("TXXX", GenericFieldKey.AMAZON_ID, ID3v23FieldKey.AMAZON_ID, "Text: ASIN", false, false, false, false, false, false),
        ARRANGER_SORT("TXXX", GenericFieldKey.ARRANGER_SORT, ID3v23FieldKey.ARRANGER_SORT, "Text: ARRANGER_SORT", false, false, false, false, false, false),
        ARTISTS("TXXX", GenericFieldKey.ARTISTS, ID3v23FieldKey.ARTISTS, "Text: ARTISTS", false, false, false, false, false, false),
        ARTISTS_SORT("TXXX", GenericFieldKey.ARTISTS_SORT, ID3v23FieldKey.ARTISTS_SORT, "Text: ARTISTS_SORT", false, false, false, false, false, false),
        BARCODE("TXXX", GenericFieldKey.BARCODE, ID3v23FieldKey.BARCODE, "Text: BARCODE", false, false, false, false, false, false),
        CATALOG_NO("TXXX", GenericFieldKey.CATALOG_NO, ID3v23FieldKey.CATALOG_NO, "Text: CATALOGNUMBER", false, false, false, false, false, false),
        CHOIR("TXXX", GenericFieldKey.CHOIR, ID3v23FieldKey.CHOIR, "Text: CHOIR", false, false, false, false, false, false),
        CHOIR_SORT("TXXX", GenericFieldKey.CHOIR_SORT, ID3v23FieldKey.CHOIR_SORT, "Text: CHOIR_SORT", false, false, false, false, false, false),
        CLASSICAL_CATALOG("TXXX", GenericFieldKey.CLASSICAL_CATALOG, ID3v23FieldKey.CLASSICAL_CATALOG, "Text: CLASSICAL_CATALOG", false, false, false, false, false, false),
        CLASSICAL_NICKNAME("TXXX", GenericFieldKey.CLASSICAL_NICKNAME, ID3v23FieldKey.CLASSICAL_NICKNAME, "Text: CLASSICAL_NICKNAME", false, false, false, false, false, false),
        CONDUCTOR_SORT("TXXX", GenericFieldKey.CONDUCTOR_SORT, ID3v23FieldKey.CONDUCTOR_SORT, "Text: CONDUCTOR_SORT", false, false, false, false, false, false),
        COUNTRY("TXXX", GenericFieldKey.COUNTRY, ID3v23FieldKey.COUNTRY, "Text: Country", false, false, false, false, false, false),
        ENSEMBLE("TXXX", GenericFieldKey.ENSEMBLE, ID3v23FieldKey.ENSEMBLE, "Text: ENSEMBLE", false, false, false, false, false, false),
        ENSEMBLE_SORT("TXXX", GenericFieldKey.ENSEMBLE_SORT, ID3v23FieldKey.ENSEMBLE_SORT, "Text: ENSEMBLE_SORT", false, false, false, false, false, false),
        FBPM("TXXX", GenericFieldKey.FBPM, ID3v23FieldKey.FBPM, "Text: FBPM", false, false, false, false, false, false),
        IS_CLASSICAL("TXXX", GenericFieldKey.IS_CLASSICAL, ID3v23FieldKey.IS_CLASSICAL, "Text: IS_CLASSICAL", false, false, false, false, false, false),
        IS_SOUNDTRACK("TXXX", GenericFieldKey.IS_SOUNDTRACK, ID3v23FieldKey.IS_SOUNDTRACK, "Text: IS_SOUNDTRACK", false, false, false, false, false, false),
        MOOD("TXXX", GenericFieldKey.MOOD, ID3v23FieldKey.MOOD, "Text: MOOD", false, false, false, false, false, false),
        MOOD_ACOUSTIC("TXXX", GenericFieldKey.MOOD_ACOUSTIC, ID3v23FieldKey.MOOD_ACOUSTIC, "Text: MOOD_ACOUSTIC", false, false, false, false, false, false),
        MOOD_AGGRESSIVE("TXXX", GenericFieldKey.MOOD_AGGRESSIVE, ID3v23FieldKey.MOOD_AGGRESSIVE, "Text: MOOD_AGGRESSIVE", false, false, false, false, false, false),
        MOOD_AROUSAL("TXXX", GenericFieldKey.MOOD_AROUSAL, ID3v23FieldKey.MOOD_AROUSAL, "Text: MOOD_AROUSAL", false, false, false, false, false, false),
        MOOD_DANCEABILITY("TXXX", GenericFieldKey.MOOD_DANCEABILITY, ID3v23FieldKey.MOOD_DANCEABILITY, "Text: MOOD_DANCEABILITY", false, false, false, false, false, false),
        MOOD_ELECTRONIC("TXXX", GenericFieldKey.MOOD_ELECTRONIC, ID3v23FieldKey.MOOD_ELECTRONIC, "Text: MOOD_ELECTRONIC", false, false, false, false, false, false),
        MOOD_HAPPY("TXXX", GenericFieldKey.MOOD_HAPPY, ID3v23FieldKey.MOOD_HAPPY, "Text: MOOD_HAPPY", false, false, false, false, false, false),
        MOOD_INSTRUMENTAL("TXXX", GenericFieldKey.MOOD_INSTRUMENTAL, ID3v23FieldKey.MOOD_INSTRUMENTAL, "Text: MOOD_INSTRUMENTAL", false, false, false, false, false, false),
        MOOD_PARTY("TXXX", GenericFieldKey.MOOD_PARTY, ID3v23FieldKey.MOOD_PARTY, "Text: MOOD_PARTY", false, false, false, false, false, false),
        MOOD_RELAXED("TXXX", GenericFieldKey.MOOD_RELAXED, ID3v23FieldKey.MOOD_RELAXED, "Text: MOOD_RELAXED", false, false, false, false, false, false),
        MOOD_SAD("TXXX", GenericFieldKey.MOOD_SAD, ID3v23FieldKey.MOOD_SAD, "Text: MOOD_SAD", false, false, false, false, false, false),
        MOOD_VALENCE("TXXX", GenericFieldKey.MOOD_VALENCE, ID3v23FieldKey.MOOD_VALENCE, "Text: MOOD_VALENCE", false, false, false, false, false, false),
        MUSICBRAINZ_ARTISTID("TXXX", GenericFieldKey.MUSICBRAINZ_ARTISTID, ID3v23FieldKey.MUSICBRAINZ_ARTISTID, "Text: MusicBrainz Artist Id", false, false, false, false, false, false),
        MUSICBRAINZ_DISC_ID("TXXX", GenericFieldKey.MUSICBRAINZ_DISC_ID, ID3v23FieldKey.MUSICBRAINZ_DISC_ID, "Text: MusicBrainz Disc Id", false, false, false, false, false, false),
        MUSICBRAINZ_ORIGINAL_RELEASEID("TXXX", null, ID3v23FieldKey.MUSICBRAINZ_ORIGINAL_RELEASEID, "Text: MusicBrainz Original Album Id", false, false, false, false, false, false),
        MUSICBRAINZ_RELEASEARTISTID("TXXX", GenericFieldKey.MUSICBRAINZ_RELEASEARTISTID, ID3v23FieldKey.MUSICBRAINZ_RELEASEARTISTID, "Text: MusicBrainz Album Artist Id", false, false, false, false, false, false),
        MUSICBRAINZ_RELEASEID("TXXX", GenericFieldKey.MUSICBRAINZ_RELEASEID, ID3v23FieldKey.MUSICBRAINZ_RELEASEID, "Text: MusicBrainz Album Id", false, false, false, false, false, false),
        MUSICBRAINZ_RELEASE_COUNTRY("TXXX", GenericFieldKey.MUSICBRAINZ_RELEASE_COUNTRY, ID3v23FieldKey.MUSICBRAINZ_RELEASE_COUNTRY, "Text: MusicBrainz Album Release Country", false, false, false, false, false, false),
        MUSICBRAINZ_RELEASE_GROUP_ID("TXXX", GenericFieldKey.MUSICBRAINZ_RELEASE_GROUP_ID, ID3v23FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID, "Text: MusicBrainz Release Group Id", false, false, false, false, false, false),
        MUSICBRAINZ_RELEASE_STATUS("TXXX", GenericFieldKey.MUSICBRAINZ_RELEASE_STATUS, ID3v23FieldKey.MUSICBRAINZ_RELEASE_STATUS, "Text: MusicBrainz Album Status", false, false, false, false, false, false),
        MUSICBRAINZ_RELEASE_TRACK_ID("TXXX", GenericFieldKey.MUSICBRAINZ_RELEASE_TRACK_ID, ID3v23FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID, "Text: MusicBrainz Release Track Id", false, false, false, false, false, false),
        MUSICBRAINZ_RELEASE_TYPE("TXXX", GenericFieldKey.MUSICBRAINZ_RELEASE_TYPE, ID3v23FieldKey.MUSICBRAINZ_RELEASE_TYPE, "Text: MusicBrainz Album Type", false, false, false, false, false, false),
        MUSICBRAINZ_WORK("TXXX", GenericFieldKey.MUSICBRAINZ_WORK, ID3v23FieldKey.MUSICBRAINZ_WORK, "Text: MUSICBRAINZ_WORK", false, false, false, false, false, false),
        MUSICBRAINZ_WORK_COMPOSITION("TXXX", GenericFieldKey.MUSICBRAINZ_WORK_COMPOSITION, ID3v23FieldKey.MUSICBRAINZ_WORK_COMPOSITION, "Text: MUSICBRAINZ_WORK_COMPOSITION", false, false, false, false, false, false),
        MUSICBRAINZ_WORK_COMPOSITION_ID("TXXX", GenericFieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID, ID3v23FieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID, "Text: MUSICBRAINZ_WORK_COMPOSITION_ID", false, false, false, false, false, false),
        MUSICBRAINZ_WORK_ID("TXXX", GenericFieldKey.MUSICBRAINZ_WORK_ID, ID3v23FieldKey.MUSICBRAINZ_WORK_ID, "Text: MusicBrainz Work Id", false, false, false, false, false, false),
        MUSICBRAINZ_WORK_PART_LEVEL1_ID("TXXX", GenericFieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID, ID3v23FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID, "Text: MUSICBRAINZ_WORK_PART_LEVEL1_ID", false, false, false, false, false, false),
        MUSICBRAINZ_WORK_PART_LEVEL2_ID("TXXX", GenericFieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID, ID3v23FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID, "Text: MUSICBRAINZ_WORK_PART_LEVEL2_ID", false, false, false, false, false, false),
        MUSICBRAINZ_WORK_PART_LEVEL3_ID("TXXX", GenericFieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID, ID3v23FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID, "Text: MUSICBRAINZ_WORK_PART_LEVEL3_ID", false, false, false, false, false, false),
        MUSICBRAINZ_WORK_PART_LEVEL4_ID("TXXX", GenericFieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID, ID3v23FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID, "Text: MUSICBRAINZ_WORK_PART_LEVEL4_ID", false, false, false, false, false, false),
        MUSICBRAINZ_WORK_PART_LEVEL5_ID("TXXX", GenericFieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID, ID3v23FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID, "Text: MUSICBRAINZ_WORK_PART_LEVEL5_ID", false, false, false, false, false, false),
        MUSICBRAINZ_WORK_PART_LEVEL6_ID("TXXX", GenericFieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID, ID3v23FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID, "Text: MUSICBRAINZ_WORK_PART_LEVEL6_ID", false, false, false, false, false, false),
        MUSICIP_ID("TXXX", GenericFieldKey.MUSICIP_ID, ID3v23FieldKey.MUSICIP_ID, "Text: MusicIP PUID", false, false, false, false, false, false),
        OPUS("TXXX", GenericFieldKey.OPUS, ID3v23FieldKey.OPUS, "Text: OPUS", false, false, false, false, false, false),
        ORCHESTRA("TXXX", GenericFieldKey.ORCHESTRA, ID3v23FieldKey.ORCHESTRA, "Text: ORCHESTRA", false, false, false, false, false, false),
        ORCHESTRA_SORT("TXXX", GenericFieldKey.ORCHESTRA_SORT, ID3v23FieldKey.ORCHESTRA_SORT, "Text: ORCHESTRA_SORT", false, false, false, false, false, false),
        PART("TXXX", GenericFieldKey.PART, ID3v23FieldKey.PART, "Text: PART", false, false, false, false, false, false),
        PART_NUMBER("TXXX", GenericFieldKey.PART_NUMBER, ID3v23FieldKey.PART_NUMBER, "Text: PARTNUMBER", false, false, false, false, false, false),
        PART_TYPE("TXXX", GenericFieldKey.PART_TYPE, ID3v23FieldKey.PART_TYPE, "Text: PART_TYPE", false, false, false, false, false, false),
        PERFORMER_NAME("TXXX", GenericFieldKey.PERFORMER_NAME, ID3v23FieldKey.PERFORMER_NAME, "Text: PERFORMER_NAME", false, false, false, false, false, false),
        PERFORMER_NAME_SORT("TXXX", GenericFieldKey.PERFORMER_NAME_SORT, ID3v23FieldKey.PERFORMER_NAME_SORT, "Text: PERFORMER_NAME_SORT", false, false, false, false, false, false),
        PERIOD("TXXX", GenericFieldKey.PERIOD, ID3v23FieldKey.PERIOD, "Text: PERIOD", false, false, false, false, false, false),
        RANKING("TXXX", GenericFieldKey.RANKING, ID3v23FieldKey.RANKING, "Text: RANKING", false, false, false, false, false, false),
        SCRIPT("TXXX", GenericFieldKey.SCRIPT, ID3v23FieldKey.SCRIPT, "Text: Script", false, false, false, false, false, false),
        SINGLE_DISC_TRACK_NO("TXXX", GenericFieldKey.SINGLE_DISC_TRACK_NO, ID3v23FieldKey.SINGLE_DISC_TRACK_NO, "Text: SINGLE_DISC_TRACK_NO", false, false, false, false, false, false),
        TAGS("TXXX", GenericFieldKey.TAGS, ID3v23FieldKey.TAGS, "Text: TAGS", false, false, false, false, false, false),
        TIMBRE("TXXX", GenericFieldKey.TIMBRE, ID3v23FieldKey.TIMBRE, "Text: TIMBRE_BRIGHTNESS", false, false, false, false, false, false),
        TITLE_MOVEMENT("TXXX", GenericFieldKey.TITLE_MOVEMENT, ID3v23FieldKey.TITLE_MOVEMENT, "Text: TITLE_MOVEMENT", false, false, false, false, false, false),
        TONALITY("TXXX", GenericFieldKey.TONALITY, ID3v23FieldKey.TONALITY, "Text: TONALITY", false, false, false, false, false, false),
        WORK("TXXX", GenericFieldKey.WORK, ID3v23FieldKey.WORK, "Text: WORK", false, false, false, false, false, false),
        WORK_PART_LEVEL1("TXXX", null, ID3v23FieldKey.WORK_PART_LEVEL1, "Text: MUSICBRAINZ_WORK_PART_LEVEL1", false, false, false, false, false, false),
        WORK_PART_LEVEL1_TYPE("TXXX", null, ID3v23FieldKey.WORK_PART_LEVEL1_TYPE, "Text: MUSICBRAINZ_WORK_PART_LEVEL1_TYPE", false, false, false, false, false, false),
        WORK_PART_LEVEL2("TXXX", null, ID3v23FieldKey.WORK_PART_LEVEL2, "Text: MUSICBRAINZ_WORK_PART_LEVEL2", false, false, false, false, false, false),
        WORK_PART_LEVEL2_TYPE("TXXX", null, ID3v23FieldKey.WORK_PART_LEVEL2_TYPE, "Text: MUSICBRAINZ_WORK_PART_LEVEL2_TYPE", false, false, false, false, false, false),
        WORK_PART_LEVEL3("TXXX", null, ID3v23FieldKey.WORK_PART_LEVEL3, "Text: MUSICBRAINZ_WORK_PART_LEVEL3", false, false, false, false, false, false),
        WORK_PART_LEVEL3_TYPE("TXXX", null, ID3v23FieldKey.WORK_PART_LEVEL3_TYPE, "Text: MUSICBRAINZ_WORK_PART_LEVEL3_TYPE", false, false, false, false, false, false),
        WORK_PART_LEVEL4("TXXX", null, ID3v23FieldKey.WORK_PART_LEVEL4, "Text: MUSICBRAINZ_WORK_PART_LEVEL4", false, false, false, false, false, false),
        WORK_PART_LEVEL4_TYPE("TXXX", null, ID3v23FieldKey.WORK_PART_LEVEL4_TYPE, "Text: MUSICBRAINZ_WORK_PART_LEVEL4_TYPE", false, false, false, false, false, false),
        WORK_PART_LEVEL5("TXXX", null, ID3v23FieldKey.WORK_PART_LEVEL5, "Text: MUSICBRAINZ_WORK_PART_LEVEL5", false, false, false, false, false, false),
        WORK_PART_LEVEL5_TYPE("TXXX", null, ID3v23FieldKey.WORK_PART_LEVEL5_TYPE, "Text: MUSICBRAINZ_WORK_PART_LEVEL5_TYPE", false, false, false, false, false, false),
        WORK_PART_LEVEL6("TXXX", null, ID3v23FieldKey.WORK_PART_LEVEL6, "Text: MUSICBRAINZ_WORK_PART_LEVEL6", false, false, false, false, false, false),
        WORK_PART_LEVEL6_TYPE("TXXX", null, ID3v23FieldKey.WORK_PART_LEVEL6_TYPE, "Text: MUSICBRAINZ_WORK_PART_LEVEL6_TYPE", false, false, false, false, false, false),
        WORK_TYPE("TXXX", GenericFieldKey.WORK_TYPE, ID3v23FieldKey.WORK_TYPE, "Text: WORK_TYPE", false, false, false, false, false, false),
    ;

    companion object {

        fun contains(id: String?): Boolean = entries.any { e -> id == e.id }

        fun fromId(id: String?): ID3v23FrameId? = entries.find { e -> id == e.id }

        fun fromFieldKey(fieldKey: GenericFieldKey?): ID3v23FrameId? = entries.find { e -> fieldKey == e.genericFieldKey }

        fun commonFrames(): List<ID3v23FrameId> = entries.filter { e -> e.isCommon }

        fun binaryFrames(): List<ID3v23FrameId> = entries.filter { e -> e.isBinary }

        fun multipleFrames(): List<ID3v23FrameId> = entries.filter { e -> e.isMultipleAllowed }

        fun supprtedFrames(): List<ID3v23FrameId> = entries.filter { e -> e.isSupported }

        fun extensionFrames(): List<ID3v23FrameId> = entries.filter { e -> e.isExtension }

        fun discardIfFileAltered(): List<ID3v23FrameId> = entries.filter { e -> e.isDiscardedIfFileAltered }

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