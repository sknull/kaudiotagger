package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.types.ID3V24Frame
import java.nio.ByteBuffer

/**
 * Part of a set Text information frame.
 *
 *
 * The 'Part of a set' frame is a numeric string that describes which part of a set the audio came from.
 * This frame is used if the source described in the "TALB" frame is divided into several mediums, e.g. a double CD.
 * The value may be extended with a "/" character and a numeric string containing the total number of parts in the set.
 * e.g. "1/2".
 *
 *
 * For more details, please refer to the ID3 specifications:
 *
 *  * [ID3 v2.3.0 Spec](http://www.id3.org/id3v2.3.0.txt)
 *
 *
 * @author : Paul Taylor
 * @author : Eric Farng
 * @version $Id$
 */
class FrameBodyTPOS: AbstractFrameBodyNumberTotal, ID3v23FrameBody, ID3v24FrameBody {
    /**
     * Creates a new FrameBodyTRCK datatype.
     */
    constructor() : super()

    constructor(body: FrameBodyTPOS) : super(body)

    /**
     * Creates a new FrameBodyTRCK datatype, the value is parsed literally
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String) : super(textEncoding, text)

    constructor(textEncoding: Byte, discNo: Int, discTotal: Int) : super(textEncoding, discNo, discTotal)

    /**
     * Creates a new FrameBodyTRCK datatype.
     *
     * @param byteBuffer
     * @param frameSize
     * @throws java.io.IOException
     * @throws InvalidTagException
     */
    constructor(byteBuffer: ByteBuffer?, frameSize: Int) : super(byteBuffer, frameSize)

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3V24Frame.SET.id
    }

    fun getDiscNo(): Int {
        return getNumber()
    }

    fun setDiscNo(discNo: Int?) {
        setNumber(discNo!!)
    }

    fun setDiscNo(discNo: String?) {
        setNumber(discNo!!)
    }

    fun getDiscNoAsText(): String {
        return getNumberAsText()!!
    }

    fun getDiscTotal(): Int {
        return getTotal()
    }

    fun setDiscTotal(discTotal: Int?) {
        setTotal(discTotal!!)
    }

    fun setDiscTotal(discTotal: String?) {
        setTotal(discTotal!!)
    }

    fun getDiscTotalAsText(): String {
        return getTotalAsText()
    }
}
