package de.visualdigits.kaudiotagger.model.id3.frame

import de.visualdigits.kaudiotagger.model.id3.types.ID3V22Frame
import de.visualdigits.kaudiotagger.model.id3.types.ID3V23Frame
import de.visualdigits.kaudiotagger.model.id3.types.ID3V24Frame

abstract class ID3Frames {

    companion object {
        
        /**
         * Mapping from v22 to v23
         */
        val convertv22Tov23: MutableMap<ID3V22Frame, ID3V23Frame> = mutableMapOf()
        val convertv23Tov22: MutableMap<ID3V23Frame, ID3V22Frame> = mutableMapOf()

        val forcev22Tov23: MutableMap<ID3V22Frame, ID3V23Frame> = mutableMapOf()
        val forcev23Tov22: MutableMap<ID3V23Frame, ID3V22Frame> = mutableMapOf()

        val convertv23Tov24: MutableMap<ID3V23Frame, ID3V24Frame> = mutableMapOf()
        val convertv24Tov23: MutableMap<ID3V24Frame, ID3V23Frame> = mutableMapOf()

        val forcev23Tov24: MutableMap<ID3V23Frame, ID3V24Frame> = mutableMapOf()
        val forcev24Tov23: MutableMap<ID3V24Frame, ID3V23Frame> = mutableMapOf()
        
        init {
            // All v22 ids were renamed in v23, but are essentially the same
            convertv22Tov23[ID3V22Frame.ACCOMPANIMENT] = ID3V23Frame.ACCOMPANIMENT
            convertv22Tov23[ID3V22Frame.ALBUM] = ID3V23Frame.ALBUM
            convertv22Tov23[ID3V22Frame.ARTIST] = ID3V23Frame.ARTIST
            convertv22Tov23[ID3V22Frame.AUDIO_ENCRYPTION] = ID3V23Frame.AUDIO_ENCRYPTION
            convertv22Tov23[ID3V22Frame.BPM] = ID3V23Frame.BPM
            convertv22Tov23[ID3V22Frame.COMMENT] = ID3V23Frame.COMMENT
            convertv22Tov23[ID3V22Frame.COMMENT] = ID3V23Frame.COMMENT
            convertv22Tov23[ID3V22Frame.COMPOSER] = ID3V23Frame.COMPOSER
            convertv22Tov23[ID3V22Frame.CONDUCTOR] = ID3V23Frame.CONDUCTOR
            convertv22Tov23[ID3V22Frame.CONTENT_GROUP_DESC] = ID3V23Frame.CONTENT_GROUP_DESC
            convertv22Tov23[ID3V22Frame.COPYRIGHTINFO] = ID3V23Frame.COPYRIGHTINFO
            convertv22Tov23[ID3V22Frame.ENCODEDBY] = ID3V23Frame.ENCODEDBY
            convertv22Tov23[ID3V22Frame.EQUALISATION] = ID3V23Frame.EQUALISATION
            convertv22Tov23[ID3V22Frame.EVENT_TIMING_CODES] = ID3V23Frame.EVENT_TIMING_CODES
            convertv22Tov23[ID3V22Frame.FILE_TYPE] = ID3V23Frame.FILE_TYPE
            convertv22Tov23[ID3V22Frame.GENERAL_ENCAPS_OBJECT] = ID3V23Frame.GENERAL_ENCAPS_OBJECT
            convertv22Tov23[ID3V22Frame.GENRE] = ID3V23Frame.GENRE
            convertv22Tov23[ID3V22Frame.HW_SW_SETTINGS] = ID3V23Frame.HW_SW_SETTINGS
            convertv22Tov23[ID3V22Frame.INITIAL_KEY] = ID3V23Frame.INITIAL_KEY
            convertv22Tov23[ID3V22Frame.IPLS] = ID3V23Frame.INVOLVED_PEOPLE
            convertv22Tov23[ID3V22Frame.ISRC] = ID3V23Frame.ISRC
            convertv22Tov23[ID3V22Frame.ITUNES_GROUPING] = ID3V23Frame.ITUNES_GROUPING
            convertv22Tov23[ID3V22Frame.LANGUAGE] = ID3V23Frame.LANGUAGE
            convertv22Tov23[ID3V22Frame.LENGTH] = ID3V23Frame.LENGTH
            convertv22Tov23[ID3V22Frame.LINKED_INFO] = ID3V23Frame.LINKED_INFO
            convertv22Tov23[ID3V22Frame.LYRICIST] = ID3V23Frame.LYRICIST
            convertv22Tov23[ID3V22Frame.MEDIA_TYPE] = ID3V23Frame.MEDIA_TYPE
            convertv22Tov23[ID3V22Frame.MOVEMENT] = ID3V23Frame.MOVEMENT
            convertv22Tov23[ID3V22Frame.MOVEMENT_NO] = ID3V23Frame.MOVEMENT_NO
            convertv22Tov23[ID3V22Frame.MPEG_LOCATION_LOOKUP_TABLE] = ID3V23Frame.MPEG_LOCATION_LOOKUP_TABLE
            convertv22Tov23[ID3V22Frame.MUSIC_CD_ID] = ID3V23Frame.MUSIC_CD_ID
            convertv22Tov23[ID3V22Frame.ORIGARTIST] = ID3V23Frame.ORIGARTIST
            convertv22Tov23[ID3V22Frame.ORIG_FILENAME] = ID3V23Frame.ORIG_FILENAME
            convertv22Tov23[ID3V22Frame.ORIG_LYRICIST] = ID3V23Frame.ORIG_LYRICIST
            convertv22Tov23[ID3V22Frame.ORIG_TITLE] = ID3V23Frame.ORIG_TITLE
            convertv22Tov23[ID3V22Frame.PLAYLIST_DELAY] = ID3V23Frame.PLAYLIST_DELAY
            convertv22Tov23[ID3V22Frame.PLAY_COUNTER] = ID3V23Frame.PLAY_COUNTER
            convertv22Tov23[ID3V22Frame.PLAY_COUNTER] = ID3V23Frame.PLAY_COUNTER
            convertv22Tov23[ID3V22Frame.POPULARIMETER] = ID3V23Frame.POPULARIMETER
            convertv22Tov23[ID3V22Frame.PUBLISHER] = ID3V23Frame.PUBLISHER
            convertv22Tov23[ID3V22Frame.RECOMMENDED_BUFFER_SIZE] = ID3V23Frame.RECOMMENDED_BUFFER_SIZE
            convertv22Tov23[ID3V22Frame.RECOMMENDED_BUFFER_SIZE] = ID3V23Frame.RECOMMENDED_BUFFER_SIZE
            convertv22Tov23[ID3V22Frame.RELATIVE_VOLUME_ADJUSTMENT] = ID3V23Frame.RELATIVE_VOLUME_ADJUSTMENT
            convertv22Tov23[ID3V22Frame.REMIXED] = ID3V23Frame.REMIXED
            convertv22Tov23[ID3V22Frame.REVERB] = ID3V23Frame.REVERB
            convertv22Tov23[ID3V22Frame.SET] = ID3V23Frame.SET
            convertv22Tov23[ID3V22Frame.SET_SUBTITLE] = ID3V23Frame.SET_SUBTITLE
            convertv22Tov23[ID3V22Frame.SYNC_LYRIC] = ID3V23Frame.SYNC_LYRIC
            convertv22Tov23[ID3V22Frame.SYNC_TEMPO] = ID3V23Frame.SYNC_TEMPO
            convertv22Tov23[ID3V22Frame.TDAT] = ID3V23Frame.TDAT
            convertv22Tov23[ID3V22Frame.TIME] = ID3V23Frame.TIME
            convertv22Tov23[ID3V22Frame.TITLE_REFINEMENT] = ID3V23Frame.TITLE_REFINEMENT
            convertv22Tov23[ID3V22Frame.TORY] = ID3V23Frame.TORY
            convertv22Tov23[ID3V22Frame.TRACK] = ID3V23Frame.TRACK
            convertv22Tov23[ID3V22Frame.TRDA] = ID3V23Frame.TRDA
            convertv22Tov23[ID3V22Frame.TSIZ] = ID3V23Frame.TSIZ
            convertv22Tov23[ID3V22Frame.TYER] = ID3V23Frame.TYER
            convertv22Tov23[ID3V22Frame.UNIQUE_FILE_ID] = ID3V23Frame.UNIQUE_FILE_ID
            convertv22Tov23[ID3V22Frame.UNIQUE_FILE_ID] = ID3V23Frame.UNIQUE_FILE_ID
            convertv22Tov23[ID3V22Frame.UNSYNC_LYRICS] = ID3V23Frame.UNSYNC_LYRICS
            convertv22Tov23[ID3V22Frame.URL_ARTIST_WEB] = ID3V23Frame.URL_ARTIST_WEB
            convertv22Tov23[ID3V22Frame.URL_COMMERCIAL] = ID3V23Frame.URL_COMMERCIAL
            convertv22Tov23[ID3V22Frame.URL_COPYRIGHT] = ID3V23Frame.URL_COPYRIGHT
            convertv22Tov23[ID3V22Frame.URL_FILE_WEB] = ID3V23Frame.URL_FILE_WEB
            convertv22Tov23[ID3V22Frame.URL_OFFICIAL_RADIO] = ID3V23Frame.URL_OFFICIAL_RADIO
            convertv22Tov23[ID3V22Frame.URL_PAYMENT] = ID3V23Frame.URL_PAYMENT
            convertv22Tov23[ID3V22Frame.URL_PUBLISHERS] = ID3V23Frame.URL_PUBLISHERS
            convertv22Tov23[ID3V22Frame.URL_SOURCE_WEB] = ID3V23Frame.URL_SOURCE_WEB
            convertv22Tov23[ID3V22Frame.USER_DEFINED_INFO] = ID3V23Frame.USER_DEFINED_INFO
            convertv22Tov23[ID3V22Frame.USER_DEFINED_URL] = ID3V23Frame.USER_DEFINED_URL
            convertv22Tov23[ID3V22Frame.TITLE] = ID3V23Frame.TITLE
            convertv22Tov23[ID3V22Frame.IS_COMPILATION] = ID3V23Frame.IS_COMPILATION
            convertv22Tov23[ID3V22Frame.TITLE_SORT_ORDER_ITUNES] = ID3V23Frame.TITLE_SORT_ORDER_ITUNES
            convertv22Tov23[ID3V22Frame.ARTIST_SORT_ORDER_ITUNES] = ID3V23Frame.ARTIST_SORT_ORDER_ITUNES
            convertv22Tov23[ID3V22Frame.ALBUM_SORT_ORDER_ITUNES] = ID3V23Frame.ALBUM_SORT_ORDER_ITUNES
            convertv22Tov23[ID3V22Frame.ALBUM_ARTIST_SORT_ORDER_ITUNES] = ID3V23Frame.ALBUM_ARTIST_SORT_ORDER_ITUNES
            convertv22Tov23[ID3V22Frame.COMPOSER_SORT_ORDER_ITUNES] = ID3V23Frame.COMPOSER_SORT_ORDER_ITUNES

            // v23 to v22 The translation is both way
            convertv22Tov23.keys.forEach { key -> convertv22Tov23[key]?.also { v -> convertv23Tov22[v] = key }  }

            //This one way translation allows us to convert XSOT to TST, but in the other direction gets converted to TSOT
            convertv23Tov22[ID3V23Frame.TITLE_SORT_ORDER_MUSICBRAINZ] = ID3V22Frame.TITLE_SORT_ORDER_ITUNES
            convertv23Tov22[ID3V23Frame.ARTIST_SORT_ORDER_MUSICBRAINZ] = ID3V22Frame.ARTIST_SORT_ORDER_ITUNES
            convertv23Tov22[ID3V23Frame.ALBUM_SORT_ORDER_MUSICBRAINZ] = ID3V22Frame.ALBUM_SORT_ORDER_ITUNES
            //TODO What does CRM Map to
            // Force v22 to v23,  Extra fields in v23 version
            forcev22Tov23[ID3V22Frame.ATTACHED_PICTURE] = ID3V23Frame.ATTACHED_PICTURE
            // Force v23 to v22
            forcev23Tov22[ID3V23Frame.ATTACHED_PICTURE] = ID3V22Frame.ATTACHED_PICTURE

            // Define the mapping from v23 to v24 only maps values where
            // the v23 ID is not a v24 ID and where the translation from v23 to v24
            // ID does not affect the framebody.
            //This one way allows us to convert XSOT to TSOT,XSOP to TSOP and XSOA - TSOA but in the other direction gets converted to TSOT,TSOP,TSOA
            convertv23Tov24[ID3V23Frame.TITLE_SORT_ORDER_MUSICBRAINZ] = ID3V24Frame.TITLE_SORT_ORDER
            convertv23Tov24[ID3V23Frame.ARTIST_SORT_ORDER_MUSICBRAINZ] = ID3V24Frame.ARTIST_SORT_ORDER
            convertv23Tov24[ID3V23Frame.ALBUM_SORT_ORDER_MUSICBRAINZ] = ID3V24Frame.ALBUM_SORT_ORDER
            // No others exist because most v23 mappings are identical to v24 therefore no mapping required and the ones that
            // are different need to be forced.

            // Force v23 to v24 These are deprecated and need to do a forced mapping
            forcev23Tov24[ID3V23Frame.RELATIVE_VOLUME_ADJUSTMENT] = ID3V24Frame.RELATIVE_VOLUME_ADJUSTMENT2
            forcev23Tov24[ID3V23Frame.EQUALISATION] = ID3V24Frame.EQUALISATION2
            forcev23Tov24[ID3V23Frame.INVOLVED_PEOPLE] = ID3V24Frame.INVOLVED_PEOPLE
            forcev23Tov24[ID3V23Frame.TDAT] = ID3V24Frame.YEAR
            forcev23Tov24[ID3V23Frame.TIME] = ID3V24Frame.YEAR
            forcev23Tov24[ID3V23Frame.TORY] = ID3V24Frame.ORIGINAL_RELEASE_TIME
            forcev23Tov24[ID3V23Frame.TRDA] = ID3V24Frame.YEAR
            forcev23Tov24[ID3V23Frame.TYER] = ID3V24Frame.YEAR

            //Note Force v24 to v23, TDRC is a 1M relationship handled specially.
            // @TODO EQUALISATION
            forcev24Tov23[ID3V24Frame.RELATIVE_VOLUME_ADJUSTMENT2] = ID3V23Frame.RELATIVE_VOLUME_ADJUSTMENT
            //Used to be a special frame now a text frame
            forcev24Tov23[ID3V24Frame.INVOLVED_PEOPLE] = ID3V23Frame.INVOLVED_PEOPLE
            //No Mood frame in v23 so use a TXXX frame
            forcev24Tov23[ID3V24Frame.MOOD] = ID3V23Frame.USER_DEFINED_INFO
            //Release time can be mapped to release year (but can only hold year)
            forcev24Tov23[ID3V24Frame.ORIGINAL_RELEASE_TIME] = ID3V23Frame.TORY
        }
    }
}