package de.visualdigits.kaudiotagger.model.lyrics3.datatype

import de.visualdigits.kaudiotagger.model.common.frame.framebody.AbstractTagFrameBody
import de.visualdigits.kaudiotagger.model.id3.datatype.AbstractDataType
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

    constructor(copyObject: Lyrics3Image): super(copyObject) {
        this.timeStamp = copyObject.timeStamp?.let { ts -> Lyrics3TimeStamp(ts) }
        this.description = copyObject.description
        this.filename = copyObject.filename
    }

    override fun getSize(): Int {
        var size: Int = (filename?.length?:0) + 2 + (description?.length?:0) + 2
        if (this.timeStamp != null) {
            size += timeStamp?.getSize()?:0
        }

        return size
    }

    override fun toString(): String {
        var str = "filename = $filename, description = $description"

        if (this.timeStamp != null) {
            str += (", timestamp = " + this.timeStamp)
        }

        return str + "\n"
    }

    override fun readByteArray(byteArray: ByteArray, offset: Int) {
        readString(byteArray.contentToString(), offset)
    }

    /**
     * @param imageString
     * @param offset
     */
    fun readString(imageString: String, offset: Int) {
        var offset = offset

        if ((offset < 0) || (offset >= imageString.length)) {
            throw IndexOutOfBoundsException(
                "Offset to image string is out of bounds: offset = " +
                        offset +
                        ", string.length()" +
                        imageString.length
            )
        }

        var delim: Int = imageString.indexOf("||", offset)
        filename = imageString.substring(offset, delim)

        offset = delim + 2
        delim = imageString.indexOf("||", offset)
        description = imageString.substring(offset, delim)

        offset = delim + 2
        val timestamp = imageString.substring(offset)

        if (timestamp.length == 7) {
            this.timeStamp = Lyrics3TimeStamp("Time Stamp")
            timeStamp?.readString(timestamp)
        }
    }

    override fun writeByteArray(): ByteArray {
        return writeString().toByteArray(StandardCharsets.ISO_8859_1)
    }

    fun writeString(): String {
        var str: String

        if (filename == null) {
            str = "||"
        } else {
            str = "$filename||"
        }

        if (description == null) {
            str += "||"
        } else {
            str += ("$description||")
        }

        if (this.timeStamp != null) {
            str += timeStamp?.writeString()
        }

        return str
    }
}
