package de.visualdigits.kaudiotagger.model.tag.lyrics3

import de.visualdigits.kaudiotagger.model.datatype.StringSizeTerminated
import java.nio.ByteBuffer


class FieldFrameBodyAUT : AbstractLyrics3v2FieldFrameBody {

    /**
     * Creates a new FieldBodyAUT datatype.
     */
    constructor()

    constructor(
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(byteBuffer, frameSize)

    constructor(body: FieldFrameBodyAUT) : super(body)

    /**
     * Creates a new FieldBodyAUT datatype.
     *
     * @param author
     */
    constructor(author: String) {
        this.setObjectValue("Author", author)
    }

    /**
     * Creates a new FieldBodyAUT datatype.
     *
     * @param byteBuffer
     * @throws InvalidTagException
     */
    constructor(byteBuffer: ByteBuffer) {
        this.read(byteBuffer)
    }

    var author: String?
        /**
         * @return
         */
        get() = getObjectValue("Author") as String?
        /**
         * @param author
         */
        set(author) {
            setObjectValue("Author", author)
        }

    /**
     * @return
     */
    override fun getIdentifier(): String {
        return "AUT"
    }

    /**
     *
     */
    override fun setupObjectList() {
        objectList.add(StringSizeTerminated("Author", this))
    }
}
