package de.visualdigits.kaudiotagger.model.lyrics3.field.framebody

import de.visualdigits.kaudiotagger.model.id3.datatype.StringSizeTerminated
import java.nio.ByteBuffer


class FieldFrameBodyEAR : AbstractLyrics3v2FieldFrameBody {

    /**
     * Creates a new FieldBodyEAR datatype.
     */
    constructor()

    constructor(body: FieldFrameBodyEAR) : super(body)

    /**
     * Creates a new FieldBodyEAR datatype.
     *
     * @param artist
     */
    constructor(artist: String?) {
        this.setObjectValue("Artist", artist)
    }

    /**
     * Creates a new FieldBodyEAR datatype.
     *
     * @param byteBuffer
     */
    constructor(byteBuffer: ByteBuffer) {
        this.read(byteBuffer)
    }

    fun getArtist(): String? {
        return getObjectValue("Artist") as? String
    }

    fun setArtist(artist: String?) {
        setObjectValue("Artist", artist)
    }

    override fun getIdentifier(): String {
        return "EAR"
    }

    override fun setupObjectList() {
        objectList.add(StringSizeTerminated("Artist", this))
    }
}
