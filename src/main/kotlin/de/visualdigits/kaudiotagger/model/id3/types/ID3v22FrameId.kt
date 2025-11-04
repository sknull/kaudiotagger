package de.visualdigits.kaudiotagger.model.id3.types

import de.visualdigits.kaudiotagger.model.common.types.GenericFieldKey

enum class ID3v22FrameId(
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
) : FrameId {

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
        CUSTOM1("COM", GenericFieldKey.CUSTOM1, ID3v22FieldKey.CUSTOM1, "Text: Custom 1", false, false, false, true, false, false),
        CUSTOM2("COM", GenericFieldKey.CUSTOM2, ID3v22FieldKey.CUSTOM2, "Text: Custom 2", false, false, false, true, false, false),
        CUSTOM3("COM", GenericFieldKey.CUSTOM3, ID3v22FieldKey.CUSTOM3, "Text: Custom 3", false, false, false, true, false, false),
        CUSTOM4("COM", GenericFieldKey.CUSTOM4, ID3v22FieldKey.CUSTOM4, "Text: Custom 4", false, false, false, true, false, false),
        CUSTOM5("COM", GenericFieldKey.CUSTOM5, ID3v22FieldKey.CUSTOM5, "Text: Custom 5", false, false, false, true, false, false),
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
        URL_OFFICIAL_RELEASE_SITE("WXX", GenericFieldKey.URL_OFFICIAL_RELEASE_SITE, ID3v22FieldKey.URL_OFFICIAL_RELEASE_SITE, "URL: Official release site", false, false, false, true, false, false),
        URL_PAYMENT("WPAY", null, null, "URL: Official payment site", false, false, false, true, false, false),
        URL_PUBLISHERS("WPB", null, null, "URL: Publishers official webpage", false, false, false, true, false, false),
        URL_SOURCE_WEB("WAS", null, null, "URL: Official audio source webpage", false, false, false, true, false, false),
        USER_DEFINED_URL("WXX", GenericFieldKey.URL_DISCOGS_ARTIST_SITE, ID3v22FieldKey.URL_DISCOGS_ARTIST_SITE, "User defined URL link frame", false, false, true, true, false, false),
        IS_COMPILATION("TCP", GenericFieldKey.IS_COMPILATION, ID3v22FieldKey.IS_COMPILATION, "Is Compilation", false, false, false, false, true, false),
        TITLE_SORT_ORDER_ITUNES("TST", GenericFieldKey.TITLE_SORT, ID3v22FieldKey.TITLE_SORT, "Text: title sort order", false, false, false, false, true, false),
        ARTIST_SORT_ORDER_ITUNES("TSP", GenericFieldKey.ARTIST_SORT, ID3v22FieldKey.ARTIST_SORT, "Text: artist sort order", false, false, false, false, true, false),
        ALBUM_SORT_ORDER_ITUNES("TSA", GenericFieldKey.ALBUM_SORT, ID3v22FieldKey.ALBUM_SORT, "Text: album sort order", false, false, false, false, true, false),
        ALBUM_ARTIST_SORT_ORDER_ITUNES("TS2", GenericFieldKey.ALBUM_ARTIST_SORT, ID3v22FieldKey.ALBUM_ARTIST_SORT, "Text:Album Artist Sort Order Frame", false, false, false, false, true, false),
        COMPOSER_SORT_ORDER_ITUNES("TSC", GenericFieldKey.COMPOSER_SORT, ID3v22FieldKey.COMPOSER_SORT, "Text:Composer Sort Order Frame", false, false, false, false, true, false),

        USER_DEFINED_INFO("TXX", GenericFieldKey.ACOUSTID_FINGERPRINT, ID3v22FieldKey.ACOUSTID_FINGERPRINT, "User defined text information frame", false, false, true, true, false, false),
        ACOUSTID_FINGERPRINT("TXX", GenericFieldKey.ACOUSTID_FINGERPRINT, ID3v22FieldKey.ACOUSTID_FINGERPRINT, "Text: Acoustid Fingerprint", false, false, false, false, false, false),
        ACOUSTID_ID("TXX", GenericFieldKey.ACOUSTID_ID, ID3v22FieldKey.ACOUSTID_ID, "Text: Acoustid Id", false, false, false, false, false, false),
        ALBUM_ARTISTS("TXX", GenericFieldKey.ALBUM_ARTISTS, ID3v22FieldKey.ALBUM_ARTISTS, "Text: ALBUM_ARTISTS", false, false, false, false, false, false),
        ALBUM_ARTISTS_SORT("TXX", GenericFieldKey.ALBUM_ARTISTS_SORT, ID3v22FieldKey.ALBUM_ARTISTS_SORT, "Text: ALBUM_ARTISTS_SORT", false, false, false, false, false, false),
        AMAZON_ID("TXX", GenericFieldKey.AMAZON_ID, ID3v22FieldKey.AMAZON_ID, "Text: ASIN", false, false, false, false, false, false),
        ARRANGER_SORT("TXX", GenericFieldKey.ARRANGER_SORT, ID3v22FieldKey.ARRANGER_SORT, "Text: ARRANGER_SORT", false, false, false, false, false, false),
        ARTISTS("TXX", GenericFieldKey.ARTISTS, ID3v22FieldKey.ARTISTS, "Text: ARTISTS", false, false, false, false, false, false),
        ARTISTS_SORT("TXX", GenericFieldKey.ARTISTS_SORT, ID3v22FieldKey.ARTISTS_SORT, "Text: ARTISTS_SORT", false, false, false, false, false, false),
        BARCODE("TXX", GenericFieldKey.BARCODE, ID3v22FieldKey.BARCODE, "Text: BARCODE", false, false, false, false, false, false),
        CATALOG_NO("TXX", GenericFieldKey.CATALOG_NO, ID3v22FieldKey.CATALOG_NO, "Text: CATALOGNUMBER", false, false, false, false, false, false),
        CHOIR("TXX", GenericFieldKey.CHOIR, ID3v22FieldKey.CHOIR, "Text: CHOIR", false, false, false, false, false, false),
        CHOIR_SORT("TXX", GenericFieldKey.CHOIR_SORT, ID3v22FieldKey.CHOIR_SORT, "Text: CHOIR_SORT", false, false, false, false, false, false),
        CLASSICAL_CATALOG("TXX", GenericFieldKey.CLASSICAL_CATALOG, ID3v22FieldKey.CLASSICAL_CATALOG, "Text: CLASSICAL_CATALOG", false, false, false, false, false, false),
        CLASSICAL_NICKNAME("TXX", GenericFieldKey.CLASSICAL_NICKNAME, ID3v22FieldKey.CLASSICAL_NICKNAME, "Text: CLASSICAL_NICKNAME", false, false, false, false, false, false),
        CONDUCTOR_SORT("TXX", GenericFieldKey.CONDUCTOR_SORT, ID3v22FieldKey.CONDUCTOR_SORT, "Text: CONDUCTOR_SORT", false, false, false, false, false, false),
        COUNTRY("TXX", GenericFieldKey.COUNTRY, ID3v22FieldKey.COUNTRY, "Text: Country", false, false, false, false, false, false),
        ENSEMBLE("TXX", GenericFieldKey.ENSEMBLE, ID3v22FieldKey.ENSEMBLE, "Text: ENSEMBLE", false, false, false, false, false, false),
        ENSEMBLE_SORT("TXX", GenericFieldKey.ENSEMBLE_SORT, ID3v22FieldKey.ENSEMBLE_SORT, "Text: ENSEMBLE_SORT", false, false, false, false, false, false),
        FBPM("TXX", GenericFieldKey.FBPM, ID3v22FieldKey.FBPM, "Text: FBPM", false, false, false, false, false, false),
        IS_CLASSICAL("TXX", GenericFieldKey.IS_CLASSICAL, ID3v22FieldKey.IS_CLASSICAL, "Text: IS_CLASSICAL", false, false, false, false, false, false),
        IS_SOUNDTRACK("TXX", GenericFieldKey.IS_SOUNDTRACK, ID3v22FieldKey.IS_SOUNDTRACK, "Text: IS_SOUNDTRACK", false, false, false, false, false, false),
        MOOD("TXX", GenericFieldKey.MOOD, ID3v22FieldKey.MOOD, "Text: MOOD", false, false, false, false, false, false),
        MOOD_ACOUSTIC("TXX", GenericFieldKey.MOOD_ACOUSTIC, ID3v22FieldKey.MOOD_ACOUSTIC, "Text: MOOD_ACOUSTIC", false, false, false, false, false, false),
        MOOD_AGGRESSIVE("TXX", GenericFieldKey.MOOD_AGGRESSIVE, ID3v22FieldKey.MOOD_AGGRESSIVE, "Text: MOOD_AGGRESSIVE", false, false, false, false, false, false),
        MOOD_AROUSAL("TXX", GenericFieldKey.MOOD_AROUSAL, ID3v22FieldKey.MOOD_AROUSAL, "Text: MOOD_AROUSAL", false, false, false, false, false, false),
        MOOD_DANCEABILITY("TXX", GenericFieldKey.MOOD_DANCEABILITY, ID3v22FieldKey.MOOD_DANCEABILITY, "Text: MOOD_DANCEABILITY", false, false, false, false, false, false),
        MOOD_ELECTRONIC("TXX", GenericFieldKey.MOOD_ELECTRONIC, ID3v22FieldKey.MOOD_ELECTRONIC, "Text: MOOD_ELECTRONIC", false, false, false, false, false, false),
        MOOD_HAPPY("TXX", GenericFieldKey.MOOD_HAPPY, ID3v22FieldKey.MOOD_HAPPY, "Text: MOOD_HAPPY", false, false, false, false, false, false),
        MOOD_INSTRUMENTAL("TXX", GenericFieldKey.MOOD_INSTRUMENTAL, ID3v22FieldKey.MOOD_INSTRUMENTAL, "Text: MOOD_INSTRUMENTAL", false, false, false, false, false, false),
        MOOD_PARTY("TXX", GenericFieldKey.MOOD_PARTY, ID3v22FieldKey.MOOD_PARTY, "Text: MOOD_PARTY", false, false, false, false, false, false),
        MOOD_RELAXED("TXX", GenericFieldKey.MOOD_RELAXED, ID3v22FieldKey.MOOD_RELAXED, "Text: MOOD_RELAXED", false, false, false, false, false, false),
        MOOD_SAD("TXX", GenericFieldKey.MOOD_SAD, ID3v22FieldKey.MOOD_SAD, "Text: MOOD_SAD", false, false, false, false, false, false),
        MOOD_VALENCE("TXX", GenericFieldKey.MOOD_VALENCE, ID3v22FieldKey.MOOD_VALENCE, "Text: MOOD_VALENCE", false, false, false, false, false, false),
        MUSICBRAINZ_ARTISTID("TXX", GenericFieldKey.MUSICBRAINZ_ARTISTID, ID3v22FieldKey.MUSICBRAINZ_ARTISTID, "Text: MusicBrainz Artist Id", false, false, false, false, false, false),
        MUSICBRAINZ_DISC_ID("TXX", GenericFieldKey.MUSICBRAINZ_DISC_ID, ID3v22FieldKey.MUSICBRAINZ_DISC_ID, "Text: MusicBrainz Disc Id", false, false, false, false, false, false),
        MUSICBRAINZ_ORIGINAL_RELEASEID("TXX", null, ID3v22FieldKey.MUSICBRAINZ_ORIGINAL_RELEASEID, "Text: MusicBrainz Original Album Id", false, false, false, false, false, false),
        MUSICBRAINZ_RELEASEARTISTID("TXX", GenericFieldKey.MUSICBRAINZ_RELEASEARTISTID, ID3v22FieldKey.MUSICBRAINZ_RELEASEARTISTID, "Text: MusicBrainz Album Artist Id", false, false, false, false, false, false),
        MUSICBRAINZ_RELEASEID("TXX", GenericFieldKey.MUSICBRAINZ_RELEASEID, ID3v22FieldKey.MUSICBRAINZ_RELEASEID, "Text: MusicBrainz Album Id", false, false, false, false, false, false),
        MUSICBRAINZ_RELEASE_COUNTRY("TXX", GenericFieldKey.MUSICBRAINZ_RELEASE_COUNTRY, ID3v22FieldKey.MUSICBRAINZ_RELEASE_COUNTRY, "Text: MusicBrainz Album Release Country", false, false, false, false, false, false),
        MUSICBRAINZ_RELEASE_GROUP_ID("TXX", GenericFieldKey.MUSICBRAINZ_RELEASE_GROUP_ID, ID3v22FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID, "Text: MusicBrainz Release Group Id", false, false, false, false, false, false),
        MUSICBRAINZ_RELEASE_STATUS("TXX", GenericFieldKey.MUSICBRAINZ_RELEASE_STATUS, ID3v22FieldKey.MUSICBRAINZ_RELEASE_STATUS, "Text: MusicBrainz Album Status", false, false, false, false, false, false),
        MUSICBRAINZ_RELEASE_TRACK_ID("TXX", GenericFieldKey.MUSICBRAINZ_RELEASE_TRACK_ID, ID3v22FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID, "Text: MusicBrainz Release Track Id", false, false, false, false, false, false),
        MUSICBRAINZ_RELEASE_TYPE("TXX", GenericFieldKey.MUSICBRAINZ_RELEASE_TYPE, ID3v22FieldKey.MUSICBRAINZ_RELEASE_TYPE, "Text: MusicBrainz Album Type", false, false, false, false, false, false),
        MUSICBRAINZ_WORK("TXX", GenericFieldKey.MUSICBRAINZ_WORK, ID3v22FieldKey.MUSICBRAINZ_WORK, "Text: MUSICBRAINZ_WORK", false, false, false, false, false, false),
        MUSICBRAINZ_WORK_COMPOSITION("TXX", GenericFieldKey.MUSICBRAINZ_WORK_COMPOSITION, ID3v22FieldKey.MUSICBRAINZ_WORK_COMPOSITION, "Text: MUSICBRAINZ_WORK_COMPOSITION", false, false, false, false, false, false),
        MUSICBRAINZ_WORK_COMPOSITION_ID("TXX", GenericFieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID, ID3v22FieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID, "Text: MUSICBRAINZ_WORK_COMPOSITION_ID", false, false, false, false, false, false),
        MUSICBRAINZ_WORK_ID("TXX", GenericFieldKey.MUSICBRAINZ_WORK_ID, ID3v22FieldKey.MUSICBRAINZ_WORK_ID, "Text: MusicBrainz Work Id", false, false, false, false, false, false),
        MUSICBRAINZ_WORK_PART_LEVEL1_ID("TXX", GenericFieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID, ID3v22FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID, "Text: MUSICBRAINZ_WORK_PART_LEVEL1_ID", false, false, false, false, false, false),
        MUSICBRAINZ_WORK_PART_LEVEL2_ID("TXX", GenericFieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID, ID3v22FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID, "Text: MUSICBRAINZ_WORK_PART_LEVEL2_ID", false, false, false, false, false, false),
        MUSICBRAINZ_WORK_PART_LEVEL3_ID("TXX", GenericFieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID, ID3v22FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID, "Text: MUSICBRAINZ_WORK_PART_LEVEL3_ID", false, false, false, false, false, false),
        MUSICBRAINZ_WORK_PART_LEVEL4_ID("TXX", GenericFieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID, ID3v22FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID, "Text: MUSICBRAINZ_WORK_PART_LEVEL4_ID", false, false, false, false, false, false),
        MUSICBRAINZ_WORK_PART_LEVEL5_ID("TXX", GenericFieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID, ID3v22FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID, "Text: MUSICBRAINZ_WORK_PART_LEVEL5_ID", false, false, false, false, false, false),
        MUSICBRAINZ_WORK_PART_LEVEL6_ID("TXX", GenericFieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID, ID3v22FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID, "Text: MUSICBRAINZ_WORK_PART_LEVEL6_ID", false, false, false, false, false, false),
        MUSICIP_ID("TXX", GenericFieldKey.MUSICIP_ID, ID3v22FieldKey.MUSICIP_ID, "Text: MusicIP PUID", false, false, false, false, false, false),
        OPUS("TXX", GenericFieldKey.OPUS, ID3v22FieldKey.OPUS, "Text: OPUS", false, false, false, false, false, false),
        ORCHESTRA("TXX", GenericFieldKey.ORCHESTRA, ID3v22FieldKey.ORCHESTRA, "Text: ORCHESTRA", false, false, false, false, false, false),
        ORCHESTRA_SORT("TXX", GenericFieldKey.ORCHESTRA_SORT, ID3v22FieldKey.ORCHESTRA_SORT, "Text: ORCHESTRA_SORT", false, false, false, false, false, false),
        PART("TXX", GenericFieldKey.PART, ID3v22FieldKey.PART, "Text: PART", false, false, false, false, false, false),
        PART_NUMBER("TXX", GenericFieldKey.PART_NUMBER, ID3v22FieldKey.PART_NUMBER, "Text: PARTNUMBER", false, false, false, false, false, false),
        PART_TYPE("TXX", GenericFieldKey.PART_TYPE, ID3v22FieldKey.PART_TYPE, "Text: PART_TYPE", false, false, false, false, false, false),
        PERFORMER_NAME("TXX", GenericFieldKey.PERFORMER_NAME, ID3v22FieldKey.PERFORMER_NAME, "Text: PERFORMER_NAME", false, false, false, false, false, false),
        PERFORMER_NAME_SORT("TXX", GenericFieldKey.PERFORMER_NAME_SORT, ID3v22FieldKey.PERFORMER_NAME_SORT, "Text: PERFORMER_NAME_SORT", false, false, false, false, false, false),
        PERIOD("TXX", GenericFieldKey.PERIOD, ID3v22FieldKey.PERIOD, "Text: PERIOD", false, false, false, false, false, false),
        RANKING("TXX", GenericFieldKey.RANKING, ID3v22FieldKey.RANKING, "Text: RANKING", false, false, false, false, false, false),
        SCRIPT("TXX", GenericFieldKey.SCRIPT, ID3v22FieldKey.SCRIPT, "Text: Script", false, false, false, false, false, false),
        SINGLE_DISC_TRACK_NO("TXX", GenericFieldKey.SINGLE_DISC_TRACK_NO, ID3v22FieldKey.SINGLE_DISC_TRACK_NO, "Text: SINGLE_DISC_TRACK_NO", false, false, false, false, false, false),
        TAGS("TXX", GenericFieldKey.TAGS, ID3v22FieldKey.TAGS, "Text: TAGS", false, false, false, false, false, false),
        TIMBRE("TXX", GenericFieldKey.TIMBRE, ID3v22FieldKey.TIMBRE, "Text: TIMBRE_BRIGHTNESS", false, false, false, false, false, false),
        TITLE_MOVEMENT("TXX", GenericFieldKey.TITLE_MOVEMENT, ID3v22FieldKey.TITLE_MOVEMENT, "Text: TITLE_MOVEMENT", false, false, false, false, false, false),
        TONALITY("TXX", GenericFieldKey.TONALITY, ID3v22FieldKey.TONALITY, "Text: TONALITY", false, false, false, false, false, false),
        WORK("TXX", GenericFieldKey.WORK, ID3v22FieldKey.WORK, "Text: WORK", false, false, false, false, false, false),
        WORK_PART_LEVEL1("TXX", null, ID3v22FieldKey.WORK_PART_LEVEL1, "Text: MUSICBRAINZ_WORK_PART_LEVEL1", false, false, false, false, false, false),
        WORK_PART_LEVEL1_TYPE("TXX", null, ID3v22FieldKey.WORK_PART_LEVEL1_TYPE, "Text: MUSICBRAINZ_WORK_PART_LEVEL1_TYPE", false, false, false, false, false, false),
        WORK_PART_LEVEL2("TXX", null, ID3v22FieldKey.WORK_PART_LEVEL2, "Text: MUSICBRAINZ_WORK_PART_LEVEL2", false, false, false, false, false, false),
        WORK_PART_LEVEL2_TYPE("TXX", null, ID3v22FieldKey.WORK_PART_LEVEL2_TYPE, "Text: MUSICBRAINZ_WORK_PART_LEVEL2_TYPE", false, false, false, false, false, false),
        WORK_PART_LEVEL3("TXX", null, ID3v22FieldKey.WORK_PART_LEVEL3, "Text: MUSICBRAINZ_WORK_PART_LEVEL3", false, false, false, false, false, false),
        WORK_PART_LEVEL3_TYPE("TXX", null, ID3v22FieldKey.WORK_PART_LEVEL3_TYPE, "Text: MUSICBRAINZ_WORK_PART_LEVEL3_TYPE", false, false, false, false, false, false),
        WORK_PART_LEVEL4("TXX", null, ID3v22FieldKey.WORK_PART_LEVEL4, "Text: MUSICBRAINZ_WORK_PART_LEVEL4", false, false, false, false, false, false),
        WORK_PART_LEVEL4_TYPE("TXX", null, ID3v22FieldKey.WORK_PART_LEVEL4_TYPE, "Text: MUSICBRAINZ_WORK_PART_LEVEL4_TYPE", false, false, false, false, false, false),
        WORK_PART_LEVEL5("TXX", null, ID3v22FieldKey.WORK_PART_LEVEL5, "Text: MUSICBRAINZ_WORK_PART_LEVEL5", false, false, false, false, false, false),
        WORK_PART_LEVEL5_TYPE("TXX", null, ID3v22FieldKey.WORK_PART_LEVEL5_TYPE, "Text: MUSICBRAINZ_WORK_PART_LEVEL5_TYPE", false, false, false, false, false, false),
        WORK_PART_LEVEL6("TXX", null, ID3v22FieldKey.WORK_PART_LEVEL6, "Text: MUSICBRAINZ_WORK_PART_LEVEL6", false, false, false, false, false, false),
        WORK_PART_LEVEL6_TYPE("TXX", null, ID3v22FieldKey.WORK_PART_LEVEL6_TYPE, "Text: MUSICBRAINZ_WORK_PART_LEVEL6_TYPE", false, false, false, false, false, false),
        WORK_TYPE("TXX", GenericFieldKey.WORK_TYPE, ID3v22FieldKey.WORK_TYPE, "Text: WORK_TYPE", false, false, false, false, false, false),
    ;

    companion object {

        fun contains(id: String?): Boolean = entries.any { e -> id == e.id }

        fun fromId(id: String?): ID3v22FrameId? = entries.find { e -> id == e.id }

        fun fromFieldKey(fieldKey: GenericFieldKey?): ID3v22FrameId? = entries.find { e -> fieldKey == e.genericFieldKey }

        fun commonFrames(): List<ID3v22FrameId> = entries.filter { e -> e.isCommon }

        fun binaryFrames(): List<ID3v22FrameId> = entries.filter { e -> e.isBinary }

        fun multipleFrames(): List<ID3v22FrameId> = entries.filter { e -> e.isMultipleAllowed }

        fun supprtedFrames(): List<ID3v22FrameId> = entries.filter { e -> e.isSupported }

        fun extensionFrames(): List<ID3v22FrameId> = entries.filter { e -> e.isExtension }

        fun discardIfFileAltered(): List<ID3v22FrameId> = entries.filter { e -> e.isDiscardedIfFileAltered }

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