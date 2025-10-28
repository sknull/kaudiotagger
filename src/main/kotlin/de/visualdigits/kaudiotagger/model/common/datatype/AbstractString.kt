package de.visualdigits.kaudiotagger.model.common.datatype

import de.visualdigits.kaudiotagger.model.common.frame.framebody.AbstractTagFrameBody
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.nio.charset.CharsetDecoder
import java.nio.charset.StandardCharsets

abstract class AbstractString: AbstractDataType {

    /**
     * Creates a new  datatype
     *
     * @param identifier
     * @param frameBody
     */
    constructor(identifier: String?, frameBody: AbstractTagFrameBody?) : super(identifier, frameBody)

    /**
     * Creates a new  datatype, with value
     *
     * @param identifier
     * @param frameBody
     * @param value
     */
    constructor(
        identifier: String?,
        frameBody: AbstractTagFrameBody?,
        value: String
    ) : super(identifier, frameBody, value)

    /**
     * Copy constructor
     *
     * @param `object`
     */
    constructor(copyObject: AbstractString) : super(copyObject)

    /**
     * Check the value can be encoded with the specified encoding
     *
     * @return
     */
    fun canBeEncoded(): Boolean {
        return getBody()?.getTextEncoding()?.let { te ->
            TextEncoding
                .fromId(te)
                ?.charSet
                ?.newEncoder()
                ?.canEncode(getValue() as String) == true
        }?:false
    }

    /**
     * If they have specified UTF-16 then decoder works out by looking at BOM
     * but if missing we have to make an educated guess otherwise just use
     * specified decoder
     *
     * @param inBuffer
     * @return
     */
    fun getCorrectDecoder(inBuffer: ByteBuffer): CharsetDecoder? {
        val decoder = if (inBuffer.remaining() <= 2) {
            getTextEncodingCharSet()?.newDecoder()
        } else if (getTextEncodingCharSet() == StandardCharsets.UTF_16) {
            if (inBuffer.getChar(0).code == 0xfffe || inBuffer.getChar(0).code == 0xfeff) {
                //Get the Specified Decoder
                getTextEncodingCharSet()?.newDecoder()
            } else {
                if (inBuffer.get(0).toInt() == 0) {
                    StandardCharsets.UTF_16BE.newDecoder()
                } else {
                    StandardCharsets.UTF_16LE.newDecoder()
                }
            }
        } else {
            getTextEncodingCharSet()?.newDecoder()
        }
        decoder?.reset()

        return decoder
    }

    /**
     * Get the text encoding being used.
     *
     *
     * The text encoding is defined by the frame body that the text field belongs to.
     *
     * @return the text encoding charset
     */
    open fun getTextEncodingCharSet(): Charset? {
        val textEncoding = getBody()?.getTextEncoding()
        val charSetName = textEncoding?.let { te -> TextEncoding.fromId(te) }?.charSet
        log.debug("text encoding:$textEncoding charset:${charSetName?.name()}")

        return charSetName
    }

    /**
     * Return String representation of data type
     *
     * @return a string representation of the value
     */
    override fun toString(): String {
        return (getValue() as? String)?:""
    }
}