package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.types.ID3v23FrameId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class FrameBodyXSOPTest : AbstractTestCase() {
    @Test
    fun testCreateFrameBody() {
        var fb: FrameBodyXSOP? = null
        fb = FrameBodyXSOP(
            TextEncoding.ISO_8859_1.id,
            ARTIST_SORT
        )

        assertEquals(
            ID3v23FrameId.ARTIST_SORT_ORDER_MUSICBRAINZ.id,
            fb!!.getIdentifier()
        )
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(ARTIST_SORT, fb.getText())
    }

    @Test
    fun testCreateFrameBodyEmptyConstructor() {
        var fb: FrameBodyXSOP? = null
        fb = FrameBodyXSOP()
        fb.setText(ARTIST_SORT)

        assertEquals(
            ID3v23FrameId.ARTIST_SORT_ORDER_MUSICBRAINZ.id,
            fb!!.getIdentifier()
        )
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(ARTIST_SORT, fb.getText())
    }

    companion object {
        const val ARTIST_SORT: String = "artistsort"

        fun getInitialisedBody(): FrameBodyXSOP {
            val fb = FrameBodyXSOP()
            fb.setText(ARTIST_SORT)
            return fb
        }
    }
}
