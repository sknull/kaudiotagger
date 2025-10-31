package de.visualdigits.kaudiotagger.model.lyrics3.field.framebody

import java.io.RandomAccessFile
import java.nio.ByteBuffer

class FieldFrameBodyUnsupported : AbstractLyrics3v2FieldFrameBody {
    var value: ByteArray? = null

    /**
     * Creates a new FieldBodyUnsupported datatype.
     */
    constructor()

    constructor(copyObject: FieldFrameBodyUnsupported) : super(copyObject) {
        this.value = copyObject.value?.clone()
    }

    /**
     * Creates a new FieldBodyUnsupported datatype.
     *
     * @param value
     */
    constructor(value: ByteArray) {
        this.value = value
    }

    /**
     * Creates a new FieldBodyUnsupported datatype.
     *
     * @param byteBuffer
     */
    constructor(byteBuffer: ByteBuffer) {
        this.read(byteBuffer)
    }

    /**
     * @param byteBuffer
     */
    override fun read(byteBuffer: ByteBuffer?): Boolean {
        if (byteBuffer == null) {
            return false
        }
        val size: Int
        val buffer = ByteArray(5)

        // read the 5 character size
        byteBuffer.get(buffer, 0, 5)
        size = String(buffer, 0, 5).toInt()

        value = ByteArray(size)

        // read the SIZE length description
        byteBuffer.get(value)

        return false
    }

    /**
     * @param obj
     * @return
     */
    override fun isSubsetOf(obj: Any?): Boolean {
        if (obj !is FieldFrameBodyUnsupported) {
            return false
        }

        val subset = value?.let { v -> String(v) }
        val superset = obj.value?.let { v -> String(v) }

        return subset?.let { s -> superset?.contains(s) } == true && super.isSubsetOf(obj)
    }

    override fun toString(): String {
        return "${getIdentifier()} : ${value?.let { v -> String(v) }}"
    }

    override fun getIdentifier(): String {
        return "ZZZ"
    }

    /**
     * @param file
     */
    override fun write(file: RandomAccessFile) {
        var offset = 0
        val buffer = ByteArray(5)

        val str = value?.size?.toString()

        for (i in 0..<(5 - (str?.length?:0))) {
            buffer[i] = '0'.code.toByte()
        }

        offset += (5 - (str?.length?:0))

        for (i in 0..< (str?.length?:0)) {
            str?.get(i)?.code?.toByte()?.also { b -> buffer[i + offset] = b }
        }

        file.write(buffer)

        file.write(value)
    }

    /**
     * TODO
     */
    override fun setupObjectList() {
    }
}