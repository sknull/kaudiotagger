package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FrameBodyWXXXTest : AbstractTestCase() {
    @Test
    fun testCreateFrameBody() {
        var fb: FrameBodyWXXX? = null
        fb = FrameBodyWXXX(
            TextEncoding.ISO_8859_1.id,
            WXXX_TEST_STRING,
            WXXX_TEST_URL
        )

        assertEquals(ID3v24FrameId.USER_DEFINED_URL.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(WXXX_TEST_STRING, fb.getDescription())
        assertEquals(WXXX_TEST_URL, fb.getUrlLink())
    }

    companion object {
        const val WXXX_TEST_URL: String = "http:// test.url.com"

        const val WXXX_TEST_STRING: String = "simple url"
        const val WXXX_UNICODE_REQUIRED_TEST_STRING: String = "\u01ff\u01ffcomplex url"

        fun getInitialisedBody(): FrameBodyWXXX {
            // Text Encoding doesnt matter until written to file
            val fb = FrameBodyWXXX(
                TextEncoding.ISO_8859_1.id,
                WXXX_TEST_STRING,
                WXXX_TEST_URL
            )
            return fb
        }

        fun getUnicodeRequiredInitialisedBody(): FrameBodyWXXX {
            // Text Encoding doesnt matter until written to file
            val fb = FrameBodyWXXX(
                TextEncoding.ISO_8859_1.id,
                WXXX_UNICODE_REQUIRED_TEST_STRING,
                WXXX_TEST_URL
            )
            return fb
        }
    }
}
