package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.common.datatype.BooleanByte
import de.visualdigits.kaudiotagger.model.common.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.common.datatype.NumberFixedLength
import de.visualdigits.kaudiotagger.model.id3.types.ID3V24Frame
import java.nio.ByteBuffer

/**
 * Body of Recommended buffer size frame, generally used for streaming audio
 */
class FrameBodyRBUF

    : AbstractID3v2FrameBody, ID3v24FrameBody, ID3v23FrameBody {
    /**
     * Creates a new FrameBodyRBUF datatype.
     */
    constructor() {
        this.setObjectValue(DataTypes.OBJ_BUFFER_SIZE, 0.toByte())
        this.setObjectValue(DataTypes.OBJ_EMBED_FLAG, java.lang.Boolean.FALSE)
        this.setObjectValue(DataTypes.OBJ_OFFSET, 0.toByte())
    }

    constructor(body: FrameBodyRBUF) : super(body)

    /**
     * Creates a new FrameBodyRBUF datatype.
     *
     * @param bufferSize
     * @param embeddedInfoFlag
     * @param offsetToNextTag
     */
    constructor(
        bufferSize: Byte,
        embeddedInfoFlag: Boolean,
        offsetToNextTag: Byte
    ) {
        this.setObjectValue(DataTypes.OBJ_BUFFER_SIZE, bufferSize)
        this.setObjectValue(DataTypes.OBJ_EMBED_FLAG, embeddedInfoFlag)
        this.setObjectValue(DataTypes.OBJ_OFFSET, offsetToNextTag)
    }

    /**
     * Creates a new FrameBodyRBUF datatype.
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
        return ID3V24Frame.RECOMMENDED_BUFFER_SIZE.id
    }

    /**
     *
     */
    override fun setupObjectList() {
        objectList.add(
            NumberFixedLength(DataTypes.OBJ_BUFFER_SIZE, this, BUFFER_FIELD_SIZE)
        )
        objectList.add(
            BooleanByte(
                DataTypes.OBJ_EMBED_FLAG,
                this,
                EMBED_FLAG_BIT_POSITION
            )
        )
        objectList.add(
            NumberFixedLength(DataTypes.OBJ_OFFSET, this, OFFSET_FIELD_SIZE)
        )
    }

    companion object {
        private const val BUFFER_FIELD_SIZE = 3
        private const val EMBED_FLAG_BIT_POSITION = 1
        private const val OFFSET_FIELD_SIZE = 4
    }
}
