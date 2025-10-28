package de.visualdigits.kaudiotagger.model.id3.tag

import de.visualdigits.kaudiotagger.model.id3.types.ID3v24Frames

class ID3v24PreferredFrameOrderComparator private constructor() : Comparator<String> {

    companion object {

        val frameIdsInPreferredOrder: MutableList<String> = mutableListOf(
            //these are the key ones we want at the top
            ID3v24Frames.UNIQUE_FILE_ID.id,
            ID3v24Frames.TITLE.id,
            ID3v24Frames.ARTIST.id,
            ID3v24Frames.ALBUM.id,
            ID3v24Frames.ALBUM_SORT_ORDER.id,
            ID3v24Frames.GENRE.id,
            ID3v24Frames.COMPOSER.id,
            ID3v24Frames.CONDUCTOR.id,
            ID3v24Frames.CONTENT_GROUP_DESC.id,
            ID3v24Frames.TRACK.id,
            ID3v24Frames.YEAR.id,
            ID3v24Frames.ACCOMPANIMENT.id,
            ID3v24Frames.BPM.id,
            ID3v24Frames.ISRC.id,
            ID3v24Frames.TITLE_SORT_ORDER.id,
            ID3v24Frames.TITLE_REFINEMENT.id,
            ID3v24Frames.UNSYNC_LYRICS.id,
            ID3v24Frames.USER_DEFINED_INFO.id,
            ID3v24Frames.USER_DEFINED_URL.id,
            ID3v24Frames.URL_ARTIST_WEB.id,
            ID3v24Frames.URL_COMMERCIAL.id,
            ID3v24Frames.URL_COPYRIGHT.id,
            ID3v24Frames.URL_FILE_WEB.id,
            ID3v24Frames.URL_OFFICIAL_RADIO.id,
            ID3v24Frames.URL_PAYMENT.id,
            ID3v24Frames.URL_PUBLISHERS.id,
            ID3v24Frames.URL_COMMERCIAL.id,
            ID3v24Frames.LYRICIST.id,
            ID3v24Frames.MEDIA_TYPE.id,
            ID3v24Frames.INVOLVED_PEOPLE.id,
            ID3v24Frames.LANGUAGE.id,
            ID3v24Frames.ARTIST_SORT_ORDER.id,
            ID3v24Frames.PLAYLIST_DELAY.id,
            ID3v24Frames.PLAY_COUNTER.id,
            ID3v24Frames.POPULARIMETER.id,
            ID3v24Frames.PUBLISHER.id,
            ID3v24Frames.ALBUM_ARTIST_SORT_ORDER_ITUNES.id,
            ID3v24Frames.COMPOSER_SORT_ORDER_ITUNES.id,
            ID3v24Frames.IS_COMPILATION.id,
            ID3v24Frames.COMMENT.id,

            //Not so bothered about these
            ID3v24Frames.AUDIO_SEEK_POINT_INDEX.id,
            ID3v24Frames.COMMERCIAL_FRAME.id,
            ID3v24Frames.COPYRIGHTINFO.id,
            ID3v24Frames.ENCODEDBY.id,
            ID3v24Frames.ENCODING_TIME.id,
            ID3v24Frames.ENCRYPTION.id,
            ID3v24Frames.EQUALISATION2.id,
            ID3v24Frames.EVENT_TIMING_CODES.id,
            ID3v24Frames.FILE_OWNER.id,
            ID3v24Frames.FILE_TYPE.id,
            ID3v24Frames.GROUP_ID_REG.id,
            ID3v24Frames.HW_SW_SETTINGS.id,
            ID3v24Frames.INITIAL_KEY.id,
            ID3v24Frames.LENGTH.id,
            ID3v24Frames.LINKED_INFO.id,
            ID3v24Frames.MOOD.id,
            ID3v24Frames.MPEG_LOCATION_LOOKUP_TABLE.id,
            ID3v24Frames.MUSICIAN_CREDITS.id,
            ID3v24Frames.ORIGARTIST.id,
            ID3v24Frames.ORIGINAL_RELEASE_TIME.id,
            ID3v24Frames.ORIG_FILENAME.id,
            ID3v24Frames.ORIG_LYRICIST.id,
            ID3v24Frames.ORIG_TITLE.id,
            ID3v24Frames.OWNERSHIP.id,
            ID3v24Frames.POSITION_SYNC.id,
            ID3v24Frames.PRODUCED_NOTICE.id,
            ID3v24Frames.RADIO_NAME.id,
            ID3v24Frames.RADIO_OWNER.id,
            ID3v24Frames.RECOMMENDED_BUFFER_SIZE.id,
            ID3v24Frames.RELATIVE_VOLUME_ADJUSTMENT2.id,
            ID3v24Frames.RELEASE_TIME.id,
            ID3v24Frames.REMIXED.id,
            ID3v24Frames.REVERB.id,
            ID3v24Frames.SEEK.id,
            ID3v24Frames.SET.id,
            ID3v24Frames.SET_SUBTITLE.id,
            ID3v24Frames.SIGNATURE.id,
            ID3v24Frames.SYNC_LYRIC.id,
            ID3v24Frames.SYNC_TEMPO.id,
            ID3v24Frames.TAGGING_TIME.id,
            ID3v24Frames.TERMS_OF_USE.id,

            //Want this near the end because can cause problems with unsyncing
            ID3v24Frames.ATTACHED_PICTURE.id,

            //Itunes doesnt seem to like these, and of little use so put right at end
            ID3v24Frames.PRIVATE.id,
            ID3v24Frames.MUSIC_CD_ID.id,
            ID3v24Frames.AUDIO_ENCRYPTION.id,
            ID3v24Frames.GENERAL_ENCAPS_OBJECT.id,
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