package de.visualdigits.kaudiotagger.model.lyrics3.field.framebody

import de.visualdigits.kaudiotagger.model.id3.datatype.BooleanString
import java.nio.ByteBuffer


class FieldFrameBodyIND : AbstractLyrics3v2FieldFrameBody {
    /**
     * Creates a new FieldBodyIND datatype.
     */
    constructor()

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
     */
    constructor(byteBuffer: ByteBuffer) {
        this.read(byteBuffer)
    }

    fun getAuthor(): String? {
        return getObjectValue("Author") as? String
    }

    /**
     * @param author
     */
    fun setAuthor(author: String?) {
        setObjectValue("Author", author)
    }

    override fun getIdentifier(): String {
        return "IND"
    }

    override fun setupObjectList() {
        objectList.add(BooleanString("Lyrics Present", this))
        objectList.add(BooleanString("Timestamp Present", this))
    }
}
