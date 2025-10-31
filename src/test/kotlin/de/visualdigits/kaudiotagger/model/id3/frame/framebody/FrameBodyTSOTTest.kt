package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FrameBodyTSOTTest : AbstractTestCase() {
    @Test
    fun testCreateFrameBody() {
        var fb: FrameBodyTSOT? = null
        fb = FrameBodyTSOT(TextEncoding.ISO_8859_1.id, TITLE_SORT)

        assertEquals(ID3v24FrameId.TITLE_SORT_ORDER.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(TITLE_SORT, fb.getText())
    }

    @Test
    fun testCreateFrameBodyEmptyConstructor() {
        var fb: FrameBodyTSOT? = null
        fb = FrameBodyTSOT()
        fb.setText(TITLE_SORT)

        assertEquals(ID3v24FrameId.TITLE_SORT_ORDER.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(TITLE_SORT, fb.getText())
    }

    companion object {
        const val TITLE_SORT: String = "titlesort"

        fun getInitialisedBody(): FrameBodyTSOT {
            val fb = FrameBodyTSOT()
            fb.setText(TITLE_SORT)
            return fb
        }
    }
}
