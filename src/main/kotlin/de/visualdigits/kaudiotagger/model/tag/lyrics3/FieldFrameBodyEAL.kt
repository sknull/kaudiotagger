package de.visualdigits.kaudiotagger.model.tag.lyrics3

import de.visualdigits.kaudiotagger.model.datatype.StringSizeTerminated
import java.nio.ByteBuffer


class FieldFrameBodyEAL : AbstractLyrics3v2FieldFrameBody {

    /**
     * Creates a new FieldBodyEAL datatype.
     */
    constructor()

    constructor(body: FieldFrameBodyEAL) : super(body)

    /**
     * Creates a new FieldBodyEAL datatype.
     *
     * @param album
     */
    constructor(album: String) {
        this.setObjectValue("Album", album)
    }

    /**
     * Creates a new FieldBodyEAL datatype.
     *
     * @param byteBuffer
     * @throws InvalidTagException
     */
    constructor(byteBuffer: ByteBuffer) {
        read(byteBuffer)
    }

    /**
     * @return
     */
    fun getAlbum(): String? {
        return getObjectValue("Album") as String?
    }

    /**
     * @param album
     */
    fun setAlbum(album: String?) {
        setObjectValue("Album", album)
    }

    /**
     * @return
     */
    override fun getIdentifier(): String {
        return "EAL"
    }

    /**
     *
     */
    override fun setupObjectList() {
        objectList.add(StringSizeTerminated("Album", this))
    }
}
