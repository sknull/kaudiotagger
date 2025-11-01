package de.visualdigits.kaudiotagger.model.id3.datatype

import de.visualdigits.kaudiotagger.model.common.frame.framebody.AbstractTagFrameBody
import de.visualdigits.kaudiotagger.util.ID3Tags

/**
 * Represents a timestamp field
 */
class StringDate : StringFixedLength {

    /**
     * Creates a new ObjectStringDate datatype.
     *
     * @param identifier
     * @param frameBody
     */
    constructor(identifier: String?, frameBody: AbstractTagFrameBody) : super(identifier, frameBody, 8)

    constructor(copyObject: StringDate) : super(copyObject)

    override fun getValue(): Any? {
        return if (getValue() != null) {
            ID3Tags.stripChar(getValue().toString(), '-')
        } else {
            null
        }
    }

    override fun setValue(value: Any?) {
        if (value != null) {
            setValue(ID3Tags.stripChar(value.toString(), '-'))
        }
    }
}
