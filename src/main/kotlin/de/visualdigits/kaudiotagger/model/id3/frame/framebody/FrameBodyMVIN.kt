package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.common.types.ID3v24Frames
import java.nio.ByteBuffer

/**
 * Apple defined Movement No/Total frame works the same way as the TRCK frame
 *
 *
 * This is not an official standard frame, but Apple makes its own rules !
 *
 * @author : Paul Taylor
 */
class FrameBodyMVIN: AbstractFrameBodyNumberTotal, ID3v24FrameBody, ID3v23FrameBody {
    /**
     * Creates a new FrameBodyTALB datatype.
     */
    constructor()

    constructor(body: FrameBodyMVIN) : super(body)

    /**
     * Creates a new FrameBodyTALB datatype.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String) : super(textEncoding, text)

    /**
     * Creates a new FrameBodyTALB datatype.
     *
     * @param byteBuffer
     * @param frameSize
     * @throws org.jaudiotagger.tag.InvalidTagException if unable to create framebody from buffer
     */
    constructor(byteBuffer: ByteBuffer?, frameSize: Int) : super(byteBuffer, frameSize)

    constructor(
        textEncoding: Byte,
        movementNo: Int,
        movementTotal: Int
    ) : super(textEncoding, movementNo, movementTotal)

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v24Frames.MOVEMENT_NO.id
    }
}
