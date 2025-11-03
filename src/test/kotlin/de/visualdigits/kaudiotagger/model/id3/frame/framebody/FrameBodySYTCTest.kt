package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v24Frame
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v24Tag
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class FrameBodySYTCTest : AbstractTestCase() {

    companion object {

        fun getInitialisedBody(): FrameBodySYTC {
            val fb = FrameBodySYTC()
            fb.addTempo(0, 1)
            fb.addTempo(5, 400)
            fb.setTimestampFormat(2)
            return fb
        }

        val initialisedFrame: ID3v24Frame
            get() {
                val frame =  ID3v24Frame(ID3v24FrameId.SYNC_TEMPO.id)
                val fb = getInitialisedBody()
                frame.frameBody = fb
                return frame
            }
    }

    @Test
    @Disabled("currently not working") // todo fix this test
    fun testSaveToFile() {
        val testFile =  copyAudioToTmp("testV1.mp3")
        var mp3File =  MP3File.read(testFile)

        // Create and Save
        val tag =  ID3v24Tag()
        val referenceFrame =  initialisedFrame
        val referenceBody =
            referenceFrame.frameBody as FrameBodySYTC
        tag.setFrame(referenceFrame)
        mp3File.setTag(tag)
        mp3File.save()

        // Reload
        mp3File = MP3File.read(testFile)
        val frame =  mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v24FrameId.SYNC_TEMPO.id) as ID3v24Frame
        val body =  frame.frameBody as FrameBodySYTC
        assertEquals(referenceBody.getTimestampFormat(), body.getTimestampFormat())

        val reference =  referenceBody
            .getTempi()
            .entries
            .iterator()
        val loaded =  body
            .getTempi()
            .entries
            .iterator()
        while (reference.hasNext() && loaded.hasNext()) {
            val refEntry = reference.next()
            val loadedEntry = loaded.next()
            assertEquals(refEntry.key, loadedEntry.key)
            assertEquals(refEntry.value, loadedEntry.value)
        }
        assertFalse(reference.hasNext())
        assertFalse(loaded.hasNext())
    }

    @Test
    fun testSaveEmptyFrameToFile() {
        val testFile =  copyAudioToTmp("testV1.mp3")
        var mp3File =  MP3File.read(testFile)

        var frame =  ID3v24Frame(ID3v24FrameId.SYNC_TEMPO.id)
        val referenceBody =  FrameBodySYTC()
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
            ?.getFrame(ID3v24FrameId.SYNC_TEMPO.id) as ID3v24Frame
        val body =  frame.frameBody as FrameBodySYTC
        assertEquals(referenceBody.getTimestampFormat(), body.getTimestampFormat())
        assertEquals(referenceBody.getTempi(), body.getTempi())
    }

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
}
