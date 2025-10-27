package de.visualdigits.kaudiotagger.model.id3.frame

import de.visualdigits.kaudiotagger.model.common.types.ID3v22Frames
import de.visualdigits.kaudiotagger.model.common.types.ID3v23Frames
import de.visualdigits.kaudiotagger.model.common.types.ID3v24Frames

abstract class ID3Frames {

    companion object {
        
        /**
         * Mapping from v22 to v23
         */
        val convertv22Tov23: MutableMap<ID3v22Frames, ID3v23Frames> = mutableMapOf()
        val convertv23Tov22: MutableMap<ID3v23Frames, ID3v22Frames> = mutableMapOf()

        val forcev22Tov23: MutableMap<ID3v22Frames, ID3v23Frames> = mutableMapOf()
        val forcev23Tov22: MutableMap<ID3v23Frames, ID3v22Frames> = mutableMapOf()

        val convertv23Tov24: MutableMap<ID3v23Frames, ID3v24Frames> = mutableMapOf()
        val convertv24Tov23: MutableMap<ID3v24Frames, ID3v23Frames> = mutableMapOf()

        val forcev23Tov24: MutableMap<ID3v23Frames, ID3v24Frames> = mutableMapOf()
        val forcev24Tov23: MutableMap<ID3v24Frames, ID3v23Frames> = mutableMapOf()
        
        init {
            // All v22 ids were renamed in v23, but are essentially the same
            convertv22Tov23[ID3v22Frames.ACCOMPANIMENT] = ID3v23Frames.ACCOMPANIMENT
            convertv22Tov23[ID3v22Frames.ALBUM] = ID3v23Frames.ALBUM
            convertv22Tov23[ID3v22Frames.ARTIST] = ID3v23Frames.ARTIST
            convertv22Tov23[ID3v22Frames.AUDIO_ENCRYPTION] = ID3v23Frames.AUDIO_ENCRYPTION
            convertv22Tov23[ID3v22Frames.BPM] = ID3v23Frames.BPM
            convertv22Tov23[ID3v22Frames.COMMENT] = ID3v23Frames.COMMENT
            convertv22Tov23[ID3v22Frames.COMMENT] = ID3v23Frames.COMMENT
            convertv22Tov23[ID3v22Frames.COMPOSER] = ID3v23Frames.COMPOSER
            convertv22Tov23[ID3v22Frames.CONDUCTOR] = ID3v23Frames.CONDUCTOR
            convertv22Tov23[ID3v22Frames.CONTENT_GROUP_DESC] = ID3v23Frames.CONTENT_GROUP_DESC
            convertv22Tov23[ID3v22Frames.COPYRIGHTINFO] = ID3v23Frames.COPYRIGHTINFO
            convertv22Tov23[ID3v22Frames.ENCODEDBY] = ID3v23Frames.ENCODEDBY
            convertv22Tov23[ID3v22Frames.EQUALISATION] = ID3v23Frames.EQUALISATION
            convertv22Tov23[ID3v22Frames.EVENT_TIMING_CODES] = ID3v23Frames.EVENT_TIMING_CODES
            convertv22Tov23[ID3v22Frames.FILE_TYPE] = ID3v23Frames.FILE_TYPE
            convertv22Tov23[ID3v22Frames.GENERAL_ENCAPS_OBJECT] = ID3v23Frames.GENERAL_ENCAPS_OBJECT
            convertv22Tov23[ID3v22Frames.GENRE] = ID3v23Frames.GENRE
            convertv22Tov23[ID3v22Frames.HW_SW_SETTINGS] = ID3v23Frames.HW_SW_SETTINGS
            convertv22Tov23[ID3v22Frames.INITIAL_KEY] = ID3v23Frames.INITIAL_KEY
            convertv22Tov23[ID3v22Frames.IPLS] = ID3v23Frames.INVOLVED_PEOPLE
            convertv22Tov23[ID3v22Frames.ISRC] = ID3v23Frames.ISRC
            convertv22Tov23[ID3v22Frames.ITUNES_GROUPING] = ID3v23Frames.ITUNES_GROUPING
            convertv22Tov23[ID3v22Frames.LANGUAGE] = ID3v23Frames.LANGUAGE
            convertv22Tov23[ID3v22Frames.LENGTH] = ID3v23Frames.LENGTH
            convertv22Tov23[ID3v22Frames.LINKED_INFO] = ID3v23Frames.LINKED_INFO
            convertv22Tov23[ID3v22Frames.LYRICIST] = ID3v23Frames.LYRICIST
            convertv22Tov23[ID3v22Frames.MEDIA_TYPE] = ID3v23Frames.MEDIA_TYPE
            convertv22Tov23[ID3v22Frames.MOVEMENT] = ID3v23Frames.MOVEMENT
            convertv22Tov23[ID3v22Frames.MOVEMENT_NO] = ID3v23Frames.MOVEMENT_NO
            convertv22Tov23[ID3v22Frames.MPEG_LOCATION_LOOKUP_TABLE] = ID3v23Frames.MPEG_LOCATION_LOOKUP_TABLE
            convertv22Tov23[ID3v22Frames.MUSIC_CD_ID] = ID3v23Frames.MUSIC_CD_ID
            convertv22Tov23[ID3v22Frames.ORIGARTIST] = ID3v23Frames.ORIGARTIST
            convertv22Tov23[ID3v22Frames.ORIG_FILENAME] = ID3v23Frames.ORIG_FILENAME
            convertv22Tov23[ID3v22Frames.ORIG_LYRICIST] = ID3v23Frames.ORIG_LYRICIST
            convertv22Tov23[ID3v22Frames.ORIG_TITLE] = ID3v23Frames.ORIG_TITLE
            convertv22Tov23[ID3v22Frames.PLAYLIST_DELAY] = ID3v23Frames.PLAYLIST_DELAY
            convertv22Tov23[ID3v22Frames.PLAY_COUNTER] = ID3v23Frames.PLAY_COUNTER
            convertv22Tov23[ID3v22Frames.PLAY_COUNTER] = ID3v23Frames.PLAY_COUNTER
            convertv22Tov23[ID3v22Frames.POPULARIMETER] = ID3v23Frames.POPULARIMETER
            convertv22Tov23[ID3v22Frames.PUBLISHER] = ID3v23Frames.PUBLISHER
            convertv22Tov23[ID3v22Frames.RECOMMENDED_BUFFER_SIZE] = ID3v23Frames.RECOMMENDED_BUFFER_SIZE
            convertv22Tov23[ID3v22Frames.RECOMMENDED_BUFFER_SIZE] = ID3v23Frames.RECOMMENDED_BUFFER_SIZE
            convertv22Tov23[ID3v22Frames.RELATIVE_VOLUME_ADJUSTMENT] = ID3v23Frames.RELATIVE_VOLUME_ADJUSTMENT
            convertv22Tov23[ID3v22Frames.REMIXED] = ID3v23Frames.REMIXED
            convertv22Tov23[ID3v22Frames.REVERB] = ID3v23Frames.REVERB
            convertv22Tov23[ID3v22Frames.SET] = ID3v23Frames.SET
            convertv22Tov23[ID3v22Frames.SET_SUBTITLE] = ID3v23Frames.SET_SUBTITLE
            convertv22Tov23[ID3v22Frames.SYNC_LYRIC] = ID3v23Frames.SYNC_LYRIC
            convertv22Tov23[ID3v22Frames.SYNC_TEMPO] = ID3v23Frames.SYNC_TEMPO
            convertv22Tov23[ID3v22Frames.TDAT] = ID3v23Frames.TDAT
            convertv22Tov23[ID3v22Frames.TIME] = ID3v23Frames.TIME
            convertv22Tov23[ID3v22Frames.TITLE_REFINEMENT] = ID3v23Frames.TITLE_REFINEMENT
            convertv22Tov23[ID3v22Frames.TORY] = ID3v23Frames.TORY
            convertv22Tov23[ID3v22Frames.TRACK] = ID3v23Frames.TRACK
            convertv22Tov23[ID3v22Frames.TRDA] = ID3v23Frames.TRDA
            convertv22Tov23[ID3v22Frames.TSIZ] = ID3v23Frames.TSIZ
            convertv22Tov23[ID3v22Frames.TYER] = ID3v23Frames.TYER
            convertv22Tov23[ID3v22Frames.UNIQUE_FILE_ID] = ID3v23Frames.UNIQUE_FILE_ID
            convertv22Tov23[ID3v22Frames.UNIQUE_FILE_ID] = ID3v23Frames.UNIQUE_FILE_ID
            convertv22Tov23[ID3v22Frames.UNSYNC_LYRICS] = ID3v23Frames.UNSYNC_LYRICS
            convertv22Tov23[ID3v22Frames.URL_ARTIST_WEB] = ID3v23Frames.URL_ARTIST_WEB
            convertv22Tov23[ID3v22Frames.URL_COMMERCIAL] = ID3v23Frames.URL_COMMERCIAL
            convertv22Tov23[ID3v22Frames.URL_COPYRIGHT] = ID3v23Frames.URL_COPYRIGHT
            convertv22Tov23[ID3v22Frames.URL_FILE_WEB] = ID3v23Frames.URL_FILE_WEB
            convertv22Tov23[ID3v22Frames.URL_OFFICIAL_RADIO] = ID3v23Frames.URL_OFFICIAL_RADIO
            convertv22Tov23[ID3v22Frames.URL_PAYMENT] = ID3v23Frames.URL_PAYMENT
            convertv22Tov23[ID3v22Frames.URL_PUBLISHERS] = ID3v23Frames.URL_PUBLISHERS
            convertv22Tov23[ID3v22Frames.URL_SOURCE_WEB] = ID3v23Frames.URL_SOURCE_WEB
            convertv22Tov23[ID3v22Frames.USER_DEFINED_INFO] = ID3v23Frames.USER_DEFINED_INFO
            convertv22Tov23[ID3v22Frames.USER_DEFINED_URL] = ID3v23Frames.USER_DEFINED_URL
            convertv22Tov23[ID3v22Frames.TITLE] = ID3v23Frames.TITLE
            convertv22Tov23[ID3v22Frames.IS_COMPILATION] = ID3v23Frames.IS_COMPILATION
            convertv22Tov23[ID3v22Frames.TITLE_SORT_ORDER_ITUNES] = ID3v23Frames.TITLE_SORT_ORDER_ITUNES
            convertv22Tov23[ID3v22Frames.ARTIST_SORT_ORDER_ITUNES] = ID3v23Frames.ARTIST_SORT_ORDER_ITUNES
            convertv22Tov23[ID3v22Frames.ALBUM_SORT_ORDER_ITUNES] = ID3v23Frames.ALBUM_SORT_ORDER_ITUNES
            convertv22Tov23[ID3v22Frames.ALBUM_ARTIST_SORT_ORDER_ITUNES] = ID3v23Frames.ALBUM_ARTIST_SORT_ORDER_ITUNES
            convertv22Tov23[ID3v22Frames.COMPOSER_SORT_ORDER_ITUNES] = ID3v23Frames.COMPOSER_SORT_ORDER_ITUNES

            // v23 to v22 The translation is both way
            convertv22Tov23.keys.forEach { key -> convertv22Tov23[key]?.also { v -> convertv23Tov22[v] = key }  }

            //This one way translation allows us to convert XSOT to TST, but in the other direction gets converted to TSOT
            convertv23Tov22[ID3v23Frames.TITLE_SORT_ORDER_MUSICBRAINZ] = ID3v22Frames.TITLE_SORT_ORDER_ITUNES
            convertv23Tov22[ID3v23Frames.ARTIST_SORT_ORDER_MUSICBRAINZ] = ID3v22Frames.ARTIST_SORT_ORDER_ITUNES
            convertv23Tov22[ID3v23Frames.ALBUM_SORT_ORDER_MUSICBRAINZ] = ID3v22Frames.ALBUM_SORT_ORDER_ITUNES
            //TODO What does CRM Map to
            // Force v22 to v23,  Extra fields in v23 version
            forcev22Tov23[ID3v22Frames.ATTACHED_PICTURE] = ID3v23Frames.ATTACHED_PICTURE
            // Force v23 to v22
            forcev23Tov22[ID3v23Frames.ATTACHED_PICTURE] = ID3v22Frames.ATTACHED_PICTURE

            // Define the mapping from v23 to v24 only maps values where
            // the v23 ID is not a v24 ID and where the translation from v23 to v24
            // ID does not affect the framebody.
            //This one way allows us to convert XSOT to TSOT,XSOP to TSOP and XSOA - TSOA but in the other direction gets converted to TSOT,TSOP,TSOA
            convertv23Tov24[ID3v23Frames.TITLE_SORT_ORDER_MUSICBRAINZ] = ID3v24Frames.TITLE_SORT_ORDER
            convertv23Tov24[ID3v23Frames.ARTIST_SORT_ORDER_MUSICBRAINZ] = ID3v24Frames.ARTIST_SORT_ORDER
            convertv23Tov24[ID3v23Frames.ALBUM_SORT_ORDER_MUSICBRAINZ] = ID3v24Frames.ALBUM_SORT_ORDER
            // No others exist because most v23 mappings are identical to v24 therefore no mapping required and the ones that
            // are different need to be forced.

            // Force v23 to v24 These are deprecated and need to do a forced mapping
            forcev23Tov24[ID3v23Frames.RELATIVE_VOLUME_ADJUSTMENT] = ID3v24Frames.RELATIVE_VOLUME_ADJUSTMENT2
            forcev23Tov24[ID3v23Frames.EQUALISATION] = ID3v24Frames.EQUALISATION2
            forcev23Tov24[ID3v23Frames.INVOLVED_PEOPLE] = ID3v24Frames.INVOLVED_PEOPLE
            forcev23Tov24[ID3v23Frames.TDAT] = ID3v24Frames.YEAR
            forcev23Tov24[ID3v23Frames.TIME] = ID3v24Frames.YEAR
            forcev23Tov24[ID3v23Frames.TORY] = ID3v24Frames.ORIGINAL_RELEASE_TIME
            forcev23Tov24[ID3v23Frames.TRDA] = ID3v24Frames.YEAR
            forcev23Tov24[ID3v23Frames.TYER] = ID3v24Frames.YEAR

            //Note Force v24 to v23, TDRC is a 1M relationship handled specially.
            // @TODO EQUALISATION
            forcev24Tov23[ID3v24Frames.RELATIVE_VOLUME_ADJUSTMENT2] = ID3v23Frames.RELATIVE_VOLUME_ADJUSTMENT
            //Used to be a special frame now a text frame
            forcev24Tov23[ID3v24Frames.INVOLVED_PEOPLE] = ID3v23Frames.INVOLVED_PEOPLE
            //No Mood frame in v23 so use a TXXX frame
            forcev24Tov23[ID3v24Frames.MOOD] = ID3v23Frames.USER_DEFINED_INFO
            //Release time can be mapped to release year (but can only hold year)
            forcev24Tov23[ID3v24Frames.ORIGINAL_RELEASE_TIME] = ID3v23Frames.TORY
        }
    }
}