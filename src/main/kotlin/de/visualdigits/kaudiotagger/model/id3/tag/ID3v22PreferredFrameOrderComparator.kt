package de.visualdigits.kaudiotagger.model.id3.tag

import de.visualdigits.kaudiotagger.model.id3.types.ID3V22FrameId

class ID3v22PreferredFrameOrderComparator private constructor() : Comparator<String> {

    companion object {

        val frameIdsInPreferredOrder: MutableList<String> = mutableListOf(
            //these are the key ones we want at the top
            ID3V22FrameId.UNIQUE_FILE_ID.id,
            ID3V22FrameId.TITLE.id,
            ID3V22FrameId.ARTIST.id,
            ID3V22FrameId.ALBUM.id,
            ID3V22FrameId.TORY.id,
            ID3V22FrameId.GENRE.id,
            ID3V22FrameId.COMPOSER.id,
            ID3V22FrameId.CONDUCTOR.id,
            ID3V22FrameId.CONTENT_GROUP_DESC.id,
            ID3V22FrameId.TRACK.id,
            ID3V22FrameId.TYER.id,
            ID3V22FrameId.TDAT.id,
            ID3V22FrameId.TIME.id,
            ID3V22FrameId.BPM.id,
            ID3V22FrameId.ISRC.id,
            ID3V22FrameId.TORY.id,
            ID3V22FrameId.ACCOMPANIMENT.id,
            ID3V22FrameId.TITLE_REFINEMENT.id,
            ID3V22FrameId.UNSYNC_LYRICS.id,
            ID3V22FrameId.USER_DEFINED_INFO.id,
            ID3V22FrameId.USER_DEFINED_URL.id,
            ID3V22FrameId.URL_ARTIST_WEB.id,
            ID3V22FrameId.URL_COMMERCIAL.id,
            ID3V22FrameId.URL_COPYRIGHT.id,
            ID3V22FrameId.URL_FILE_WEB.id,
            ID3V22FrameId.URL_OFFICIAL_RADIO.id,
            ID3V22FrameId.URL_PAYMENT.id,
            ID3V22FrameId.URL_PUBLISHERS.id,
            ID3V22FrameId.URL_COMMERCIAL.id,
            ID3V22FrameId.LYRICIST.id,
            ID3V22FrameId.MEDIA_TYPE.id,
            ID3V22FrameId.IPLS.id,
            ID3V22FrameId.LANGUAGE.id,
            ID3V22FrameId.TITLE_SORT_ORDER_ITUNES.id,
            ID3V22FrameId.PLAYLIST_DELAY.id,
            ID3V22FrameId.PLAY_COUNTER.id,
            ID3V22FrameId.POPULARIMETER.id,
            ID3V22FrameId.PUBLISHER.id,
            ID3V22FrameId.ALBUM_ARTIST_SORT_ORDER_ITUNES.id,
            ID3V22FrameId.COMPOSER_SORT_ORDER_ITUNES.id,
            ID3V22FrameId.IS_COMPILATION.id,
            ID3V22FrameId.TITLE_SORT_ORDER_ITUNES.id,
            ID3V22FrameId.ARTIST_SORT_ORDER_ITUNES.id,
            ID3V22FrameId.ALBUM_SORT_ORDER_ITUNES.id,
            ID3V22FrameId.ALBUM_ARTIST_SORT_ORDER_ITUNES.id,
            ID3V22FrameId.COMPOSER_SORT_ORDER_ITUNES.id,
            ID3V22FrameId.COMMENT.id,

            //Not so bothered about these
            ID3V22FrameId.TRDA.id,
            ID3V22FrameId.COPYRIGHTINFO.id,
            ID3V22FrameId.ENCODEDBY.id,
            ID3V22FrameId.EQUALISATION.id,
            ID3V22FrameId.EVENT_TIMING_CODES.id,
            ID3V22FrameId.FILE_TYPE.id,
            ID3V22FrameId.HW_SW_SETTINGS.id,
            ID3V22FrameId.INITIAL_KEY.id,
            ID3V22FrameId.LENGTH.id,
            ID3V22FrameId.LINKED_INFO.id,
            ID3V22FrameId.TSIZ.id,
            ID3V22FrameId.MPEG_LOCATION_LOOKUP_TABLE.id,
            ID3V22FrameId.ORIGARTIST.id,
            ID3V22FrameId.ORIG_FILENAME.id,
            ID3V22FrameId.ORIG_LYRICIST.id,
            ID3V22FrameId.ORIG_TITLE.id,
            ID3V22FrameId.RECOMMENDED_BUFFER_SIZE.id,
            ID3V22FrameId.REMIXED.id,
            ID3V22FrameId.REVERB.id,
            ID3V22FrameId.SET.id,
            ID3V22FrameId.SYNC_LYRIC.id,
            ID3V22FrameId.SYNC_TEMPO.id,

            //Want this near the end because can cause problems with unsyncing
            ID3V22FrameId.ATTACHED_PICTURE.id,

            //Itunes doesnt seem to like these, and of little use so put right at end
            ID3V22FrameId.MUSIC_CD_ID.id,
            ID3V22FrameId.AUDIO_ENCRYPTION.id,
            ID3V22FrameId.GENERAL_ENCAPS_OBJECT.id,
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