package de.visualdigits.kaudiotagger.model.frame.framebody

import de.visualdigits.kaudiotagger.model.tag.lyrics3.AbstractLyrics3v2FieldFrameBody
import java.io.RandomAccessFile
import java.nio.ByteBuffer


class FieldFrameBodyUnsupported : AbstractLyrics3v2FieldFrameBody {
    /**
     *
     */
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
     * @throws org.jaudiotagger.tag.InvalidTagException
     */
    constructor(byteBuffer: ByteBuffer) {
        this.read(byteBuffer)
    }

    /**
     * @param byteBuffer
     * @throws IOException
     */
    override fun read(byteBuffer: ByteBuffer?) {
        if (byteBuffer == null) {
            return
        }
        val size: Int
        val buffer = ByteArray(5)

        // read the 5 character size
        byteBuffer.get(buffer, 0, 5)
        size = String(buffer, 0, 5).toInt()

        value = ByteArray(size)

        // read the SIZE length description
        byteBuffer.get(value)
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

    /**
     * @param obj
     * @return
     */
    override fun equals(obj: Any?): Boolean {
        if (obj !is FieldFrameBodyUnsupported) {
            return false
        }

        return (this.value.contentEquals(obj.value) && super.equals(obj)
                )
    }

    /**
     * @return
     */
    override fun toString(): String {
        return "${getIdentifier()} : ${value?.let { v -> String(v) }}"
    }

    /**
     * @return
     */
    override fun getIdentifier(): String {
        return "ZZZ"
    }

    /**
     * @param file
     * @throws IOException
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
