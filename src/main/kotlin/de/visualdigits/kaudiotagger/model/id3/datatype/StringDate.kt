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
    constructor(identifier: String?, frameBody: AbstractTagFrameBody) : super(identifier!!, frameBody, 8)

    constructor(copyObject: StringDate) : super(copyObject)

    /**
     * @return
     */
    override fun getValue(): Any? {
        if (getValue() != null) {
            return ID3Tags.stripChar(getValue().toString(), '-')
        } else {
            return null
        }
    }

    /**
     * @param value
     */
    override fun setValue(value: Any?) {
        if (value != null) {
            setValue(ID3Tags.stripChar(value.toString(), '-'))
        }
    }

    override fun equals(obj: Any?): Boolean {
        return obj is StringDate && super.equals(obj)
    }
}
