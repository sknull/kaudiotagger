package de.visualdigits.kaudiotagger.model.frame.framebody

import de.visualdigits.kaudiotagger.model.datatype.ByteArraySizeTerminated
import de.visualdigits.kaudiotagger.model.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.AbstractID3v2FrameBody
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v22FrameBody
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v23FrameBody
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v24FrameBody
import java.nio.ByteBuffer

class FrameBodyUnsupported: AbstractID3v2FrameBody, ID3v22FrameBody, ID3v23FrameBody, ID3v24FrameBody {

    /**
     * Because used by any unknown frame identifier varies
     */
    private var identifier: String? = ""

    constructor(
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(byteBuffer, frameSize)

    /**
     * Create a new FrameBodyUnsupported
     *
     * @param identifier
     * @param value
     */
    constructor(identifier: String?, value: ByteArray?) {
        this.identifier = identifier
        setObjectValue(DataTypes.OBJ_DATA, value)
    }

    /**
     * Copy constructor
     *
     * @param copyObject a copy is made of this
     */
    constructor(copyObject: FrameBodyUnsupported): super(copyObject) {
        this.identifier = copyObject.identifier
    }

    /**
     * Creates a new FrameBodyUnsupported
     *
     * @param identifier
     */
    constructor(identifier: String) {
        this.identifier = identifier
    }

    /**
     * Setup the Object List. A byte Array which will be read upto frame size
     * bytes.
     */
    override fun setupObjectList() {
        objectList.add(ByteArraySizeTerminated(DataTypes.OBJ_DATA, this))
    }

    override fun getIdentifier(): String? {
        return identifier
    }
}