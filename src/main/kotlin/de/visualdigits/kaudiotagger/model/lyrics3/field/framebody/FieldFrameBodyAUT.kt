package de.visualdigits.kaudiotagger.model.lyrics3.field.framebody

import de.visualdigits.kaudiotagger.model.id3.datatype.StringSizeTerminated
import java.nio.ByteBuffer


class FieldFrameBodyAUT : AbstractLyrics3v2FieldFrameBody {

    /**
     * Creates a new FieldBodyAUT datatype.
     */
    constructor()

    constructor(body: FieldFrameBodyAUT) : super(body)

    /**
     * Creates a new FieldBodyAUT datatype.
     *
     * @param author
     */
    constructor(author: String?) {
        this.setObjectValue("Author", author)
    }

    /**
     * Creates a new FieldBodyAUT datatype.
     *
     * @param byteBuffer
     */
    constructor(byteBuffer: ByteBuffer) {
        this.read(byteBuffer)
    }

    fun getAuthor(): String? {
        return getObjectValue("Author") as? String
    }

    fun setAuthor(author: String?) {
        setObjectValue("Author", author)
    }

    override fun getIdentifier(): String {
        return "AUT"
    }

    override fun setupObjectList() {
        objectList.add(StringSizeTerminated("Author", this))
    }
}
