package de.visualdigits.kaudiotagger.model.id3.tag

import de.visualdigits.kaudiotagger.model.id3.types.ID3v22Frames

class ID3v22PreferredFrameOrderComparator private constructor() : Comparator<String> {

    companion object {

        val frameIdsInPreferredOrder: MutableList<String> = mutableListOf(
            //these are the key ones we want at the top
            ID3v22Frames.UNIQUE_FILE_ID.id,
            ID3v22Frames.TITLE.id,
            ID3v22Frames.ARTIST.id,
            ID3v22Frames.ALBUM.id,
            ID3v22Frames.TORY.id,
            ID3v22Frames.GENRE.id,
            ID3v22Frames.COMPOSER.id,
            ID3v22Frames.CONDUCTOR.id,
            ID3v22Frames.CONTENT_GROUP_DESC.id,
            ID3v22Frames.TRACK.id,
            ID3v22Frames.TYER.id,
            ID3v22Frames.TDAT.id,
            ID3v22Frames.TIME.id,
            ID3v22Frames.BPM.id,
            ID3v22Frames.ISRC.id,
            ID3v22Frames.TORY.id,
            ID3v22Frames.ACCOMPANIMENT.id,
            ID3v22Frames.TITLE_REFINEMENT.id,
            ID3v22Frames.UNSYNC_LYRICS.id,
            ID3v22Frames.USER_DEFINED_INFO.id,
            ID3v22Frames.USER_DEFINED_URL.id,
            ID3v22Frames.URL_ARTIST_WEB.id,
            ID3v22Frames.URL_COMMERCIAL.id,
            ID3v22Frames.URL_COPYRIGHT.id,
            ID3v22Frames.URL_FILE_WEB.id,
            ID3v22Frames.URL_OFFICIAL_RADIO.id,
            ID3v22Frames.URL_PAYMENT.id,
            ID3v22Frames.URL_PUBLISHERS.id,
            ID3v22Frames.URL_COMMERCIAL.id,
            ID3v22Frames.LYRICIST.id,
            ID3v22Frames.MEDIA_TYPE.id,
            ID3v22Frames.IPLS.id,
            ID3v22Frames.LANGUAGE.id,
            ID3v22Frames.TITLE_SORT_ORDER_ITUNES.id,
            ID3v22Frames.PLAYLIST_DELAY.id,
            ID3v22Frames.PLAY_COUNTER.id,
            ID3v22Frames.POPULARIMETER.id,
            ID3v22Frames.PUBLISHER.id,
            ID3v22Frames.ALBUM_ARTIST_SORT_ORDER_ITUNES.id,
            ID3v22Frames.COMPOSER_SORT_ORDER_ITUNES.id,
            ID3v22Frames.IS_COMPILATION.id,
            ID3v22Frames.TITLE_SORT_ORDER_ITUNES.id,
            ID3v22Frames.ARTIST_SORT_ORDER_ITUNES.id,
            ID3v22Frames.ALBUM_SORT_ORDER_ITUNES.id,
            ID3v22Frames.ALBUM_ARTIST_SORT_ORDER_ITUNES.id,
            ID3v22Frames.COMPOSER_SORT_ORDER_ITUNES.id,
            ID3v22Frames.COMMENT.id,

            //Not so bothered about these
            ID3v22Frames.TRDA.id,
            ID3v22Frames.COPYRIGHTINFO.id,
            ID3v22Frames.ENCODEDBY.id,
            ID3v22Frames.EQUALISATION.id,
            ID3v22Frames.EVENT_TIMING_CODES.id,
            ID3v22Frames.FILE_TYPE.id,
            ID3v22Frames.HW_SW_SETTINGS.id,
            ID3v22Frames.INITIAL_KEY.id,
            ID3v22Frames.LENGTH.id,
            ID3v22Frames.LINKED_INFO.id,
            ID3v22Frames.TSIZ.id,
            ID3v22Frames.MPEG_LOCATION_LOOKUP_TABLE.id,
            ID3v22Frames.ORIGARTIST.id,
            ID3v22Frames.ORIG_FILENAME.id,
            ID3v22Frames.ORIG_LYRICIST.id,
            ID3v22Frames.ORIG_TITLE.id,
            ID3v22Frames.RECOMMENDED_BUFFER_SIZE.id,
            ID3v22Frames.REMIXED.id,
            ID3v22Frames.REVERB.id,
            ID3v22Frames.SET.id,
            ID3v22Frames.SYNC_LYRIC.id,
            ID3v22Frames.SYNC_TEMPO.id,

            //Want this near the end because can cause problems with unsyncing
            ID3v22Frames.ATTACHED_PICTURE.id,

            //Itunes doesnt seem to like these, and of little use so put right at end
            ID3v22Frames.MUSIC_CD_ID.id,
            ID3v22Frames.AUDIO_ENCRYPTION.id,
            ID3v22Frames.GENERAL_ENCAPS_OBJECT.id,
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