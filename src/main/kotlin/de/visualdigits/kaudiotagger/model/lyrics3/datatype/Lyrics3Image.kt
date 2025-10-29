package de.visualdigits.kaudiotagger.model.lyrics3.datatype

import de.visualdigits.kaudiotagger.model.id3.datatype.AbstractDataType
import de.visualdigits.kaudiotagger.model.common.frame.framebody.AbstractTagFrameBody
import java.nio.charset.StandardCharsets


class Lyrics3Image : AbstractDataType {

    var timeStamp: Lyrics3TimeStamp? = null

    var description: String? = ""

    var filename: String? = ""

    /**
     * Creates a new ObjectLyrics3Image datatype.
     *
     * @param identifier
     * @param frameBody
     */
    constructor(identifier: String?, frameBody: AbstractTagFrameBody) : super(identifier, frameBody)

    constructor(copy: Lyrics3Image) : super(copy) {
        this.timeStamp = copy.timeStamp?.let { ts -> Lyrics3TimeStamp(ts) }
        this.description = copy.description
        this.filename = copy.filename
    }

    /**
     * @return
     */
    override fun getSize(): Int {
        var size: Int = (filename?.length?:0) + 2 + (description?.length?:0) + 2
        if (this.timeStamp != null) {
            size += timeStamp?.getSize()?:0
        }

        return size
    }

    /**
     * @param obj
     * @return
     */
    override fun equals(obj: Any?): Boolean {
        if (obj !is Lyrics3Image) {
            return false
        }

        if (this.description != obj.description) {
            return false
        }

        if (this.filename != obj.filename) {
            return false
        }

        if (this.timeStamp == null) {
            if (obj.timeStamp != null) {
                return false
            }
        } else {
            if (this.timeStamp?.equals(obj.timeStamp) == false) {
                return false
            }
        }

        return super.equals(obj)
    }

    /**
     * @return
     */
    override fun toString(): String {
        var str: String?
        str = "filename = " + filename + ", description = " + description

        if (this.timeStamp != null) {
            str += (", timestamp = " + this.timeStamp)
        }

        return str + "\n"
    }

    override fun readByteArray(arr: ByteArray, offset: Int) {
        readString(arr.toString(), offset)
    }

    /**
     * @param imageString
     * @param offset
     */
    fun readString(imageString: String, offset: Int) {
        var offset = offset
        if (imageString == null) {
            throw NullPointerException("Image string is null")
        }

        if ((offset < 0) || (offset >= imageString.length)) {
            throw IndexOutOfBoundsException(
                "Offset to image string is out of bounds: offset = " +
                        offset +
                        ", string.length()" +
                        imageString.length
            )
        }

        val timestamp: String?
        var delim: Int

        delim = imageString.indexOf("||", offset)
        filename = imageString.substring(offset, delim)

        offset = delim + 2
        delim = imageString.indexOf("||", offset)
        description = imageString.substring(offset, delim)

        offset = delim + 2
        timestamp = imageString.substring(offset)

        if (timestamp.length == 7) {
            this.timeStamp = Lyrics3TimeStamp("Time Stamp")
            timeStamp?.readString(timestamp)
        }
    }

    override fun writeByteArray(): ByteArray {
        return writeString().toByteArray(StandardCharsets.ISO_8859_1)
    }

    /**
     * @return
     */
    fun writeString(): String {
        var str: String

        if (filename == null) {
            str = "||"
        } else {
            str = filename + "||"
        }

        if (description == null) {
            str += "||"
        } else {
            str += (description + "||")
        }

        if (this.timeStamp != null) {
            str += timeStamp?.writeString()
        }

        return str
    }
}
