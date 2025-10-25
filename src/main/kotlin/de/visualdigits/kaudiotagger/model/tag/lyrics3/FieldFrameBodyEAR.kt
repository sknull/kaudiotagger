package de.visualdigits.kaudiotagger.model.tag.lyrics3

import de.visualdigits.kaudiotagger.model.datatype.StringSizeTerminated
import java.nio.ByteBuffer


class FieldFrameBodyEAR : AbstractLyrics3v2FieldFrameBody {

    /**
     * Creates a new FieldBodyEAR datatype.
     */
    constructor()

    constructor(
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(byteBuffer, frameSize)

    constructor(body: FieldFrameBodyEAR) : super(body)

    /**
     * Creates a new FieldBodyEAR datatype.
     *
     * @param artist
     */
    constructor(artist: String) {
        this.setObjectValue("Artist", artist)
    }

    /**
     * Creates a new FieldBodyEAR datatype.
     *
     * @param byteBuffer
     * @throws InvalidTagException
     */
    constructor(byteBuffer: ByteBuffer) {
        this.read(byteBuffer)
    }

    var artist: String?
        /**
         * @return
         */
        get() = getObjectValue("Artist") as String?
        /**
         * @param artist
         */
        set(artist) {
            setObjectValue("Artist", artist)
        }

    /**
     * @return
     */
    override fun getIdentifier(): String {
        return "EAR"
    }

    /**
     *
     */
    override fun setupObjectList() {
        objectList.add(StringSizeTerminated("Artist", this))
    }
}
