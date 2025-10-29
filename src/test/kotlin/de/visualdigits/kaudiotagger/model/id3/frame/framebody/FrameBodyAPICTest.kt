package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class FrameBodyAPICTest {

    companion object {

        var DESCRIPTION: String = "ImageTest"

        fun getInitialisedBody(): FrameBodyAPIC {
            val fb = FrameBodyAPIC()
            fb.setDescription(DESCRIPTION)
            return fb
        }
    }

    @Test
    fun testCreateFrameBody() {
        var fb: FrameBodyAPIC? = null
        fb = FrameBodyAPIC()

        assertEquals(ID3v24FrameId.ATTACHED_PICTURE.id, fb!!.getIdentifier())
        assertNull(fb.getDescription())
    }

    @Test
    fun testCreateFrameBodyEmptyConstructor() {
        var fb: FrameBodyAPIC? = null
        fb = FrameBodyAPIC()
        fb.setDescription(DESCRIPTION)

        assertEquals(ID3v24FrameId.ATTACHED_PICTURE.id, fb!!.getIdentifier())
        assertEquals(DESCRIPTION, fb.getDescription())
    }
}