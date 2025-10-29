package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class FrameBodyDeprecatedTest : AbstractTestCase() {
    @Test
    fun testCreateFrameBody() {
        var fb: FrameBodyDeprecated? = null
        fb = FrameBodyDeprecated(FrameBodyTPE1Test.getInitialisedBody())

        assertEquals(ID3v24FrameId.ARTIST.id, fb!!.getIdentifier())
        assertEquals(
            FrameBodyTPE1Test.getInitialisedBody().getBriefDescription(),
            fb.getBriefDescription()
        )
    }
}
