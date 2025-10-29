package de.visualdigits.kaudiotagger.model.id3.tag

import de.visualdigits.kaudiotagger.model.id3.types.ID3v22FrameId

class ID3v22PreferredFrameOrderComparator private constructor() : Comparator<String> {

    companion object {

        val frameIdsInPreferredOrder: MutableList<String> = mutableListOf(
            //these are the key ones we want at the top
            ID3v22FrameId.UNIQUE_FILE_ID.id,
            ID3v22FrameId.TITLE.id,
            ID3v22FrameId.ARTIST.id,
            ID3v22FrameId.ALBUM.id,
            ID3v22FrameId.TORY.id,
            ID3v22FrameId.GENRE.id,
            ID3v22FrameId.COMPOSER.id,
            ID3v22FrameId.CONDUCTOR.id,
            ID3v22FrameId.CONTENT_GROUP_DESC.id,
            ID3v22FrameId.TRACK.id,
            ID3v22FrameId.TYER.id,
            ID3v22FrameId.TDAT.id,
            ID3v22FrameId.TIME.id,
            ID3v22FrameId.BPM.id,
            ID3v22FrameId.ISRC.id,
            ID3v22FrameId.TORY.id,
            ID3v22FrameId.ACCOMPANIMENT.id,
            ID3v22FrameId.TITLE_REFINEMENT.id,
            ID3v22FrameId.UNSYNC_LYRICS.id,
            ID3v22FrameId.USER_DEFINED_INFO.id,
            ID3v22FrameId.USER_DEFINED_URL.id,
            ID3v22FrameId.URL_ARTIST_WEB.id,
            ID3v22FrameId.URL_COMMERCIAL.id,
            ID3v22FrameId.URL_COPYRIGHT.id,
            ID3v22FrameId.URL_FILE_WEB.id,
            ID3v22FrameId.URL_OFFICIAL_RADIO.id,
            ID3v22FrameId.URL_PAYMENT.id,
            ID3v22FrameId.URL_PUBLISHERS.id,
            ID3v22FrameId.URL_COMMERCIAL.id,
            ID3v22FrameId.LYRICIST.id,
            ID3v22FrameId.MEDIA_TYPE.id,
            ID3v22FrameId.IPLS.id,
            ID3v22FrameId.LANGUAGE.id,
            ID3v22FrameId.TITLE_SORT_ORDER_ITUNES.id,
            ID3v22FrameId.PLAYLIST_DELAY.id,
            ID3v22FrameId.PLAY_COUNTER.id,
            ID3v22FrameId.POPULARIMETER.id,
            ID3v22FrameId.PUBLISHER.id,
            ID3v22FrameId.ALBUM_ARTIST_SORT_ORDER_ITUNES.id,
            ID3v22FrameId.COMPOSER_SORT_ORDER_ITUNES.id,
            ID3v22FrameId.IS_COMPILATION.id,
            ID3v22FrameId.TITLE_SORT_ORDER_ITUNES.id,
            ID3v22FrameId.ARTIST_SORT_ORDER_ITUNES.id,
            ID3v22FrameId.ALBUM_SORT_ORDER_ITUNES.id,
            ID3v22FrameId.ALBUM_ARTIST_SORT_ORDER_ITUNES.id,
            ID3v22FrameId.COMPOSER_SORT_ORDER_ITUNES.id,
            ID3v22FrameId.COMMENT.id,

            //Not so bothered about these
            ID3v22FrameId.TRDA.id,
            ID3v22FrameId.COPYRIGHTINFO.id,
            ID3v22FrameId.ENCODEDBY.id,
            ID3v22FrameId.EQUALISATION.id,
            ID3v22FrameId.EVENT_TIMING_CODES.id,
            ID3v22FrameId.FILE_TYPE.id,
            ID3v22FrameId.HW_SW_SETTINGS.id,
            ID3v22FrameId.INITIAL_KEY.id,
            ID3v22FrameId.LENGTH.id,
            ID3v22FrameId.LINKED_INFO.id,
            ID3v22FrameId.TSIZ.id,
            ID3v22FrameId.MPEG_LOCATION_LOOKUP_TABLE.id,
            ID3v22FrameId.ORIGARTIST.id,
            ID3v22FrameId.ORIG_FILENAME.id,
            ID3v22FrameId.ORIG_LYRICIST.id,
            ID3v22FrameId.ORIG_TITLE.id,
            ID3v22FrameId.RECOMMENDED_BUFFER_SIZE.id,
            ID3v22FrameId.REMIXED.id,
            ID3v22FrameId.REVERB.id,
            ID3v22FrameId.SET.id,
            ID3v22FrameId.SYNC_LYRIC.id,
            ID3v22FrameId.SYNC_TEMPO.id,

            //Want this near the end because can cause problems with unsyncing
            ID3v22FrameId.ATTACHED_PICTURE.id,

            //Itunes doesnt seem to like these, and of little use so put right at end
            ID3v22FrameId.MUSIC_CD_ID.id,
            ID3v22FrameId.AUDIO_ENCRYPTION.id,
            ID3v22FrameId.GENERAL_ENCAPS_OBJECT.id,
        )

        val instance: ID3v22PreferredFrameOrderComparator = ID3v22PreferredFrameOrderComparator()
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