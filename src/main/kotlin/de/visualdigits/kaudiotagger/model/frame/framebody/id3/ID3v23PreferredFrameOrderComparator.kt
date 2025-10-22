package de.visualdigits.kaudiotagger.model.frame.framebody.id3

import de.visualdigits.kaudiotagger.model.kframe.ID3v23KFrame

class ID3v23PreferredFrameOrderComparator private constructor(): Comparator<String> {

    companion object {

        val frameIdsInPreferredOrder: MutableList<String> = mutableListOf(
            //these are the key ones we want at the top
            ID3v23KFrame.UNIQUE_FILE_ID.id,
            ID3v23KFrame.TITLE.id,
            ID3v23KFrame.ARTIST.id,
            ID3v23KFrame.ALBUM.id,
            ID3v23KFrame.TORY.id,
            ID3v23KFrame.GENRE.id,
            ID3v23KFrame.COMPOSER.id,
            ID3v23KFrame.CONDUCTOR.id,
            ID3v23KFrame.CONTENT_GROUP_DESC.id,
            ID3v23KFrame.TRACK.id,
            ID3v23KFrame.TYER.id,
            ID3v23KFrame.TDAT.id,
            ID3v23KFrame.TIME.id,
            ID3v23KFrame.BPM.id,
            ID3v23KFrame.ISRC.id,
            ID3v23KFrame.TORY.id,
            ID3v23KFrame.ACCOMPANIMENT.id,
            ID3v23KFrame.TITLE_REFINEMENT.id,
            ID3v23KFrame.UNSYNC_LYRICS.id,
            ID3v23KFrame.USER_DEFINED_INFO.id,
            ID3v23KFrame.USER_DEFINED_URL.id,
            ID3v23KFrame.URL_ARTIST_WEB.id,
            ID3v23KFrame.URL_COMMERCIAL.id,
            ID3v23KFrame.URL_COPYRIGHT.id,
            ID3v23KFrame.URL_FILE_WEB.id,
            ID3v23KFrame.URL_OFFICIAL_RADIO.id,
            ID3v23KFrame.URL_PAYMENT.id,
            ID3v23KFrame.URL_PUBLISHERS.id,
            ID3v23KFrame.URL_COMMERCIAL.id,
            ID3v23KFrame.LYRICIST.id,
            ID3v23KFrame.MEDIA_TYPE.id,
            ID3v23KFrame.INVOLVED_PEOPLE.id,
            ID3v23KFrame.LANGUAGE.id,
            ID3v23KFrame.TITLE_SORT_ORDER_ITUNES.id,
            ID3v23KFrame.PLAYLIST_DELAY.id,
            ID3v23KFrame.PLAY_COUNTER.id,
            ID3v23KFrame.POPULARIMETER.id,
            ID3v23KFrame.PUBLISHER.id,
            ID3v23KFrame.ALBUM_ARTIST_SORT_ORDER_ITUNES.id,
            ID3v23KFrame.COMPOSER_SORT_ORDER_ITUNES.id,
            ID3v23KFrame.IS_COMPILATION.id,
            ID3v23KFrame.TITLE_SORT_ORDER_ITUNES.id,
            ID3v23KFrame.ARTIST_SORT_ORDER_ITUNES.id,
            ID3v23KFrame.ALBUM_SORT_ORDER_ITUNES.id,
            ID3v23KFrame.TITLE_SORT_ORDER_MUSICBRAINZ.id,
            ID3v23KFrame.ARTIST_SORT_ORDER_MUSICBRAINZ.id,
            ID3v23KFrame.ALBUM_SORT_ORDER_MUSICBRAINZ.id,
            ID3v23KFrame.ALBUM_ARTIST_SORT_ORDER_ITUNES.id,
            ID3v23KFrame.COMPOSER_SORT_ORDER_ITUNES.id,
            ID3v23KFrame.COMMENT.id,

            //Not so bothered about these
            ID3v23KFrame.TRDA.id,
            ID3v23KFrame.COMMERCIAL_FRAME.id,
            ID3v23KFrame.COPYRIGHTINFO.id,
            ID3v23KFrame.ENCODEDBY.id,
            ID3v23KFrame.ENCRYPTION.id,
            ID3v23KFrame.EQUALISATION.id,
            ID3v23KFrame.EVENT_TIMING_CODES.id,
            ID3v23KFrame.FILE_OWNER.id,
            ID3v23KFrame.FILE_TYPE.id,
            ID3v23KFrame.GROUP_ID_REG.id,
            ID3v23KFrame.HW_SW_SETTINGS.id,
            ID3v23KFrame.INITIAL_KEY.id,
            ID3v23KFrame.LENGTH.id,
            ID3v23KFrame.LINKED_INFO.id,
            ID3v23KFrame.TSIZ.id,
            ID3v23KFrame.MPEG_LOCATION_LOOKUP_TABLE.id,
            ID3v23KFrame.ORIGARTIST.id,
            ID3v23KFrame.ORIG_FILENAME.id,
            ID3v23KFrame.ORIG_LYRICIST.id,
            ID3v23KFrame.ORIG_TITLE.id,
            ID3v23KFrame.OWNERSHIP.id,
            ID3v23KFrame.POSITION_SYNC.id,
            ID3v23KFrame.RADIO_NAME.id,
            ID3v23KFrame.RADIO_OWNER.id,
            ID3v23KFrame.RECOMMENDED_BUFFER_SIZE.id,
            ID3v23KFrame.REMIXED.id,
            ID3v23KFrame.REVERB.id,
            ID3v23KFrame.SET.id,
            ID3v23KFrame.SYNC_LYRIC.id,
            ID3v23KFrame.SYNC_TEMPO.id,
            ID3v23KFrame.TERMS_OF_USE.id,

            //Want this near the end because can cause problems with unsyncing
            ID3v23KFrame.ATTACHED_PICTURE.id,

            //Itunes doesnt seem to like these.id, and of little use so put right at end
            ID3v23KFrame.PRIVATE.id,
            ID3v23KFrame.MUSIC_CD_ID.id,
            ID3v23KFrame.AUDIO_ENCRYPTION.id,
            ID3v23KFrame.GENERAL_ENCAPS_OBJECT.id
        )

        val instance: ID3v23PreferredFrameOrderComparator = ID3v23PreferredFrameOrderComparator()
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