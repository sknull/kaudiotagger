package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class FrameBodyTSOCTest : AbstractTestCase() {
    @Test
    fun testCreateFrameBody() {
        var fb: FrameBodyTSOC? = null
        fb = FrameBodyTSOC(
            TextEncoding.ISO_8859_1.id,
            COMPOSER_SORT
        )

        assertEquals(
            ID3v24FrameId.COMPOSER_SORT_ORDER_ITUNES.id,
            fb!!.getIdentifier()
        )
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(COMPOSER_SORT, fb.getText())
    }

    @Test
    fun testCreateFrameBodyEmptyConstructor() {
        var fb: FrameBodyTSOC? = null
        fb = FrameBodyTSOC()
        fb.setText(COMPOSER_SORT)

        assertEquals(
            ID3v24FrameId.COMPOSER_SORT_ORDER_ITUNES.id,
            fb!!.getIdentifier()
        )
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(COMPOSER_SORT, fb.getText())
    }

    companion object {
        const val COMPOSER_SORT: String = "composersort"

        fun getInitialisedBody(): FrameBodyTSOC {
            val fb = FrameBodyTSOC()
            fb.setText(COMPOSER_SORT)
            return fb
        }
    }
}
