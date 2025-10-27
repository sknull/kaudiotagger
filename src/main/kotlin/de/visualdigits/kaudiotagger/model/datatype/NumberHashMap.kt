package de.visualdigits.kaudiotagger.model.datatype

import de.visualdigits.kaudiotagger.model.datatype.types.ChannelTypes
import de.visualdigits.kaudiotagger.model.datatype.types.EventTimingTimestampTypes
import de.visualdigits.kaudiotagger.model.datatype.types.EventTimingTypes
import de.visualdigits.kaudiotagger.model.datatype.types.GenreTypes
import de.visualdigits.kaudiotagger.model.datatype.types.InterpolationTypes
import de.visualdigits.kaudiotagger.model.datatype.types.PictureTypes
import de.visualdigits.kaudiotagger.model.datatype.types.ReceivedAsTypes
import de.visualdigits.kaudiotagger.model.datatype.types.SynchronisedLyricsContentType
import de.visualdigits.kaudiotagger.model.datatype.types.TextEncoding
import de.visualdigits.kaudiotagger.model.exceptions.InvalidDataTypeException
import de.visualdigits.kaudiotagger.model.frame.framebody.AbstractTagFrameBody
import de.visualdigits.kaudiotagger.util.ErrorMessage

class NumberHashMap: NumberFixedLength, HashMapInterface<Int, String>  {

    /**
     * key to value map
     */
    var keyToValueMap: Map<Int, String> = mapOf()

    /**
     * value to key map
     */
    var valueToKeyMap: Map<String, Int> = mapOf()

    /**
     *
     */
    var hasEmptyValue = false

    constructor(
        identifier: String?,
        frameBody: AbstractTagFrameBody? = null,
        value: Any? = null
    ): super(identifier, frameBody, value)
    
    /**
     * Creates a new ObjectNumberHashMap datatype.
     *
     * @param identifier
     * @param frameBody
     * @param size
     * @throws IllegalArgumentException
     */
    constructor(
        identifier: String?,
        frameBody: AbstractTagFrameBody? = null,
        size: Int
    ): super(identifier, frameBody, size) {

        if (identifier == DataTypes.OBJ_GENRE) {
            valueToKeyMap = GenreTypes.getValueToIdMap()
            keyToValueMap = GenreTypes.getIdToValueMap()

            //genres can be an id or literal value
            hasEmptyValue = true
        } else if (identifier == DataTypes.OBJ_TEXT_ENCODING) {
            valueToKeyMap = TextEncoding.getValueToIdMap()
            keyToValueMap = TextEncoding.getIdToValueMap()
        } else if (identifier == DataTypes.OBJ_INTERPOLATION_METHOD) {
            valueToKeyMap = InterpolationTypes.getValueToIdMap()
            keyToValueMap = InterpolationTypes.getIdToValueMap()
        } else if (identifier == DataTypes.OBJ_PICTURE_TYPE) {
            valueToKeyMap = PictureTypes.getValueToIdMap()
            keyToValueMap = PictureTypes.getIdToValueMap()

            //Issue #224 Values should map, but have examples where they dont, this is a workaround
            hasEmptyValue = true
        } else if (identifier == DataTypes.OBJ_TYPE_OF_EVENT) {
            valueToKeyMap = EventTimingTypes.getValueToIdMap()
            keyToValueMap = EventTimingTypes.getIdToValueMap()
        } else if (identifier == DataTypes.OBJ_TIME_STAMP_FORMAT) {
            valueToKeyMap = EventTimingTimestampTypes.getValueToIdMap()
            keyToValueMap = EventTimingTimestampTypes.getIdToValueMap()
        } else if (identifier == DataTypes.OBJ_TYPE_OF_CHANNEL) {
            valueToKeyMap = ChannelTypes.getValueToIdMap()
            keyToValueMap = ChannelTypes.getIdToValueMap()
        } else if (identifier == DataTypes.OBJ_RECIEVED_AS) {
            valueToKeyMap = ReceivedAsTypes.getValueToIdMap()
            keyToValueMap = ReceivedAsTypes.getIdToValueMap()
        } else if (identifier == DataTypes.OBJ_CONTENT_TYPE) {
            valueToKeyMap =
                SynchronisedLyricsContentType.getValueToIdMap()
            keyToValueMap =
                SynchronisedLyricsContentType.getIdToValueMap()
        } else {
            throw IllegalArgumentException(
                "Hashmap identifier not defined in this class: $identifier"
            )
        }
    }

    constructor(copyObject: NumberHashMap): this(copyObject.identifier) {
        this.hasEmptyValue = copyObject.hasEmptyValue

        // we don't need to clone/copy the maps here because they are static
        this.keyToValueMap = copyObject.keyToValueMap
        this.valueToKeyMap = copyObject.valueToKeyMap
    }

    /**
     * Read the key from the buffer.
     *
     * @param arr
     * @param offset
     * @throws InvalidDataTypeException if emptyValues are not allowed and the eky was invalid.
     */
    override fun readByteArray(arr: ByteArray, offset: Int) {
        super.readByteArray(arr, offset)

        //Mismatch:Superclass uses Long, but maps expect Integer
        val intValue = (getValue() as Long).toInt()
        if (!keyToValueMap.containsKey(intValue)) {
            if (!hasEmptyValue) {
                throw InvalidDataTypeException(
                    ErrorMessage.MP3_REFERENCE_KEY_INVALID.getMsg(identifier, intValue)
                )
            } else if (identifier == DataTypes.OBJ_PICTURE_TYPE) {
                log.warn(ErrorMessage.MP3_PICTURE_TYPE_INVALID.getMsg(getValue()))
            }
        }
    }

    override fun getKeyToValue(): Map<Int, String> {
        return keyToValueMap
    }

    override fun getValueToKey(): Map<String, Int> {
        return valueToKeyMap
    }

    /**
     * @return
     */
    override fun toString(): String {
        return getValue()?.let { v -> keyToValueMap[v]}?:""
    }
}