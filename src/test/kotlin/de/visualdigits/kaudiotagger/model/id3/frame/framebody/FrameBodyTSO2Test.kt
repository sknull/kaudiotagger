package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FrameBodyTSO2Test : AbstractTestCase() {
    @Test
    fun testCreateFrameBody() {
        var fb: FrameBodyTSO2? = null
        fb = FrameBodyTSO2(
            TextEncoding.ISO_8859_1.id,
            ALBUM_ARTIST_SORT
        )

        assertEquals(
            ID3v24FrameId.ALBUM_ARTIST_SORT_ORDER_ITUNES.id,
            fb!!.getIdentifier()
        )
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(ALBUM_ARTIST_SORT, fb.getText())
    }

    @Test
    fun testCreateFrameBodyEmptyConstructor() {
        var fb: FrameBodyTSO2? = null
        fb = FrameBodyTSO2()
        fb.setText(ALBUM_ARTIST_SORT)

        assertEquals(
            ID3v24FrameId.ALBUM_ARTIST_SORT_ORDER_ITUNES.id,
            fb!!.getIdentifier()
        )
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(ALBUM_ARTIST_SORT, fb.getText())
    }

    companion object {
        const val ALBUM_ARTIST_SORT: String = "albumartistsort"

        fun getInitialisedBody(): FrameBodyTSO2 {
            val fb = FrameBodyTSO2()
            fb.setText(ALBUM_ARTIST_SORT)
            return fb
        }
    }
}
