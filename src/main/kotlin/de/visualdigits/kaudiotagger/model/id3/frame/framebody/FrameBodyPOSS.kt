package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.common.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.common.datatype.NumberHashMap
import de.visualdigits.kaudiotagger.model.common.datatype.NumberVariableLength
import de.visualdigits.kaudiotagger.model.id3.types.EventTimingTimestampTypes
import de.visualdigits.kaudiotagger.model.id3.types.ID3V24Frame
import java.nio.ByteBuffer

/**
 * Position synchronisation frame.
 *
 *
 *
 *
 * This frame delivers information to the listener of how far into the
 * audio stream he picked up; in effect, it states the time offset of
 * the first frame in the stream. The frame layout is:
 *
 *
 * Head for 'Position synchronisation', ID:
 * Time stamp format $xx
 * Position          $xx (xx ...)
 *
 *
 * Where time stamp format is:
 *
 *
 * $01 Absolute time, 32 bit sized, using MPEG frames as unit
 * $02 Absolute time, 32 bit sized, using milliseconds as unit
 *
 *
 * and position is where in the audio the listener starts to receive,
 * i.e. the beginning of the next frame. If this frame is used in the
 * beginning of a file the value is always 0. There may only be one
 * "POSS" frame in each tag.
 *
 *
 *
 * For more details, please refer to the ID3 specifications:
 *
 *  * [ID3 v2.3.0 Spec](http://www.id3.org/id3v2.3.0.txt)
 *
 *
 * @author : Paul Taylor
 * @author : Eric Farng
 * @version $Id$
 */
class FrameBodyPOSS: AbstractID3v2FrameBody, ID3v24FrameBody, ID3v23FrameBody {
    /**
     * Creates a new FrameBodyPOSS datatype.
     */
    constructor()

    constructor(body: FrameBodyPOSS) : super(body)

    /**
     * Creates a new FrameBodyPOSS datatype.
     *
     * @param timeStampFormat
     * @param position
     */
    constructor(timeStampFormat: Byte, position: Long) {
        this.setObjectValue(DataTypes.OBJ_TIME_STAMP_FORMAT, timeStampFormat)
        this.setObjectValue(DataTypes.OBJ_POSITION, position)
    }

    /**
     * Creates a new FrameBodyPOSS datatype.
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
        return ID3V24Frame.POSITION_SYNC.id
    }

    /**
     *
     */
    override fun setupObjectList() {
        objectList.add(
            NumberHashMap(
                DataTypes.OBJ_TIME_STAMP_FORMAT,
                this,
                EventTimingTimestampTypes.TIMESTAMP_KEY_FIELD_SIZE
            )
        )
        objectList.add(NumberVariableLength(DataTypes.OBJ_POSITION, this, 1))
    }
}
