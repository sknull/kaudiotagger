package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.types.ID3v23FrameId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FrameBodyIPLSTest : AbstractTestCase() {
    @Test
    fun testCreateFrameBody() {
        var fb: FrameBodyIPLS? = null
        fb = FrameBodyIPLS(
            TextEncoding.ISO_8859_1.id,
            INVOLVED_PEOPLE
        )

        assertEquals(ID3v23FrameId.INVOLVED_PEOPLE.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals("*$INVOLVED_PEOPLE*", "*" + fb.getText() + "*")
        assertEquals(2, fb.getNumberOfPairs())
        assertEquals("producer", fb.getKeyAtIndex(0))
        assertEquals("eno,lanois", fb.getValueAtIndex(0))
        assertEquals("engineer", fb.getKeyAtIndex(1))
        assertEquals("lillywhite", fb.getValueAtIndex(1))
    }

    @Test
    fun testCreateFrameBodyEmptyConstructor() {
        var fb: FrameBodyIPLS? = null
        fb = FrameBodyIPLS()
        fb.setText(INVOLVED_PEOPLE)

        assertEquals(ID3v23FrameId.INVOLVED_PEOPLE.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(
            "*" + INVOLVED_PEOPLE + "*",
            "*" + fb.getText() + "*"
        )
        assertEquals(2, fb.getNumberOfPairs())
        assertEquals("producer", fb.getKeyAtIndex(0))
        assertEquals("eno,lanois", fb.getValueAtIndex(0))
        assertEquals("engineer", fb.getKeyAtIndex(1))
        assertEquals("lillywhite", fb.getValueAtIndex(1))
    }

    @Test
    fun testCreateFromTIPL() {
        val fbv4 = FrameBodyTIPLTest.getInitialisedBody()
        var fb: FrameBodyIPLS? = null
        fb = FrameBodyIPLS(fbv4)

        assertEquals(ID3v23FrameId.INVOLVED_PEOPLE.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(
            "*" + fb.getText() + "*",
            "*" + FrameBodyTIPLTest.INVOLVED_PEOPLE + "*"
        )
        assertEquals(1, fb.getNumberOfPairs())
        assertEquals("producer", fb.getKeyAtIndex(0))
        assertEquals("eno,lanois", fb.getValueAtIndex(0))
    }

    companion object {
        const val INVOLVED_PEOPLE: String = "producer\u0000eno,lanois\u0000engineer\u0000lillywhite"


        fun getInitialisedBody(): FrameBodyIPLS {
            val fb = FrameBodyIPLS()
            fb.setText(INVOLVED_PEOPLE)
            return fb
        }
    }
}
