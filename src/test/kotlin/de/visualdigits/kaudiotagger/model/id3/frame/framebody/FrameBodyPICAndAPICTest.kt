package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.AbstractTestCase
import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v22Frame
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v23Frame
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v24Frame
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v22Tag
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v23Tag
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v24Tag
import de.visualdigits.kaudiotagger.model.id3.types.ID3v22FrameId
import de.visualdigits.kaudiotagger.model.id3.types.ID3v23FrameId
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class FrameBodyPICAndAPICTest : AbstractTestCase() {

    @Test
    fun testCreateID3v24Frame() {
        val frame = ID3v24Frame(ID3v24FrameId.ATTACHED_PICTURE.id)
        val fb = FrameBodyAPICTest.getInitialisedBody()
        frame.frameBody = fb

        Assertions.assertEquals(ID3v24FrameId.ATTACHED_PICTURE.id, frame.getIdentifier())
        Assertions.assertFalse(
            ID3v24FrameId.isExtension(frame.getIdentifier())
        )
        Assertions.assertTrue(
            ID3v24FrameId.isSupported(frame.getIdentifier())
        )
        Assertions.assertEquals(FrameBodyAPICTest.DESCRIPTION, fb.getDescription())
    }

    @Test
    fun testCreateID3v23Frame() {
        val frame = ID3v23Frame(ID3v23FrameId.ATTACHED_PICTURE.id)
        val fb = FrameBodyAPICTest.getInitialisedBody()
        frame.frameBody = fb

        Assertions.assertEquals(
            ID3v23FrameId.ATTACHED_PICTURE.id,
            frame.getIdentifier()
        )
        Assertions.assertFalse(
            ID3v23FrameId.isExtension(frame.getIdentifier())
        )
        Assertions.assertTrue(
            ID3v23FrameId.isSupported(frame.getIdentifier())
        )
        Assertions.assertEquals(FrameBodyAPICTest.DESCRIPTION, fb.getDescription())
    }

    @Test
    fun testCreateID3v22Frame() {
        val frame = ID3v22Frame(ID3v22FrameId.ATTACHED_PICTURE.id)
        val fb = FrameBodyPICTest.getInitialisedBody()
        frame.frameBody = fb

        Assertions.assertEquals(
            ID3v22FrameId.ATTACHED_PICTURE.id,
            frame.getIdentifier()
        )
        Assertions.assertFalse(
            ID3v22FrameId.isExtension(frame.getIdentifier())
        )
        Assertions.assertTrue(
            ID3v22FrameId.isSupported(frame.getIdentifier())
        )
        Assertions.assertEquals(FrameBodyPICTest.DESCRIPTION, fb.getDescription())
    }

    @Test
    fun testSaveToFile() {
        val testFile =  copyAudioToTmp("testV1.mp3")
        var mp3File =  MP3File.read(testFile)

        // Create and Save
        val tag = ID3v24Tag()
        tag.setFrame(v24InitialisedFrame)
        mp3File.setTag(tag)
        mp3File.save()

        // Reload
        mp3File = MP3File.read(testFile)
        val frame =  mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v24FrameId.ATTACHED_PICTURE.id) as ID3v24Frame
        Assertions.assertNotNull(frame)
        val body =  frame.frameBody as FrameBodyAPIC
        Assertions.assertInstanceOf<FrameBodyAPIC>(FrameBodyAPIC::class.java, body)
        Assertions.assertEquals(FrameBodyAPICTest.DESCRIPTION, body.getDescription())
    }

    @Test
    fun testConvertV24ToV23() {
        val testFile =  copyAudioToTmp("testV1.mp3")
        var mp3File =  MP3File.read(testFile)

        // Create and Save
        val tag = ID3v24Tag()
        tag.setFrame(v24InitialisedFrame)

        mp3File.setTag(tag)
        mp3File.save()

        // Reload and convert to v23 and save
        mp3File = MP3File.read(testFile)
        mp3File.setTag(ID3v23Tag(mp3File.getID3v2TagAsv24()))
        mp3File.save()

        // Reload
        mp3File = MP3File.read(testFile)
        val frame =  mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v23FrameId.ATTACHED_PICTURE.id) as ID3v23Frame
        Assertions.assertNotNull(frame)
        val body =  frame.frameBody as FrameBodyAPIC
        Assertions.assertInstanceOf<FrameBodyAPIC>(FrameBodyAPIC::class.java, body)
        Assertions.assertEquals(FrameBodyAPICTest.DESCRIPTION, body.getDescription())
    }

    @Test
    fun testConvertV24ToV22() {
        val testFile =  copyAudioToTmp("testV1.mp3")
        var mp3File =  MP3File.read(testFile)

        // Create and Save
        val tag = ID3v24Tag()
        tag.setFrame(v24InitialisedFrame)

        mp3File.setTag(tag)
        mp3File.save()

        // Reload and convert to v22 and save
        mp3File = MP3File.read(testFile)
        mp3File.setTag(ID3v22Tag(mp3File.getID3v2TagAsv24()))
        mp3File.save()

        // Reload
        mp3File = MP3File.read(testFile)
        val frame =  mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v22FrameId.ATTACHED_PICTURE.id) as ID3v22Frame
        Assertions.assertNotNull(frame)
        val body =  frame.frameBody as FrameBodyPIC
        Assertions.assertInstanceOf<FrameBodyPIC>(FrameBodyPIC::class.java, body)
        Assertions.assertEquals(FrameBodyAPICTest.DESCRIPTION, body.getDescription())
    }

    @Test
    fun testConvertV22ToV24() {
        val testFile =  copyAudioToTmp("testV1.mp3")
        var mp3File =  MP3File.read(testFile)

        // Create and Save
        val tag = ID3v22Tag()

        //..Notes (uses v22Frame but frame body will be the v23/24 version)
        tag.setFrame(v22InitialisedFrame)

        mp3File.setTag(tag)
        mp3File.save()

        // Reload and convert from v22 to v24 and save
        mp3File = MP3File.read(testFile)
        mp3File.setTag(ID3v24Tag(mp3File.getID3v2Tag()))
        mp3File.save()

        // Reload
        mp3File = MP3File.read(testFile)
        val frame =  mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v24FrameId.ATTACHED_PICTURE.id) as ID3v24Frame
        Assertions.assertNotNull(frame)
        val body =  frame.frameBody as FrameBodyAPIC
        Assertions.assertInstanceOf<FrameBodyAPIC>(FrameBodyAPIC::class.java, body)
        Assertions.assertEquals(FrameBodyPICTest.DESCRIPTION, body.getDescription())
    }

    companion object {

        fun cmp(a: ByteArray, b: ByteArray): String? {
            if (a.size != b.size) {
                return ("length of byte arrays differ (" + a.size + "!=" + b.size + ")"
                        )
            }
            for (i in a.indices) {
                if (a[i] != b[i]) {
                    return ("byte arrays differ at offset " + i + " (" + a[i] + "!=" + b[i] + ")"
                            )
                }
            }
            return null
        }

        val v23InitialisedFrame: ID3v23Frame
            get() {
                val frame = ID3v23Frame(
                    ID3v23FrameId.ATTACHED_PICTURE.id
                )
                val fb =  FrameBodyAPICTest.getInitialisedBody()
                frame.frameBody = fb
                return frame
            }

        val v24InitialisedFrame: ID3v24Frame
            get() {
                val frame = ID3v24Frame(ID3v24FrameId.ATTACHED_PICTURE.id)
                val fb =  FrameBodyAPICTest.getInitialisedBody()
                frame.frameBody = fb
                return frame
            }

        val v22InitialisedFrame: ID3v22Frame
            get() {
                val frame = ID3v22Frame(
                    ID3v22FrameId.ATTACHED_PICTURE.id
                )
                val fb =  FrameBodyPICTest.getInitialisedBody()
                frame.frameBody = fb
                return frame
            }
    }
}