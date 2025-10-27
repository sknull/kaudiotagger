package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.common.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.common.datatype.NumberFixedLength
import de.visualdigits.kaudiotagger.model.common.types.ID3v24Frames
import java.nio.ByteBuffer

class FrameBodySEEK: AbstractID3v2FrameBody, ID3v24FrameBody {
    /**
     * Creates a new FrameBodySEEK datatype.
     */
    constructor()

    /**
     * Creates a new FrameBodySEEK datatype.
     *
     * @param minOffsetToNextTag
     */
    constructor(minOffsetToNextTag: Int) {
        this.setObjectValue(DataTypes.OBJ_OFFSET, minOffsetToNextTag)
    }

    constructor(body: FrameBodySEEK) : super(body)

    /**
     * Creates a new FrameBodySEEK datatype.
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
        return ID3v24Frames.AUDIO_SEEK_POINT_INDEX.id
    }

    /**
     *
     */
    override fun setupObjectList() {
        objectList.add(NumberFixedLength(DataTypes.OBJ_OFFSET, this, 4))
    }
}
