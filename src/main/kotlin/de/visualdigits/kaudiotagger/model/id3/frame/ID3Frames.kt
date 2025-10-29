package de.visualdigits.kaudiotagger.model.id3.frame

import de.visualdigits.kaudiotagger.model.id3.types.ID3V22FrameId
import de.visualdigits.kaudiotagger.model.id3.types.ID3V23FrameId
import de.visualdigits.kaudiotagger.model.id3.types.ID3V24FrameId

abstract class ID3Frames {

    companion object {
        
        /**
         * Mapping from v22 to v23
         */
        val convertv22Tov23: MutableMap<ID3V22FrameId, ID3V23FrameId> = mutableMapOf()
        val convertv23Tov22: MutableMap<ID3V23FrameId, ID3V22FrameId> = mutableMapOf()

        val forcev22Tov23: MutableMap<ID3V22FrameId, ID3V23FrameId> = mutableMapOf()
        val forcev23Tov22: MutableMap<ID3V23FrameId, ID3V22FrameId> = mutableMapOf()

        val convertv23Tov24: MutableMap<ID3V23FrameId, ID3V24FrameId> = mutableMapOf()
        val convertv24Tov23: MutableMap<ID3V24FrameId, ID3V23FrameId> = mutableMapOf()

        val forcev23Tov24: MutableMap<ID3V23FrameId, ID3V24FrameId> = mutableMapOf()
        val forcev24Tov23: MutableMap<ID3V24FrameId, ID3V23FrameId> = mutableMapOf()
        
        init {
            // All v22 ids were renamed in v23, but are essentially the same
            convertv22Tov23[ID3V22FrameId.ACCOMPANIMENT] = ID3V23FrameId.ACCOMPANIMENT
            convertv22Tov23[ID3V22FrameId.ALBUM] = ID3V23FrameId.ALBUM
            convertv22Tov23[ID3V22FrameId.ARTIST] = ID3V23FrameId.ARTIST
            convertv22Tov23[ID3V22FrameId.AUDIO_ENCRYPTION] = ID3V23FrameId.AUDIO_ENCRYPTION
            convertv22Tov23[ID3V22FrameId.BPM] = ID3V23FrameId.BPM
            convertv22Tov23[ID3V22FrameId.COMMENT] = ID3V23FrameId.COMMENT
            convertv22Tov23[ID3V22FrameId.COMMENT] = ID3V23FrameId.COMMENT
            convertv22Tov23[ID3V22FrameId.COMPOSER] = ID3V23FrameId.COMPOSER
            convertv22Tov23[ID3V22FrameId.CONDUCTOR] = ID3V23FrameId.CONDUCTOR
            convertv22Tov23[ID3V22FrameId.CONTENT_GROUP_DESC] = ID3V23FrameId.CONTENT_GROUP_DESC
            convertv22Tov23[ID3V22FrameId.COPYRIGHTINFO] = ID3V23FrameId.COPYRIGHTINFO
            convertv22Tov23[ID3V22FrameId.ENCODEDBY] = ID3V23FrameId.ENCODEDBY
            convertv22Tov23[ID3V22FrameId.EQUALISATION] = ID3V23FrameId.EQUALISATION
            convertv22Tov23[ID3V22FrameId.EVENT_TIMING_CODES] = ID3V23FrameId.EVENT_TIMING_CODES
            convertv22Tov23[ID3V22FrameId.FILE_TYPE] = ID3V23FrameId.FILE_TYPE
            convertv22Tov23[ID3V22FrameId.GENERAL_ENCAPS_OBJECT] = ID3V23FrameId.GENERAL_ENCAPS_OBJECT
            convertv22Tov23[ID3V22FrameId.GENRE] = ID3V23FrameId.GENRE
            convertv22Tov23[ID3V22FrameId.HW_SW_SETTINGS] = ID3V23FrameId.HW_SW_SETTINGS
            convertv22Tov23[ID3V22FrameId.INITIAL_KEY] = ID3V23FrameId.INITIAL_KEY
            convertv22Tov23[ID3V22FrameId.IPLS] = ID3V23FrameId.INVOLVED_PEOPLE
            convertv22Tov23[ID3V22FrameId.ISRC] = ID3V23FrameId.ISRC
            convertv22Tov23[ID3V22FrameId.ITUNES_GROUPING] = ID3V23FrameId.ITUNES_GROUPING
            convertv22Tov23[ID3V22FrameId.LANGUAGE] = ID3V23FrameId.LANGUAGE
            convertv22Tov23[ID3V22FrameId.LENGTH] = ID3V23FrameId.LENGTH
            convertv22Tov23[ID3V22FrameId.LINKED_INFO] = ID3V23FrameId.LINKED_INFO
            convertv22Tov23[ID3V22FrameId.LYRICIST] = ID3V23FrameId.LYRICIST
            convertv22Tov23[ID3V22FrameId.MEDIA_TYPE] = ID3V23FrameId.MEDIA_TYPE
            convertv22Tov23[ID3V22FrameId.MOVEMENT] = ID3V23FrameId.MOVEMENT
            convertv22Tov23[ID3V22FrameId.MOVEMENT_NO] = ID3V23FrameId.MOVEMENT_NO
            convertv22Tov23[ID3V22FrameId.MPEG_LOCATION_LOOKUP_TABLE] = ID3V23FrameId.MPEG_LOCATION_LOOKUP_TABLE
            convertv22Tov23[ID3V22FrameId.MUSIC_CD_ID] = ID3V23FrameId.MUSIC_CD_ID
            convertv22Tov23[ID3V22FrameId.ORIGARTIST] = ID3V23FrameId.ORIGARTIST
            convertv22Tov23[ID3V22FrameId.ORIG_FILENAME] = ID3V23FrameId.ORIG_FILENAME
            convertv22Tov23[ID3V22FrameId.ORIG_LYRICIST] = ID3V23FrameId.ORIG_LYRICIST
            convertv22Tov23[ID3V22FrameId.ORIG_TITLE] = ID3V23FrameId.ORIG_TITLE
            convertv22Tov23[ID3V22FrameId.PLAYLIST_DELAY] = ID3V23FrameId.PLAYLIST_DELAY
            convertv22Tov23[ID3V22FrameId.PLAY_COUNTER] = ID3V23FrameId.PLAY_COUNTER
            convertv22Tov23[ID3V22FrameId.PLAY_COUNTER] = ID3V23FrameId.PLAY_COUNTER
            convertv22Tov23[ID3V22FrameId.POPULARIMETER] = ID3V23FrameId.POPULARIMETER
            convertv22Tov23[ID3V22FrameId.PUBLISHER] = ID3V23FrameId.PUBLISHER
            convertv22Tov23[ID3V22FrameId.RECOMMENDED_BUFFER_SIZE] = ID3V23FrameId.RECOMMENDED_BUFFER_SIZE
            convertv22Tov23[ID3V22FrameId.RECOMMENDED_BUFFER_SIZE] = ID3V23FrameId.RECOMMENDED_BUFFER_SIZE
            convertv22Tov23[ID3V22FrameId.RELATIVE_VOLUME_ADJUSTMENT] = ID3V23FrameId.RELATIVE_VOLUME_ADJUSTMENT
            convertv22Tov23[ID3V22FrameId.REMIXED] = ID3V23FrameId.REMIXED
            convertv22Tov23[ID3V22FrameId.REVERB] = ID3V23FrameId.REVERB
            convertv22Tov23[ID3V22FrameId.SET] = ID3V23FrameId.SET
            convertv22Tov23[ID3V22FrameId.SET_SUBTITLE] = ID3V23FrameId.SET_SUBTITLE
            convertv22Tov23[ID3V22FrameId.SYNC_LYRIC] = ID3V23FrameId.SYNC_LYRIC
            convertv22Tov23[ID3V22FrameId.SYNC_TEMPO] = ID3V23FrameId.SYNC_TEMPO
            convertv22Tov23[ID3V22FrameId.TDAT] = ID3V23FrameId.TDAT
            convertv22Tov23[ID3V22FrameId.TIME] = ID3V23FrameId.TIME
            convertv22Tov23[ID3V22FrameId.TITLE_REFINEMENT] = ID3V23FrameId.TITLE_REFINEMENT
            convertv22Tov23[ID3V22FrameId.TORY] = ID3V23FrameId.TORY
            convertv22Tov23[ID3V22FrameId.TRACK] = ID3V23FrameId.TRACK
            convertv22Tov23[ID3V22FrameId.TRDA] = ID3V23FrameId.TRDA
            convertv22Tov23[ID3V22FrameId.TSIZ] = ID3V23FrameId.TSIZ
            convertv22Tov23[ID3V22FrameId.TYER] = ID3V23FrameId.TYER
            convertv22Tov23[ID3V22FrameId.UNIQUE_FILE_ID] = ID3V23FrameId.UNIQUE_FILE_ID
            convertv22Tov23[ID3V22FrameId.UNIQUE_FILE_ID] = ID3V23FrameId.UNIQUE_FILE_ID
            convertv22Tov23[ID3V22FrameId.UNSYNC_LYRICS] = ID3V23FrameId.UNSYNC_LYRICS
            convertv22Tov23[ID3V22FrameId.URL_ARTIST_WEB] = ID3V23FrameId.URL_ARTIST_WEB
            convertv22Tov23[ID3V22FrameId.URL_COMMERCIAL] = ID3V23FrameId.URL_COMMERCIAL
            convertv22Tov23[ID3V22FrameId.URL_COPYRIGHT] = ID3V23FrameId.URL_COPYRIGHT
            convertv22Tov23[ID3V22FrameId.URL_FILE_WEB] = ID3V23FrameId.URL_FILE_WEB
            convertv22Tov23[ID3V22FrameId.URL_OFFICIAL_RADIO] = ID3V23FrameId.URL_OFFICIAL_RADIO
            convertv22Tov23[ID3V22FrameId.URL_PAYMENT] = ID3V23FrameId.URL_PAYMENT
            convertv22Tov23[ID3V22FrameId.URL_PUBLISHERS] = ID3V23FrameId.URL_PUBLISHERS
            convertv22Tov23[ID3V22FrameId.URL_SOURCE_WEB] = ID3V23FrameId.URL_SOURCE_WEB
            convertv22Tov23[ID3V22FrameId.USER_DEFINED_INFO] = ID3V23FrameId.USER_DEFINED_INFO
            convertv22Tov23[ID3V22FrameId.USER_DEFINED_URL] = ID3V23FrameId.USER_DEFINED_URL
            convertv22Tov23[ID3V22FrameId.TITLE] = ID3V23FrameId.TITLE
            convertv22Tov23[ID3V22FrameId.IS_COMPILATION] = ID3V23FrameId.IS_COMPILATION
            convertv22Tov23[ID3V22FrameId.TITLE_SORT_ORDER_ITUNES] = ID3V23FrameId.TITLE_SORT_ORDER_ITUNES
            convertv22Tov23[ID3V22FrameId.ARTIST_SORT_ORDER_ITUNES] = ID3V23FrameId.ARTIST_SORT_ORDER_ITUNES
            convertv22Tov23[ID3V22FrameId.ALBUM_SORT_ORDER_ITUNES] = ID3V23FrameId.ALBUM_SORT_ORDER_ITUNES
            convertv22Tov23[ID3V22FrameId.ALBUM_ARTIST_SORT_ORDER_ITUNES] = ID3V23FrameId.ALBUM_ARTIST_SORT_ORDER_ITUNES
            convertv22Tov23[ID3V22FrameId.COMPOSER_SORT_ORDER_ITUNES] = ID3V23FrameId.COMPOSER_SORT_ORDER_ITUNES

            // v23 to v22 The translation is both way
            convertv22Tov23.keys.forEach { key -> convertv22Tov23[key]?.also { v -> convertv23Tov22[v] = key }  }

            //This one way translation allows us to convert XSOT to TST, but in the other direction gets converted to TSOT
            convertv23Tov22[ID3V23FrameId.TITLE_SORT_ORDER_MUSICBRAINZ] = ID3V22FrameId.TITLE_SORT_ORDER_ITUNES
            convertv23Tov22[ID3V23FrameId.ARTIST_SORT_ORDER_MUSICBRAINZ] = ID3V22FrameId.ARTIST_SORT_ORDER_ITUNES
            convertv23Tov22[ID3V23FrameId.ALBUM_SORT_ORDER_MUSICBRAINZ] = ID3V22FrameId.ALBUM_SORT_ORDER_ITUNES
            //TODO What does CRM Map to
            // Force v22 to v23,  Extra fields in v23 version
            forcev22Tov23[ID3V22FrameId.ATTACHED_PICTURE] = ID3V23FrameId.ATTACHED_PICTURE
            // Force v23 to v22
            forcev23Tov22[ID3V23FrameId.ATTACHED_PICTURE] = ID3V22FrameId.ATTACHED_PICTURE

            // Define the mapping from v23 to v24 only maps values where
            // the v23 ID is not a v24 ID and where the translation from v23 to v24
            // ID does not affect the framebody.
            //This one way allows us to convert XSOT to TSOT,XSOP to TSOP and XSOA - TSOA but in the other direction gets converted to TSOT,TSOP,TSOA
            convertv23Tov24[ID3V23FrameId.TITLE_SORT_ORDER_MUSICBRAINZ] = ID3V24FrameId.TITLE_SORT_ORDER
            convertv23Tov24[ID3V23FrameId.ARTIST_SORT_ORDER_MUSICBRAINZ] = ID3V24FrameId.ARTIST_SORT_ORDER
            convertv23Tov24[ID3V23FrameId.ALBUM_SORT_ORDER_MUSICBRAINZ] = ID3V24FrameId.ALBUM_SORT_ORDER
            // No others exist because most v23 mappings are identical to v24 therefore no mapping required and the ones that
            // are different need to be forced.

            // Force v23 to v24 These are deprecated and need to do a forced mapping
            forcev23Tov24[ID3V23FrameId.RELATIVE_VOLUME_ADJUSTMENT] = ID3V24FrameId.RELATIVE_VOLUME_ADJUSTMENT2
            forcev23Tov24[ID3V23FrameId.EQUALISATION] = ID3V24FrameId.EQUALISATION2
            forcev23Tov24[ID3V23FrameId.INVOLVED_PEOPLE] = ID3V24FrameId.INVOLVED_PEOPLE
            forcev23Tov24[ID3V23FrameId.TDAT] = ID3V24FrameId.YEAR
            forcev23Tov24[ID3V23FrameId.TIME] = ID3V24FrameId.YEAR
            forcev23Tov24[ID3V23FrameId.TORY] = ID3V24FrameId.ORIGINAL_RELEASE_TIME
            forcev23Tov24[ID3V23FrameId.TRDA] = ID3V24FrameId.YEAR
            forcev23Tov24[ID3V23FrameId.TYER] = ID3V24FrameId.YEAR

            //Note Force v24 to v23, TDRC is a 1M relationship handled specially.
            // @TODO EQUALISATION
            forcev24Tov23[ID3V24FrameId.RELATIVE_VOLUME_ADJUSTMENT2] = ID3V23FrameId.RELATIVE_VOLUME_ADJUSTMENT
            //Used to be a special frame now a text frame
            forcev24Tov23[ID3V24FrameId.INVOLVED_PEOPLE] = ID3V23FrameId.INVOLVED_PEOPLE
            //No Mood frame in v23 so use a TXXX frame
            forcev24Tov23[ID3V24FrameId.MOOD] = ID3V23FrameId.USER_DEFINED_INFO
            //Release time can be mapped to release year (but can only hold year)
            forcev24Tov23[ID3V24FrameId.ORIGINAL_RELEASE_TIME] = ID3V23FrameId.TORY
        }
    }
}