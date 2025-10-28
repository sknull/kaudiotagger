package de.visualdigits.kaudiotagger.model.id3.tag

import de.visualdigits.kaudiotagger.model.id3.types.ID3V24Frame

class ID3v24PreferredFrameOrderComparator private constructor() : Comparator<String> {

    companion object {

        val frameIdsInPreferredOrder: MutableList<String> = mutableListOf(
            //these are the key ones we want at the top
            ID3V24Frame.UNIQUE_FILE_ID.id,
            ID3V24Frame.TITLE.id,
            ID3V24Frame.ARTIST.id,
            ID3V24Frame.ALBUM.id,
            ID3V24Frame.ALBUM_SORT_ORDER.id,
            ID3V24Frame.GENRE.id,
            ID3V24Frame.COMPOSER.id,
            ID3V24Frame.CONDUCTOR.id,
            ID3V24Frame.CONTENT_GROUP_DESC.id,
            ID3V24Frame.TRACK.id,
            ID3V24Frame.YEAR.id,
            ID3V24Frame.ACCOMPANIMENT.id,
            ID3V24Frame.BPM.id,
            ID3V24Frame.ISRC.id,
            ID3V24Frame.TITLE_SORT_ORDER.id,
            ID3V24Frame.TITLE_REFINEMENT.id,
            ID3V24Frame.UNSYNC_LYRICS.id,
            ID3V24Frame.USER_DEFINED_INFO.id,
            ID3V24Frame.USER_DEFINED_URL.id,
            ID3V24Frame.URL_ARTIST_WEB.id,
            ID3V24Frame.URL_COMMERCIAL.id,
            ID3V24Frame.URL_COPYRIGHT.id,
            ID3V24Frame.URL_FILE_WEB.id,
            ID3V24Frame.URL_OFFICIAL_RADIO.id,
            ID3V24Frame.URL_PAYMENT.id,
            ID3V24Frame.URL_PUBLISHERS.id,
            ID3V24Frame.URL_COMMERCIAL.id,
            ID3V24Frame.LYRICIST.id,
            ID3V24Frame.MEDIA_TYPE.id,
            ID3V24Frame.INVOLVED_PEOPLE.id,
            ID3V24Frame.LANGUAGE.id,
            ID3V24Frame.ARTIST_SORT_ORDER.id,
            ID3V24Frame.PLAYLIST_DELAY.id,
            ID3V24Frame.PLAY_COUNTER.id,
            ID3V24Frame.POPULARIMETER.id,
            ID3V24Frame.PUBLISHER.id,
            ID3V24Frame.ALBUM_ARTIST_SORT_ORDER_ITUNES.id,
            ID3V24Frame.COMPOSER_SORT_ORDER_ITUNES.id,
            ID3V24Frame.IS_COMPILATION.id,
            ID3V24Frame.COMMENT.id,

            //Not so bothered about these
            ID3V24Frame.AUDIO_SEEK_POINT_INDEX.id,
            ID3V24Frame.COMMERCIAL_FRAME.id,
            ID3V24Frame.COPYRIGHTINFO.id,
            ID3V24Frame.ENCODEDBY.id,
            ID3V24Frame.ENCODING_TIME.id,
            ID3V24Frame.ENCRYPTION.id,
            ID3V24Frame.EQUALISATION2.id,
            ID3V24Frame.EVENT_TIMING_CODES.id,
            ID3V24Frame.FILE_OWNER.id,
            ID3V24Frame.FILE_TYPE.id,
            ID3V24Frame.GROUP_ID_REG.id,
            ID3V24Frame.HW_SW_SETTINGS.id,
            ID3V24Frame.INITIAL_KEY.id,
            ID3V24Frame.LENGTH.id,
            ID3V24Frame.LINKED_INFO.id,
            ID3V24Frame.MOOD.id,
            ID3V24Frame.MPEG_LOCATION_LOOKUP_TABLE.id,
            ID3V24Frame.MUSICIAN_CREDITS.id,
            ID3V24Frame.ORIGARTIST.id,
            ID3V24Frame.ORIGINAL_RELEASE_TIME.id,
            ID3V24Frame.ORIG_FILENAME.id,
            ID3V24Frame.ORIG_LYRICIST.id,
            ID3V24Frame.ORIG_TITLE.id,
            ID3V24Frame.OWNERSHIP.id,
            ID3V24Frame.POSITION_SYNC.id,
            ID3V24Frame.PRODUCED_NOTICE.id,
            ID3V24Frame.RADIO_NAME.id,
            ID3V24Frame.RADIO_OWNER.id,
            ID3V24Frame.RECOMMENDED_BUFFER_SIZE.id,
            ID3V24Frame.RELATIVE_VOLUME_ADJUSTMENT2.id,
            ID3V24Frame.RELEASE_TIME.id,
            ID3V24Frame.REMIXED.id,
            ID3V24Frame.REVERB.id,
            ID3V24Frame.SEEK.id,
            ID3V24Frame.SET.id,
            ID3V24Frame.SET_SUBTITLE.id,
            ID3V24Frame.SIGNATURE.id,
            ID3V24Frame.SYNC_LYRIC.id,
            ID3V24Frame.SYNC_TEMPO.id,
            ID3V24Frame.TAGGING_TIME.id,
            ID3V24Frame.TERMS_OF_USE.id,

            //Want this near the end because can cause problems with unsyncing
            ID3V24Frame.ATTACHED_PICTURE.id,

            //Itunes doesnt seem to like these, and of little use so put right at end
            ID3V24Frame.PRIVATE.id,
            ID3V24Frame.MUSIC_CD_ID.id,
            ID3V24Frame.AUDIO_ENCRYPTION.id,
            ID3V24Frame.GENERAL_ENCAPS_OBJECT.id,
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