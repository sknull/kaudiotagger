package de.visualdigits.kaudiotagger.model.id3.tag

import de.visualdigits.kaudiotagger.model.id3.types.ID3V23FrameId

class ID3v23PreferredFrameOrderComparator private constructor(): Comparator<String> {

    companion object {

        val frameIdsInPreferredOrder: MutableList<String> = mutableListOf(
            //these are the key ones we want at the top
            ID3V23FrameId.UNIQUE_FILE_ID.id,
            ID3V23FrameId.TITLE.id,
            ID3V23FrameId.ARTIST.id,
            ID3V23FrameId.ALBUM.id,
            ID3V23FrameId.TORY.id,
            ID3V23FrameId.GENRE.id,
            ID3V23FrameId.COMPOSER.id,
            ID3V23FrameId.CONDUCTOR.id,
            ID3V23FrameId.CONTENT_GROUP_DESC.id,
            ID3V23FrameId.TRACK.id,
            ID3V23FrameId.TYER.id,
            ID3V23FrameId.TDAT.id,
            ID3V23FrameId.TIME.id,
            ID3V23FrameId.BPM.id,
            ID3V23FrameId.ISRC.id,
            ID3V23FrameId.TORY.id,
            ID3V23FrameId.ACCOMPANIMENT.id,
            ID3V23FrameId.TITLE_REFINEMENT.id,
            ID3V23FrameId.UNSYNC_LYRICS.id,
            ID3V23FrameId.USER_DEFINED_INFO.id,
            ID3V23FrameId.USER_DEFINED_URL.id,
            ID3V23FrameId.URL_ARTIST_WEB.id,
            ID3V23FrameId.URL_COMMERCIAL.id,
            ID3V23FrameId.URL_COPYRIGHT.id,
            ID3V23FrameId.URL_FILE_WEB.id,
            ID3V23FrameId.URL_OFFICIAL_RADIO.id,
            ID3V23FrameId.URL_PAYMENT.id,
            ID3V23FrameId.URL_PUBLISHERS.id,
            ID3V23FrameId.URL_COMMERCIAL.id,
            ID3V23FrameId.LYRICIST.id,
            ID3V23FrameId.MEDIA_TYPE.id,
            ID3V23FrameId.INVOLVED_PEOPLE.id,
            ID3V23FrameId.LANGUAGE.id,
            ID3V23FrameId.TITLE_SORT_ORDER_ITUNES.id,
            ID3V23FrameId.PLAYLIST_DELAY.id,
            ID3V23FrameId.PLAY_COUNTER.id,
            ID3V23FrameId.POPULARIMETER.id,
            ID3V23FrameId.PUBLISHER.id,
            ID3V23FrameId.ALBUM_ARTIST_SORT_ORDER_ITUNES.id,
            ID3V23FrameId.COMPOSER_SORT_ORDER_ITUNES.id,
            ID3V23FrameId.IS_COMPILATION.id,
            ID3V23FrameId.TITLE_SORT_ORDER_ITUNES.id,
            ID3V23FrameId.ARTIST_SORT_ORDER_ITUNES.id,
            ID3V23FrameId.ALBUM_SORT_ORDER_ITUNES.id,
            ID3V23FrameId.TITLE_SORT_ORDER_MUSICBRAINZ.id,
            ID3V23FrameId.ARTIST_SORT_ORDER_MUSICBRAINZ.id,
            ID3V23FrameId.ALBUM_SORT_ORDER_MUSICBRAINZ.id,
            ID3V23FrameId.ALBUM_ARTIST_SORT_ORDER_ITUNES.id,
            ID3V23FrameId.COMPOSER_SORT_ORDER_ITUNES.id,
            ID3V23FrameId.COMMENT.id,

            //Not so bothered about these
            ID3V23FrameId.TRDA.id,
            ID3V23FrameId.COMMERCIAL_FRAME.id,
            ID3V23FrameId.COPYRIGHTINFO.id,
            ID3V23FrameId.ENCODEDBY.id,
            ID3V23FrameId.ENCRYPTION.id,
            ID3V23FrameId.EQUALISATION.id,
            ID3V23FrameId.EVENT_TIMING_CODES.id,
            ID3V23FrameId.FILE_OWNER.id,
            ID3V23FrameId.FILE_TYPE.id,
            ID3V23FrameId.GROUP_ID_REG.id,
            ID3V23FrameId.HW_SW_SETTINGS.id,
            ID3V23FrameId.INITIAL_KEY.id,
            ID3V23FrameId.LENGTH.id,
            ID3V23FrameId.LINKED_INFO.id,
            ID3V23FrameId.TSIZ.id,
            ID3V23FrameId.MPEG_LOCATION_LOOKUP_TABLE.id,
            ID3V23FrameId.ORIGARTIST.id,
            ID3V23FrameId.ORIG_FILENAME.id,
            ID3V23FrameId.ORIG_LYRICIST.id,
            ID3V23FrameId.ORIG_TITLE.id,
            ID3V23FrameId.OWNERSHIP.id,
            ID3V23FrameId.POSITION_SYNC.id,
            ID3V23FrameId.RADIO_NAME.id,
            ID3V23FrameId.RADIO_OWNER.id,
            ID3V23FrameId.RECOMMENDED_BUFFER_SIZE.id,
            ID3V23FrameId.REMIXED.id,
            ID3V23FrameId.REVERB.id,
            ID3V23FrameId.SET.id,
            ID3V23FrameId.SYNC_LYRIC.id,
            ID3V23FrameId.SYNC_TEMPO.id,
            ID3V23FrameId.TERMS_OF_USE.id,

            //Want this near the end because can cause problems with unsyncing
            ID3V23FrameId.ATTACHED_PICTURE.id,

            //Itunes doesnt seem to like these.id, and of little use so put right at end
            ID3V23FrameId.PRIVATE.id,
            ID3V23FrameId.MUSIC_CD_ID.id,
            ID3V23FrameId.AUDIO_ENCRYPTION.id,
            ID3V23FrameId.GENERAL_ENCAPS_OBJECT.id
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