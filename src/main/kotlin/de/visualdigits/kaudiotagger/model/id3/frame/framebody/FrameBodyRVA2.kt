package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.datatype.ByteArraySizeTerminated
import de.visualdigits.kaudiotagger.model.id3.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import java.nio.ByteBuffer

class FrameBodyRVA2: AbstractID3v2FrameBody, ID3v24FrameBody {
    /**
     * Creates a new FrameBodyRVA2 datatype.
     */
    constructor()

    constructor(body: FrameBodyRVA2) : super(body)

    /**
     * Convert from V3 to V4 Frame
     *
     * @param body
     */
    constructor(body: FrameBodyRVAD) {
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
        return ID3v24FrameId.RELATIVE_VOLUME_ADJUSTMENT2.id
    }

    /**
     * Setup the Object List. A byte Array which will be read upto frame size
     * bytes.
     */
    override fun setupObjectList() {
        objectList.add(ByteArraySizeTerminated(DataTypes.OBJ_DATA, this))
    }
}
