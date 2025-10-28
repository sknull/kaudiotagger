package de.visualdigits.kaudiotagger.model.common.datatype

import de.visualdigits.kaudiotagger.model.common.frame.framebody.AbstractTagFrameBody
import de.visualdigits.kaudiotagger.model.id3.types.Languages
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

class StringHashMap: StringFixedLength, HashMapInterface<String, String> {

    var keyToValueMap: Map<String, String> = mapOf()

    var valueToKeyMap: Map<String, String> = mapOf()

    /**
     *
     */
    var hasEmptyValue: Boolean = false

    /**
     * Creates a new ObjectStringHashMap datatype.
     *
     * @param identifier
     * @param frameBody
     * @param size
     * @throws IllegalArgumentException
     */
    constructor(
        identifier: String?,
        frameBody: AbstractTagFrameBody,
        size: Int
    ): super(identifier, frameBody, size) {
        if (identifier == DataTypes.OBJ_LANGUAGE) {
            valueToKeyMap = Languages.getValueToIdMap()
            keyToValueMap = Languages.getIdToValueMap()
        } else {
            throw IllegalArgumentException(
                "Hashmap identifier not defined in this class: " + identifier
            )
        }
    }

    constructor(copyObject: StringHashMap): super(copyObject) {
        this.hasEmptyValue = copyObject.hasEmptyValue
        this.keyToValueMap = copyObject.keyToValueMap
        this.valueToKeyMap = copyObject.valueToKeyMap
    }

    /**
     * @return
     */
    override fun getKeyToValue(): Map<String, String> {
        return Languages.getIdToValueMap()
    }

    /**
     * @return
     */
    override fun getValueToKey(): Map<String, String> {
        return Languages.getValueToIdMap()
    }

    /**
     * @return the ISO_8859 encoding for Datatypes of this type
     */
    override fun getTextEncodingCharSet(): Charset {
        return StandardCharsets.ISO_8859_1
    }
}