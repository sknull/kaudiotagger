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
import de.visualdigits.kaudiotagger.model.frame.framebody.AbstractTagFrameBody

class NumberHashMap: NumberFixedLength, HashMapInterface<Int, String>  {

    /**
     * key to value map
     */
    private var keyToValue: Map<Int, String> = mapOf()

    /**
     * value to key map
     */
    private var valueToKey: Map<String, Int> = mapOf()

    /**
     *
     */
    private var hasEmptyValue = false

    constructor(
        identifier: String,
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
        identifier: String,
        frameBody: AbstractTagFrameBody? = null,
        size: Int
    ): super(identifier, frameBody, size) {

        if (identifier == DataTypes.OBJ_GENRE) {
            valueToKey = GenreTypes.getValueToIdMap()
            keyToValue = GenreTypes.getIdToValueMap()

            //genres can be an id or literal value
            hasEmptyValue = true
        } else if (identifier == DataTypes.OBJ_TEXT_ENCODING) {
            valueToKey = TextEncoding.getValueToIdMap()
            keyToValue = TextEncoding.getIdToValueMap()
        } else if (identifier == DataTypes.OBJ_INTERPOLATION_METHOD) {
            valueToKey = InterpolationTypes.getValueToIdMap()
            keyToValue = InterpolationTypes.getIdToValueMap()
        } else if (identifier == DataTypes.OBJ_PICTURE_TYPE) {
            valueToKey = PictureTypes.getValueToIdMap()
            keyToValue = PictureTypes.getIdToValueMap()

            //Issue #224 Values should map, but have examples where they dont, this is a workaround
            hasEmptyValue = true
        } else if (identifier == DataTypes.OBJ_TYPE_OF_EVENT) {
            valueToKey = EventTimingTypes.getValueToIdMap()
            keyToValue = EventTimingTypes.getIdToValueMap()
        } else if (identifier == DataTypes.OBJ_TIME_STAMP_FORMAT) {
            valueToKey = EventTimingTimestampTypes.getValueToIdMap()
            keyToValue = EventTimingTimestampTypes.getIdToValueMap()
        } else if (identifier == DataTypes.OBJ_TYPE_OF_CHANNEL) {
            valueToKey = ChannelTypes.getValueToIdMap()
            keyToValue = ChannelTypes.getIdToValueMap()
        } else if (identifier == DataTypes.OBJ_RECIEVED_AS) {
            valueToKey = ReceivedAsTypes.getValueToIdMap()
            keyToValue = ReceivedAsTypes.getIdToValueMap()
        } else if (identifier == DataTypes.OBJ_CONTENT_TYPE) {
            valueToKey =
                SynchronisedLyricsContentType.getValueToIdMap()
            keyToValue =
                SynchronisedLyricsContentType.getIdToValueMap()
        } else {
            throw IllegalArgumentException(
                "Hashmap identifier not defined in this class: " + identifier
            )
        }
    }

    constructor(copyObject: NumberHashMap): this(copyObject.identifier) {
        this.hasEmptyValue = copyObject.hasEmptyValue

        // we don't need to clone/copy the maps here because they are static
        this.keyToValue = copyObject.keyToValue
        this.valueToKey = copyObject.valueToKey
    }

    override fun getKeyToValue(): Map<Int, String> {
        return keyToValue
    }

    override fun getValueToKey(): Map<String, Int> {
        return valueToKey
    }
}