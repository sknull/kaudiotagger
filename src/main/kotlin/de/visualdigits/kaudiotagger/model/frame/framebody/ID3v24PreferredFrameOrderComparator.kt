package de.visualdigits.kaudiotagger.model.frame.framebody

import de.visualdigits.kaudiotagger.model.kframe.ID3v24KFrame

class ID3v24PreferredFrameOrderComparator private constructor() : Comparator<String> {

    companion object {

        val frameIdsInPreferredOrder: MutableList<String> = mutableListOf(
            //these are the key ones we want at the top
            ID3v24KFrame.UNIQUE_FILE_ID.id,
            ID3v24KFrame.TITLE.id,
            ID3v24KFrame.ARTIST.id,
            ID3v24KFrame.ALBUM.id,
            ID3v24KFrame.ALBUM_SORT_ORDER.id,
            ID3v24KFrame.GENRE.id,
            ID3v24KFrame.COMPOSER.id,
            ID3v24KFrame.CONDUCTOR.id,
            ID3v24KFrame.CONTENT_GROUP_DESC.id,
            ID3v24KFrame.TRACK.id,
            ID3v24KFrame.YEAR.id,
            ID3v24KFrame.ACCOMPANIMENT.id,
            ID3v24KFrame.BPM.id,
            ID3v24KFrame.ISRC.id,
            ID3v24KFrame.TITLE_SORT_ORDER.id,
            ID3v24KFrame.TITLE_REFINEMENT.id,
            ID3v24KFrame.UNSYNC_LYRICS.id,
            ID3v24KFrame.USER_DEFINED_INFO.id,
            ID3v24KFrame.USER_DEFINED_URL.id,
            ID3v24KFrame.URL_ARTIST_WEB.id,
            ID3v24KFrame.URL_COMMERCIAL.id,
            ID3v24KFrame.URL_COPYRIGHT.id,
            ID3v24KFrame.URL_FILE_WEB.id,
            ID3v24KFrame.URL_OFFICIAL_RADIO.id,
            ID3v24KFrame.URL_PAYMENT.id,
            ID3v24KFrame.URL_PUBLISHERS.id,
            ID3v24KFrame.URL_COMMERCIAL.id,
            ID3v24KFrame.LYRICIST.id,
            ID3v24KFrame.MEDIA_TYPE.id,
            ID3v24KFrame.INVOLVED_PEOPLE.id,
            ID3v24KFrame.LANGUAGE.id,
            ID3v24KFrame.ARTIST_SORT_ORDER.id,
            ID3v24KFrame.PLAYLIST_DELAY.id,
            ID3v24KFrame.PLAY_COUNTER.id,
            ID3v24KFrame.POPULARIMETER.id,
            ID3v24KFrame.PUBLISHER.id,
            ID3v24KFrame.ALBUM_ARTIST_SORT_ORDER_ITUNES.id,
            ID3v24KFrame.COMPOSER_SORT_ORDER_ITUNES.id,
            ID3v24KFrame.IS_COMPILATION.id,
            ID3v24KFrame.COMMENT.id,

            //Not so bothered about these
            ID3v24KFrame.AUDIO_SEEK_POINT_INDEX.id,
            ID3v24KFrame.COMMERCIAL_FRAME.id,
            ID3v24KFrame.COPYRIGHTINFO.id,
            ID3v24KFrame.ENCODEDBY.id,
            ID3v24KFrame.ENCODING_TIME.id,
            ID3v24KFrame.ENCRYPTION.id,
            ID3v24KFrame.EQUALISATION2.id,
            ID3v24KFrame.EVENT_TIMING_CODES.id,
            ID3v24KFrame.FILE_OWNER.id,
            ID3v24KFrame.FILE_TYPE.id,
            ID3v24KFrame.GROUP_ID_REG.id,
            ID3v24KFrame.HW_SW_SETTINGS.id,
            ID3v24KFrame.INITIAL_KEY.id,
            ID3v24KFrame.LENGTH.id,
            ID3v24KFrame.LINKED_INFO.id,
            ID3v24KFrame.MOOD.id,
            ID3v24KFrame.MPEG_LOCATION_LOOKUP_TABLE.id,
            ID3v24KFrame.MUSICIAN_CREDITS.id,
            ID3v24KFrame.ORIGARTIST.id,
            ID3v24KFrame.ORIGINAL_RELEASE_TIME.id,
            ID3v24KFrame.ORIG_FILENAME.id,
            ID3v24KFrame.ORIG_LYRICIST.id,
            ID3v24KFrame.ORIG_TITLE.id,
            ID3v24KFrame.OWNERSHIP.id,
            ID3v24KFrame.POSITION_SYNC.id,
            ID3v24KFrame.PRODUCED_NOTICE.id,
            ID3v24KFrame.RADIO_NAME.id,
            ID3v24KFrame.RADIO_OWNER.id,
            ID3v24KFrame.RECOMMENDED_BUFFER_SIZE.id,
            ID3v24KFrame.RELATIVE_VOLUME_ADJUSTMENT2.id,
            ID3v24KFrame.RELEASE_TIME.id,
            ID3v24KFrame.REMIXED.id,
            ID3v24KFrame.REVERB.id,
            ID3v24KFrame.SEEK.id,
            ID3v24KFrame.SET.id,
            ID3v24KFrame.SET_SUBTITLE.id,
            ID3v24KFrame.SIGNATURE.id,
            ID3v24KFrame.SYNC_LYRIC.id,
            ID3v24KFrame.SYNC_TEMPO.id,
            ID3v24KFrame.TAGGING_TIME.id,
            ID3v24KFrame.TERMS_OF_USE.id,

            //Want this near the end because can cause problems with unsyncing
            ID3v24KFrame.ATTACHED_PICTURE.id,

            //Itunes doesnt seem to like these, and of little use so put right at end
            ID3v24KFrame.PRIVATE.id,
            ID3v24KFrame.MUSIC_CD_ID.id,
            ID3v24KFrame.AUDIO_ENCRYPTION.id,
            ID3v24KFrame.GENERAL_ENCAPS_OBJECT.id,
        )

        val instance: ID3v24PreferredFrameOrderComparator = ID3v24PreferredFrameOrderComparator()
    }

    override fun compare(frameId1: String?, frameId2: String?): Int {
        var frameId1Index = frameIdsInPreferredOrder.indexOf(frameId1)
        if (frameId1Index == -1) {
            frameId1Index = Int.Companion.MAX_VALUE
        }
        var frameId2Index = frameIdsInPreferredOrder.indexOf(frameId2)

        //Because othwerwise returns -1 whihc would be tags in list went to top of list
        if (frameId2Index == -1) {
            frameId2Index = Int.Companion.MAX_VALUE
        }

        //To have determinable ordering AND because if returns equal Treese considers as equal
        if (frameId1Index == frameId2Index) {
            return frameId1!!.compareTo(frameId2!!)
        }

        return frameId1Index - frameId2Index
    }
}