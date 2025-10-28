package de.visualdigits.kaudiotagger.model.id3.tag

import de.visualdigits.kaudiotagger.model.id3.types.ID3v23Frames

class ID3v23PreferredFrameOrderComparator private constructor(): Comparator<String> {

    companion object {

        val frameIdsInPreferredOrder: MutableList<String> = mutableListOf(
            //these are the key ones we want at the top
            ID3v23Frames.UNIQUE_FILE_ID.id,
            ID3v23Frames.TITLE.id,
            ID3v23Frames.ARTIST.id,
            ID3v23Frames.ALBUM.id,
            ID3v23Frames.TORY.id,
            ID3v23Frames.GENRE.id,
            ID3v23Frames.COMPOSER.id,
            ID3v23Frames.CONDUCTOR.id,
            ID3v23Frames.CONTENT_GROUP_DESC.id,
            ID3v23Frames.TRACK.id,
            ID3v23Frames.TYER.id,
            ID3v23Frames.TDAT.id,
            ID3v23Frames.TIME.id,
            ID3v23Frames.BPM.id,
            ID3v23Frames.ISRC.id,
            ID3v23Frames.TORY.id,
            ID3v23Frames.ACCOMPANIMENT.id,
            ID3v23Frames.TITLE_REFINEMENT.id,
            ID3v23Frames.UNSYNC_LYRICS.id,
            ID3v23Frames.USER_DEFINED_INFO.id,
            ID3v23Frames.USER_DEFINED_URL.id,
            ID3v23Frames.URL_ARTIST_WEB.id,
            ID3v23Frames.URL_COMMERCIAL.id,
            ID3v23Frames.URL_COPYRIGHT.id,
            ID3v23Frames.URL_FILE_WEB.id,
            ID3v23Frames.URL_OFFICIAL_RADIO.id,
            ID3v23Frames.URL_PAYMENT.id,
            ID3v23Frames.URL_PUBLISHERS.id,
            ID3v23Frames.URL_COMMERCIAL.id,
            ID3v23Frames.LYRICIST.id,
            ID3v23Frames.MEDIA_TYPE.id,
            ID3v23Frames.INVOLVED_PEOPLE.id,
            ID3v23Frames.LANGUAGE.id,
            ID3v23Frames.TITLE_SORT_ORDER_ITUNES.id,
            ID3v23Frames.PLAYLIST_DELAY.id,
            ID3v23Frames.PLAY_COUNTER.id,
            ID3v23Frames.POPULARIMETER.id,
            ID3v23Frames.PUBLISHER.id,
            ID3v23Frames.ALBUM_ARTIST_SORT_ORDER_ITUNES.id,
            ID3v23Frames.COMPOSER_SORT_ORDER_ITUNES.id,
            ID3v23Frames.IS_COMPILATION.id,
            ID3v23Frames.TITLE_SORT_ORDER_ITUNES.id,
            ID3v23Frames.ARTIST_SORT_ORDER_ITUNES.id,
            ID3v23Frames.ALBUM_SORT_ORDER_ITUNES.id,
            ID3v23Frames.TITLE_SORT_ORDER_MUSICBRAINZ.id,
            ID3v23Frames.ARTIST_SORT_ORDER_MUSICBRAINZ.id,
            ID3v23Frames.ALBUM_SORT_ORDER_MUSICBRAINZ.id,
            ID3v23Frames.ALBUM_ARTIST_SORT_ORDER_ITUNES.id,
            ID3v23Frames.COMPOSER_SORT_ORDER_ITUNES.id,
            ID3v23Frames.COMMENT.id,

            //Not so bothered about these
            ID3v23Frames.TRDA.id,
            ID3v23Frames.COMMERCIAL_FRAME.id,
            ID3v23Frames.COPYRIGHTINFO.id,
            ID3v23Frames.ENCODEDBY.id,
            ID3v23Frames.ENCRYPTION.id,
            ID3v23Frames.EQUALISATION.id,
            ID3v23Frames.EVENT_TIMING_CODES.id,
            ID3v23Frames.FILE_OWNER.id,
            ID3v23Frames.FILE_TYPE.id,
            ID3v23Frames.GROUP_ID_REG.id,
            ID3v23Frames.HW_SW_SETTINGS.id,
            ID3v23Frames.INITIAL_KEY.id,
            ID3v23Frames.LENGTH.id,
            ID3v23Frames.LINKED_INFO.id,
            ID3v23Frames.TSIZ.id,
            ID3v23Frames.MPEG_LOCATION_LOOKUP_TABLE.id,
            ID3v23Frames.ORIGARTIST.id,
            ID3v23Frames.ORIG_FILENAME.id,
            ID3v23Frames.ORIG_LYRICIST.id,
            ID3v23Frames.ORIG_TITLE.id,
            ID3v23Frames.OWNERSHIP.id,
            ID3v23Frames.POSITION_SYNC.id,
            ID3v23Frames.RADIO_NAME.id,
            ID3v23Frames.RADIO_OWNER.id,
            ID3v23Frames.RECOMMENDED_BUFFER_SIZE.id,
            ID3v23Frames.REMIXED.id,
            ID3v23Frames.REVERB.id,
            ID3v23Frames.SET.id,
            ID3v23Frames.SYNC_LYRIC.id,
            ID3v23Frames.SYNC_TEMPO.id,
            ID3v23Frames.TERMS_OF_USE.id,

            //Want this near the end because can cause problems with unsyncing
            ID3v23Frames.ATTACHED_PICTURE.id,

            //Itunes doesnt seem to like these.id, and of little use so put right at end
            ID3v23Frames.PRIVATE.id,
            ID3v23Frames.MUSIC_CD_ID.id,
            ID3v23Frames.AUDIO_ENCRYPTION.id,
            ID3v23Frames.GENERAL_ENCAPS_OBJECT.id
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