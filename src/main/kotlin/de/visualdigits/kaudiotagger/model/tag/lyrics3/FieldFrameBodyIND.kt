package de.visualdigits.kaudiotagger.model.tag.lyrics3

import de.visualdigits.kaudiotagger.model.datatype.BooleanString
import java.nio.ByteBuffer


class FieldFrameBodyIND : AbstractLyrics3v2FieldFrameBody {
    /**
     * Creates a new FieldBodyIND datatype.
     */
    constructor()

    constructor(
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(byteBuffer, frameSize)

    constructor(body: FieldFrameBodyIND) : super(body)

    /**
     * Creates a new FieldBodyIND datatype.
     *
     * @param lyricsPresent
     * @param timeStampPresent
     */
    constructor(lyricsPresent: Boolean, timeStampPresent: Boolean) {
        this.setObjectValue("Lyrics Present", lyricsPresent)
        this.setObjectValue("Timestamp Present", timeStampPresent)
    }

    /**
     * Creates a new FieldBodyIND datatype.
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
        return "IND"
    }

    /**
     *
     */
    override fun setupObjectList() {
        objectList.add(BooleanString("Lyrics Present", this))
        objectList.add(BooleanString("Timestamp Present", this))
    }
}
