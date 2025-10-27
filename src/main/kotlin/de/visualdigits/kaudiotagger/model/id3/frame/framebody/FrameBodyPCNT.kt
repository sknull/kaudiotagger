package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.common.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.common.datatype.NumberVariableLength
import de.visualdigits.kaudiotagger.model.common.types.ID3v24Frames
import java.nio.ByteBuffer

/**
 * Play counter frame.
 *
 *
 *
 *
 * This is simply a counter of the number of times a file has been
 * played. The value is increased by one every time the file begins to
 * play. There may only be one "PCNT" frame in each tag. When the
 * counter reaches all one's, one byte is inserted in front of the
 * counter thus making the counter eight bits bigger. The counter must
 * be at least 32-bits long to begin with.
 *
 * <table border=0 width="70%">
 * <tr><td colspan=2> &lt;Header for 'Play counter', ID: "PCNT"&gt;</td></tr>
 * <tr><td>Counter </td><td>$xx xx xx xx (xx ...)</td></tr>
</table> *
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
class FrameBodyPCNT: AbstractID3v2FrameBody, ID3v24FrameBody, ID3v23FrameBody {

    companion object {
        private const val COUNTER_MINIMUM_FIELD_SIZE = 4
    }

    /**
     * Creates a new FrameBodyPCNT datatype.
     */
    constructor() {
        this.setObjectValue(DataTypes.OBJ_NUMBER, 0L)
    }

    constructor(body: FrameBodyPCNT) : super(body)

    /**
     * Creates a new FrameBodyPCNT datatype.
     *
     * @param counter
     */
    constructor(counter: Long) {
        this.setObjectValue(DataTypes.OBJ_NUMBER, counter)
    }

    /**
     * Creates a new FrameBodyPCNT datatype.
     *
     * @param byteBuffer
     * @param frameSize
     * @throws InvalidTagException if unable to create framebody from buffer
     */
    constructor(byteBuffer: ByteBuffer?, frameSize: Int) : super(byteBuffer, frameSize)

    /**
     * @return the play count of this file
     */
    fun getCounter(): Long {
        return (getObjectValue(DataTypes.OBJ_NUMBER) as Number).toLong()
    }

    /**
     * Set the play counter of this file
     *
     * @param counter
     */
    fun setCounter(counter: Long) {
        setObjectValue(DataTypes.OBJ_NUMBER, counter)
    }

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v24Frames.PLAY_COUNTER.id
    }

    /**
     *
     */
    override fun setupObjectList() {
        objectList.add(
            NumberVariableLength(
                DataTypes.OBJ_NUMBER,
                this,
                COUNTER_MINIMUM_FIELD_SIZE
            )
        )
    }
}
