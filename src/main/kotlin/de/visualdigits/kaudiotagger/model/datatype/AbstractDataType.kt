package de.visualdigits.kaudiotagger.model.datatype

import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.frame.framebody.AbstractTagFrameBody
import org.slf4j.LoggerFactory

abstract class AbstractDataType {

    val identifier: String
    var frameBody: AbstractTagFrameBody? = null
    private var value: Any? = null

    constructor(
        identifier: String = "",
        frameBody: AbstractTagFrameBody? = null,
        value: Any? = null
    ) {
        this.identifier = identifier
        this.frameBody = frameBody
        this.value = value
    }

    val log = LoggerFactory.getLogger(javaClass)

    companion object {

        const val TYPE_ELEMENT: String = "element"
    }

    /**
     * Holds the size of the data in file when read/written
     */
    private var size: Int = 0

    /**
     * This is used by subclasses, to clone the data within the copyObject
     *
     *
     * TODO:It seems to be missing some of the more complex value types.
     *
     * @param copyObject
     */
    constructor(copyObject: AbstractDataType): this(copyObject.identifier?:error("No identifier")) {
        // no copy constructor in super class
        this.value = when (val obj = copyObject.value) {
            is String -> obj
            is Boolean -> obj
            is Byte -> obj
            is Character -> obj
            is Double -> obj
            is Float -> obj
            is Integer -> obj
            is Long -> obj
            is Short -> obj

//            is MultipleTextEncodedStringNullTerminated.Values -> obj
//            is PairedTextEncodedStringNullTerminated.ValuePairs valuePairs -> obj
//            is PartOfSet.PartOfSetValue partOfSetValue -> obj
            is BooleanArray -> obj.clone()
            is ByteArray -> obj.clone()
            is CharArray -> obj.clone()
            is DoubleArray -> obj.clone()
            is FloatArray -> obj.clone()
            is IntArray -> obj.clone()
            is LongArray -> obj.clone()
            is ShortArray -> obj.clone()
            is Array<*> -> obj.clone()
            is List<*> -> obj.toList()
            is Map<*,*> -> obj.toMap()

            else -> null
        }
    }

    /**
     * Simplified wrapper for reading bytes from file into Object.
     * Used for reading Strings, this class should be overridden
     * for non String Objects
     *
     * @param arr
     * @throws InvalidDataTypeException
     */
    fun readByteArray(arr: ByteArray) {
        readByteArray(arr, 0)
    }

    /**
     * This is the starting point for reading bytes from the file into the ID3 datatype
     * starting at offset.
     * This class must be overridden
     *
     * @param arr
     * @param offset
     * @throws InvalidDataTypeException
     */
    abstract fun readByteArray(arr: ByteArray, offset: Int)

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

    fun getValue(): Any? = value

    fun setValue(value: Any?) {
        this.value = value
    }

    /**
     * Starting point write ID3 Datatype back to array of bytes.
     * This class must be overridden.
     *
     * @return the array of bytes representing this datatype that should be written to file
     */
    abstract fun writeByteArray(): ByteArray

    /**
     * Return String Representation of Datatype     *
     */
    fun createStructure() {
        MP3File.tagFormatter?.addElement(
            identifier,
            value.toString()
        )
    }
}