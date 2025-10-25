package de.visualdigits.kaudiotagger.model.tag.lyrics3

import de.visualdigits.kaudiotagger.model.datatype.StringSizeTerminated
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
     * @throws org.jaudiotagger.tag.InvalidTagException
     */
    constructor(byteBuffer: ByteBuffer) {
        this.read(byteBuffer)
    }

    var additionalInformation: String?
        /**
         * @return
         */
        get() = getObjectValue("Additional Information") as String?
        /**
         * @param additionalInformation
         */
        set(additionalInformation) {
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
