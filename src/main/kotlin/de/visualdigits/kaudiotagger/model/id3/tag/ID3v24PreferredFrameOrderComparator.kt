package de.visualdigits.kaudiotagger.model.id3.tag

import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId

object ID3v24PreferredFrameOrderComparator : Comparator<String> {


    val frameIdsInPreferredOrder: MutableList<String> = mutableListOf(
        // these are the key ones we want at the top
        ID3v24FrameId.UNIQUE_FILE_ID.id,
        ID3v24FrameId.TITLE.id,
        ID3v24FrameId.ARTIST.id,
        ID3v24FrameId.ALBUM.id,
        ID3v24FrameId.ALBUM_SORT_ORDER.id,
        ID3v24FrameId.GENRE.id,
        ID3v24FrameId.COMPOSER.id,
        ID3v24FrameId.CONDUCTOR.id,
        ID3v24FrameId.CONTENT_GROUP_DESC.id,
        ID3v24FrameId.TRACK.id,
        ID3v24FrameId.YEAR.id,
        ID3v24FrameId.ACCOMPANIMENT.id,
        ID3v24FrameId.BPM.id,
        ID3v24FrameId.ISRC.id,
        ID3v24FrameId.TITLE_SORT_ORDER.id,
        ID3v24FrameId.TITLE_REFINEMENT.id,
        ID3v24FrameId.UNSYNC_LYRICS.id,
        ID3v24FrameId.USER_DEFINED_INFO.id,
        ID3v24FrameId.USER_DEFINED_URL.id,
        ID3v24FrameId.URL_ARTIST_WEB.id,
        ID3v24FrameId.URL_COMMERCIAL.id,
        ID3v24FrameId.URL_COPYRIGHT.id,
        ID3v24FrameId.URL_FILE_WEB.id,
        ID3v24FrameId.URL_OFFICIAL_RADIO.id,
        ID3v24FrameId.URL_PAYMENT.id,
        ID3v24FrameId.URL_PUBLISHERS.id,
        ID3v24FrameId.URL_COMMERCIAL.id,
        ID3v24FrameId.LYRICIST.id,
        ID3v24FrameId.MEDIA_TYPE.id,
        ID3v24FrameId.INVOLVED_PEOPLE.id,
        ID3v24FrameId.LANGUAGE.id,
        ID3v24FrameId.ARTIST_SORT_ORDER.id,
        ID3v24FrameId.PLAYLIST_DELAY.id,
        ID3v24FrameId.PLAY_COUNTER.id,
        ID3v24FrameId.POPULARIMETER.id,
        ID3v24FrameId.PUBLISHER.id,
        ID3v24FrameId.ALBUM_ARTIST_SORT_ORDER_ITUNES.id,
        ID3v24FrameId.COMPOSER_SORT_ORDER_ITUNES.id,
        ID3v24FrameId.IS_COMPILATION.id,
        ID3v24FrameId.COMMENT.id,

        // Not so bothered about these
        ID3v24FrameId.AUDIO_SEEK_POINT_INDEX.id,
        ID3v24FrameId.COMMERCIAL_FRAME.id,
        ID3v24FrameId.COPYRIGHTINFO.id,
        ID3v24FrameId.ENCODEDBY.id,
        ID3v24FrameId.ENCODING_TIME.id,
        ID3v24FrameId.ENCRYPTION.id,
        ID3v24FrameId.EQUALISATION2.id,
        ID3v24FrameId.EVENT_TIMING_CODES.id,
        ID3v24FrameId.FILE_OWNER.id,
        ID3v24FrameId.FILE_TYPE.id,
        ID3v24FrameId.GROUP_ID_REG.id,
        ID3v24FrameId.HW_SW_SETTINGS.id,
        ID3v24FrameId.INITIAL_KEY.id,
        ID3v24FrameId.LENGTH.id,
        ID3v24FrameId.LINKED_INFO.id,
        ID3v24FrameId.MOOD.id,
        ID3v24FrameId.MPEG_LOCATION_LOOKUP_TABLE.id,
        ID3v24FrameId.MUSICIAN_CREDITS.id,
        ID3v24FrameId.ORIGARTIST.id,
        ID3v24FrameId.ORIGINAL_RELEASE_TIME.id,
        ID3v24FrameId.ORIG_FILENAME.id,
        ID3v24FrameId.ORIG_LYRICIST.id,
        ID3v24FrameId.ORIG_TITLE.id,
        ID3v24FrameId.OWNERSHIP.id,
        ID3v24FrameId.POSITION_SYNC.id,
        ID3v24FrameId.PRODUCED_NOTICE.id,
        ID3v24FrameId.RADIO_NAME.id,
        ID3v24FrameId.RADIO_OWNER.id,
        ID3v24FrameId.RECOMMENDED_BUFFER_SIZE.id,
        ID3v24FrameId.RELATIVE_VOLUME_ADJUSTMENT2.id,
        ID3v24FrameId.RELEASE_TIME.id,
        ID3v24FrameId.REMIXED.id,
        ID3v24FrameId.REVERB.id,
        ID3v24FrameId.SEEK.id,
        ID3v24FrameId.SET.id,
        ID3v24FrameId.SET_SUBTITLE.id,
        ID3v24FrameId.SIGNATURE.id,
        ID3v24FrameId.SYNC_LYRIC.id,
        ID3v24FrameId.SYNC_TEMPO.id,
        ID3v24FrameId.TAGGING_TIME.id,
        ID3v24FrameId.TERMS_OF_USE.id,

        // Want this near the end because can cause problems with unsyncing
        ID3v24FrameId.ATTACHED_PICTURE.id,

        // Itunes doesnt seem to like these, and of little use so put right at end
        ID3v24FrameId.PRIVATE.id,
        ID3v24FrameId.MUSIC_CD_ID.id,
        ID3v24FrameId.AUDIO_ENCRYPTION.id,
        ID3v24FrameId.GENERAL_ENCAPS_OBJECT.id,
    )

    override fun compare(frameId1: String?, frameId2: String?): Int {
        var frameId1Index = frameIdsInPreferredOrder.indexOf(frameId1)
        if (frameId1Index == -1) {
            frameId1Index = Int.MAX_VALUE
        }
        var frameId2Index = frameIdsInPreferredOrder.indexOf(frameId2)

        // Because othwerwise returns -1 whihc would be tags in list went to top of list
        if (frameId2Index == -1) {
            frameId2Index = Int.MAX_VALUE
        }

        // To have determinable ordering AND because if returns equal Treese considers as equal
        if (frameId1Index == frameId2Index) {
            return frameId2?.let { fid -> frameId1?.compareTo(fid) }?:-1
        }

        return frameId1Index - frameId2Index
    }
}