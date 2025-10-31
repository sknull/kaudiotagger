package de.visualdigits.kaudiotagger.model.id3.datatype

import de.visualdigits.kaudiotagger.model.common.frame.framebody.AbstractTagFrameBody
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets


/**
 * Represents a String whose size is determined by finding of a null character at the end of the String with fixed text encoding.
 *
 *
 * The String will be encoded using the default encoding regardless of what encoding may be specified in the framebody
 */
class StringNullTerminated : TextEncodedStringNullTerminated {

    /**
     * Creates a new ObjectStringNullTerminated datatype.
     *
     * @param identifier identifies the frame type
     * @param frameBody
     */
    constructor(
        identifier: String?,
        frameBody: AbstractTagFrameBody
    ) : super(identifier, frameBody)

    constructor(copyObject: StringNullTerminated) : super(copyObject)

    override fun getTextEncodingCharSet(): Charset {
        return StandardCharsets.ISO_8859_1
    }
}
