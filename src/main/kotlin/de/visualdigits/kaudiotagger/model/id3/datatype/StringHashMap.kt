package de.visualdigits.kaudiotagger.model.id3.datatype

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
     * @param value
     */
    override fun setValue(value: Any?) {
        if (value is String) {
            //Issue #273 temporary hack for MM
            if (value.equals("XXX")) {
                super.setValue(value)
            } else {
                super.setValue((value as? String)?.lowercase())
            }
        } else {
            super.setValue(value)
        }
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

    /**
     * @return
     */
    override fun toString(): String {
        val keyToValue = getKeyToValue()
        val value = getValue()
        if (value == null || keyToValue.get(value) == null) {
            return ""
        } else {
            return keyToValue.get(value)?:""
        }
    }
}