package de.visualdigits.kaudiotagger.model.tag.lyrics3

import de.visualdigits.kaudiotagger.model.datatype.Lyrics3Image
import de.visualdigits.kaudiotagger.model.datatype.types.Lyrics3v2Fields
import de.visualdigits.kaudiotagger.model.exceptions.InvalidTagException
import de.visualdigits.kaudiotagger.util.TagOptionSingleton
import java.io.RandomAccessFile
import java.nio.ByteBuffer


class FieldFrameBodyIMG : AbstractLyrics3v2FieldFrameBody {
    /**
     *
     */
    var images = ArrayList<Lyrics3Image>()

    /**
     * Creates a new FieldBodyIMG datatype.
     */
    constructor()

    constructor(
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(byteBuffer, frameSize)

    constructor(copyObject: FieldFrameBodyIMG) : super(copyObject) {
        var old: Lyrics3Image?

        for (i in copyObject.images.indices) {
            old = copyObject.images.get(i)
            this.images.add(Lyrics3Image(old))
        }
    }

    /**
     * Creates a new FieldBodyIMG datatype.
     *
     * @param imageString
     */
    constructor(imageString: String) {
        readString(imageString)
    }

    /**
     * @param imageString
     */
    private fun readString(imageString: String) {
        // now read each picture and put in the vector;
        var image: Lyrics3Image?
        var token: String?
        var offset = 0
        var delim = imageString.indexOf(Lyrics3v2Fields.CRLF)
        images = ArrayList<Lyrics3Image>()

        while (delim >= 0) {
            token = imageString.substring(offset, delim)
            image = Lyrics3Image("Image", this)
            image.filename = token
            images.add(image)
            offset = delim + Lyrics3v2Fields.CRLF.length
            delim = imageString.indexOf(Lyrics3v2Fields.CRLF, offset)
        }

        if (offset < imageString.length) {
            token = imageString.substring(offset)
            image = Lyrics3Image("Image", this)
            image.filename = token
            images.add(image)
        }
    }

    /**
     * Creates a new FieldBodyIMG datatype.
     *
     * @param image
     */
    constructor(image: Lyrics3Image) {
        images.add(image)
    }

    /**
     * Creates a new FieldBodyIMG datatype.
     *
     * @param byteBuffer
     * @throws InvalidTagException
     */
    constructor(byteBuffer: ByteBuffer) {
        this.read(byteBuffer)
    }

    override fun read(byteBuffer: ByteBuffer?) {
        if (byteBuffer == null) {
            return
        }
        val imageString: String?

        var buffer = ByteArray(5)

        // read the 5 character size
        byteBuffer.get(buffer, 0, 5)

        val size = String(buffer, 0, 5).toInt()

        if ((size == 0) &&
            (!TagOptionSingleton.lyrics3KeepEmptyFieldIfRead)
        ) {
            throw InvalidTagException("Lyircs3v2 Field has size of zero.")
        }

        buffer = ByteArray(size)

        // read the SIZE length description
        byteBuffer.get(buffer)
        imageString = String(buffer)
        readString(imageString)
    }

    /**
     * @param obj
     * @return
     */
    override fun isSubsetOf(obj: Any?): Boolean {
        if (obj !is FieldFrameBodyIMG) {
            return false
        }

        val superset = obj.images

        for (image in images) {
            if (!superset.contains(image)) {
                return false
            }
        }

        return super.isSubsetOf(obj)
    }

    var value: String
        /**
         * @return
         */
        get() = writeString()
        /**
         * @param value
         */
        set(value) {
            readString(value)
        }

    /**
     * @return
     */
    private fun writeString(): String {
        var str = ""
        var image: Lyrics3Image

        for (image1 in images) {
            image = image1
            str += (image.writeString() + Lyrics3v2Fields.CRLF)
        }

        if (str.length > 2) {
            return str.substring(0, str.length - 2)
        }

        return str
    }

    /**
     * @param image
     */
    fun addImage(image: Lyrics3Image) {
        images.add(image)
    }

    /**
     * @param obj
     * @return
     */
    override fun equals(obj: Any?): Boolean {
        if (obj !is FieldFrameBodyIMG) {
            return false
        }

        return this.images == obj.images && super.equals(obj)
    }

    /**
     * @return
     */
    override fun toString(): String {
        var str = getIdentifier() + " : "

        for (image in images) {
            str += (image.toString() + " ; ")
        }

        return str
    }

    /**
     * @return
     */
    override fun getIdentifier(): String {
        return "IMG"
    }

    /**
     * @param file
     * @throws IOException
     */
    override fun write(file: RandomAccessFile) {
        val size: Int
        var offset = 0
        var buffer = ByteArray(5)
        var str: String?

        size = getSizeValue()
        str = size.toString()

        for (i in 0..<(5 - str.length)) {
            buffer[i] = '0'.code.toByte()
        }

        offset += (5 - str.length)

        for (i in 0..<str.length) {
            buffer[i + offset] = str.get(i).code.toByte()
        }

        offset += str.length

        file.write(buffer, 0, 5)

        if (size > 0) {
            str = writeString()
            buffer = ByteArray(str.length)

            for (i in 0..<str.length) {
                buffer[i] = str.get(i).code.toByte()
            }

            file.write(buffer)
        }
    }

    /**
     * @return
     */
    override fun getSizeValue(): Int {
        var size = 0
        var image: Lyrics3Image

        for (image1 in images) {
            image = image1
            size += (image.getSizeValue() + 2) // addField CRLF pair
        }

        return size - 2 // cut off trailing crlf pair
    }

    /**
     * TODO
     */
    override fun setupObjectList() {
    }
}
