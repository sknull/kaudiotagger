package de.visualdigits.kaudiotagger.model.lyrics3.field.framebody

import de.visualdigits.kaudiotagger.model.id3.datatype.StringSizeTerminated
import java.nio.ByteBuffer


class FieldFrameBodyETT : AbstractLyrics3v2FieldFrameBody {

    /**
     * Creates a new FieldBodyETT datatype.
     */
    constructor()

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

    /**
     * @return
     */
    fun getTitle(): String? {
        return getObjectValue("Title") as String?
    }

    /**
     * @param title
     */
    fun setTitle(title: String?) {
        setObjectValue("Title", title)
    }

    /**
     *
     */
    override fun setupObjectList() {
        objectList.add(StringSizeTerminated("Title", this))
    }
}
