package de.visualdigits.kaudiotagger.model.id3.frame

import de.visualdigits.kaudiotagger.model.id3.types.ID3v22FrameId
import de.visualdigits.kaudiotagger.model.id3.types.ID3v23FrameId
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId

abstract class ID3Frames {

    companion object {
        
        /**
         * Mapping from v22 to v23
         */
        val convertv22Tov23: MutableMap<ID3v22FrameId, ID3v23FrameId> = mutableMapOf()
        val convertv23Tov22: MutableMap<ID3v23FrameId, ID3v22FrameId> = mutableMapOf()

        val forcev22Tov23: MutableMap<ID3v22FrameId, ID3v23FrameId> = mutableMapOf()
        val forcev23Tov22: MutableMap<ID3v23FrameId, ID3v22FrameId> = mutableMapOf()

        val convertv23Tov24: MutableMap<ID3v23FrameId, ID3v24FrameId> = mutableMapOf()
        val convertv24Tov23: MutableMap<ID3v24FrameId, ID3v23FrameId> = mutableMapOf()

        val forcev23Tov24: MutableMap<ID3v23FrameId, ID3v24FrameId> = mutableMapOf()
        val forcev24Tov23: MutableMap<ID3v24FrameId, ID3v23FrameId> = mutableMapOf()
        
        init {
            // All v22 ids were renamed in v23, but are essentially the same
            convertv22Tov23[ID3v22FrameId.ACCOMPANIMENT] = ID3v23FrameId.ACCOMPANIMENT
            convertv22Tov23[ID3v22FrameId.ALBUM] = ID3v23FrameId.ALBUM
            convertv22Tov23[ID3v22FrameId.ARTIST] = ID3v23FrameId.ARTIST
            convertv22Tov23[ID3v22FrameId.AUDIO_ENCRYPTION] = ID3v23FrameId.AUDIO_ENCRYPTION
            convertv22Tov23[ID3v22FrameId.BPM] = ID3v23FrameId.BPM
            convertv22Tov23[ID3v22FrameId.COMMENT] = ID3v23FrameId.COMMENT
            convertv22Tov23[ID3v22FrameId.COMMENT] = ID3v23FrameId.COMMENT
            convertv22Tov23[ID3v22FrameId.COMPOSER] = ID3v23FrameId.COMPOSER
            convertv22Tov23[ID3v22FrameId.CONDUCTOR] = ID3v23FrameId.CONDUCTOR
            convertv22Tov23[ID3v22FrameId.CONTENT_GROUP_DESC] = ID3v23FrameId.CONTENT_GROUP_DESC
            convertv22Tov23[ID3v22FrameId.COPYRIGHTINFO] = ID3v23FrameId.COPYRIGHTINFO
            convertv22Tov23[ID3v22FrameId.ENCODEDBY] = ID3v23FrameId.ENCODEDBY
            convertv22Tov23[ID3v22FrameId.EQUALISATION] = ID3v23FrameId.EQUALISATION
            convertv22Tov23[ID3v22FrameId.EVENT_TIMING_CODES] = ID3v23FrameId.EVENT_TIMING_CODES
            convertv22Tov23[ID3v22FrameId.FILE_TYPE] = ID3v23FrameId.FILE_TYPE
            convertv22Tov23[ID3v22FrameId.GENERAL_ENCAPS_OBJECT] = ID3v23FrameId.GENERAL_ENCAPS_OBJECT
            convertv22Tov23[ID3v22FrameId.GENRE] = ID3v23FrameId.GENRE
            convertv22Tov23[ID3v22FrameId.HW_SW_SETTINGS] = ID3v23FrameId.HW_SW_SETTINGS
            convertv22Tov23[ID3v22FrameId.INITIAL_KEY] = ID3v23FrameId.INITIAL_KEY
            convertv22Tov23[ID3v22FrameId.IPLS] = ID3v23FrameId.INVOLVED_PEOPLE
            convertv22Tov23[ID3v22FrameId.ISRC] = ID3v23FrameId.ISRC
            convertv22Tov23[ID3v22FrameId.ITUNES_GROUPING] = ID3v23FrameId.ITUNES_GROUPING
            convertv22Tov23[ID3v22FrameId.LANGUAGE] = ID3v23FrameId.LANGUAGE
            convertv22Tov23[ID3v22FrameId.LENGTH] = ID3v23FrameId.LENGTH
            convertv22Tov23[ID3v22FrameId.LINKED_INFO] = ID3v23FrameId.LINKED_INFO
            convertv22Tov23[ID3v22FrameId.LYRICIST] = ID3v23FrameId.LYRICIST
            convertv22Tov23[ID3v22FrameId.MEDIA_TYPE] = ID3v23FrameId.MEDIA_TYPE
            convertv22Tov23[ID3v22FrameId.MOVEMENT] = ID3v23FrameId.MOVEMENT
            convertv22Tov23[ID3v22FrameId.MOVEMENT_NO] = ID3v23FrameId.MOVEMENT_NO
            convertv22Tov23[ID3v22FrameId.MPEG_LOCATION_LOOKUP_TABLE] = ID3v23FrameId.MPEG_LOCATION_LOOKUP_TABLE
            convertv22Tov23[ID3v22FrameId.MUSIC_CD_ID] = ID3v23FrameId.MUSIC_CD_ID
            convertv22Tov23[ID3v22FrameId.ORIGARTIST] = ID3v23FrameId.ORIGARTIST
            convertv22Tov23[ID3v22FrameId.ORIG_FILENAME] = ID3v23FrameId.ORIG_FILENAME
            convertv22Tov23[ID3v22FrameId.ORIG_LYRICIST] = ID3v23FrameId.ORIG_LYRICIST
            convertv22Tov23[ID3v22FrameId.ORIG_TITLE] = ID3v23FrameId.ORIG_TITLE
            convertv22Tov23[ID3v22FrameId.PLAYLIST_DELAY] = ID3v23FrameId.PLAYLIST_DELAY
            convertv22Tov23[ID3v22FrameId.PLAY_COUNTER] = ID3v23FrameId.PLAY_COUNTER
            convertv22Tov23[ID3v22FrameId.PLAY_COUNTER] = ID3v23FrameId.PLAY_COUNTER
            convertv22Tov23[ID3v22FrameId.POPULARIMETER] = ID3v23FrameId.POPULARIMETER
            convertv22Tov23[ID3v22FrameId.PUBLISHER] = ID3v23FrameId.PUBLISHER
            convertv22Tov23[ID3v22FrameId.RECOMMENDED_BUFFER_SIZE] = ID3v23FrameId.RECOMMENDED_BUFFER_SIZE
            convertv22Tov23[ID3v22FrameId.RECOMMENDED_BUFFER_SIZE] = ID3v23FrameId.RECOMMENDED_BUFFER_SIZE
            convertv22Tov23[ID3v22FrameId.RELATIVE_VOLUME_ADJUSTMENT] = ID3v23FrameId.RELATIVE_VOLUME_ADJUSTMENT
            convertv22Tov23[ID3v22FrameId.REMIXED] = ID3v23FrameId.REMIXED
            convertv22Tov23[ID3v22FrameId.REVERB] = ID3v23FrameId.REVERB
            convertv22Tov23[ID3v22FrameId.SET] = ID3v23FrameId.SET
            convertv22Tov23[ID3v22FrameId.SET_SUBTITLE] = ID3v23FrameId.SET_SUBTITLE
            convertv22Tov23[ID3v22FrameId.SYNC_LYRIC] = ID3v23FrameId.SYNC_LYRIC
            convertv22Tov23[ID3v22FrameId.SYNC_TEMPO] = ID3v23FrameId.SYNC_TEMPO
            convertv22Tov23[ID3v22FrameId.TDAT] = ID3v23FrameId.TDAT
            convertv22Tov23[ID3v22FrameId.TIME] = ID3v23FrameId.TIME
            convertv22Tov23[ID3v22FrameId.TITLE_REFINEMENT] = ID3v23FrameId.TITLE_REFINEMENT
            convertv22Tov23[ID3v22FrameId.TORY] = ID3v23FrameId.TORY
            convertv22Tov23[ID3v22FrameId.TRACK] = ID3v23FrameId.TRACK
            convertv22Tov23[ID3v22FrameId.TRDA] = ID3v23FrameId.TRDA
            convertv22Tov23[ID3v22FrameId.TSIZ] = ID3v23FrameId.TSIZ
            convertv22Tov23[ID3v22FrameId.TYER] = ID3v23FrameId.TYER
            convertv22Tov23[ID3v22FrameId.UNIQUE_FILE_ID] = ID3v23FrameId.UNIQUE_FILE_ID
            convertv22Tov23[ID3v22FrameId.UNIQUE_FILE_ID] = ID3v23FrameId.UNIQUE_FILE_ID
            convertv22Tov23[ID3v22FrameId.UNSYNC_LYRICS] = ID3v23FrameId.UNSYNC_LYRICS
            convertv22Tov23[ID3v22FrameId.URL_ARTIST_WEB] = ID3v23FrameId.URL_ARTIST_WEB
            convertv22Tov23[ID3v22FrameId.URL_COMMERCIAL] = ID3v23FrameId.URL_COMMERCIAL
            convertv22Tov23[ID3v22FrameId.URL_COPYRIGHT] = ID3v23FrameId.URL_COPYRIGHT
            convertv22Tov23[ID3v22FrameId.URL_FILE_WEB] = ID3v23FrameId.URL_FILE_WEB
            convertv22Tov23[ID3v22FrameId.URL_OFFICIAL_RADIO] = ID3v23FrameId.URL_OFFICIAL_RADIO
            convertv22Tov23[ID3v22FrameId.URL_PAYMENT] = ID3v23FrameId.URL_PAYMENT
            convertv22Tov23[ID3v22FrameId.URL_PUBLISHERS] = ID3v23FrameId.URL_PUBLISHERS
            convertv22Tov23[ID3v22FrameId.URL_SOURCE_WEB] = ID3v23FrameId.URL_SOURCE_WEB
            convertv22Tov23[ID3v22FrameId.USER_DEFINED_INFO] = ID3v23FrameId.USER_DEFINED_INFO
            convertv22Tov23[ID3v22FrameId.USER_DEFINED_URL] = ID3v23FrameId.USER_DEFINED_URL
            convertv22Tov23[ID3v22FrameId.TITLE] = ID3v23FrameId.TITLE
            convertv22Tov23[ID3v22FrameId.IS_COMPILATION] = ID3v23FrameId.IS_COMPILATION
            convertv22Tov23[ID3v22FrameId.TITLE_SORT_ORDER_ITUNES] = ID3v23FrameId.TITLE_SORT_ORDER_ITUNES
            convertv22Tov23[ID3v22FrameId.ARTIST_SORT_ORDER_ITUNES] = ID3v23FrameId.ARTIST_SORT_ORDER_ITUNES
            convertv22Tov23[ID3v22FrameId.ALBUM_SORT_ORDER_ITUNES] = ID3v23FrameId.ALBUM_SORT_ORDER_ITUNES
            convertv22Tov23[ID3v22FrameId.ALBUM_ARTIST_SORT_ORDER_ITUNES] = ID3v23FrameId.ALBUM_ARTIST_SORT_ORDER_ITUNES
            convertv22Tov23[ID3v22FrameId.COMPOSER_SORT_ORDER_ITUNES] = ID3v23FrameId.COMPOSER_SORT_ORDER_ITUNES

            // v23 to v22 The translation is both way
            convertv22Tov23.keys.forEach { key -> convertv22Tov23[key]?.also { v -> convertv23Tov22[v] = key }  }

            //This one way translation allows us to convert XSOT to TST, but in the other direction gets converted to TSOT
            convertv23Tov22[ID3v23FrameId.TITLE_SORT_ORDER_MUSICBRAINZ] = ID3v22FrameId.TITLE_SORT_ORDER_ITUNES
            convertv23Tov22[ID3v23FrameId.ARTIST_SORT_ORDER_MUSICBRAINZ] = ID3v22FrameId.ARTIST_SORT_ORDER_ITUNES
            convertv23Tov22[ID3v23FrameId.ALBUM_SORT_ORDER_MUSICBRAINZ] = ID3v22FrameId.ALBUM_SORT_ORDER_ITUNES
            //TODO What does CRM Map to
            // Force v22 to v23,  Extra fields in v23 version
            forcev22Tov23[ID3v22FrameId.ATTACHED_PICTURE] = ID3v23FrameId.ATTACHED_PICTURE
            // Force v23 to v22
            forcev23Tov22[ID3v23FrameId.ATTACHED_PICTURE] = ID3v22FrameId.ATTACHED_PICTURE

            // Define the mapping from v23 to v24 only maps values where
            // the v23 ID is not a v24 ID and where the translation from v23 to v24
            // ID does not affect the framebody.
            //This one way allows us to convert XSOT to TSOT,XSOP to TSOP and XSOA - TSOA but in the other direction gets converted to TSOT,TSOP,TSOA
            convertv23Tov24[ID3v23FrameId.TITLE_SORT_ORDER_MUSICBRAINZ] = ID3v24FrameId.TITLE_SORT_ORDER
            convertv23Tov24[ID3v23FrameId.ARTIST_SORT_ORDER_MUSICBRAINZ] = ID3v24FrameId.ARTIST_SORT_ORDER
            convertv23Tov24[ID3v23FrameId.ALBUM_SORT_ORDER_MUSICBRAINZ] = ID3v24FrameId.ALBUM_SORT_ORDER
            // No others exist because most v23 mappings are identical to v24 therefore no mapping required and the ones that
            // are different need to be forced.

            // Force v23 to v24 These are deprecated and need to do a forced mapping
            forcev23Tov24[ID3v23FrameId.RELATIVE_VOLUME_ADJUSTMENT] = ID3v24FrameId.RELATIVE_VOLUME_ADJUSTMENT2
            forcev23Tov24[ID3v23FrameId.EQUALISATION] = ID3v24FrameId.EQUALISATION2
            forcev23Tov24[ID3v23FrameId.INVOLVED_PEOPLE] = ID3v24FrameId.INVOLVED_PEOPLE
            forcev23Tov24[ID3v23FrameId.TDAT] = ID3v24FrameId.YEAR
            forcev23Tov24[ID3v23FrameId.TIME] = ID3v24FrameId.YEAR
            forcev23Tov24[ID3v23FrameId.TORY] = ID3v24FrameId.ORIGINAL_RELEASE_TIME
            forcev23Tov24[ID3v23FrameId.TRDA] = ID3v24FrameId.YEAR
            forcev23Tov24[ID3v23FrameId.TYER] = ID3v24FrameId.YEAR

            //Note Force v24 to v23, TDRC is a 1M relationship handled specially.
            // @TODO EQUALISATION
            forcev24Tov23[ID3v24FrameId.RELATIVE_VOLUME_ADJUSTMENT2] = ID3v23FrameId.RELATIVE_VOLUME_ADJUSTMENT
            //Used to be a special frame now a text frame
            forcev24Tov23[ID3v24FrameId.INVOLVED_PEOPLE] = ID3v23FrameId.INVOLVED_PEOPLE
            //No Mood frame in v23 so use a TXXX frame
            forcev24Tov23[ID3v24FrameId.MOOD] = ID3v23FrameId.USER_DEFINED_INFO
            //Release time can be mapped to release year (but can only hold year)
            forcev24Tov23[ID3v24FrameId.ORIGINAL_RELEASE_TIME] = ID3v23FrameId.TORY
        }
    }
}