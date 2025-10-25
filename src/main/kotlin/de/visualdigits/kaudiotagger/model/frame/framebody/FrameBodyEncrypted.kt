package de.visualdigits.kaudiotagger.model.frame.framebody

import de.visualdigits.kaudiotagger.model.datatype.ByteArraySizeTerminated
import de.visualdigits.kaudiotagger.model.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.frame.framebody.AbstractTagFrameBody
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.AbstractID3v2FrameBody
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v23FrameBody
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v24FrameBody
import java.nio.ByteBuffer

class FrameBodyEncrypted: AbstractID3v2FrameBody, ID3v23FrameBody, ID3v24FrameBody {

    constructor(copyObject: FrameBodyEncrypted): super(copyObject)

    constructor(
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(byteBuffer, frameSize)

    constructor(
        identifier: String? = null,
        byteBuffer: ByteBuffer,
        frameSize: Int
    ): super(identifier, byteBuffer, frameSize)

    /**
     * TODO:proper mapping
     */
    override fun setupObjectList() {
        objectList.add(ByteArraySizeTerminated(DataTypes.OBJ_DATA, this))
    }
}