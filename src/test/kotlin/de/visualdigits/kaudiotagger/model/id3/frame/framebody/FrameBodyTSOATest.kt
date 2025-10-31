package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FrameBodyTSOATest : AbstractTestCase() {
    @Test
    fun testCreateFrameBody() {
        var fb: FrameBodyTSOA? = null
        fb = FrameBodyTSOA(
            TextEncoding.ISO_8859_1.id,
            ALBUM_SORT
        )

        assertEquals(ID3v24FrameId.ALBUM_SORT_ORDER.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(ALBUM_SORT, fb.getText())
    }

    @Test
    fun testCreateFrameBodyEmptyConstructor() {
        var fb: FrameBodyTSOA? = null
        fb = FrameBodyTSOA()
        fb.setText(ALBUM_SORT)

        assertEquals(ID3v24FrameId.ALBUM_SORT_ORDER.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(ALBUM_SORT, fb.getText())
    }

    companion object {
        const val ALBUM_SORT: String = "albumsort"

        fun getInitialisedBody(): FrameBodyTSOA {
            val fb = FrameBodyTSOA()
            fb.setText(ALBUM_SORT)
            return fb
        }
    }
}
