package de.visualdigits.kaudiotagger.model.datatype

import de.visualdigits.kaudiotagger.model.datatype.types.Languages
import de.visualdigits.kaudiotagger.model.frame.framebody.AbstractTagFrameBody
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

class StringHashMap: StringFixedLength, HashMapInterface<String, String> {

    /**
     *
     */
    var keyToValue: Map<String, String> = mapOf()

    /**
     *
     */
    var valueToKey: Map<String, String> = mapOf()

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
        identifier: String,
        frameBody: AbstractTagFrameBody,
        size: Int
    ): super(identifier, frameBody, size) {
        if (identifier == DataTypes.OBJ_LANGUAGE) {
            valueToKey = Languages.getValueToIdMap()
            keyToValue = Languages.getIdToValueMap()
        } else {
            throw IllegalArgumentException(
                "Hashmap identifier not defined in this class: " + identifier
            )
        }
    }

    constructor(copyObject: StringHashMap): super(copyObject) {
        this.hasEmptyValue = copyObject.hasEmptyValue
        this.keyToValue = copyObject.keyToValue
        this.valueToKey = copyObject.valueToKey
    }

    /**
     * @return
     */
    override fun getKeyToValue(): Map<String, String> {
        return keyToValue
    }

    /**
     * @return
     */
    override fun getValueToKey(): Map<String, String> {
        return valueToKey
    }

    /**
     * @param value
     */
    fun setValue(value: Object) {
        if (value is String) {
            //Issue #273 temporary hack for MM
            if (value.equals("XXX")) {
                this.value = value.toString();
            } else {
                this.value = (value as String).lowercase();
            }
        } else {
            this.value = value;
        }
    }

    /**
     * @return the ISO_8859 encoding for Datatypes of this type
     */
    override fun getTextEncodingCharSet(): Charset {
        return StandardCharsets.ISO_8859_1
    }
}