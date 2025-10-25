package de.visualdigits.kaudiotagger.model.frame.framebody

import de.visualdigits.kaudiotagger.model.frame.framebody.id3.AbstractID3v2FrameBody
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v23FrameBody
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v24FrameBody
import java.nio.ByteBuffer

class FrameBodyDeprecated : AbstractID3v2FrameBody, ID3v24FrameBody, ID3v23FrameBody {

    /**
     * The original frameBody is held so can be retrieved
     * when converting a DeprecatedFrameBody back to a normal framebody
     */
    var originalFrameBody:AbstractID3v2FrameBody? = null

    /**
     * Creates a new FrameBodyDeprecated wrapper around the frameBody
     *
     * @param frameBody
     */
    constructor(frameBody:AbstractID3v2FrameBody) {
        this.originalFrameBody = frameBody
    }

    constructor(
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(byteBuffer, frameSize)

    /**
     * Delgate size to size of original frameBody, if frameBody already exist will take this value from the frame header
     * but it is always recalculated before writing any changes back to disk.
     *
     * @return size in bytes of this frame body
     */
    override fun getSize(): Int {
        return originalFrameBody?.getSize()?:0
    }

    /**
     * Return the frame identifier
     *
     * @return the identifier
     */
    override fun getIdentifier(): String? {
        return originalFrameBody?.getIdentifier()
    }

    /**
     * Because the contents of this frame are an array of bytes and could be large we just
     * return the identifier.
     *
     * @return a string representation of this frame
     */
    override fun toString(): String {
        return getIdentifier()?:""
    }

    /**
     * Setup the Object List.
     *
     *
     * This is handled by the wrapped class
     */
    override fun setupObjectList() {
    }

    override fun getBriefDescription(): String {
        return originalFrameBody?.getBriefDescription()?:""
    }
}