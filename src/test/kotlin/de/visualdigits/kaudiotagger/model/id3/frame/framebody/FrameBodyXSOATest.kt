package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.AbstractTestCase
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.types.ID3v23FrameId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FrameBodyXSOATest : AbstractTestCase() {
    @Test
    fun testCreateFrameBody() {
        var fb: FrameBodyXSOA? = null
        fb = FrameBodyXSOA(
            TextEncoding.ISO_8859_1.id,
            ALBUM_SORT
        )

        assertEquals(
            ID3v23FrameId.ALBUM_SORT_ORDER_MUSICBRAINZ.id,
            fb!!.getIdentifier()
        )
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(ALBUM_SORT, fb.getText())
    }

    @Test
    fun testCreateFrameBodyEmptyConstructor() {
        var fb: FrameBodyXSOA? = null
        fb = FrameBodyXSOA()
        fb.setText(ALBUM_SORT)

        assertEquals(
            ID3v23FrameId.ALBUM_SORT_ORDER_MUSICBRAINZ.id,
            fb!!.getIdentifier()
        )
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(ALBUM_SORT, fb.getText())
    }

    companion object {
        const val ALBUM_SORT: String = "albumsort"

        fun getInitialisedBody(): FrameBodyXSOA {
            val fb = FrameBodyXSOA()
            fb.setText(ALBUM_SORT)
            return fb
        }
    }
}
