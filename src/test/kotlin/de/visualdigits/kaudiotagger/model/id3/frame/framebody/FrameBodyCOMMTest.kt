package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v24Frame
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class FrameBodyCOMMTest : AbstractTestCase() {

    /**
     * Should run without throwing Runtime excception, although COMMFrame wont be loaded and will
     * throwe invalid size exception
     */
    @Test
    fun testReadFileContainingInvalidSizeCOMMFrame() {
        var e: Exception? = null
        try {
            val testFile =  prependAudioToTmp(
                "Issue77.id3",
                "testV1.mp3"
            )
            MP3File.read(testFile)
        } catch (ie: java.lang.Exception) {
            e = ie
        }
        assertNull(e)
    }

    /**
     * Should run without throwing Runtime excception, although COMMFrame wont be loaded and will
     * throwe invalid datatype exception
     */
    @Test
    fun testReadFileContainingInvalidTextEncodingCOMMFrame() {
        var e: Exception? = null
        try {
            val testFile =  prependAudioToTmp(
                "Issue80.id3",
                "testV1.mp3"
            )
            MP3File.read(testFile)
        } catch (ie: java.lang.Exception) {
            e = ie
        }
        assertNull(e)
    }

    /**
     * Can read file containing a language code that does not actually map to a code , and write it back
     * In this real example the language code has been held as three space characters
     */
    @Test
    fun testreadFrameContainingInvalidlanguageCodeCOMMFrame() {
        val INVALID_LANG_CODE = "   "
        var e: Exception? = null
        try {
            val testFile =  prependAudioToTmp(
                "Issue108.id3",
                "testV1.mp3"
            )
            val mp3File =  MP3File.read(testFile)

            assertTrue(mp3File.getID3v2Tag()?.hasField("COMM") == true)

            val commFrame =  mp3File
                .getID3v2Tag()
                ?.getFrame("COMM") as ID3v24Frame
            val frameBody =  commFrame.frameBody as? FrameBodyCOMM

            assertEquals(INVALID_LANG_CODE, frameBody?.getLanguage())
        } catch (ie: java.lang.Exception) {
            e = ie
        }
        assertNull(e)
    }

    /**
     * Can write file containing a COMM Frame with null language code
     */
    @Test
    fun testsaveFileContainingNullLanguageCodeCOMMFrame() {
        val SAFE_LANG_CODE = "   "
        val SAFE_LONGER_LANG_CODE = "aa "
        val SAFE_SHORTER_LANG_CODE = "aaa"
        var e: Exception? = null
        try {
            // Read tag
            val testFile =  prependAudioToTmp(
                "Issue108.id3",
                "testV1.mp3"
            )
            var mp3File =  MP3File.read(testFile)
            var commFrame =  mp3File
                .getID3v2Tag()
                ?.getFrame("COMM") as ID3v24Frame
            var frameBody =  commFrame.frameBody as? FrameBodyCOMM

            // Set language to null, this is common problem for new frames might null lang codes
            frameBody?.setLanguage(null)
            mp3File.save()
            mp3File = MP3File.read(testFile)
            commFrame = mp3File.getID3v2Tag()?.getFrame("COMM") as ID3v24Frame
            frameBody = commFrame.frameBody as? FrameBodyCOMM
            assertEquals(SAFE_LANG_CODE, frameBody?.getLanguage())

            // Set language to too short a value
            frameBody?.setLanguage("aa")
            mp3File.save()
            mp3File = MP3File.read(testFile)
            commFrame = mp3File.getID3v2Tag()?.getFrame("COMM") as ID3v24Frame
            frameBody = commFrame.frameBody as FrameBodyCOMM
            assertEquals(SAFE_LONGER_LANG_CODE, frameBody.getLanguage())

            // Set language to too long a value
            frameBody.setLanguage("aaaaaaa")
            mp3File.save()
            mp3File = MP3File.read(testFile)
            commFrame = mp3File.getID3v2Tag()?.getFrame("COMM") as ID3v24Frame
            frameBody = commFrame.frameBody as FrameBodyCOMM
            assertEquals(SAFE_SHORTER_LANG_CODE, frameBody.getLanguage())
        } catch (ie: java.lang.Exception) {
            e = ie
        }
        assertNull(e)
    }
}
