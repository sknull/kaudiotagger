package de.visualdigits.kaudiotagger.model.frame.id3

import de.visualdigits.kaudiotagger.model.kframe.ID3v22KFrame
import de.visualdigits.kaudiotagger.model.kframe.ID3v23KFrame
import de.visualdigits.kaudiotagger.model.kframe.ID3v24KFrame

abstract class ID3Frames {

    companion object {
        
        /**
         * Mapping from v22 to v23
         */
        val convertv22Tov23: MutableMap<ID3v22KFrame, ID3v23KFrame> = mutableMapOf()
        val convertv23Tov22: MutableMap<ID3v23KFrame, ID3v22KFrame> = mutableMapOf()

        val forcev22Tov23: MutableMap<ID3v22KFrame, ID3v23KFrame> = mutableMapOf()
        val forcev23Tov22: MutableMap<ID3v23KFrame, ID3v22KFrame> = mutableMapOf()

        val convertv23Tov24: MutableMap<ID3v23KFrame, ID3v24KFrame> = mutableMapOf()
        val convertv24Tov23: MutableMap<ID3v24KFrame, ID3v23KFrame> = mutableMapOf()

        val forcev23Tov24: MutableMap<ID3v23KFrame, ID3v24KFrame> = mutableMapOf()
        val forcev24Tov23: MutableMap<ID3v24KFrame,ID3v23KFrame> = mutableMapOf()
        
        init {
            // All v22 ids were renamed in v23, but are essentially the same
            convertv22Tov23[ID3v22KFrame.ACCOMPANIMENT] = ID3v23KFrame.ACCOMPANIMENT
            convertv22Tov23[ID3v22KFrame.ALBUM] = ID3v23KFrame.ALBUM
            convertv22Tov23[ID3v22KFrame.ARTIST] = ID3v23KFrame.ARTIST
            convertv22Tov23[ID3v22KFrame.AUDIO_ENCRYPTION] = ID3v23KFrame.AUDIO_ENCRYPTION
            convertv22Tov23[ID3v22KFrame.BPM] = ID3v23KFrame.BPM
            convertv22Tov23[ID3v22KFrame.COMMENT] = ID3v23KFrame.COMMENT
            convertv22Tov23[ID3v22KFrame.COMMENT] = ID3v23KFrame.COMMENT
            convertv22Tov23[ID3v22KFrame.COMPOSER] = ID3v23KFrame.COMPOSER
            convertv22Tov23[ID3v22KFrame.CONDUCTOR] = ID3v23KFrame.CONDUCTOR
            convertv22Tov23[ID3v22KFrame.CONTENT_GROUP_DESC] = ID3v23KFrame.CONTENT_GROUP_DESC
            convertv22Tov23[ID3v22KFrame.COPYRIGHTINFO] = ID3v23KFrame.COPYRIGHTINFO
            convertv22Tov23[ID3v22KFrame.ENCODEDBY] = ID3v23KFrame.ENCODEDBY
            convertv22Tov23[ID3v22KFrame.EQUALISATION] = ID3v23KFrame.EQUALISATION
            convertv22Tov23[ID3v22KFrame.EVENT_TIMING_CODES] = ID3v23KFrame.EVENT_TIMING_CODES
            convertv22Tov23[ID3v22KFrame.FILE_TYPE] = ID3v23KFrame.FILE_TYPE
            convertv22Tov23[ID3v22KFrame.GENERAL_ENCAPS_OBJECT] = ID3v23KFrame.GENERAL_ENCAPS_OBJECT
            convertv22Tov23[ID3v22KFrame.GENRE] = ID3v23KFrame.GENRE
            convertv22Tov23[ID3v22KFrame.HW_SW_SETTINGS] = ID3v23KFrame.HW_SW_SETTINGS
            convertv22Tov23[ID3v22KFrame.INITIAL_KEY] = ID3v23KFrame.INITIAL_KEY
            convertv22Tov23[ID3v22KFrame.IPLS] = ID3v23KFrame.INVOLVED_PEOPLE
            convertv22Tov23[ID3v22KFrame.ISRC] = ID3v23KFrame.ISRC
            convertv22Tov23[ID3v22KFrame.ITUNES_GROUPING] = ID3v23KFrame.ITUNES_GROUPING
            convertv22Tov23[ID3v22KFrame.LANGUAGE] = ID3v23KFrame.LANGUAGE
            convertv22Tov23[ID3v22KFrame.LENGTH] = ID3v23KFrame.LENGTH
            convertv22Tov23[ID3v22KFrame.LINKED_INFO] = ID3v23KFrame.LINKED_INFO
            convertv22Tov23[ID3v22KFrame.LYRICIST] = ID3v23KFrame.LYRICIST
            convertv22Tov23[ID3v22KFrame.MEDIA_TYPE] = ID3v23KFrame.MEDIA_TYPE
            convertv22Tov23[ID3v22KFrame.MOVEMENT] = ID3v23KFrame.MOVEMENT
            convertv22Tov23[ID3v22KFrame.MOVEMENT_NO] = ID3v23KFrame.MOVEMENT_NO
            convertv22Tov23[ID3v22KFrame.MPEG_LOCATION_LOOKUP_TABLE] = ID3v23KFrame.MPEG_LOCATION_LOOKUP_TABLE
            convertv22Tov23[ID3v22KFrame.MUSIC_CD_ID] = ID3v23KFrame.MUSIC_CD_ID
            convertv22Tov23[ID3v22KFrame.ORIGARTIST] = ID3v23KFrame.ORIGARTIST
            convertv22Tov23[ID3v22KFrame.ORIG_FILENAME] = ID3v23KFrame.ORIG_FILENAME
            convertv22Tov23[ID3v22KFrame.ORIG_LYRICIST] = ID3v23KFrame.ORIG_LYRICIST
            convertv22Tov23[ID3v22KFrame.ORIG_TITLE] = ID3v23KFrame.ORIG_TITLE
            convertv22Tov23[ID3v22KFrame.PLAYLIST_DELAY] = ID3v23KFrame.PLAYLIST_DELAY
            convertv22Tov23[ID3v22KFrame.PLAY_COUNTER] = ID3v23KFrame.PLAY_COUNTER
            convertv22Tov23[ID3v22KFrame.PLAY_COUNTER] = ID3v23KFrame.PLAY_COUNTER
            convertv22Tov23[ID3v22KFrame.POPULARIMETER] = ID3v23KFrame.POPULARIMETER
            convertv22Tov23[ID3v22KFrame.PUBLISHER] = ID3v23KFrame.PUBLISHER
            convertv22Tov23[ID3v22KFrame.RECOMMENDED_BUFFER_SIZE] = ID3v23KFrame.RECOMMENDED_BUFFER_SIZE
            convertv22Tov23[ID3v22KFrame.RECOMMENDED_BUFFER_SIZE] = ID3v23KFrame.RECOMMENDED_BUFFER_SIZE
            convertv22Tov23[ID3v22KFrame.RELATIVE_VOLUME_ADJUSTMENT] = ID3v23KFrame.RELATIVE_VOLUME_ADJUSTMENT
            convertv22Tov23[ID3v22KFrame.REMIXED] = ID3v23KFrame.REMIXED
            convertv22Tov23[ID3v22KFrame.REVERB] = ID3v23KFrame.REVERB
            convertv22Tov23[ID3v22KFrame.SET] = ID3v23KFrame.SET
            convertv22Tov23[ID3v22KFrame.SET_SUBTITLE] = ID3v23KFrame.SET_SUBTITLE
            convertv22Tov23[ID3v22KFrame.SYNC_LYRIC] = ID3v23KFrame.SYNC_LYRIC
            convertv22Tov23[ID3v22KFrame.SYNC_TEMPO] = ID3v23KFrame.SYNC_TEMPO
            convertv22Tov23[ID3v22KFrame.TDAT] = ID3v23KFrame.TDAT
            convertv22Tov23[ID3v22KFrame.TIME] = ID3v23KFrame.TIME
            convertv22Tov23[ID3v22KFrame.TITLE_REFINEMENT] = ID3v23KFrame.TITLE_REFINEMENT
            convertv22Tov23[ID3v22KFrame.TORY] = ID3v23KFrame.TORY
            convertv22Tov23[ID3v22KFrame.TRACK] = ID3v23KFrame.TRACK
            convertv22Tov23[ID3v22KFrame.TRDA] = ID3v23KFrame.TRDA
            convertv22Tov23[ID3v22KFrame.TSIZ] = ID3v23KFrame.TSIZ
            convertv22Tov23[ID3v22KFrame.TYER] = ID3v23KFrame.TYER
            convertv22Tov23[ID3v22KFrame.UNIQUE_FILE_ID] = ID3v23KFrame.UNIQUE_FILE_ID
            convertv22Tov23[ID3v22KFrame.UNIQUE_FILE_ID] = ID3v23KFrame.UNIQUE_FILE_ID
            convertv22Tov23[ID3v22KFrame.UNSYNC_LYRICS] = ID3v23KFrame.UNSYNC_LYRICS
            convertv22Tov23[ID3v22KFrame.URL_ARTIST_WEB] = ID3v23KFrame.URL_ARTIST_WEB
            convertv22Tov23[ID3v22KFrame.URL_COMMERCIAL] = ID3v23KFrame.URL_COMMERCIAL
            convertv22Tov23[ID3v22KFrame.URL_COPYRIGHT] = ID3v23KFrame.URL_COPYRIGHT
            convertv22Tov23[ID3v22KFrame.URL_FILE_WEB] = ID3v23KFrame.URL_FILE_WEB
            convertv22Tov23[ID3v22KFrame.URL_OFFICIAL_RADIO] = ID3v23KFrame.URL_OFFICIAL_RADIO
            convertv22Tov23[ID3v22KFrame.URL_PAYMENT] = ID3v23KFrame.URL_PAYMENT
            convertv22Tov23[ID3v22KFrame.URL_PUBLISHERS] = ID3v23KFrame.URL_PUBLISHERS
            convertv22Tov23[ID3v22KFrame.URL_SOURCE_WEB] = ID3v23KFrame.URL_SOURCE_WEB
            convertv22Tov23[ID3v22KFrame.USER_DEFINED_INFO] = ID3v23KFrame.USER_DEFINED_INFO
            convertv22Tov23[ID3v22KFrame.USER_DEFINED_URL] = ID3v23KFrame.USER_DEFINED_URL
            convertv22Tov23[ID3v22KFrame.TITLE] = ID3v23KFrame.TITLE
            convertv22Tov23[ID3v22KFrame.IS_COMPILATION] = ID3v23KFrame.IS_COMPILATION
            convertv22Tov23[ID3v22KFrame.TITLE_SORT_ORDER_ITUNES] = ID3v23KFrame.TITLE_SORT_ORDER_ITUNES
            convertv22Tov23[ID3v22KFrame.ARTIST_SORT_ORDER_ITUNES] = ID3v23KFrame.ARTIST_SORT_ORDER_ITUNES
            convertv22Tov23[ID3v22KFrame.ALBUM_SORT_ORDER_ITUNES] = ID3v23KFrame.ALBUM_SORT_ORDER_ITUNES
            convertv22Tov23[ID3v22KFrame.ALBUM_ARTIST_SORT_ORDER_ITUNES] = ID3v23KFrame.ALBUM_ARTIST_SORT_ORDER_ITUNES
            convertv22Tov23[ID3v22KFrame.COMPOSER_SORT_ORDER_ITUNES] = ID3v23KFrame.COMPOSER_SORT_ORDER_ITUNES

            // v23 to v22 The translation is both way
            convertv22Tov23.keys.forEach { key -> convertv22Tov23[key]?.also { v -> convertv23Tov22[v] = key }  }

            //This one way translation allows us to convert XSOT to TST, but in the other direction gets converted to TSOT
            convertv23Tov22[ID3v23KFrame.TITLE_SORT_ORDER_MUSICBRAINZ] = ID3v22KFrame.TITLE_SORT_ORDER_ITUNES
            convertv23Tov22[ID3v23KFrame.ARTIST_SORT_ORDER_MUSICBRAINZ] = ID3v22KFrame.ARTIST_SORT_ORDER_ITUNES
            convertv23Tov22[ID3v23KFrame.ALBUM_SORT_ORDER_MUSICBRAINZ] = ID3v22KFrame.ALBUM_SORT_ORDER_ITUNES
            //TODO What does CRM Map to
            // Force v22 to v23,  Extra fields in v23 version
            forcev22Tov23[ID3v22KFrame.ATTACHED_PICTURE] = ID3v23KFrame.ATTACHED_PICTURE
            // Force v23 to v22
            forcev23Tov22[ID3v23KFrame.ATTACHED_PICTURE] = ID3v22KFrame.ATTACHED_PICTURE

            // Define the mapping from v23 to v24 only maps values where
            // the v23 ID is not a v24 ID and where the translation from v23 to v24
            // ID does not affect the framebody.
            //This one way allows us to convert XSOT to TSOT,XSOP to TSOP and XSOA - TSOA but in the other direction gets converted to TSOT,TSOP,TSOA
            convertv23Tov24[ID3v23KFrame.TITLE_SORT_ORDER_MUSICBRAINZ] = ID3v24KFrame.TITLE_SORT_ORDER
            convertv23Tov24[ID3v23KFrame.ARTIST_SORT_ORDER_MUSICBRAINZ] = ID3v24KFrame.ARTIST_SORT_ORDER
            convertv23Tov24[ID3v23KFrame.ALBUM_SORT_ORDER_MUSICBRAINZ] = ID3v24KFrame.ALBUM_SORT_ORDER
            // No others exist because most v23 mappings are identical to v24 therefore no mapping required and the ones that
            // are different need to be forced.

            // Force v23 to v24 These are deprecated and need to do a forced mapping
            forcev23Tov24[ID3v23KFrame.RELATIVE_VOLUME_ADJUSTMENT] = ID3v24KFrame.RELATIVE_VOLUME_ADJUSTMENT2
            forcev23Tov24[ID3v23KFrame.EQUALISATION] = ID3v24KFrame.EQUALISATION2
            forcev23Tov24[ID3v23KFrame.INVOLVED_PEOPLE] = ID3v24KFrame.INVOLVED_PEOPLE
            forcev23Tov24[ID3v23KFrame.TDAT] = ID3v24KFrame.YEAR
            forcev23Tov24[ID3v23KFrame.TIME] = ID3v24KFrame.YEAR
            forcev23Tov24[ID3v23KFrame.TORY] = ID3v24KFrame.ORIGINAL_RELEASE_TIME
            forcev23Tov24[ID3v23KFrame.TRDA] = ID3v24KFrame.YEAR
            forcev23Tov24[ID3v23KFrame.TYER] = ID3v24KFrame.YEAR

            //Note Force v24 to v23, TDRC is a 1M relationship handled specially.
            // @TODO EQUALISATION
            forcev24Tov23[ID3v24KFrame.RELATIVE_VOLUME_ADJUSTMENT2] = ID3v23KFrame.RELATIVE_VOLUME_ADJUSTMENT
            //Used to be a special frame now a text frame
            forcev24Tov23[ID3v24KFrame.INVOLVED_PEOPLE] = ID3v23KFrame.INVOLVED_PEOPLE
            //No Mood frame in v23 so use a TXXX frame
            forcev24Tov23[ID3v24KFrame.MOOD] = ID3v23KFrame.USER_DEFINED_INFO
            //Release time can be mapped to release year (but can only hold year)
            forcev24Tov23[ID3v24KFrame.ORIGINAL_RELEASE_TIME] = ID3v23KFrame.TORY
        }
    }
}