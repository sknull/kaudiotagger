package de.visualdigits.kaudiotagger.model.id3.frame

import de.visualdigits.kaudiotagger.AbstractTestCase
import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyAPIC
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyCOMM
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTIT2
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v23EncodingFlags
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v23Tag
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v24EncodingFlags
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v24Tag
import de.visualdigits.kaudiotagger.model.id3.types.ID3v23FrameId
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CompressedTest: AbstractTestCase() {
    
    /**
     * This tests reading a v23tag that contains a compressed COMM frame
     *
     * @throws Exception
     */
    @Test
    fun testv23TagReadCompressedCommentFrame() {
        val COMM_TEXT = "[P-M-S] Teampms [P-M-S]"

        val testFile = prependAudioToTmp(
            "Issue98-1.id3",
            "testV1.mp3"
        )

        //Read file as currently stands
        val mp3File = MP3File.read(testFile)
        val v23tag = mp3File.getID3v2Tag() as ID3v23Tag

        assertTrue(v23tag.hasField(ID3v23FrameId.COMMENT.id))

        val frame = v23tag.getFrame(
            ID3v23FrameId.COMMENT.id
        ) as ID3v23Frame
        assertTrue(
            (frame.encodingFlags as ID3v23EncodingFlags).isCompression()
        )
        val frameBody = frame.frameBody as FrameBodyCOMM
        assertEquals(COMM_TEXT, frameBody.getText())
        assertEquals("", frameBody.getDescription())
    }

    /**
     * This tests reading a v23 tag that contains a compressed APIC frame
     *
     * @throws Exception
     */
    @Test
    fun testv23TagReadCompressedAPICFrame() {
        val FRAME_SIZE = 3220
        val TITLE_TEXT = "Crazy Train"
        val testFile = prependAudioToTmp(
            "Issue98-2.id3",
            "testV1.mp3"
        )

        //Read file as currently stands
        val mp3File = MP3File.read(testFile)
        val v23tag = mp3File.getID3v2Tag() as ID3v23Tag

        assertTrue(v23tag.hasField(ID3v23FrameId.ATTACHED_PICTURE.id))

        var frame = v23tag.getFrame(
            ID3v23FrameId.ATTACHED_PICTURE.id
        ) as ID3v23Frame
        assertTrue(
            (frame.encodingFlags as ID3v23EncodingFlags).isCompression()
        )
        val frameBody = frame.frameBody as FrameBodyAPIC
        assertEquals("", frameBody.getDescription())
        assertEquals(FRAME_SIZE, frameBody.getSize())

        //Check got to end of frame
        assertTrue(v23tag.hasField(ID3v23FrameId.TITLE.id))
        frame = v23tag.getFrame(ID3v23FrameId.TITLE.id) as ID3v23Frame
        val frameBodyTitle = frame.frameBody as FrameBodyTIT2
        assertEquals(TITLE_TEXT, frameBodyTitle.getText())
    }

    /**
     * This tests reading a v24tag that contains a compressed Picture frame
     *
     * @throws Exception
     */
    @Test
    fun testv24TagReadCompressedPictureFrame() {
        val testFile = prependAudioToTmp(
            "Issue98-3.id3",
            "testV1.mp3"
        )
        val mp3File = MP3File.read(testFile)
        val v24tag = mp3File.getID3v2Tag() as ID3v24Tag

        assertTrue(v24tag.hasField(ID3v24FrameId.ATTACHED_PICTURE.id))
        val frame = v24tag.getFrame(
            ID3v24FrameId.ATTACHED_PICTURE.id
        ) as ID3v24Frame
        assertTrue(
            (frame.encodingFlags as ID3v24EncodingFlags).isCompression()
        )
        assertEquals(27, v24tag.getFieldCount())
    }
}
