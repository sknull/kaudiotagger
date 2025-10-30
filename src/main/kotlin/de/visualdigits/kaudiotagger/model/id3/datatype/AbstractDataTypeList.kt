package de.visualdigits.kaudiotagger.model.id3.datatype

import de.visualdigits.kaudiotagger.model.common.frame.framebody.AbstractTagFrameBody

/**
 * Represents a list of [Cloneable](!!) [AbstractDataType]s, continuing until the end of the buffer.
 *
 * @author [Hendrik Schreiber](mailto:hs@tagtraum.com)
 * @version $Id:$
 */
abstract class AbstractDataTypeList<T : AbstractDataType>: AbstractDataType {

    constructor(
        identifier: String?,
        frameBody: AbstractTagFrameBody?
    ) : super(identifier, frameBody) {
        setValue(mutableListOf<T>())
    }

    /**
     * Copy constructor.
     * By convention, subclasses *must* implement a constructor, accepting an argument of their own class type
     * and call this constructor for [org.jaudiotagger.tag.id3.ID3Tags.copyObject] to work.
     * A parametrized `AbstractDataTypeList` is not sufficient.
     *
     * @param copy instance
     */
    constructor(copyObject: AbstractDataTypeList<T>): super(copyObject)

    /**
     * Reads list of [EventTimingCode]s from buffer starting at the given offset.
     *
     * @param buffer buffer
     * @param offset initial offset into the buffer
     */
    override fun readByteArray(buffer: ByteArray, offset: Int) {
        if (offset < 0) {
            throw IndexOutOfBoundsException(
                "Offset to byte array is out of bounds: offset = " +
                        offset +
                        ", array.length = " +
                        buffer.size
            )
        }

        // no events
        if (offset >= buffer.size) {
            getValue()?.clear()
            return
        }
        var currentOffset = offset
        while (currentOffset < buffer.size) {
            val data = createListElement()
            data!!.readByteArray(buffer, currentOffset)
            data.setBody(getBody())
            getValue()!!.add(data)
            currentOffset += data.getSize()
        }
    }

    override fun getValue(): MutableList<T>? {
        return super.getValue() as? MutableList<T>
    }

    override fun setValue(value: Any?) {
        when (value) {
            is MutableList<*> -> super.setValue(value)
            else -> {
                if (value != null) {
                    super.setValue(mutableListOf(value))
                } else {
                    super.setValue(mutableListOf<T>())
                }
            }
        }
    }

    /**
     * Factory method that creates new elements for this list.
     * Called from [.readByteArray].
     *
     * @return new list element
     */
    abstract fun createListElement(): T?

    /**
     * Write contents to a byte array.
     *
     * @return a byte array that that contains the data that should be persisted to file
     */
    override fun writeByteArray(): ByteArray? {
        log.debug("Writing DataTypeList " + this.identifier)
        val buffer = ByteArray(getSize())
        var offset = 0
        getValue()?.forEach { data ->
            val bytes = data.writeByteArray()?:error("Coulkd not write data")
            System.arraycopy(bytes, 0, buffer, offset, bytes.size)
            offset += bytes.size
        }

        return buffer
    }

    /**
     * Return the size in byte of this datatype list.
     *
     * @return the size in bytes
     */
    override fun getSize(): Int {
        var size = 0
        for (t in getValue()!!) {
            size += t.getSize()
        }
        return size
    }

    override fun hashCode(): Int {
        return if (getValue() != null) getValue().hashCode() else 0
    }

    override fun toString(): String {
        return if (getValue() != null) getValue().toString() else "{}"
    }
}
