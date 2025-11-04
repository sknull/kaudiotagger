package de.visualdigits.kaudiotagger.model.id3.datatype

import de.visualdigits.kaudiotagger.model.common.frame.framebody.AbstractTagFrameBody
import de.visualdigits.kaudiotagger.util.ID3Tags.copyValue
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class AbstractDataType {

    val log: Logger = LoggerFactory.getLogger(javaClass)

    companion object {

        const val TYPE_ELEMENT: String = "element"
    }

    var identifier: String?
    private var frameBody: AbstractTagFrameBody? = null
    private var value: Any? = null

    /**
     * Holds the size of the data in file when read/written
     */
    private var size: Int = 0

    constructor(
        identifier: String? = null,
        frameBody: AbstractTagFrameBody? = null,
        value: Any? = null
    ) {
        this.identifier = identifier
        this.frameBody = frameBody
        this.value = value
    }

    /**
     * This is used by subclasses, to clone the data within the copyObject
     * <p>
     *
     * @param copyObject
     */
    constructor(copyObject: AbstractDataType): this(copyObject.identifier) {
        this.value = copyObject.value?.let { v -> copyValue(v) }

        log.debug("Set value '{}' to '{}'", identifier, value)
    }

    /**
     * Simplified wrapper for reading bytes from file into Object.
     * Used for reading Strings, this class should be overridden
     * for non String Objects
     *
     * @param byteArray
     */
    fun readByteArray(byteArray: ByteArray) {
        readByteArray(byteArray, 0)
    }

    /**
     * This is the starting point for reading bytes from the file into the ID3 datatype
     * starting at offset.
     * This class must be overridden
     *
     * @param byteArray
     * @param offset
     */
    abstract fun readByteArray(byteArray: ByteArray, offset: Int)

    /**
     * This defines the size in bytes of the datatype being
     * held when read/written to file.
     *
     * @return the size in bytes of the datatype
     */
    open fun getSize(): Int = size

    fun setSize(size: Int) {
        this.size = size
    }

    fun addSize(amount: Int) {
        this.size += amount
    }

    /**
     * Get the framebody associated with this datatype
     *
     * @return the framebody that this datatype is associated with
     */
    fun getBody(): AbstractTagFrameBody? {
        return frameBody
    }

    /**
     * Set the framebody that this datatype is associated with
     *
     * @param frameBody
     */
    open fun setBody(frameBody: AbstractTagFrameBody?) {
        this.frameBody = frameBody
    }

    open fun getValue(): Any? = value

    open fun toByte(): Byte? {
        return when (val value = getValue()) {
            is Byte -> value
            is Number -> value.toByte()
            else -> null
        }
    }

    open fun setValue(value: Any?) {
        this.value = value
        when (value) {
            is String -> this.value = value.trimEnd { c -> c.code == 0 } // trim trailing null byte
            else ->
                this.value = value
        }
    }

    /**
     * Starting point write ID3 Datatype back to array of bytes.
     * This class must be overridden.
     *
     * @return the array of bytes representing this datatype that should be written to file
     */
    abstract fun writeByteArray(): ByteArray?
}