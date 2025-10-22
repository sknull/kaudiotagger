package de.visualdigits.kaudiotagger.model.frame.framebody

import de.visualdigits.kaudiotagger.model.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.datatype.PartOfSetValue
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v23FrameBody
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v24FrameBody
import de.visualdigits.kaudiotagger.model.kframe.ID3v24KFrame
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

    /**
     * Creates a new FrameBodyTRCK datatype.
     *
     * @param byteBuffer
     * @param frameSize
     * @throws java.io.IOException
     * @throws InvalidTagException
     */
    constructor(byteBuffer: ByteBuffer, frameSize: Int): super(byteBuffer, frameSize)

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v24KFrame.TRACK.id
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