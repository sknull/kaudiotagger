package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.common.datatype.ByteArraySizeTerminated
import de.visualdigits.kaudiotagger.model.common.datatype.DataTypes
import java.nio.ByteBuffer

class FrameBodyEncrypted: AbstractID3v2FrameBody, ID3v23FrameBody, ID3v24FrameBody {

    private var identifier: String? = null

    constructor(copyObject: FrameBodyEncrypted): super(copyObject)

    /**
     * Read from file
     *
     * @param identifier
     * @param byteBuffer
     * @param frameSize
     * @throws InvalidTagException
     */
    constructor(
        identifier: String?,
        byteBuffer: ByteBuffer?,
        frameSize: Int
    ): super(byteBuffer, frameSize) {
        this.identifier = identifier
    }

    constructor(
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(byteBuffer, frameSize)

    /**
     * Creates a new FrameBodyEncrypted dataType.
     */
    constructor(identifier: String?) {
        this.identifier = identifier
    }

    /**
     * TODO:proper mapping
     */
    override fun setupObjectList() {
        objectList.add(ByteArraySizeTerminated(DataTypes.OBJ_DATA, this))
    }

    override fun getIdentifier(): String? {
        return identifier
    }
}