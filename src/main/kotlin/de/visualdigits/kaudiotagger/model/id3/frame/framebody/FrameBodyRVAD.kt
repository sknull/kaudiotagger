package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.common.datatype.ByteArraySizeTerminated
import de.visualdigits.kaudiotagger.model.common.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.id3.types.ID3V23Frame
import java.nio.ByteBuffer

/**
 * Relative volume adjustment frame.
 *
 *
 * Only partially implemented.
 *
 * @author : Paul Taylor
 * @author : Eric Farng
 * @version $Id$
 */
class FrameBodyRVAD: AbstractID3v2FrameBody, ID3v23FrameBody {
    /**
     * Creates a new FrameBodyRVAD datatype.
     */
    constructor()

    constructor(copyObject: FrameBodyRVAD) : super(copyObject)

    /**
     * Convert from V4 to V3 Frame
     *
     * @param body
     */
    constructor(body: FrameBodyRVA2) {
        setObjectValue(DataTypes.OBJ_DATA, body.getObjectValue(DataTypes.OBJ_DATA))
    }

    /**
     * Creates a new FrameBodyRVAD datatype.
     *
     * @param byteBuffer
     * @param frameSize
     */
    constructor(byteBuffer: ByteBuffer?, frameSize: Int) : super(byteBuffer, frameSize)

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3V23Frame.RELATIVE_VOLUME_ADJUSTMENT.id
    }

    /**
     * Setup the Object List. A byte Array which will be read upto frame size
     * bytes.
     */
    override fun setupObjectList() {
        objectList.add(ByteArraySizeTerminated(DataTypes.OBJ_DATA, this))
    }
}
