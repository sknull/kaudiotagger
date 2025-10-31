package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v23Frame
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v24Frame
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v23Tag
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v24Tag
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIf

class FrameETCOTest : AbstractTestCase() {
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

    companion object {

        fun getInitialisedFrame(): ID3v24Frame {
            val frame = ID3v24Frame(ID3v24FrameId.EVENT_TIMING_CODES.id)
            val fb = FrameBodyETCOTest.getInitialisedBody()
            frame.frameBody = fb
            return frame
        }
    }
}
