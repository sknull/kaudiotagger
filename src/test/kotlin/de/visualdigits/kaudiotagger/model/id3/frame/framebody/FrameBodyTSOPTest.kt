package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class FrameBodyTSOPTest : AbstractTestCase() {
    @Test
    fun testCreateFrameBody() {
        var fb: FrameBodyTSOP? = null
        fb = FrameBodyTSOP(
            TextEncoding.ISO_8859_1.id,
            ARTIST_SORT
        )

        assertEquals(ID3v24FrameId.ARTIST_SORT_ORDER.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(ARTIST_SORT, fb.getText())
    }

    @Test
    fun testCreateFrameBodyEmptyConstructor() {
        var fb: FrameBodyTSOP? = null
        fb = FrameBodyTSOP()
        fb.setText(ARTIST_SORT)

        assertEquals(ID3v24FrameId.ARTIST_SORT_ORDER.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(ARTIST_SORT, fb.getText())
    }

    companion object {
        const val ARTIST_SORT: String = "artistsort"

        fun getInitialisedBody(): FrameBodyTSOP {
            val fb = FrameBodyTSOP()
            fb.setText(ARTIST_SORT)
            return fb
        }
    }
}
