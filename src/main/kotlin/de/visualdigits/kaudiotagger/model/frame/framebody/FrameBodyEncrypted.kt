package de.visualdigits.kaudiotagger.model.frame.framebody

import de.visualdigits.kaudiotagger.model.datatype.ByteArraySizeTerminated
import de.visualdigits.kaudiotagger.model.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.AbstractID3v2FrameBody
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v23FrameBody
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v24FrameBody
import java.nio.ByteBuffer

class FrameBodyEncrypted: AbstractID3v2FrameBody, ID3v23FrameBody, ID3v24FrameBody {

    var identifier: String? = null

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
        identifier: String? = null,
        byteBuffer: ByteBuffer,
        frameSize: Int
    ): super(byteBuffer, frameSize) {
        this.identifier = identifier
    }

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String? {
        return identifier?:""
    }

    /**
     * TODO:proper mapping
     */
    override fun setupObjectList() {
        objectList.add(ByteArraySizeTerminated(DataTypes.OBJ_DATA, this))
    }
}