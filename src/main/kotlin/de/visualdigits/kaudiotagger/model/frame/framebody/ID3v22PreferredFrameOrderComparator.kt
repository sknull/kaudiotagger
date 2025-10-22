package de.visualdigits.kaudiotagger.model.frame.framebody

import de.visualdigits.kaudiotagger.model.kframe.ID3v22KFrame

class ID3v22PreferredFrameOrderComparator private constructor() : Comparator<String> {

    companion object {

        val frameIdsInPreferredOrder: MutableList<String> = mutableListOf(
            //these are the key ones we want at the top
            ID3v22KFrame.UNIQUE_FILE_ID.id,
            ID3v22KFrame.TITLE.id,
            ID3v22KFrame.ARTIST.id,
            ID3v22KFrame.ALBUM.id,
            ID3v22KFrame.TORY.id,
            ID3v22KFrame.GENRE.id,
            ID3v22KFrame.COMPOSER.id,
            ID3v22KFrame.CONDUCTOR.id,
            ID3v22KFrame.CONTENT_GROUP_DESC.id,
            ID3v22KFrame.TRACK.id,
            ID3v22KFrame.TYER.id,
            ID3v22KFrame.TDAT.id,
            ID3v22KFrame.TIME.id,
            ID3v22KFrame.BPM.id,
            ID3v22KFrame.ISRC.id,
            ID3v22KFrame.TORY.id,
            ID3v22KFrame.ACCOMPANIMENT.id,
            ID3v22KFrame.TITLE_REFINEMENT.id,
            ID3v22KFrame.UNSYNC_LYRICS.id,
            ID3v22KFrame.USER_DEFINED_INFO.id,
            ID3v22KFrame.USER_DEFINED_URL.id,
            ID3v22KFrame.URL_ARTIST_WEB.id,
            ID3v22KFrame.URL_COMMERCIAL.id,
            ID3v22KFrame.URL_COPYRIGHT.id,
            ID3v22KFrame.URL_FILE_WEB.id,
            ID3v22KFrame.URL_OFFICIAL_RADIO.id,
            ID3v22KFrame.URL_PAYMENT.id,
            ID3v22KFrame.URL_PUBLISHERS.id,
            ID3v22KFrame.URL_COMMERCIAL.id,
            ID3v22KFrame.LYRICIST.id,
            ID3v22KFrame.MEDIA_TYPE.id,
            ID3v22KFrame.IPLS.id,
            ID3v22KFrame.LANGUAGE.id,
            ID3v22KFrame.TITLE_SORT_ORDER_ITUNES.id,
            ID3v22KFrame.PLAYLIST_DELAY.id,
            ID3v22KFrame.PLAY_COUNTER.id,
            ID3v22KFrame.POPULARIMETER.id,
            ID3v22KFrame.PUBLISHER.id,
            ID3v22KFrame.ALBUM_ARTIST_SORT_ORDER_ITUNES.id,
            ID3v22KFrame.COMPOSER_SORT_ORDER_ITUNES.id,
            ID3v22KFrame.IS_COMPILATION.id,
            ID3v22KFrame.TITLE_SORT_ORDER_ITUNES.id,
            ID3v22KFrame.ARTIST_SORT_ORDER_ITUNES.id,
            ID3v22KFrame.ALBUM_SORT_ORDER_ITUNES.id,
            ID3v22KFrame.ALBUM_ARTIST_SORT_ORDER_ITUNES.id,
            ID3v22KFrame.COMPOSER_SORT_ORDER_ITUNES.id,
            ID3v22KFrame.COMMENT.id,

            //Not so bothered about these
            ID3v22KFrame.TRDA.id,
            ID3v22KFrame.COPYRIGHTINFO.id,
            ID3v22KFrame.ENCODEDBY.id,
            ID3v22KFrame.EQUALISATION.id,
            ID3v22KFrame.EVENT_TIMING_CODES.id,
            ID3v22KFrame.FILE_TYPE.id,
            ID3v22KFrame.HW_SW_SETTINGS.id,
            ID3v22KFrame.INITIAL_KEY.id,
            ID3v22KFrame.LENGTH.id,
            ID3v22KFrame.LINKED_INFO.id,
            ID3v22KFrame.TSIZ.id,
            ID3v22KFrame.MPEG_LOCATION_LOOKUP_TABLE.id,
            ID3v22KFrame.ORIGARTIST.id,
            ID3v22KFrame.ORIG_FILENAME.id,
            ID3v22KFrame.ORIG_LYRICIST.id,
            ID3v22KFrame.ORIG_TITLE.id,
            ID3v22KFrame.RECOMMENDED_BUFFER_SIZE.id,
            ID3v22KFrame.REMIXED.id,
            ID3v22KFrame.REVERB.id,
            ID3v22KFrame.SET.id,
            ID3v22KFrame.SYNC_LYRIC.id,
            ID3v22KFrame.SYNC_TEMPO.id,

            //Want this near the end because can cause problems with unsyncing
            ID3v22KFrame.ATTACHED_PICTURE.id,

            //Itunes doesnt seem to like these, and of little use so put right at end
            ID3v22KFrame.MUSIC_CD_ID.id,
            ID3v22KFrame.AUDIO_ENCRYPTION.id,
            ID3v22KFrame.GENERAL_ENCAPS_OBJECT.id,
        )

        val instance: ID3v22PreferredFrameOrderComparator = ID3v22PreferredFrameOrderComparator()
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