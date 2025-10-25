package de.visualdigits.kaudiotagger.util

import de.visualdigits.kaudiotagger.model.exceptions.TagException
import de.visualdigits.kaudiotagger.model.frame.id3.ID3Frames
import de.visualdigits.kaudiotagger.model.datatype.types.ID3v22Frames
import de.visualdigits.kaudiotagger.model.datatype.types.ID3v23Frames
import de.visualdigits.kaudiotagger.model.datatype.types.ID3v24Frames
import de.visualdigits.kaudiotagger.util.ID3Tags.copyObject

object ID3Tags {

    /**
     * Returns true if the identifier is a valid ID3v2.2 frame identifier
     *
     * @param identifier string to test
     * @return true if the identifier is a valid ID3v2.2 frame identifier
     */
    fun isID3v22FrameIdentifier(identifier: String?): Boolean {
        //If less than 3 cant be an identifier
        if ((identifier?.length?:0) < 3) {
            return false
        } else {
            return ((identifier?.length?:0) == 3 && ID3v22Frames.contains(identifier))
        }
    }

    /**
     * Returns true if the identifier is a valid ID3v2.3 frame identifier
     *
     * @param identifier string to test
     * @return true if the identifier is a valid ID3v2.3 frame identifier
     */
    fun isID3v23FrameIdentifier(identifier: String?): Boolean {
        return ((identifier?.length?:0) >= 4 && ID3v23Frames.contains(identifier?.take(4)))
    }

    /**
     * Returns true if the identifier is a valid ID3v2.4 frame identifier
     *
     * @param identifier string to test
     * @return true if the identifier is a valid ID3v2.4 frame identifier
     */
    fun isID3v24FrameIdentifier(identifier: String?): Boolean {
        return ((identifier?.length?:0) >= 4 && ID3v24Frames.contains(identifier?.take(4)))
    }

    /**
     * Convert from ID3v22 FrameIdentifier to ID3v23
     *
     * @param identifier
     * @return
     */
    fun convertFrameID22To23(identifier: String?): ID3v23Frames? {
        if ((identifier?.length?:0) < 3) {
            return null
        }
        return ID3Frames.convertv22Tov23[ID3v22Frames.fromId(identifier?.take( 3))]
    }

    /**
     * Convert from ID3v22 FrameIdentifier to ID3v24
     *
     * @param identifier
     * @return
     */
    fun convertFrameID22To24(identifier: String?): ID3v24Frames? {
        //Idv22 identifiers are only of length 3 times
        if ((identifier?.length?:0) < 3) {
            return null
        }
        //Has idv22 been mapped to v23
        val v23id = ID3Frames.convertv22Tov23[ID3v22Frames.fromId(identifier?.take(3))]
        if (v23id != null) {
            //has v2.3 been mapped to v2.4
            val v24id = ID3Frames.convertv23Tov24[v23id]
            if (v24id == null) {
                //if not it may be because v2.3 and and v2.4 are same so wont be
                //in mapping
                if (ID3v24Frames.contains(v23id.id)) {
                    return ID3v24Frames.fromId(v23id.id)
                } else {
                    return null
                }
            } else {
                return v24id
            }
        } else {
            return null
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

        //If it is a v23 identifier
        if (ID3v23Frames.contains(identifier)) {
            //If only name has changed  v22 and modified in v23 return result of.
            return ID3Frames.convertv23Tov22[ID3v23Frames.fromId(identifier?.take(4))]?.id
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

        //If it is a ID3v23 identifier
        if (ID3v23Frames.contains(identifier)) {
            //If no change between ID3v23 and ID3v24 should be in ID3v24 list.
            if (ID3v24Frames.contains(identifier)) {
                return ID3v24Frames.fromId(identifier)?.id
            } else {
                return ID3Frames.convertv23Tov24[ID3v23Frames.fromId(identifier?.take(4))]?.id
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
        return ID3Frames.forcev22Tov23[ID3v22Frames.fromId(identifier)]?.id
    }

    /**
     * Force from ID3v22 FrameIdentifier to ID3v23, this is where the frame and structure
     * has changed from v2 to v3 but we can still do some kind of conversion.
     *
     * @param identifier
     * @return
     */
    fun forceFrameID23To22(identifier: String?): String? {
        return ID3Frames.forcev23Tov22[ID3v23Frames.fromId(identifier)]?.id
    }

    /**
     * Force from ID3v2.30 FrameIdentifier to ID3v2.40, this is where the frame and structure
     * has changed from v3 to v4 but we can still do some kind of conversion.
     *
     * @param identifier
     * @return
     */
    fun forceFrameID23To24(identifier: String?): String? {
        return ID3Frames.forcev23Tov24[ID3v23Frames.fromId(identifier)]?.id
    }

    /**
     * Force from ID3v2.40 FrameIdentifier to ID3v2.30, this is where the frame and structure
     * has changed between v4 to v3 but we can still do some kind of conversion.
     *
     * @param identifier
     * @return
     */
    fun forceFrameID24To23(identifier: String?): String? {
        return ID3Frames.forcev24Tov23[ID3v24Frames.fromId(identifier)]?.id
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
        var v23id = ID3Frames.convertv24Tov23[ID3v24Frames.fromId(identifier)]
        if (v23id == null) {
            if (ID3v23Frames.contains(identifier)) {
                v23id = ID3v23Frames.fromId(identifier)
            }
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
     * @throws IllegalArgumentException if no suitable constructor exists
     */
    fun copyObject(copyObject: Any?): Any? {
        return try {
            copyObject?.let { co ->
                val constructorParameterArray = Array<Class<*>>(1, { co.javaClass })
                val constructor = co.javaClass
                    .getConstructor(*constructorParameterArray)
                val parameterArray = Array(1, { co })
                parameterArray.let { pa ->
                    constructor.newInstance(*pa)
                }
            }
        } catch (e: Exception) {
            throw IllegalStateException("Something went wrong", e)
        }
    }

    /**
     * Find the first whole number that can be parsed from the string
     *
     * @param str string to search
     * @return first whole number that can be parsed from the string
     */
    fun findNumber(str: String): Long {
        return findNumber(str, 0)
    }


    /**
     * Find the first whole number that can be parsed from the string
     *
     * @param str    string to search
     * @param offset start seaching from this index
     * @return first whole number that can be parsed from the string
     */
    fun findNumber(str: String, offset: Int): Long {
        if ((offset < 0) || (offset >= str.length)) {
            throw IndexOutOfBoundsException(
                "Offset to image string is out of bounds: offset = $offset, string.length${str.length}"
            )
        }
        var i: Int = offset
        var num: Long
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
            throw TagException("Unable to find integer in string: $str")
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
    fun truncate(str: String?, len: Int): String {
        if (len < 0) {
            return ""
        }
        return if ((str?.length ?: Int.MIN_VALUE) > len) {
            str?.take(len)
        } else {
            str
        }?:""
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
}