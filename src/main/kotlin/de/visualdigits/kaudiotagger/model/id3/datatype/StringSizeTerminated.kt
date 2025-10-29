package de.visualdigits.kaudiotagger.model.id3.datatype

import de.visualdigits.kaudiotagger.model.common.frame.framebody.AbstractTagFrameBody
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets


/**
 * Represents a String which is not delimited by null character with fixed text encoding.
 *
 *
 * This type of String will usually only be used when it is the last field within a frame, when reading the remainder of the byte array will
 * be read, when writing the frame will accommodate the required size for the String. The String will be encoded
 * using the default encoding regardless of what encoding may be specified in the framebody
 */
open class StringSizeTerminated : TextEncodedStringSizeTerminated {
    /**
     * Creates a new ObjectStringSizeTerminated datatype.
     *
     * @param identifier identifies the frame type
     * @param frameBody
     */
    constructor(
        identifier: String?,
        frameBody: AbstractTagFrameBody
    ) : super(identifier, frameBody)

    constructor(`object`: StringSizeTerminated) : super(`object`)

    override fun equals(obj: Any?): Boolean {
        return obj is StringSizeTerminated && super.equals(obj)
    }

    override fun getTextEncodingCharSet(): Charset {
        return StandardCharsets.ISO_8859_1
    }
}
