package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class FrameBodyTXXXTest : AbstractTestCase() {
    @Test
    fun testCreateFrameBody() {
        var fb: FrameBodyTXXX? = null
        fb = FrameBodyTXXX(
            TextEncoding.ISO_8859_1.id,
            TXXX_TEST_STRING,
            TXXX_TEST_DESC
        )

        assertEquals(ID3v24FrameId.USER_DEFINED_INFO.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(TXXX_TEST_STRING, fb.getDescription())
        assertEquals(TXXX_TEST_DESC, fb.getFirstTextValue())
    }

    companion object {
        val TXXX_TEST_DESC: String = FrameBodyTXXX.BARCODE

        const val TXXX_TEST_STRING: String = "0123456789"

        fun getInitialisedBody(): FrameBodyTXXX {
            //Text Encoding doesnt matter until written to file
            val fb = FrameBodyTXXX(
                TextEncoding.ISO_8859_1.id,
                TXXX_TEST_STRING,
                TXXX_TEST_DESC
            )
            return fb
        }
    }
}
