package de.visualdigits.kaudiotagger.model.common.frame.framebody

import de.visualdigits.kaudiotagger.model.common.frame.AbstractTagFrame
import de.visualdigits.kaudiotagger.model.common.tag.AbstractTagItem
import de.visualdigits.kaudiotagger.model.common.types.ByteRepresentation
import de.visualdigits.kaudiotagger.model.id3.datatype.AbstractDataType
import de.visualdigits.kaudiotagger.model.id3.datatype.DataTypes
import de.visualdigits.kaudiotagger.util.ID3Tags

abstract class AbstractTagFrameBody : AbstractTagItem {

    /**
     * List of data types that make up this particular frame body.
     */
    var objectList: MutableList<AbstractDataType> = mutableListOf()

    /**
     * Reference to the header associated with this frame body, a framebody can be created without a header
     * but one it is associated with a header this should be set. It is principally useful for the framebody to know
     * its header, because this will specify its tag version and some framebodies behave slighly different
     * between tag versions.
     */
    var header: AbstractTagFrame? = null

    constructor() {
        setupObjectList()
    }

    /**
     * Copy Constructor for fragment body. Copies all objects in the
     * Object Iterator with data.
     *
     * @param copyObject
     */
    constructor(copyObject: AbstractTagFrameBody) {
        copyObject.objectList.forEach { o ->
            val newObject = ID3Tags.copyObject(o) as AbstractDataType
            newObject.setBody(this)
            this.objectList.add(newObject)
        }
    }

    /**
     * Return the Text Encoding
     *
     * @return the text encoding used by this framebody
     */
    fun getTextEncoding(): Byte {
        val type = getObject(DataTypes.OBJ_TEXT_ENCODING)
        return type
            ?.let { o ->
                when (val value = o.getValue()) {
                    is Byte -> value
                    is Number -> value.toByte()
                    is AbstractDataType -> value.toByte()
                    is ByteRepresentation -> value.toByte()
                    else -> {
                        if (value == null) {
                            value
                        } else {
                            error("Unexpected value class: ${value?.javaClass}")
                        }
                    }
                }
            } ?: 0
    }

    /**
     * Set the Text Encoding to use for this frame body
     *
     * @param textEncoding to use for this frame body
     */
    open fun setTextEncoding(textEncoding: Byte?) {
        // Number HashMap actually converts this byte to a long
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding)
    }

    /**
     * Sets all objects of identifier type to value defined by `obj` argument.
     *
     * @param identifier `MP3Object` identifier
     * @param value      new datatype value
     */
    fun setObjectValue(identifier: String?, value: Any?) {
        objectList
            .find { obj ->
                obj.identifier == identifier
            }
            ?.also { obj ->
                obj.setValue(value)
            }
    }

    /**
     * Returns the datatype with the specified
     * `identifier`
     *
     * @param identifier
     * @return the datatype with the specified
     * `identifier`
     */
    fun getObject(identifier: String): AbstractDataType? {
        return objectList.find { obj -> obj.identifier == identifier }
    }

    /**
     * @return the text value that the user would expect to see for this framebody type, this should be overridden
     * for all frame-bodies
     */
    open fun getUserFriendlyValue(): String? {
        return toString()
    }

    /**
     * Return brief description of FrameBody
     *
     * @return brief description of FrameBody
     */
    override fun toString(): String {
        return getBriefDescription()
    }

    /**
     * This method calls `toString` for all it's objects and appends
     * them without any newline characters.
     *
     * @return brief description string
     */
    open fun getBriefDescription(): String {
        return objectList.joinToString("; ") { obj -> "${obj.identifier}=\"$obj\"" }
    }

    /**
     * This method calls `toString` for all it's objects and appends
     * them. It contains new line characters and is more suited for display
     * purposes
     *
     * @return formatted description string
     */
    fun getLongDescription(): String {
        return objectList.joinToString("\n") { obj -> "${obj.identifier}=\"$obj\"" }
    }

    /**
     * Returns the value of the datatype with the specified
     * `identifier`
     *
     * @param identifier
     * @return the value of the dattype with the specified
     * `identifier`
     */
    fun getObjectValue(identifier: String): Any? {
        return getObject(identifier)?.getValue()
    }

    /**
     * Returns true if this instance and its entire DataType
     * array list is a subset of the argument. This class is a subset if it is
     * the same class as the argument.
     *
     * @param obj datatype to determine subset of
     * @return true if this instance and its entire datatype array list is a
     * subset of the argument.
     */
    override fun isSubsetOf(obj: Any?): Boolean {
        if (obj !is AbstractTagFrameBody) {
            return false
        }
        val superset = obj.objectList
        objectList.forEach { anObjectList ->
            if (anObjectList.getValue() != null) {
                if (!superset.contains(anObjectList)) {
                    return false
                }
            }
        }
        return true
    }

    /**
     * Returns the size in bytes of this fragmentbody
     *
     * @return estimated size in bytes of this datatype
     */
    override fun getSize(): Int {
        return objectList.sumOf { o -> o.getSize() }
    }

    /**
     * Create the list of Datatypes that this body
     * expects in the correct order This method needs to be implemented by concrete subclasses
     */
    abstract fun setupObjectList()

    open fun createStructure() {
    }
}