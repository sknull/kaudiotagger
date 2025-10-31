package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.id3.datatype.StringSizeTerminated
import de.visualdigits.kaudiotagger.util.ErrorMessage
import java.io.ByteArrayOutputStream
import java.net.URLEncoder
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

/**
 * Abstract super class of all URL Frames
 */
abstract class AbstractFrameBodyUrlLink : AbstractID3v2FrameBody {

    /**
     * Creates a new FrameBodyUrlLink datatype.
     */
    constructor() : super()

    /**
     * Copy Constructor
     *
     * @param body
     */
    constructor(body: AbstractFrameBodyUrlLink) : super(body)

    /**
     * Creates a new FrameBodyUrlLink datatype., set up with data.
     *
     * @param urlLink
     */
    constructor(urlLink: String) {
        setObjectValue(DataTypes.OBJ_URLLINK, urlLink)
    }

    constructor(
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(byteBuffer, frameSize)

    override fun getUserFriendlyValue(): String {
        return getUrlLink()
    }

    /**
     * Get URL Link
     *
     * @return the urllink
     */
    fun getUrlLink(): String {
        return getObjectValue(DataTypes.OBJ_URLLINK) as String
    }

    /**
     * Set URL Link
     *
     * @param urlLink
     */
    fun setUrlLink(urlLink: String) {
        setObjectValue(DataTypes.OBJ_URLLINK, urlLink)
    }

    /**
     * If the description cannot be encoded using the current encoding change the encoder
     */
    override fun write(tagBuffer: ByteArrayOutputStream) {
        val encoder = StandardCharsets.ISO_8859_1.newEncoder()
        val origUrl = this.getUrlLink()
        if (!encoder.canEncode(origUrl)) {
            // ALL W Frames only support ISO-8859-1 for the url itself, if unable to encode let us assume
            // the link just needs url encoding
            this.setUrlLink(encodeURL(origUrl))

            // We still cant convert so just set log error and set to blank to allow save to continue
            if (!encoder.canEncode(this.getUrlLink())) {
                log.warn(ErrorMessage.MP3_UNABLE_TO_ENCODE_URL.getMsg(origUrl))
                this.setUrlLink("")
            } else {
                log.warn(
                    ErrorMessage.MP3_URL_SAVED_ENCODED.getMsg(origUrl, this.getUrlLink())
                )
            }
        }
        super.write(tagBuffer)
    }

    /**
     * Encode url because may receive url already encoded or not, but we can only store as ISO8859-1
     *
     * @param url
     * @return
     */
    private fun encodeURL(url: String): String {
        val splitURL: Array<String?> = url.split("(?<!/)/(?!/)".toRegex()).toTypedArray()
        val sb = StringBuffer(splitURL[0])
        for (i in 1..<splitURL.size) {
            sb
                .append("/")
                .append(URLEncoder.encode(splitURL[i], StandardCharsets.UTF_8))
        }
        return sb.toString()
    }

    override fun setupObjectList() {
        objectList.add(StringSizeTerminated(DataTypes.OBJ_URLLINK, this))
    }
}
