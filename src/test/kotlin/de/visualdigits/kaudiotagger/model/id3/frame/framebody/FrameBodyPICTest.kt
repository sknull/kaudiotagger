package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.AbstractTestCase
import de.visualdigits.kaudiotagger.model.id3.types.ID3v22FrameId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class FrameBodyPICTest : AbstractTestCase() {
    @Test
    fun testCreateFrameBody() {
        var fb: FrameBodyPIC? = null
        fb = FrameBodyPIC()

        assertEquals(ID3v22FrameId.ATTACHED_PICTURE.id, fb!!.getIdentifier())
        assertNull(fb.getDescription())
    }

    @Test
    fun testCreateFrameBodyEmptyConstructor() {
        var fb: FrameBodyPIC? = null
        fb = FrameBodyPIC()
        fb.setDescription(DESCRIPTION)

        assertEquals(ID3v22FrameId.ATTACHED_PICTURE.id, fb!!.getIdentifier())
        assertEquals(DESCRIPTION, fb.getDescription())
    }

    companion object {
        var DESCRIPTION: String = "ImageTestv22"

        fun getInitialisedBody(): FrameBodyPIC {
            val fb = FrameBodyPIC()
            fb.setDescription(DESCRIPTION)
            return fb
        }
    }
}
