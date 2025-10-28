package de.visualdigits.kaudiotagger.model.id3.tag

import de.visualdigits.kaudiotagger.model.id3.types.ID3V23Frame

class ID3v23PreferredFrameOrderComparator private constructor(): Comparator<String> {

    companion object {

        val frameIdsInPreferredOrder: MutableList<String> = mutableListOf(
            //these are the key ones we want at the top
            ID3V23Frame.UNIQUE_FILE_ID.id,
            ID3V23Frame.TITLE.id,
            ID3V23Frame.ARTIST.id,
            ID3V23Frame.ALBUM.id,
            ID3V23Frame.TORY.id,
            ID3V23Frame.GENRE.id,
            ID3V23Frame.COMPOSER.id,
            ID3V23Frame.CONDUCTOR.id,
            ID3V23Frame.CONTENT_GROUP_DESC.id,
            ID3V23Frame.TRACK.id,
            ID3V23Frame.TYER.id,
            ID3V23Frame.TDAT.id,
            ID3V23Frame.TIME.id,
            ID3V23Frame.BPM.id,
            ID3V23Frame.ISRC.id,
            ID3V23Frame.TORY.id,
            ID3V23Frame.ACCOMPANIMENT.id,
            ID3V23Frame.TITLE_REFINEMENT.id,
            ID3V23Frame.UNSYNC_LYRICS.id,
            ID3V23Frame.USER_DEFINED_INFO.id,
            ID3V23Frame.USER_DEFINED_URL.id,
            ID3V23Frame.URL_ARTIST_WEB.id,
            ID3V23Frame.URL_COMMERCIAL.id,
            ID3V23Frame.URL_COPYRIGHT.id,
            ID3V23Frame.URL_FILE_WEB.id,
            ID3V23Frame.URL_OFFICIAL_RADIO.id,
            ID3V23Frame.URL_PAYMENT.id,
            ID3V23Frame.URL_PUBLISHERS.id,
            ID3V23Frame.URL_COMMERCIAL.id,
            ID3V23Frame.LYRICIST.id,
            ID3V23Frame.MEDIA_TYPE.id,
            ID3V23Frame.INVOLVED_PEOPLE.id,
            ID3V23Frame.LANGUAGE.id,
            ID3V23Frame.TITLE_SORT_ORDER_ITUNES.id,
            ID3V23Frame.PLAYLIST_DELAY.id,
            ID3V23Frame.PLAY_COUNTER.id,
            ID3V23Frame.POPULARIMETER.id,
            ID3V23Frame.PUBLISHER.id,
            ID3V23Frame.ALBUM_ARTIST_SORT_ORDER_ITUNES.id,
            ID3V23Frame.COMPOSER_SORT_ORDER_ITUNES.id,
            ID3V23Frame.IS_COMPILATION.id,
            ID3V23Frame.TITLE_SORT_ORDER_ITUNES.id,
            ID3V23Frame.ARTIST_SORT_ORDER_ITUNES.id,
            ID3V23Frame.ALBUM_SORT_ORDER_ITUNES.id,
            ID3V23Frame.TITLE_SORT_ORDER_MUSICBRAINZ.id,
            ID3V23Frame.ARTIST_SORT_ORDER_MUSICBRAINZ.id,
            ID3V23Frame.ALBUM_SORT_ORDER_MUSICBRAINZ.id,
            ID3V23Frame.ALBUM_ARTIST_SORT_ORDER_ITUNES.id,
            ID3V23Frame.COMPOSER_SORT_ORDER_ITUNES.id,
            ID3V23Frame.COMMENT.id,

            //Not so bothered about these
            ID3V23Frame.TRDA.id,
            ID3V23Frame.COMMERCIAL_FRAME.id,
            ID3V23Frame.COPYRIGHTINFO.id,
            ID3V23Frame.ENCODEDBY.id,
            ID3V23Frame.ENCRYPTION.id,
            ID3V23Frame.EQUALISATION.id,
            ID3V23Frame.EVENT_TIMING_CODES.id,
            ID3V23Frame.FILE_OWNER.id,
            ID3V23Frame.FILE_TYPE.id,
            ID3V23Frame.GROUP_ID_REG.id,
            ID3V23Frame.HW_SW_SETTINGS.id,
            ID3V23Frame.INITIAL_KEY.id,
            ID3V23Frame.LENGTH.id,
            ID3V23Frame.LINKED_INFO.id,
            ID3V23Frame.TSIZ.id,
            ID3V23Frame.MPEG_LOCATION_LOOKUP_TABLE.id,
            ID3V23Frame.ORIGARTIST.id,
            ID3V23Frame.ORIG_FILENAME.id,
            ID3V23Frame.ORIG_LYRICIST.id,
            ID3V23Frame.ORIG_TITLE.id,
            ID3V23Frame.OWNERSHIP.id,
            ID3V23Frame.POSITION_SYNC.id,
            ID3V23Frame.RADIO_NAME.id,
            ID3V23Frame.RADIO_OWNER.id,
            ID3V23Frame.RECOMMENDED_BUFFER_SIZE.id,
            ID3V23Frame.REMIXED.id,
            ID3V23Frame.REVERB.id,
            ID3V23Frame.SET.id,
            ID3V23Frame.SYNC_LYRIC.id,
            ID3V23Frame.SYNC_TEMPO.id,
            ID3V23Frame.TERMS_OF_USE.id,

            //Want this near the end because can cause problems with unsyncing
            ID3V23Frame.ATTACHED_PICTURE.id,

            //Itunes doesnt seem to like these.id, and of little use so put right at end
            ID3V23Frame.PRIVATE.id,
            ID3V23Frame.MUSIC_CD_ID.id,
            ID3V23Frame.AUDIO_ENCRYPTION.id,
            ID3V23Frame.GENERAL_ENCAPS_OBJECT.id
        )

        val instance: ID3v23PreferredFrameOrderComparator = ID3v23PreferredFrameOrderComparator()
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