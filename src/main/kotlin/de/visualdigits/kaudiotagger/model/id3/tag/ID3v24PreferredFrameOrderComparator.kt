package de.visualdigits.kaudiotagger.model.id3.tag

import de.visualdigits.kaudiotagger.model.id3.types.ID3V24FrameId

class ID3v24PreferredFrameOrderComparator private constructor() : Comparator<String> {

    companion object {

        val frameIdsInPreferredOrder: MutableList<String> = mutableListOf(
            //these are the key ones we want at the top
            ID3V24FrameId.UNIQUE_FILE_ID.id,
            ID3V24FrameId.TITLE.id,
            ID3V24FrameId.ARTIST.id,
            ID3V24FrameId.ALBUM.id,
            ID3V24FrameId.ALBUM_SORT_ORDER.id,
            ID3V24FrameId.GENRE.id,
            ID3V24FrameId.COMPOSER.id,
            ID3V24FrameId.CONDUCTOR.id,
            ID3V24FrameId.CONTENT_GROUP_DESC.id,
            ID3V24FrameId.TRACK.id,
            ID3V24FrameId.YEAR.id,
            ID3V24FrameId.ACCOMPANIMENT.id,
            ID3V24FrameId.BPM.id,
            ID3V24FrameId.ISRC.id,
            ID3V24FrameId.TITLE_SORT_ORDER.id,
            ID3V24FrameId.TITLE_REFINEMENT.id,
            ID3V24FrameId.UNSYNC_LYRICS.id,
            ID3V24FrameId.USER_DEFINED_INFO.id,
            ID3V24FrameId.USER_DEFINED_URL.id,
            ID3V24FrameId.URL_ARTIST_WEB.id,
            ID3V24FrameId.URL_COMMERCIAL.id,
            ID3V24FrameId.URL_COPYRIGHT.id,
            ID3V24FrameId.URL_FILE_WEB.id,
            ID3V24FrameId.URL_OFFICIAL_RADIO.id,
            ID3V24FrameId.URL_PAYMENT.id,
            ID3V24FrameId.URL_PUBLISHERS.id,
            ID3V24FrameId.URL_COMMERCIAL.id,
            ID3V24FrameId.LYRICIST.id,
            ID3V24FrameId.MEDIA_TYPE.id,
            ID3V24FrameId.INVOLVED_PEOPLE.id,
            ID3V24FrameId.LANGUAGE.id,
            ID3V24FrameId.ARTIST_SORT_ORDER.id,
            ID3V24FrameId.PLAYLIST_DELAY.id,
            ID3V24FrameId.PLAY_COUNTER.id,
            ID3V24FrameId.POPULARIMETER.id,
            ID3V24FrameId.PUBLISHER.id,
            ID3V24FrameId.ALBUM_ARTIST_SORT_ORDER_ITUNES.id,
            ID3V24FrameId.COMPOSER_SORT_ORDER_ITUNES.id,
            ID3V24FrameId.IS_COMPILATION.id,
            ID3V24FrameId.COMMENT.id,

            //Not so bothered about these
            ID3V24FrameId.AUDIO_SEEK_POINT_INDEX.id,
            ID3V24FrameId.COMMERCIAL_FRAME.id,
            ID3V24FrameId.COPYRIGHTINFO.id,
            ID3V24FrameId.ENCODEDBY.id,
            ID3V24FrameId.ENCODING_TIME.id,
            ID3V24FrameId.ENCRYPTION.id,
            ID3V24FrameId.EQUALISATION2.id,
            ID3V24FrameId.EVENT_TIMING_CODES.id,
            ID3V24FrameId.FILE_OWNER.id,
            ID3V24FrameId.FILE_TYPE.id,
            ID3V24FrameId.GROUP_ID_REG.id,
            ID3V24FrameId.HW_SW_SETTINGS.id,
            ID3V24FrameId.INITIAL_KEY.id,
            ID3V24FrameId.LENGTH.id,
            ID3V24FrameId.LINKED_INFO.id,
            ID3V24FrameId.MOOD.id,
            ID3V24FrameId.MPEG_LOCATION_LOOKUP_TABLE.id,
            ID3V24FrameId.MUSICIAN_CREDITS.id,
            ID3V24FrameId.ORIGARTIST.id,
            ID3V24FrameId.ORIGINAL_RELEASE_TIME.id,
            ID3V24FrameId.ORIG_FILENAME.id,
            ID3V24FrameId.ORIG_LYRICIST.id,
            ID3V24FrameId.ORIG_TITLE.id,
            ID3V24FrameId.OWNERSHIP.id,
            ID3V24FrameId.POSITION_SYNC.id,
            ID3V24FrameId.PRODUCED_NOTICE.id,
            ID3V24FrameId.RADIO_NAME.id,
            ID3V24FrameId.RADIO_OWNER.id,
            ID3V24FrameId.RECOMMENDED_BUFFER_SIZE.id,
            ID3V24FrameId.RELATIVE_VOLUME_ADJUSTMENT2.id,
            ID3V24FrameId.RELEASE_TIME.id,
            ID3V24FrameId.REMIXED.id,
            ID3V24FrameId.REVERB.id,
            ID3V24FrameId.SEEK.id,
            ID3V24FrameId.SET.id,
            ID3V24FrameId.SET_SUBTITLE.id,
            ID3V24FrameId.SIGNATURE.id,
            ID3V24FrameId.SYNC_LYRIC.id,
            ID3V24FrameId.SYNC_TEMPO.id,
            ID3V24FrameId.TAGGING_TIME.id,
            ID3V24FrameId.TERMS_OF_USE.id,

            //Want this near the end because can cause problems with unsyncing
            ID3V24FrameId.ATTACHED_PICTURE.id,

            //Itunes doesnt seem to like these, and of little use so put right at end
            ID3V24FrameId.PRIVATE.id,
            ID3V24FrameId.MUSIC_CD_ID.id,
            ID3V24FrameId.AUDIO_ENCRYPTION.id,
            ID3V24FrameId.GENERAL_ENCAPS_OBJECT.id,
        )

        val instance: ID3v24PreferredFrameOrderComparator = ID3v24PreferredFrameOrderComparator()
    }

    override fun compare(frameId1: String?, frameId2: String?): Int {
        var frameId1Index = frameIdsInPreferredOrder.indexOf(frameId1)
        if (frameId1Index == -1) {
            frameId1Index = Int.MAX_VALUE
        }
        var frameId2Index = frameIdsInPreferredOrder.indexOf(frameId2)

        //Because othwerwise returns -1 whihc would be tags in list went to top of list
        if (frameId2Index == -1) {
            frameId2Index = Int.MAX_VALUE
        }

        //To have determinable ordering AND because if returns equal Treese considers as equal
        if (frameId1Index == frameId2Index) {
            return frameId2?.let { fid -> frameId1?.compareTo(fid) }?:-1
        }

        return frameId1Index - frameId2Index
    }
}