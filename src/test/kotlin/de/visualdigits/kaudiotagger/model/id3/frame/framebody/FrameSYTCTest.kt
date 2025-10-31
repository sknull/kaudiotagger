package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v24Frame
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v24Tag
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class FrameSYTCTest : AbstractTestCase() {

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

    companion object {
        val initialisedFrame: ID3v24Frame
            get() {
                val frame =  ID3v24Frame(ID3v24FrameId.SYNC_TEMPO.id)
                val fb =  FrameBodySYTCTest.getInitialisedBody()
                frame.frameBody = fb
                return frame
            }
    }
}
