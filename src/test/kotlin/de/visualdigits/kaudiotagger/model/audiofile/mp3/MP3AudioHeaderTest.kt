/*
 * Jaudiotagger Copyright (C)2004,2005
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public  License as published by the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not,
 * you can getFields a copy from http://www.opensource.org/licenses/lgpl-license.php or write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package de.visualdigits.kaudiotagger.model.audiofile.mp3

import de.visualdigits.kaudiotagger.model.audiofile.header.mp3.MP3AudioHeader
import de.visualdigits.kaudiotagger.model.audiofile.header.mp3.MPEGFrameHeader
import de.visualdigits.kaudiotagger.model.common.types.GenericFieldKey
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIf
import java.text.SimpleDateFormat
import java.util.Date

class MP3AudioHeaderTest : AbstractTestCase() {
    @Test
    fun testReadV1L3VbrOld() {
        var exceptionCaught: Exception? = null
        val testFile = copyAudioToTmp("testV1vbrOld0.mp3")
        var mp3AudioHeader: MP3AudioHeader? = null
        try {
            mp3AudioHeader = MP3File.read(testFile).getMP3AudioHeader()
        } catch (e: Exception) {
            exceptionCaught = e
        }
        assertNull(exceptionCaught)
        assertEquals("44100", mp3AudioHeader?.getSampleRate())
        assertEquals("00:14", mp3AudioHeader?.getTrackLengthAsString())
        assertTrue(mp3AudioHeader?.isVariableBitRate() == true)
        assertEquals(
            MPEGFrameHeader.mpegVersionMap.get(
                Integer.valueOf(MPEGFrameHeader.VERSION_1)
            ),
            mp3AudioHeader?.getMpegVersion()
        )
        assertEquals(
            MPEGFrameHeader.mpegLayerMap.get(
                Integer.valueOf(MPEGFrameHeader.LAYER_III)
            ),
            mp3AudioHeader?.getMpegLayer()
        )
        assertEquals(
            MPEGFrameHeader.modeMap.get(Integer.valueOf(MPEGFrameHeader.MODE_MONO)),
            mp3AudioHeader?.getChannels()
        )
        assertTrue(mp3AudioHeader?.isOriginal() == true)
        assertFalse(mp3AudioHeader?.isCopyrighted() == true)
        assertFalse(mp3AudioHeader?.isPrivate() == true)
        assertFalse(mp3AudioHeader?.isProtected() == true)
        assertEquals("~127", mp3AudioHeader?.getBitRate())
        assertEquals("mp3", mp3AudioHeader?.getEncodingType())
        assertEquals("LAME3.96r", mp3AudioHeader?.encoder)
    }

    @Test
    fun testReadV1L3VbrNew() {
        var exceptionCaught: Exception? = null
        val testFile = copyAudioToTmp("testV1vbrNew0.mp3")
        var mp3AudioHeader: MP3AudioHeader? = null
        try {
            mp3AudioHeader = MP3File.read(testFile).getMP3AudioHeader()
        } catch (e: Exception) {
            exceptionCaught = e
        }
        assertNull(exceptionCaught)
        assertEquals("44100", mp3AudioHeader?.getSampleRate())
        assertEquals("00:14", mp3AudioHeader?.getTrackLengthAsString())
        assertTrue(mp3AudioHeader?.isVariableBitRate() == true)
        assertEquals(
            MPEGFrameHeader.mpegVersionMap.get(
                Integer.valueOf(MPEGFrameHeader.VERSION_1)
            ),
            mp3AudioHeader?.getMpegVersion()
        )
        assertEquals(
            MPEGFrameHeader.mpegLayerMap.get(
                Integer.valueOf(MPEGFrameHeader.LAYER_III)
            ),
            mp3AudioHeader?.getMpegLayer()
        )
        assertEquals(
            MPEGFrameHeader.modeMap.get(Integer.valueOf(MPEGFrameHeader.MODE_MONO)),
            mp3AudioHeader?.getChannels()
        )
        assertTrue(mp3AudioHeader?.isOriginal() == true)
        assertFalse(mp3AudioHeader?.isCopyrighted() == true)
        assertFalse(mp3AudioHeader?.isPrivate() == true)
        assertFalse(mp3AudioHeader?.isProtected() == true)
        assertEquals("~127", mp3AudioHeader?.getBitRate())
        assertEquals("mp3", mp3AudioHeader?.getEncodingType())
        assertEquals("LAME3.96r", mp3AudioHeader?.encoder)
    }

    @Test
    fun testReadV1L3Cbr128() {
        var exceptionCaught: Exception? = null
        val testFile = copyAudioToTmp("testV1Cbr128.mp3")
        var mp3AudioHeader: MP3AudioHeader? = null
        try {
            mp3AudioHeader = MP3File.read(testFile).getMP3AudioHeader()
        } catch (e: Exception) {
            exceptionCaught = e
        }
        assertNull(exceptionCaught)
        assertEquals("44100", mp3AudioHeader?.getSampleRate())
        assertEquals("00:14", mp3AudioHeader?.getTrackLengthAsString())
        assertFalse(mp3AudioHeader?.isVariableBitRate() == true)
        assertEquals(
            MPEGFrameHeader.mpegVersionMap.get(
                Integer.valueOf(MPEGFrameHeader.VERSION_1)
            ),
            mp3AudioHeader?.getMpegVersion()
        )
        assertEquals(
            MPEGFrameHeader.mpegLayerMap.get(
                Integer.valueOf(MPEGFrameHeader.LAYER_III)
            ),
            mp3AudioHeader?.getMpegLayer()
        )
        assertEquals(
            MPEGFrameHeader.modeMap.get(Integer.valueOf(MPEGFrameHeader.MODE_MONO)),
            mp3AudioHeader?.getChannels()
        )
        assertTrue(mp3AudioHeader?.isOriginal() == true)
        assertFalse(mp3AudioHeader?.isCopyrighted() == true)
        assertFalse(mp3AudioHeader?.isPrivate() == true)
        assertFalse(mp3AudioHeader?.isProtected() == true)
        assertEquals("128", mp3AudioHeader?.getBitRate())
        assertEquals("mp3", mp3AudioHeader?.getEncodingType())
        assertEquals("LAME3.96r", mp3AudioHeader?.encoder)
    }

    @Test
    fun testReadV1L3Cbr192() {
        var exceptionCaught: Exception? = null
        val testFile = copyAudioToTmp("testV1Cbr192.mp3")
        var mp3AudioHeader: MP3AudioHeader? = null
        try {
            mp3AudioHeader = MP3File.read(testFile).getMP3AudioHeader()
        } catch (e: Exception) {
            exceptionCaught = e
        }
        assertNull(exceptionCaught)
        assertEquals("44100", mp3AudioHeader?.getSampleRate())
        assertEquals("00:14", mp3AudioHeader?.getTrackLengthAsString())
        assertFalse(mp3AudioHeader?.isVariableBitRate() == true)
        assertEquals(
            MPEGFrameHeader.mpegVersionMap.get(
                Integer.valueOf(MPEGFrameHeader.VERSION_1)
            ),
            mp3AudioHeader?.getMpegVersion()
        )
        assertEquals(
            MPEGFrameHeader.mpegLayerMap.get(
                Integer.valueOf(MPEGFrameHeader.LAYER_III)
            ),
            mp3AudioHeader?.getMpegLayer()
        )
        assertEquals(
            MPEGFrameHeader.modeMap.get(Integer.valueOf(MPEGFrameHeader.MODE_MONO)),
            mp3AudioHeader?.getChannels()
        )
        assertTrue(mp3AudioHeader?.isOriginal() == true)
        assertFalse(mp3AudioHeader?.isCopyrighted() == true)
        assertFalse(mp3AudioHeader?.isPrivate() == true)
        assertFalse(mp3AudioHeader?.isProtected() == true)
        assertEquals("192", mp3AudioHeader?.getBitRate())
        assertEquals("mp3", mp3AudioHeader?.getEncodingType())
        assertEquals("LAME3.96r", mp3AudioHeader?.encoder)
    }

    @Test
    fun testReadV2L3VbrOld() {
        var exceptionCaught: Exception? = null
        val testFile = copyAudioToTmp("testV2vbrOld0.mp3")
        var mp3AudioHeader: MP3AudioHeader? = null
        try {
            mp3AudioHeader = MP3File.read(testFile).getMP3AudioHeader()
        } catch (e: Exception) {
            exceptionCaught = e
        }
        assertNull(exceptionCaught)
        assertEquals("22050", mp3AudioHeader?.getSampleRate())
        assertEquals("00:14", mp3AudioHeader?.getTrackLengthAsString())
        assertTrue(mp3AudioHeader?.isVariableBitRate() == true)
        assertEquals(
            MPEGFrameHeader.mpegVersionMap.get(
                Integer.valueOf(MPEGFrameHeader.VERSION_2)
            ),
            mp3AudioHeader?.getMpegVersion()
        )
        assertEquals(
            MPEGFrameHeader.mpegLayerMap.get(
                Integer.valueOf(MPEGFrameHeader.LAYER_III)
            ),
            mp3AudioHeader?.getMpegLayer()
        )
        assertEquals(
            MPEGFrameHeader.modeMap.get(Integer.valueOf(MPEGFrameHeader.MODE_MONO)),
            mp3AudioHeader?.getChannels()
        )
        assertTrue(mp3AudioHeader?.isOriginal() == true)
        assertFalse(mp3AudioHeader?.isCopyrighted() == true)
        assertFalse(mp3AudioHeader?.isPrivate() == true)
        assertFalse(mp3AudioHeader?.isProtected() == true)
        assertEquals("~127", mp3AudioHeader?.getBitRate())
        assertEquals("mp3", mp3AudioHeader?.getEncodingType())
        assertEquals("LAME3.96r", mp3AudioHeader?.encoder)
    }

    @Test
    fun testReadV2L3MonoVbrNew() {
        var exceptionCaught: Exception? = null
        val testFile = copyAudioToTmp("testV2vbrNew0.mp3")
        var mp3AudioHeader: MP3AudioHeader? = null
        try {
            mp3AudioHeader = MP3File.read(testFile).getMP3AudioHeader()
        } catch (e: Exception) {
            exceptionCaught = e
        }
        assertNull(exceptionCaught)
        assertEquals("22050", mp3AudioHeader?.getSampleRate())
        assertEquals("00:14", mp3AudioHeader?.getTrackLengthAsString())
        assertTrue(mp3AudioHeader?.isVariableBitRate() == true)
        assertEquals(
            MPEGFrameHeader.mpegVersionMap.get(
                Integer.valueOf(MPEGFrameHeader.VERSION_2)
            ),
            mp3AudioHeader?.getMpegVersion()
        )
        assertEquals(
            MPEGFrameHeader.mpegLayerMap.get(
                Integer.valueOf(MPEGFrameHeader.LAYER_III)
            ),
            mp3AudioHeader?.getMpegLayer()
        )
        assertEquals(
            MPEGFrameHeader.modeMap.get(Integer.valueOf(MPEGFrameHeader.MODE_MONO)),
            mp3AudioHeader?.getChannels()
        )
        assertTrue(mp3AudioHeader?.isOriginal() == true)
        assertFalse(mp3AudioHeader?.isCopyrighted() == true)
        assertFalse(mp3AudioHeader?.isPrivate() == true)
        assertFalse(mp3AudioHeader?.isProtected() == true)
        assertEquals("~127", mp3AudioHeader?.getBitRate())
        assertEquals("mp3", mp3AudioHeader?.getEncodingType())
        assertEquals("LAME3.96r", mp3AudioHeader?.encoder)
    }

    @Test
    fun testReadV1L2Stereo() {
        var exceptionCaught: Exception? = null
        val testFile = copyAudioToTmp("testV1L2stereo.mp3")
        var mp3AudioHeader: MP3AudioHeader? = null
        try {
            mp3AudioHeader = MP3File.read(testFile).getMP3AudioHeader()
        } catch (e: Exception) {
            exceptionCaught = e
        }
        assertNull(exceptionCaught)
        assertEquals("44100", mp3AudioHeader?.getSampleRate())
        //assertEquals("00:13", mp3AudioHeader?.getTrackLengthAsString()); Incorrectly returning 6
        assertFalse(mp3AudioHeader?.isVariableBitRate() == true)
        assertEquals(
            MPEGFrameHeader.mpegVersionMap.get(
                Integer.valueOf(MPEGFrameHeader.VERSION_1)
            ),
            mp3AudioHeader?.getMpegVersion()
        )
        assertEquals(
            MPEGFrameHeader.mpegLayerMap.get(
                Integer.valueOf(MPEGFrameHeader.LAYER_II)
            ),
            mp3AudioHeader?.getMpegLayer()
        )
        assertEquals(
            MPEGFrameHeader.modeMap.get(Integer.valueOf(MPEGFrameHeader.MODE_STEREO)),
            mp3AudioHeader?.getChannels()
        )
        assertFalse(mp3AudioHeader?.isOriginal() == true)
        assertFalse(mp3AudioHeader?.isCopyrighted() == true)
        assertFalse(mp3AudioHeader?.isPrivate() == true)
        assertFalse(mp3AudioHeader?.isProtected() == true)
        assertEquals("192", mp3AudioHeader?.getBitRate())
        assertEquals("mp3", mp3AudioHeader?.getEncodingType())
        assertEquals("", mp3AudioHeader?.encoder)
    }

    @Test
    fun testReadV1L2Mono() {
        var exceptionCaught: Exception? = null
        val testFile = copyAudioToTmp("testV1L2mono.mp3")
        var mp3AudioHeader: MP3AudioHeader? = null
        try {
            mp3AudioHeader = MP3File.read(testFile).getMP3AudioHeader()
        } catch (e: Exception) {
            exceptionCaught = e
        }
        assertNull(exceptionCaught)
        assertEquals("44100", mp3AudioHeader?.getSampleRate())
        assertEquals("00:13", mp3AudioHeader?.getTrackLengthAsString())
        assertFalse(mp3AudioHeader?.isVariableBitRate() == true)
        assertEquals(
            MPEGFrameHeader.mpegVersionMap.get(
                Integer.valueOf(MPEGFrameHeader.VERSION_1)
            ),
            mp3AudioHeader?.getMpegVersion()
        )
        assertEquals(
            MPEGFrameHeader.mpegLayerMap.get(
                Integer.valueOf(MPEGFrameHeader.LAYER_II)
            ),
            mp3AudioHeader?.getMpegLayer()
        )
        assertEquals(
            MPEGFrameHeader.modeMap.get(Integer.valueOf(MPEGFrameHeader.MODE_MONO)),
            mp3AudioHeader?.getChannels()
        )
        assertFalse(mp3AudioHeader?.isOriginal() == true)
        assertFalse(mp3AudioHeader?.isCopyrighted() == true)
        assertFalse(mp3AudioHeader?.isPrivate() == true)
        assertFalse(mp3AudioHeader?.isProtected() == true)
        assertEquals("192", mp3AudioHeader?.getBitRate())
        assertEquals("mp3", mp3AudioHeader?.getEncodingType())
        assertEquals("", mp3AudioHeader?.encoder)
    }

    @Test
    fun testReadV25L3VbrOld() {
        var exceptionCaught: Exception? = null
        val testFile = copyAudioToTmp("testV25vbrOld0.mp3")
        var mp3AudioHeader: MP3AudioHeader? = null
        try {
            mp3AudioHeader = MP3File.read(testFile).getMP3AudioHeader()
        } catch (e: Exception) {
            exceptionCaught = e
        }
        assertNull(exceptionCaught)
        assertEquals("12000", mp3AudioHeader?.getSampleRate())
        assertEquals("00:14", mp3AudioHeader?.getTrackLengthAsString())
        assertTrue(mp3AudioHeader?.isVariableBitRate() == true)
        assertEquals(
            MPEGFrameHeader.mpegVersionMap.get(
                Integer.valueOf(MPEGFrameHeader.VERSION_2_5)
            ),
            mp3AudioHeader?.getMpegVersion()
        )
        assertEquals(
            MPEGFrameHeader.mpegLayerMap.get(
                Integer.valueOf(MPEGFrameHeader.LAYER_III)
            ),
            mp3AudioHeader?.getMpegLayer()
        )
        assertEquals(
            MPEGFrameHeader.modeMap.get(Integer.valueOf(MPEGFrameHeader.MODE_MONO)),
            mp3AudioHeader?.getChannels()
        )
        assertTrue(mp3AudioHeader?.isOriginal() == true)
        assertFalse(mp3AudioHeader?.isCopyrighted() == true)
        assertFalse(mp3AudioHeader?.isPrivate() == true)
        assertFalse(mp3AudioHeader?.isProtected() == true)
        assertEquals("~128", mp3AudioHeader?.getBitRate())
        assertEquals("mp3", mp3AudioHeader?.getEncodingType())
        assertEquals("LAME3.96r", mp3AudioHeader?.encoder)
    }

    @Test
    fun testReadV25L3() {
        var exceptionCaught: Exception? = null
        val testFile = copyAudioToTmp("testV25.mp3")
        var mp3AudioHeader: MP3AudioHeader? = null
        try {
            mp3AudioHeader = MP3File.read(testFile).getMP3AudioHeader()
        } catch (e: Exception) {
            exceptionCaught = e
        }
        assertNull(exceptionCaught)
        assertEquals("12000", mp3AudioHeader?.getSampleRate())
        assertEquals("00:14", mp3AudioHeader?.getTrackLengthAsString())
        assertFalse(mp3AudioHeader?.isVariableBitRate() == true)
        assertEquals(
            MPEGFrameHeader.mpegVersionMap.get(
                Integer.valueOf(MPEGFrameHeader.VERSION_2_5)
            ),
            mp3AudioHeader?.getMpegVersion()
        )
        assertEquals(
            MPEGFrameHeader.mpegLayerMap.get(
                Integer.valueOf(MPEGFrameHeader.LAYER_III)
            ),
            mp3AudioHeader?.getMpegLayer()
        )
        assertEquals(
            MPEGFrameHeader.modeMap.get(Integer.valueOf(MPEGFrameHeader.MODE_MONO)),
            mp3AudioHeader?.getChannels()
        )
        assertTrue(mp3AudioHeader?.isOriginal() == true)
        assertFalse(mp3AudioHeader?.isCopyrighted() == true)
        assertFalse(mp3AudioHeader?.isPrivate() == true)
        assertFalse(mp3AudioHeader?.isProtected() == true)
        assertEquals("16", mp3AudioHeader?.getBitRate())
        assertEquals("mp3", mp3AudioHeader?.getEncodingType())
        assertEquals("", mp3AudioHeader?.encoder) //No Lame header so blank
    }

    @Test
    fun testReadV25L3VbrNew() {
        var exceptionCaught: Exception? = null
        val testFile = copyAudioToTmp("testV25vbrNew0.mp3")
        var mp3AudioHeader: MP3AudioHeader? = null
        try {
            mp3AudioHeader = MP3File.read(testFile).getMP3AudioHeader()
        } catch (e: Exception) {
            exceptionCaught = e
        }
        assertNull(exceptionCaught)
        assertEquals("12000", mp3AudioHeader?.getSampleRate())
        assertEquals("00:14", mp3AudioHeader?.getTrackLengthAsString())
        assertTrue(mp3AudioHeader?.isVariableBitRate() == true)
        assertEquals(
            MPEGFrameHeader.mpegVersionMap.get(
                Integer.valueOf(MPEGFrameHeader.VERSION_2_5)
            ),
            mp3AudioHeader?.getMpegVersion()
        )
        assertEquals(
            MPEGFrameHeader.mpegLayerMap.get(
                Integer.valueOf(MPEGFrameHeader.LAYER_III)
            ),
            mp3AudioHeader?.getMpegLayer()
        )
        assertEquals(
            MPEGFrameHeader.modeMap.get(Integer.valueOf(MPEGFrameHeader.MODE_MONO)),
            mp3AudioHeader?.getChannels()
        )
        assertTrue(mp3AudioHeader?.isOriginal() == true)
        assertFalse(mp3AudioHeader?.isCopyrighted() == true)
        assertFalse(mp3AudioHeader?.isPrivate() == true)
        assertFalse(mp3AudioHeader?.isProtected() == true)
        assertEquals("~128", mp3AudioHeader?.getBitRate())
        assertEquals("mp3", mp3AudioHeader?.getEncodingType())
        assertEquals("LAME3.96r", mp3AudioHeader?.encoder)
    }

    @Test
    fun testReadV2L2() {
        var exceptionCaught: Exception? = null
        val testFile = copyAudioToTmp("testV2L2.mp3")
        var mp3AudioHeader: MP3AudioHeader? = null
        try {
            mp3AudioHeader = MP3File.read(testFile).getMP3AudioHeader()
        } catch (e: Exception) {
            exceptionCaught = e
        }
        assertNull(exceptionCaught)
        assertEquals("24000", mp3AudioHeader?.getSampleRate())

        assertFalse(mp3AudioHeader?.isVariableBitRate() == true)
        assertEquals(
            MPEGFrameHeader.mpegVersionMap.get(
                Integer.valueOf(MPEGFrameHeader.VERSION_2)
            ),
            mp3AudioHeader?.getMpegVersion()
        )
        assertEquals(
            MPEGFrameHeader.mpegLayerMap.get(
                Integer.valueOf(MPEGFrameHeader.LAYER_II)
            ),
            mp3AudioHeader?.getMpegLayer()
        )
        assertEquals(
            MPEGFrameHeader.modeMap.get(
                Integer.valueOf(MPEGFrameHeader.MODE_JOINT_STEREO)
            ),
            mp3AudioHeader?.getChannels()
        )
        //assertEquals("00:14", mp3AudioHeader?.getTrackLengthAsString()); not working returning 0
        assertTrue(mp3AudioHeader?.isOriginal() == true)
        assertFalse(mp3AudioHeader?.isCopyrighted() == true)
        assertFalse(mp3AudioHeader?.isPrivate() == true)
        assertTrue(mp3AudioHeader?.isProtected() == true)
        assertEquals("16", mp3AudioHeader?.getBitRate())
        assertEquals("mp3", mp3AudioHeader?.getEncodingType())
        assertEquals("", mp3AudioHeader?.encoder) //No Lame header so blank
    }

    @Test
    fun testReadV2L3Stereo() {
        var exceptionCaught: Exception? = null
        val testFile = copyAudioToTmp("testV2L3Stereo.mp3")
        var mp3AudioHeader: MP3AudioHeader? = null
        try {
            mp3AudioHeader = MP3File.read(testFile).getMP3AudioHeader()
        } catch (e: Exception) {
            exceptionCaught = e
        }
        assertNull(exceptionCaught)
        assertEquals("24000", mp3AudioHeader?.getSampleRate())
        //assertEquals("00:14", mp3AudioHeader?.getTrackLengthAsString());
        assertFalse(mp3AudioHeader?.isVariableBitRate() == true)
        assertEquals(
            MPEGFrameHeader.mpegVersionMap.get(
                Integer.valueOf(MPEGFrameHeader.VERSION_2)
            ),
            mp3AudioHeader?.getMpegVersion()
        )
        assertEquals(
            MPEGFrameHeader.mpegLayerMap.get(
                Integer.valueOf(MPEGFrameHeader.LAYER_III)
            ),
            mp3AudioHeader?.getMpegLayer()
        )
        assertEquals(
            MPEGFrameHeader.modeMap.get(
                Integer.valueOf(MPEGFrameHeader.MODE_JOINT_STEREO)
            ),
            mp3AudioHeader?.getChannels()
        )
        //assertEquals("00:14", mp3AudioHeader?.getTrackLengthAsString()); not working returning 0
        assertTrue(mp3AudioHeader?.isOriginal() == true)
        assertFalse(mp3AudioHeader?.isCopyrighted() == true)
        assertFalse(mp3AudioHeader?.isPrivate() == true)
        assertFalse(mp3AudioHeader?.isProtected() == true)
        assertEquals("64", mp3AudioHeader?.getBitRate())
        assertEquals("mp3", mp3AudioHeader?.getEncodingType())
        assertEquals("LAME3.97 ", mp3AudioHeader?.encoder) //TODO should we be removing trailing space
    }

    /**
     * Test trying to parse an mp3 file which is not a valid MP3 fails gracefully with expected exception
     */
    @Test
    fun testIssue79() {
        var exceptionCaught: Exception? = null
        val testFile = copyAudioToTmp("Issue79.mp3")
        var mp3AudioHeader: MP3AudioHeader? = null
        try {
            mp3AudioHeader = MP3File.read(testFile).getMP3AudioHeader()
        } catch (e: Exception) {
            exceptionCaught = e
        }
        assertInstanceOf(
            Exception::class.java,
            exceptionCaught
        )
    }

    /**
     * Test trying to parse an mp3 file which is not a valid MP3 and is extremely small
     * Should fail gracefully
     */
    @Test
    fun testIssue81() {
        var exceptionCaught: Exception? = null
        val testFile = copyAudioToTmp("Issue81.mp3")
        var mp3AudioHeader: MP3AudioHeader? = null
        try {
            mp3AudioHeader = MP3File.read(testFile).getMP3AudioHeader()
        } catch (e: Exception) {
            exceptionCaught = e
        }
        assertInstanceOf(
            Exception::class.java,
            exceptionCaught
        )
    }

    /**
     * Test trying to parse an mp3 file which is a valid MP3 but problems with frame
     */
    @Test
    fun testIssue199() {
        var exceptionCaught: Exception? = null
        val testFile = copyAudioToTmp("testV2L2.mp3")
        var mp3AudioHeader: MP3AudioHeader? = null
        try {
            mp3AudioHeader = MP3File.read(testFile).getMP3AudioHeader()
        } catch (e: Exception) {
            exceptionCaught = e
        }
        assertNull(exceptionCaught)
    }

    /**
     * Test mp3s display tracks over an hour correctly, dont actually have any such track so have to emulate
     * the mp3 rather than calling it directly.
     */
    @Test
    fun testIssue85() {
        var exceptionCaught: Exception? = null

        val timeInFormat = SimpleDateFormat("ss")
        val timeOutFormat = SimpleDateFormat("mm:ss")
        val timeOutOverAnHourFormat = SimpleDateFormat("kk:mm:ss")

        try {
            val lengthLessThanHour = 3500
            var timeIn: Date? = timeInFormat.parse(lengthLessThanHour.toString())
            assertEquals("58:20", timeOutFormat.format(timeIn))

            val lengthIsAnHour = 3600
            timeIn = timeInFormat.parse(lengthIsAnHour.toString())
            assertEquals("01:00:00", timeOutOverAnHourFormat.format(timeIn))

            val lengthMoreThanHour = 4000
            timeIn = timeInFormat.parse(lengthMoreThanHour.toString())
            assertEquals("01:06:40", timeOutOverAnHourFormat.format(timeIn))
        } catch (e: Exception) {
            exceptionCaught = e
        }
        assertNull(exceptionCaught)
    }

    /**
     * Test trying to parse an mp3 file with a ID3 tag header reporting to short causing
     * jaudiotagger to end up reading mp3 header from too early causing audio header to be
     * read incorrectly
     */
    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    fun testIssue110() {
        var exceptionCaught: Exception? = null
        val testFile = copyAudioToTmp("test28.mp3")
        var mp3AudioHeader: MP3AudioHeader? = null
        try {
            mp3AudioHeader = MP3File.read(testFile).getMP3AudioHeader()
        } catch (e: Exception) {
            exceptionCaught = e
        }
        assertNull(exceptionCaught)
        assertEquals(
            MPEGFrameHeader.mpegVersionMap.get(
                Integer.valueOf(MPEGFrameHeader.VERSION_1)
            ),
            mp3AudioHeader?.getMpegVersion()
        )
        assertEquals(
            MPEGFrameHeader.mpegLayerMap.get(
                Integer.valueOf(MPEGFrameHeader.LAYER_III)
            ),
            mp3AudioHeader?.getMpegLayer()
        )
        assertEquals(
            MPEGFrameHeader.modeMap.get(
                Integer.valueOf(MPEGFrameHeader.MODE_JOINT_STEREO)
            ),
            mp3AudioHeader?.getChannels()
        )
    }

    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    fun testReadVRBIFrame() {
        var exceptionCaught: Exception? = null
        val testFile = copyAudioToTmp("test30.mp3")
        var mp3AudioHeader: MP3AudioHeader? = null
        try {
            mp3AudioHeader = MP3File.read(testFile).getMP3AudioHeader()
        } catch (e: Exception) {
            exceptionCaught = e
        }
        assertNull(exceptionCaught)
        assertEquals(
            MPEGFrameHeader.mpegVersionMap.get(
                Integer.valueOf(MPEGFrameHeader.VERSION_1)
            ),
            mp3AudioHeader?.getMpegVersion()
        )
        assertEquals(
            MPEGFrameHeader.mpegLayerMap.get(
                Integer.valueOf(MPEGFrameHeader.LAYER_III)
            ),
            mp3AudioHeader?.getMpegLayer()
        )
        assertEquals(
            MPEGFrameHeader.modeMap.get(Integer.valueOf(MPEGFrameHeader.MODE_STEREO)),
            mp3AudioHeader?.getChannels()
        )
        assertTrue(mp3AudioHeader?.isVariableBitRate() == true)
        assertEquals(147, mp3AudioHeader?.getBitRateAsNumber())
        assertEquals("Fraunhofer", mp3AudioHeader?.encoder)
    }

    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    fun testWriteToFileWithVRBIFrame() {
        var exceptionCaught: Exception? = null
        val testFile = copyAudioToTmp("test30.mp3")
        var mp3AudioHeader: MP3AudioHeader? = null
        var mp3file: MP3File? = null
        try {
            mp3file = MP3File.read(testFile)
            mp3AudioHeader = mp3file.getMP3AudioHeader()

            //make change to file
            mp3file.getID3v2Tag()!!.setField(GenericFieldKey.TITLE, "FREDDY")
            mp3file.getID3v2Tag()!!.deleteField(GenericFieldKey.COVER_ART)
            mp3file.getID3v2Tag()!!.removeFrame("PRIV")

            mp3file.save()

            mp3file = MP3File.read(testFile)
            mp3AudioHeader = mp3file.getMP3AudioHeader()
        } catch (e: Exception) {
            exceptionCaught = e
        }
        //change has been made and VBRI Frame is left intact
        assertEquals("FREDDY", mp3file!!.getID3v2Tag()!!.getFirst(GenericFieldKey.TITLE))

        assertNull(exceptionCaught)
        assertEquals(
            MPEGFrameHeader.mpegVersionMap.get(
                Integer.valueOf(MPEGFrameHeader.VERSION_1)
            ),
            mp3AudioHeader?.getMpegVersion()
        )
        assertEquals(
            MPEGFrameHeader.mpegLayerMap.get(
                Integer.valueOf(MPEGFrameHeader.LAYER_III)
            ),
            mp3AudioHeader?.getMpegLayer()
        )
        assertEquals(
            MPEGFrameHeader.modeMap.get(Integer.valueOf(MPEGFrameHeader.MODE_STEREO)),
            mp3AudioHeader?.getChannels()
        )
        assertTrue(mp3AudioHeader?.isVariableBitRate() == true)
        assertEquals(147, mp3AudioHeader?.getBitRateAsNumber())
        assertEquals("Fraunhofer", mp3AudioHeader?.encoder)
    }
}
