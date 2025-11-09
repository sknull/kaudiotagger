package de.visualdigits.kaudiotagger.util

import de.visualdigits.kaudiotagger.model.id3.datatype.BooleanByte
import de.visualdigits.kaudiotagger.model.id3.datatype.BooleanString
import de.visualdigits.kaudiotagger.model.id3.datatype.ByteArraySizeTerminated
import de.visualdigits.kaudiotagger.model.id3.datatype.EventTimingCode
import de.visualdigits.kaudiotagger.model.id3.datatype.EventTimingCodeList
import de.visualdigits.kaudiotagger.model.id3.datatype.ID3v2LyricLine
import de.visualdigits.kaudiotagger.model.id3.datatype.NumberFixedLength
import de.visualdigits.kaudiotagger.model.id3.datatype.NumberHashMap
import de.visualdigits.kaudiotagger.model.id3.datatype.NumberVariableLength
import de.visualdigits.kaudiotagger.model.id3.datatype.PairedTextEncodedStringNullTerminated
import de.visualdigits.kaudiotagger.model.id3.datatype.PartOfSet
import de.visualdigits.kaudiotagger.model.id3.datatype.PartOfSetValue
import de.visualdigits.kaudiotagger.model.id3.datatype.StringDate
import de.visualdigits.kaudiotagger.model.id3.datatype.StringFixedLength
import de.visualdigits.kaudiotagger.model.id3.datatype.StringHashMap
import de.visualdigits.kaudiotagger.model.id3.datatype.StringNullTerminated
import de.visualdigits.kaudiotagger.model.id3.datatype.StringSizeTerminated
import de.visualdigits.kaudiotagger.model.id3.datatype.TCONString
import de.visualdigits.kaudiotagger.model.id3.datatype.TextEncodedStringNullTerminated
import de.visualdigits.kaudiotagger.model.id3.datatype.TextEncodedStringSizeTerminated
import de.visualdigits.kaudiotagger.model.id3.datatype.ValuePairs
import de.visualdigits.kaudiotagger.model.id3.frame.ID3Frames
import de.visualdigits.kaudiotagger.model.id3.tag.AbstractID3v2Tag
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v22Tag
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v23Tag
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v24Tag
import de.visualdigits.kaudiotagger.model.id3.types.ID3v22FrameId
import de.visualdigits.kaudiotagger.model.id3.types.ID3v23FrameId
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import de.visualdigits.kaudiotagger.model.id3.types.ID3v2Version
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.LinkedList

object ID3Tags {

    val log: Logger = LoggerFactory.getLogger(javaClass)

    /**
     * If using ID3 format convert tag from current version to another as specified by id3V2Version,
     *
     * @return the converted tag or the original if no conversion necessary
     */
    fun convertID3Tag(
        tag: AbstractID3v2Tag,
        id3V2Version: ID3v2Version
    ): AbstractID3v2Tag? {
        return when (tag) {
            is ID3v24Tag -> {
                when (id3V2Version) {
                    ID3v2Version.ID3_V22 -> ID3v22Tag(tag)
                    ID3v2Version.ID3_V23 -> ID3v23Tag(tag)
                    ID3v2Version.ID3_V24 -> tag
                }
            }

            is ID3v23Tag -> {
                when (id3V2Version) {
                    ID3v2Version.ID3_V22 -> ID3v22Tag(tag)
                    ID3v2Version.ID3_V23 -> tag
                    ID3v2Version.ID3_V24 -> ID3v24Tag(tag)
                }
            }

            is ID3v22Tag -> {
                when (id3V2Version) {
                    ID3v2Version.ID3_V22 -> tag
                    ID3v2Version.ID3_V23 -> ID3v23Tag(tag)
                    ID3v2Version.ID3_V24 -> ID3v24Tag(tag)
                }
            }

            else -> null
        }
    }

    /**
     * Returns true if the identifier is a valid ID3v2.2 frame identifier
     *
     * @param identifier string to test
     * @return true if the identifier is a valid ID3v2.2 frame identifier
     */
    fun isID3v22FrameIdentifier(identifier: String?): Boolean {
        // If less than 3 cant be an identifier
        return if ((identifier?.length?:0) < 3) {
            false
        } else {
            ((identifier?.length?:0) == 3 && ID3v22FrameId.contains(identifier))
        }
    }

    /**
     * Returns true if the identifier is a valid ID3v2.3 frame identifier
     *
     * @param identifier string to test
     * @return true if the identifier is a valid ID3v2.3 frame identifier
     */
    fun isID3v23FrameIdentifier(identifier: String?): Boolean {
        return ((identifier?.length?:0) >= 4 && ID3v23FrameId.contains(identifier?.take(4)))
    }

    /**
     * Returns true if the identifier is a valid ID3v2.4 frame identifier
     *
     * @param identifier string to test
     * @return true if the identifier is a valid ID3v2.4 frame identifier
     */
    fun isID3v24FrameIdentifier(identifier: String?): Boolean {
        return ((identifier?.length?:0) >= 4 && ID3v24FrameId.contains(identifier?.take(4)))
    }

    /**
     * Convert from ID3v22 FrameIdentifier to ID3v23
     *
     * @param identifier
     * @return
     */
    fun convertFrameID22To23(identifier: String?): ID3v23FrameId? {
        if ((identifier?.length?:0) < 3) {
            return null
        }
        return ID3Frames.convertv22Tov23[ID3v22FrameId.fromId(identifier?.take( 3))]
    }

    /**
     * Convert from ID3v22 FrameIdentifier to ID3v24
     *
     * @param identifier
     * @return
     */
    fun convertFrameID22To24(identifier: String?): ID3v24FrameId? {
        // Idv22 identifiers are only of length 3 times
        if ((identifier?.length?:0) < 3) {
            return null
        }
        // Has idv22 been mapped to v23
        val v23id = ID3Frames.convertv22Tov23[ID3v22FrameId.fromId(identifier?.take(3))]
        return if (v23id != null) {
            // has v2.3 been mapped to v2.4
            val v24id = ID3Frames.convertv23Tov24[v23id]
            if (v24id == null) {
                // if not it may be because v2.3 and and v2.4 are same so wont be
                // in mapping
                if (ID3v24FrameId.contains(v23id.id)) {
                    ID3v24FrameId.fromId(v23id.id)
                } else {
                    null
                }
            } else {
                v24id
            }
        } else {
            null
        }
    }

    /**
     * Convert from ID3v23 FrameIdentifier to ID3v22
     *
     * @param identifier
     * @return
     */
    fun convertFrameID23To22(identifier: String?): String? {
        if ((identifier?.length?:0) < 4) {
            return null
        }

        // If it is a v23 identifier
        if (ID3v23FrameId.contains(identifier)) {
            // If only name has changed  v22 and modified in v23 return result of.
            return ID3Frames.convertv23Tov22[ID3v23FrameId.fromId(identifier?.take(4))]?.id
        }
        return null
    }

    /**
     * Convert from ID3v23 FrameIdentifier to ID3v24
     *
     * @param identifier
     * @return
     */
    fun convertFrameID23To24(identifier: String?): String? {
        if ((identifier?.length?:0) < 4) {
            return null
        }

        // If it is a ID3v23 identifier
        if (ID3v23FrameId.contains(identifier)) {
            // If no change between ID3v23 and ID3v24 should be in ID3v24 list.
            return if (ID3v24FrameId.contains(identifier)) {
                ID3v24FrameId.fromId(identifier)?.id
            } else {
                ID3Frames.convertv23Tov24[ID3v23FrameId.fromId(identifier?.take(4))]?.id
            }
        }
        return null
    }

    /**
     * Force from ID3v22 FrameIdentifier to ID3v23, this is where the frame and structure
     * has changed from v2 to v3 but we can still do some kind of conversion.
     *
     * @param identifier
     * @return
     */
    fun forceFrameID22To23(identifier: String?): String? {
        return ID3Frames.forcev22Tov23[ID3v22FrameId.fromId(identifier)]?.id
    }

    /**
     * Force from ID3v22 FrameIdentifier to ID3v23, this is where the frame and structure
     * has changed from v2 to v3 but we can still do some kind of conversion.
     *
     * @param identifier
     * @return
     */
    fun forceFrameID23To22(identifier: String?): String? {
        return ID3Frames.forcev23Tov22[ID3v23FrameId.fromId(identifier)]?.id
    }

    /**
     * Force from ID3v2.30 FrameIdentifier to ID3v2.40, this is where the frame and structure
     * has changed from v3 to v4 but we can still do some kind of conversion.
     *
     * @param identifier
     * @return
     */
    fun forceFrameID23To24(identifier: String?): String? {
        return ID3Frames.forcev23Tov24[ID3v23FrameId.fromId(identifier)]?.id
    }

    /**
     * Force from ID3v2.40 FrameIdentifier to ID3v2.30, this is where the frame and structure
     * has changed between v4 to v3 but we can still do some kind of conversion.
     *
     * @param identifier
     * @return
     */
    fun forceFrameID24To23(identifier: String?): String? {
        return ID3Frames.forcev24Tov23[ID3v24FrameId.fromId(identifier)]?.id
    }

    /**
     * Convert from ID3v24 FrameIdentifier to ID3v23
     *
     * @param identifier
     * @return
     */
    fun convertFrameID24To23(identifier: String?): String? {
        if ((identifier?.length?:0) < 4) {
            return null
        }
        var v23id = ID3Frames.convertv24Tov23[ID3v24FrameId.fromId(identifier)]
        if (v23id == null && ID3v23FrameId.contains(identifier)) {
            v23id = ID3v23FrameId.fromId(identifier)
        }
        return v23id?.id
    }

    /**
     * Unable to instantiate abstract classes, so can't call the copy
     * constructor. So find out the instantiated class name and call the copy
     * constructor through reflection (e.g for a a FrameBody would have to have a constructor
     * that takes another frameBody as the same type as a parameter)
     *
     * @param copyObject
     * @return
     */
    fun copyObject(copyObject: Any?): Any? {
        val co =  try {
            copyObject?.let { co ->
                val constructorParameterArray = Array<Class<*>>(1, { co.javaClass })
                val constructor = co.javaClass.getConstructor(*constructorParameterArray)
                val parameterArray = Array(1, { co })
                constructor.newInstance(*parameterArray)            }
        } catch (e: Exception) {
            throw IllegalStateException("Something went wrong", e)
        }
        return co
    }

    inline fun <reified T : Any> copyValue(value: T): T {
        return when (value) {
            // simple types which can be copied over
            is String -> value
            is Boolean -> value
            is Byte -> value
            is Character -> value
            is Double -> value
            is Float -> value
            is Integer -> value
            is Long -> value
            is Short -> value

            // colleections to be cloned
            is BooleanArray -> value.clone()
            is ByteArray -> value.clone()
            is CharArray -> value.clone()
            is DoubleArray -> value.clone()
            is FloatArray -> value.clone()
            is IntArray -> value.clone()
            is LongArray -> value.clone()
            is ShortArray -> value.clone()
            is Array<*> -> value.clone()
            is ArrayList<*> -> value.clone()
            is LinkedList<*> -> value.clone()

            // complex objects which have a copy constructor
            is BooleanByte -> BooleanByte(value)
            is BooleanString -> BooleanString(value)
            is ByteArraySizeTerminated -> ByteArraySizeTerminated(value)
            is EventTimingCode -> EventTimingCode(value)
            is EventTimingCodeList -> EventTimingCodeList(value)
            is ID3v2LyricLine -> ID3v2LyricLine(value)
            is NumberHashMap -> NumberHashMap(value)
            is NumberFixedLength -> NumberFixedLength(value)
            is NumberVariableLength -> NumberVariableLength(value)
            is PairedTextEncodedStringNullTerminated -> PairedTextEncodedStringNullTerminated(value)
            is PartOfSet -> PartOfSet(value)
            is PartOfSetValue -> PartOfSetValue(value)
            is StringDate -> StringDate(value)
            is StringHashMap -> StringHashMap(value)
            is StringFixedLength -> StringFixedLength(value)
            is StringNullTerminated -> StringNullTerminated(value)
            is StringSizeTerminated -> StringSizeTerminated(value)
            is TCONString -> TCONString(value)
            is TextEncodedStringNullTerminated -> TextEncodedStringNullTerminated(value)
            is TextEncodedStringSizeTerminated -> TextEncodedStringSizeTerminated(value)
            is ValuePairs -> ValuePairs(value)
            else -> error("Unable to create copy of class ${value?.javaClass}")
        } as T
    }

    /**
     * Find the first whole number that can be parsed from the string
     *
     * @param str string to search
     * @return first whole number that can be parsed from the string
     */
    fun findNumber(str: String): Long? {
        return findNumber(str, 0)
    }


    /**
     * Find the first whole number that can be parsed from the string
     *
     * @param str    string to search
     * @param offset start seaching from this index
     * @return first whole number that can be parsed from the string
     */
    fun findNumber(str: String, offset: Int): Long? {
        if ((offset < 0) || (offset >= str.length)) {
            throw IndexOutOfBoundsException("Offset to image string is out of bounds: offset = $offset, string.length${str.length}")
        }
        var i: Int = offset
        var num: Long?
        while (i < str.length) {
            if (
                    ((str[i] >= '0') && (str[i] <= '9')) ||
                            (str[i] == '-')
            ) {
                break
            }
            i++
        }
        var j: Int = i + 1
        while (j < str.length) {
            if (((str[j] < '0') || (str[j] > '9'))) {
                break
            }
            j++
        }
        if ((j <= str.length) && (j > i)) {
            num = str.substring(i, j).toLong()
        } else {
            num = null
        }

        return num
    }
    
    /**
     * truncate a string if it longer than the argument
     *
     * @param str String to truncate
     * @param len maximum desired length of string
     * @return
     */
    fun truncate(str: String?, len: Int): String? {
        if (len < 0) {
            return null
        }
        return if ((str?.length ?: Int.MIN_VALUE) > len) {
            str?.take(len)
        } else {
            str
        }?:null
    }

    /**
     * Given an datatype, try to return it as a <code>long</code>. This tries to
     * parse a string, and takes <code>Long, Short, Byte, Integer</code>
     * objects and gets their value. An exception is not explicitly thrown
     * here because it would causes too many other methods to also throw it.
     *
     * @param value datatype to find long from.
     * @return <code>long</code> value
     */
    fun getWholeNumber(value: Any?): Long {
        return when (value) {
            is String -> {
                value.toLong()
            }
            is Byte -> {
                value.toLong()
            }
            is Short -> {
                value.toLong()
            }
            is Integer -> {
                value.toLong()
            }
            is Long -> {
                value
            }
            else -> null
        }?:error("Value of class '${value?.javaClass}' not supported")
    }

    /**
     * Remove all occurances of the given character from the string argument.
     *
     * @param str String to search
     * @param ch  character to remove
     * @return new String without the given charcter
     */
    fun stripChar(str: String?, ch: Char): String? {
        return if (str != null) {
            val buffer = CharArray(str.length)
            var next = 0
            (0..<str.length).forEach { i ->
                if (str[i] != ch) {
                    buffer[next++] = str[i]
                }
            }
            String(buffer, 0, next)
        } else {
            null
        }
    }
}