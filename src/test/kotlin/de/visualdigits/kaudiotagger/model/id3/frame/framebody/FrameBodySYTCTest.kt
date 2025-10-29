package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class FrameBodySYTCTest : AbstractTestCase() {
    @Test
    fun testAddTempo() {
        val body = FrameBodySYTC()
        body.addTempo(10, 0)
        body.addTempo(5, 0)
        body.addTempo(5, 1)
        body.addTempo(11, 400)
        val timingCodes = body.getTempi()

        // verify content
        assertEquals(0, timingCodes.get(10L) as Int)
        assertEquals(1, timingCodes.get(5L) as Int)
        assertEquals(400, timingCodes.get(11L) as Int)

        // verify order
        var lastTimestamp: Long = 0
        for (timestamp in timingCodes.keys) {
            assertTrue(timestamp >= lastTimestamp)
            lastTimestamp = timestamp
        }
    }

    @Test
    fun testRemoveTempo() {
        val body = FrameBodySYTC()
        body.addTempo(10, 0)
        body.addTempo(5, 0)
        body.addTempo(5, 1)
        body.addTempo(11, 400)

        body.removeTempo(5)

        val timingCodes = body.getTempi()
        assertEquals(0, timingCodes.get(10L) as Int)
        assertEquals(400, timingCodes.get(11L) as Int)
        assertNull(timingCodes.get(5L))
    }

    @Test
    fun testClearTempi() {
        val body = FrameBodySYTC()
        body.addTempo(10, 0)
        body.addTempo(5, 0)
        body.addTempo(5, 1)
        body.addTempo(11, 400)

        body.clearTempi()

        val timingCodes = body.getTempi()
        assertTrue(timingCodes.isEmpty())
    }

    companion object {

        fun getInitialisedBody(): FrameBodySYTC {
            val fb = FrameBodySYTC()
            fb.addTempo(0, 1)
            fb.addTempo(5, 400)
            fb.setTimestampFormat(2)
            return fb
        }
    }
}
