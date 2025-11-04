package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.AbstractTestCase
import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v23Frame
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v24Frame
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v23Tag
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v24Tag
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIf

class FrameBodyETCOTest : AbstractTestCase() {

    companion object {

        fun getInitialisedBody(): FrameBodyETCO {
            val fb = FrameBodyETCO()
            fb.addTimingCode(0, 1, 2)
            fb.addTimingCode(5, 1)
            fb.setTimestampFormat(2)
            return fb
        }

        fun getInitialisedFrame(): ID3v24Frame {
            val frame = ID3v24Frame(ID3v24FrameId.EVENT_TIMING_CODES.id)
            val fb = getInitialisedBody()
            frame.frameBody = fb
            return frame
        }
    }

    /**
     * This tests reading a file that contains an ETCO frame.
     *
     * @throws Exception
     */
    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    fun testReadFile() {
        val testFile = copyAudioToTmp("test20.mp3")
        val f = MP3File.read(testFile)
        val frame =
            ((f.getTag() as? ID3v23Tag)?.getFrame(
                ID3v24FrameId.EVENT_TIMING_CODES.id
            ) as ID3v23Frame)
        val body =  frame.frameBody as FrameBodyETCO
        assertEquals(2, body.getTimestampFormat())
        assertEquals(1, body.getTimingCodes().size)
        val entry =  body
            .getTimingCodes()
            .entries
            .iterator()
            .next()
        assertEquals(224, entry.value[0])
        assertEquals(56963L, entry.key as Long)
    }

    @Test
    @Disabled("currently not working") // todo fix this test
    fun testSaveToFile() {
        val testFile = copyAudioToTmp("testV1.mp3")
        var mp3File =  MP3File.read(testFile)

        // Create and Save
        val tag =  ID3v24Tag()
        val referenceFrame =  getInitialisedFrame()
        val referenceBody =
            referenceFrame.frameBody as FrameBodyETCO
        tag.setFrame(referenceFrame)
        mp3File.setTag(tag)
        mp3File.save()

        // Reload
        mp3File = MP3File.read(testFile)
        val frame =  mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v24FrameId.EVENT_TIMING_CODES.id) as ID3v24Frame
        val body =  frame.frameBody as FrameBodyETCO
        assertEquals(referenceBody.getTimestampFormat(), body.getTimestampFormat())

        val reference =  referenceBody
            .getTimingCodes()
            .entries
            .iterator()
        val loaded =  body
            .getTimingCodes()
            .entries
            .iterator()
        while (reference.hasNext() && loaded.hasNext()) {
            val refEntry = reference.next()
            val loadedEntry = loaded.next()
            assertEquals(refEntry.key, loadedEntry.key)
            assertArrayEquals(refEntry.value, loadedEntry.value)
        }
        assertFalse(reference.hasNext())
        assertFalse(loaded.hasNext())
    }

    @Test
    fun testSaveEmptyFrameToFile() {
        val testFile = copyAudioToTmp("testV1.mp3")
        var mp3File =  MP3File.read(testFile)

        var frame =  ID3v24Frame(
            ID3v24FrameId.EVENT_TIMING_CODES.id
        )
        val referenceBody =  FrameBodyETCO()
        frame.frameBody = referenceBody

        // Create and Save
        val tag =  ID3v24Tag()
        tag.setFrame(frame)
        mp3File.setTag(tag)
        mp3File.save()

        // Reload
        mp3File = MP3File.read(testFile)
        frame = mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v24FrameId.EVENT_TIMING_CODES.id) as ID3v24Frame
        val body =  frame.frameBody as FrameBodyETCO
        assertEquals(referenceBody.getTimestampFormat(), body.getTimestampFormat())
        assertEquals(referenceBody.getTimingCodes(), body.getTimingCodes())
    }

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
}
