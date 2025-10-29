package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class FrameBodyPCNTTest : AbstractTestCase() {
    @Test
    fun testCreateFrameBody() {
        var fb: FrameBodyPCNT? = null
        fb = FrameBodyPCNT(PCNT_COUNTER)

        assertEquals(ID3v24FrameId.PLAY_COUNTER.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(PCNT_COUNTER, fb.getCounter())
    }

    @Test
    fun testCreateFrameBodyEmptyConstructor() {
        var fb: FrameBodyPCNT? = null
        fb = FrameBodyPCNT()
        fb.setCounter(PCNT_COUNTER)

        assertEquals(ID3v24FrameId.PLAY_COUNTER.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(PCNT_COUNTER, fb.getCounter())
    }

    companion object {
        const val PCNT_COUNTER: Long = 1000

        fun getInitialisedBody(): FrameBodyPCNT {
            val fb = FrameBodyPCNT(PCNT_COUNTER)
            return fb
        }
    }
}
