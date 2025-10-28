package de.visualdigits.kaudiotagger.model.id3.tag

import de.visualdigits.kaudiotagger.model.id3.types.ID3V22Frame

class ID3v22PreferredFrameOrderComparator private constructor() : Comparator<String> {

    companion object {

        val frameIdsInPreferredOrder: MutableList<String> = mutableListOf(
            //these are the key ones we want at the top
            ID3V22Frame.UNIQUE_FILE_ID.id,
            ID3V22Frame.TITLE.id,
            ID3V22Frame.ARTIST.id,
            ID3V22Frame.ALBUM.id,
            ID3V22Frame.TORY.id,
            ID3V22Frame.GENRE.id,
            ID3V22Frame.COMPOSER.id,
            ID3V22Frame.CONDUCTOR.id,
            ID3V22Frame.CONTENT_GROUP_DESC.id,
            ID3V22Frame.TRACK.id,
            ID3V22Frame.TYER.id,
            ID3V22Frame.TDAT.id,
            ID3V22Frame.TIME.id,
            ID3V22Frame.BPM.id,
            ID3V22Frame.ISRC.id,
            ID3V22Frame.TORY.id,
            ID3V22Frame.ACCOMPANIMENT.id,
            ID3V22Frame.TITLE_REFINEMENT.id,
            ID3V22Frame.UNSYNC_LYRICS.id,
            ID3V22Frame.USER_DEFINED_INFO.id,
            ID3V22Frame.USER_DEFINED_URL.id,
            ID3V22Frame.URL_ARTIST_WEB.id,
            ID3V22Frame.URL_COMMERCIAL.id,
            ID3V22Frame.URL_COPYRIGHT.id,
            ID3V22Frame.URL_FILE_WEB.id,
            ID3V22Frame.URL_OFFICIAL_RADIO.id,
            ID3V22Frame.URL_PAYMENT.id,
            ID3V22Frame.URL_PUBLISHERS.id,
            ID3V22Frame.URL_COMMERCIAL.id,
            ID3V22Frame.LYRICIST.id,
            ID3V22Frame.MEDIA_TYPE.id,
            ID3V22Frame.IPLS.id,
            ID3V22Frame.LANGUAGE.id,
            ID3V22Frame.TITLE_SORT_ORDER_ITUNES.id,
            ID3V22Frame.PLAYLIST_DELAY.id,
            ID3V22Frame.PLAY_COUNTER.id,
            ID3V22Frame.POPULARIMETER.id,
            ID3V22Frame.PUBLISHER.id,
            ID3V22Frame.ALBUM_ARTIST_SORT_ORDER_ITUNES.id,
            ID3V22Frame.COMPOSER_SORT_ORDER_ITUNES.id,
            ID3V22Frame.IS_COMPILATION.id,
            ID3V22Frame.TITLE_SORT_ORDER_ITUNES.id,
            ID3V22Frame.ARTIST_SORT_ORDER_ITUNES.id,
            ID3V22Frame.ALBUM_SORT_ORDER_ITUNES.id,
            ID3V22Frame.ALBUM_ARTIST_SORT_ORDER_ITUNES.id,
            ID3V22Frame.COMPOSER_SORT_ORDER_ITUNES.id,
            ID3V22Frame.COMMENT.id,

            //Not so bothered about these
            ID3V22Frame.TRDA.id,
            ID3V22Frame.COPYRIGHTINFO.id,
            ID3V22Frame.ENCODEDBY.id,
            ID3V22Frame.EQUALISATION.id,
            ID3V22Frame.EVENT_TIMING_CODES.id,
            ID3V22Frame.FILE_TYPE.id,
            ID3V22Frame.HW_SW_SETTINGS.id,
            ID3V22Frame.INITIAL_KEY.id,
            ID3V22Frame.LENGTH.id,
            ID3V22Frame.LINKED_INFO.id,
            ID3V22Frame.TSIZ.id,
            ID3V22Frame.MPEG_LOCATION_LOOKUP_TABLE.id,
            ID3V22Frame.ORIGARTIST.id,
            ID3V22Frame.ORIG_FILENAME.id,
            ID3V22Frame.ORIG_LYRICIST.id,
            ID3V22Frame.ORIG_TITLE.id,
            ID3V22Frame.RECOMMENDED_BUFFER_SIZE.id,
            ID3V22Frame.REMIXED.id,
            ID3V22Frame.REVERB.id,
            ID3V22Frame.SET.id,
            ID3V22Frame.SYNC_LYRIC.id,
            ID3V22Frame.SYNC_TEMPO.id,

            //Want this near the end because can cause problems with unsyncing
            ID3V22Frame.ATTACHED_PICTURE.id,

            //Itunes doesnt seem to like these, and of little use so put right at end
            ID3V22Frame.MUSIC_CD_ID.id,
            ID3V22Frame.AUDIO_ENCRYPTION.id,
            ID3V22Frame.GENERAL_ENCAPS_OBJECT.id,
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