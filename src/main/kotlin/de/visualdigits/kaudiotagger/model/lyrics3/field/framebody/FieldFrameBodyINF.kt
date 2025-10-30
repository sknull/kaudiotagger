package de.visualdigits.kaudiotagger.model.lyrics3.field.framebody

import de.visualdigits.kaudiotagger.model.id3.datatype.StringSizeTerminated
import java.nio.ByteBuffer


class FieldFrameBodyINF : AbstractLyrics3v2FieldFrameBody {

    /**
     * Creates a new FieldBodyINF datatype.
     */
    constructor()

    constructor(body: FieldFrameBodyINF) : super(body)

    /**
     * Creates a new FieldBodyINF datatype.
     *
     * @param additionalInformation
     */
    constructor(additionalInformation: String?) {
        this.setObjectValue("Additional Information", additionalInformation)
    }

    /**
     * Creates a new FieldBodyINF datatype.
     *
     * @param byteBuffer
     */
    constructor(byteBuffer: ByteBuffer) {
        this.read(byteBuffer)
    }

    /**
     * @return
     */
    fun getAdditionalInformation(): String? {
        return getObjectValue("Additional Information") as? String
    }

    /**
     * @param additionalInformation
     */
    fun setAdditionalInformation(additionalInformation: String?) {
        setObjectValue("Additional Information", additionalInformation)
    }

    /**
     * @return
     */
    override fun getIdentifier(): String {
        return "INF"
    }

    /**
     *
     */
    override fun setupObjectList() {
        objectList.add(StringSizeTerminated("Additional Information", this))
    }
}
