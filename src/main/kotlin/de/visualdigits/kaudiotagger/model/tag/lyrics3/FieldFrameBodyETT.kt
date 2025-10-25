package de.visualdigits.kaudiotagger.model.tag.lyrics3

import de.visualdigits.kaudiotagger.model.datatype.StringSizeTerminated
import java.nio.ByteBuffer


class FieldFrameBodyETT : AbstractLyrics3v2FieldFrameBody {

    /**
     * Creates a new FieldBodyETT datatype.
     */
    constructor()

    constructor(
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(byteBuffer, frameSize)

    constructor(body: FieldFrameBodyETT) : super(body)

    /**
     * Creates a new FieldBodyETT datatype.
     *
     * @param title
     */
    constructor(title: String) {
        this.setObjectValue("Title", title)
    }

    /**
     * Creates a new FieldBodyETT datatype.
     *
     * @param byteBuffer
     * @throws InvalidTagException
     */
    constructor(byteBuffer: ByteBuffer) {
        this.read(byteBuffer)
    }

    /**
     * @return
     */
    override fun getIdentifier(): String {
        return "ETT"
    }

    var title: String?
        /**
         * @return
         */
        get() = getObjectValue("Title") as String?
        /**
         * @param title
         */
        set(title) {
            setObjectValue("Title", title)
        }

    /**
     *
     */
    override fun setupObjectList() {
        objectList.add(StringSizeTerminated("Title", this))
    }
}
