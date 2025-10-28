package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.common.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.common.datatype.PartOfSetValue
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24Frames
import java.nio.ByteBuffer

class FrameBodyTRCK: AbstractFrameBodyNumberTotal, ID3v23FrameBody, ID3v24FrameBody {

    /**
     * Creates a new FrameBodyTRCK datatype.
     */
    constructor()

    constructor(body: FrameBodyTRCK): super(body)

    /**
     * Creates a new FrameBodyTRCK datatype, the value is parsed literally
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String): super(textEncoding, text)

    constructor(textEncoding: Byte, trackNo: Int, trackTotal: Int): super(textEncoding, trackNo, trackTotal)

    constructor(
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(byteBuffer, frameSize)

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v24Frames.TRACK.id
    }

    fun getTrackNo(): Int {
        return getNumber()
    }

    fun setTrackNo(trackNo: Int) {
        setNumber(trackNo)
    }

    fun setTrackNo(trackNo: String) {
        setNumber(trackNo)
    }

    fun getTrackNoAsText(): String? {
        return getNumberAsText()
    }

    fun getTrackTotal(): Int {
        return getTotal()
    }

    fun setTrackTotal(trackTotal: Int) {
        setTotal(trackTotal)
    }

    fun setTrackTotal(trackTotal: String) {
        setTotal(trackTotal)
    }

    fun getTrackTotalAsText(): String {
        return getTotalAsText()
    }

    override fun setText(text: String) {
        setObjectValue(DataTypes.OBJ_TEXT, PartOfSetValue(text))
    }
}