package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class FrameBodyETCOTest : AbstractTestCase() {

    @Test
    fun testAddTimingCode() {
        val body = FrameBodyETCO()
        body.addTimingCode(10, 0)
        body.addTimingCode(5, 0)
        body.addTimingCode(5, 1)
        body.addTimingCode(11, 1, 2)
        val timingCodes = body.getTimingCodes()

        // verify content
        assertArrayEquals(intArrayOf(0), timingCodes.get(10L))
        assertArrayEquals(intArrayOf(0, 1), timingCodes.get(5L))
        assertArrayEquals(intArrayOf(1, 2), timingCodes.get(11L))

        // verify order
        var lastTimestamp: Long = 0
        for (timestamp in timingCodes.keys) {
            assertTrue(timestamp >= lastTimestamp)
            lastTimestamp = timestamp
        }
    }

    @Test
    fun testRemoveTimingCode() {
        val body = FrameBodyETCO()
        body.addTimingCode(10, 0)
        body.addTimingCode(5, 0)
        body.addTimingCode(5, 1)
        body.addTimingCode(11, 1, 2)

        body.removeTimingCode(5, 0)

        val timingCodes = body.getTimingCodes()
        assertArrayEquals(intArrayOf(0), timingCodes.get(10L))
        assertArrayEquals(intArrayOf(1), timingCodes.get(5L))
        assertArrayEquals(intArrayOf(1, 2), timingCodes.get(11L))
    }

    @Test
    fun testClearTimingCode() {
        val body = FrameBodyETCO()
        body.addTimingCode(10, 0)
        body.addTimingCode(5, 0)
        body.addTimingCode(5, 1)
        body.addTimingCode(11, 1, 2)

        body.clearTimingCodes()

        val timingCodes = body.getTimingCodes()
        assertTrue(timingCodes.isEmpty())
    }

    companion object {

        fun getInitialisedBody(): FrameBodyETCO {
            val fb = FrameBodyETCO()
            fb.addTimingCode(0, 1, 2)
            fb.addTimingCode(5, 1)
            fb.setTimestampFormat(2)
            return fb
        }
    }
}
