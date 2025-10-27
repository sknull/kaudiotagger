package de.visualdigits.kaudiotagger.model.frame.framebody.id3

import de.visualdigits.kaudiotagger.model.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.datatype.NumberFixedLength
import de.visualdigits.kaudiotagger.model.datatype.StringNullTerminated
import de.visualdigits.kaudiotagger.model.datatype.types.ID3v2ChapterFrames
import java.nio.ByteBuffer

/**
 * Chapter frame.
 *
 *
 *
 *
 * The purpose of this frame is to describe a single chapter within an
 * audio file. There may be more than one frame of this type in a tag
 * but each must have an Element ID that is unique with respect to any
 * other "CHAP" frame or "CTOC" frame in the tag.
 *
 * <table border="0" width="70%" align="center">
 * <tr><td nowrap="nowrap">&lt;ID3v2.3 or ID3v2.4 frame header, ID: "CHAP"&gt;</td><td rowspan="7">&nbsp;&nbsp;</td><td>(10 bytes)</td></tr>
 * <tr><td>Element ID</td><td width="70%">&lt;text string&gt; $00</td></tr>
 * <tr><td>Start time</td><td>$xx xx xx xx</td></tr>
 * <tr><td>End time</td><td>$xx xx xx xx</td></tr>
 * <tr><td>Start offset</td><td>$xx xx xx xx</td></tr>
 * <tr><td>End offset</td><td>$xx xx xx xx</td></tr>
 * <tr><td>&lt;Optional embedded sub-frames&gt;</td></tr>
</table> *
 *
 *
 * The Element ID uniquely identifies the frame. It is not intended to
 * be human readable and should not be presented to the end user.
 *
 *
 * The Start and End times are a count in milliseconds from the
 * beginning of the file to the start and end of the chapter
 * respectively.
 *
 *
 * The Start offset is a zero-based count of bytes from the beginning
 * of the file to the first byte of the first audio frame in the
 * chapter. If these bytes are all set to 0xFF then the value should be
 * ignored and the start time value should be utilized.
 *
 *
 * The End offset is a zero-based count of bytes from the beginning of
 * the file to the first byte of the audio frame following the end of
 * the chapter. If these bytes are all set to 0xFF then the value should
 * be ignored and the end time value should be utilized.
 *
 *
 * There then follows a sequence of optional frames that are embedded
 * within the "CHAP" frame and which describe the content of the chapter
 * (e.g. a "TIT2" frame representing the chapter name) or provide
 * related material such as URLs and images. These sub-frames are
 * contained within the bounds of the "CHAP" frame as signalled by the
 * size field in the "CHAP" frame header. If a parser does not recognise
 * "CHAP" frames it can skip them using the size field in the frame
 * header. When it does this it will skip any embedded sub-frames
 * carried within the frame.
 *
 *
 *
 * For more details, please refer to the ID3 Chapter Frame specifications:
 *
 *  * [ID3 v2 Chapter Frame Spec](http://www.id3.org/id3v2-chapters-1.0.txt)
 *
 *
 * @author Marc Gimpel, Horizon Wimba S.A.
 * @version $Id$
 */
class FrameBodyCHAP: AbstractID3v2FrameBody, ID3v2ChapterFrameBody {
    
    /**
     * Creates a new FrameBodyCHAP datatype.
     */
    constructor()

    /**
     * Creates a new FrameBodyCHAP datatype.
     *
     * @param body
     */
    constructor(body: FrameBodyCHAP) : super(body)

    /**
     * Creates a new FrameBodyCHAP datatype.
     *
     * @param elementId
     * @param startTime
     * @param endTime
     * @param startOffset
     * @param endOffset
     */
    constructor(
        elementId: String?,
        startTime: Int,
        endTime: Int,
        startOffset: Int,
        endOffset: Int
    ) {
        this.setObjectValue(DataTypes.OBJ_ELEMENT_ID, elementId)
        this.setObjectValue(DataTypes.OBJ_START_TIME, startTime)
        this.setObjectValue(DataTypes.OBJ_END_TIME, endTime)
        this.setObjectValue(DataTypes.OBJ_START_OFFSET, startOffset)
        this.setObjectValue(DataTypes.OBJ_END_OFFSET, endOffset)
    }

    /**
     * Creates a new FrameBodyAENC datatype.
     *
     * @param byteBuffer
     * @param frameSize
     * @throws InvalidTagException if unable to create framebody from buffer
     */
    constructor(byteBuffer: ByteBuffer?, frameSize: Int) : super(byteBuffer, frameSize)

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v2ChapterFrames.CHAPTER.id
    }

    /**
     *
     */
    override fun setupObjectList() {
        objectList.add(StringNullTerminated(DataTypes.OBJ_ELEMENT_ID, this))
        objectList.add(NumberFixedLength(DataTypes.OBJ_START_TIME, this, 4))
        objectList.add(NumberFixedLength(DataTypes.OBJ_END_TIME, this, 4))
        objectList.add(NumberFixedLength(DataTypes.OBJ_START_OFFSET, this, 4))
        objectList.add(NumberFixedLength(DataTypes.OBJ_END_OFFSET, this, 4))
    }
}
