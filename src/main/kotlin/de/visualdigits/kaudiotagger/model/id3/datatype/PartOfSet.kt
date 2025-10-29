package de.visualdigits.kaudiotagger.model.id3.datatype

import de.visualdigits.kaudiotagger.model.common.frame.framebody.AbstractTagFrameBody
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.util.EqualsUtil
import de.visualdigits.kaudiotagger.util.TagOptionSingleton
import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.charset.Charset
import java.nio.charset.CharsetEncoder
import java.nio.charset.CodingErrorAction
import java.nio.charset.StandardCharsets

/**
 * Represents the form 01/10 whereby the second part is optional. This is used by frame such as TRCK and TPOS and MVNM
 *
 *
 * Some applications like to prepend the count with a zero to aid sorting, (i.e 02 comes before 10)
 *
 *
 * If TagOptionSingleton.getInstance().isPadNumbers() is enabled then all fields will be written to file padded
 * depending on the value of agOptionSingleton.getInstance().getPadNumberTotalLength(). Additionally fields returned
 * from file will be returned as padded even if they are not currently stored as padded in the file.
 *
 *
 * If TagOptionSingleton.getInstance().isPadNumbers() is disabled then count and track are written to file as they
 * are provided, i.e if provided pre-padded they will be stored pre-padded, if not they will not. Values read from
 * file will be returned as they are currently stored in file.
 */
class PartOfSet : AbstractString {

    /**
     * Creates a new empty  PartOfSet datatype.
     *
     * @param identifier identifies the frame type
     * @param frameBody
     */
    constructor(identifier: String?, frameBody: AbstractTagFrameBody?) : super(identifier, frameBody)

    /**
     * Copy constructor
     *
     * @param object
     */
    constructor(`object`: PartOfSet) : super(`object`)

    override fun equals(obj: Any?): Boolean {
        if (obj === this) {
            return true
        }

        if (obj !is PartOfSet) {
            return false
        }

        return EqualsUtil.areEqual(getValue(), obj.getValue())
    }

    /**
     * Read a 'n' bytes from buffer into a String where n is the frameSize - offset
     * so therefore cannot use this if there are other objects after it because it has no
     * delimiter.
     *
     *
     * Must take into account the text encoding defined in the Encoding Object
     * ID3 Text Frames often allow multiple strings separated by the null char
     * appropriate for the encoding.
     *
     * @param arr    this is the buffer for the frame
     * @param offset this is where to start reading in the buffer for this field
     * @throws NullPointerException
     * @throws IndexOutOfBoundsException
     */
    override fun readByteArray(arr: ByteArray, offset: Int) {
        log.debug("Reading from array from offset:$offset")

        //Get the Specified Decoder
        val decoder = getTextEncodingCharSet()?.newDecoder()

        //Decode sliced inBuffer
        val inBuffer = ByteBuffer.wrap(
            arr,
            offset,
            arr.size - offset
        ).slice()
        val outBuffer = CharBuffer.allocate(arr.size - offset)
        decoder?.reset()
        val coderResult = decoder?.decode(inBuffer, outBuffer, true)
        if (coderResult?.isError == true) {
            log.warn("Decoding error:$coderResult")
        }
        decoder?.flush(outBuffer)
        outBuffer.flip()

        //Store value
        val stringValue = outBuffer.toString()
        setValue(PartOfSetValue(stringValue))

        //SetSize, important this is correct for finding the next datatype
        setSize(arr.size - offset)
        log.debug("Read SizeTerminatedString:{} size:{}", getValue(), getSize())
    }

    /**
     * Get the text encoding being used.
     *
     *
     * The text encoding is defined by the frame body that the text field belongs to.
     *
     * @return the text encoding charset
     */
    override fun getTextEncodingCharSet(): Charset? {
        val textEncoding = this.getBody()?.getTextEncoding()
        val charset = TextEncoding.fromId(textEncoding?.toInt())?.charSet
        log.debug("text encoding:{} charset:{}", textEncoding, charset?.name())

        return charset
    }

    /**
     * Write String into byte array
     *
     *
     * It will remove a trailing null terminator if exists if the option
     * RemoveTrailingTerminatorOnWrite has been set.
     *
     * @return the data as a byte array in format to write to file
     */
    override fun writeByteArray(): ByteArray {
        var value = getValue().toString()
        val data: ByteArray?
        //Try and write to buffer using the CharSet defined by getTextEncodingCharSet()
        try {
            if (TagOptionSingleton.removeTrailingTerminatorOnWrite) {
                if (value.isNotEmpty()) {
                    if (value.get(value.length - 1) == '\u0000') {
                        value = value.substring(0, value.length - 1)
                    }
                }
            }

            val charset = getTextEncodingCharSet()
            val valueWithBOM: String
            val encoder: CharsetEncoder?
            if (StandardCharsets.UTF_16 == charset) {
                encoder = StandardCharsets.UTF_16LE.newEncoder()
                //Note remember LE BOM is ff fe but this is handled by encoder Unicode char is fe ff
                valueWithBOM = '\ufeff'.toString() + value
            } else {
                encoder = charset?.newEncoder()
                valueWithBOM = value
            }
            encoder?.onMalformedInput(CodingErrorAction.IGNORE)
            encoder?.onUnmappableCharacter(CodingErrorAction.IGNORE)

            val bb = encoder?.encode(CharBuffer.wrap(valueWithBOM))
            data = ByteArray(bb?.limit()?:0)
            bb?.get(data, 0, bb.limit())
        } catch (ce: CharacterCodingException) { //Should never happen so if does throw a RuntimeException
            log.error(ce.message)
            throw RuntimeException(ce)
        }
        setSize(data.size)

        return data
    }

    override fun getValue(): PartOfSetValue? {
        return super.getValue() as PartOfSetValue?
    }

    override fun toString(): String {
        return getValue().toString()
    }
}