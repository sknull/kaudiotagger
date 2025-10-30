package de.visualdigits.kaudiotagger.model.id3.datatype

import de.visualdigits.kaudiotagger.model.common.exceptions.InvalidDataTypeException
import de.visualdigits.kaudiotagger.model.common.frame.framebody.AbstractTagFrameBody
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.charset.Charset
import java.nio.charset.CharsetEncoder
import java.nio.charset.StandardCharsets

open class StringFixedLength : AbstractString {

    /**
     * Creates a new ObjectStringFixedsize datatype.
     *
     * @param identifier
     * @param frameBody
     * @param size
     */
    constructor(
        identifier: String?,
        frameBody: AbstractTagFrameBody,
        size: Int
    ) : super(identifier, frameBody) {
        if (size < 0) {
            throw IllegalArgumentException("size is less than zero: $size")
        }
        setSize(size)
    }

    constructor(copyObject: StringFixedLength) : super(copyObject) {
        setSize(copyObject.getSize())
    }

    /**
     * Read a string from buffer of fixed size(size has already been set in constructor)
     *
     * @param arr    this is the buffer for the frame
     * @param offset this is where to start reading in the buffer for this field
     */
    override fun readByteArray(arr: ByteArray, offset: Int) {
        log.debug("Reading from array from offset:$offset")
        try {
            val decoder = getTextEncodingCharSet()?.newDecoder()

            //Decode buffer if runs into problems should through exception which we
            //catch and then set value to empty string.
            log.debug(
                "Array length is:${arr.size}offset is:${offset}Size is:${getSize()}()"
            )

            if (arr.size - offset < getSize()) {
                throw InvalidDataTypeException(
                    "byte array is to small to retrieve string of declared length:${getSize()}"
                )
            }
            val str = decoder
                ?.decode(ByteBuffer.wrap(arr, offset, getSize()))
                ?.toString()
            if (str == null) {
                throw NullPointerException("String is null")
            }
            setValue(str)
        } catch (ce: CharacterCodingException) {
            log.error(ce.message)
            setValue("")
        }
        log.debug("Read StringFixedLength:${getValue()}")
    }

    /**
     * @return the encoding of the frame body this datatype belongs to
     */
    override fun getTextEncodingCharSet(): Charset? {
        val textEncoding = this.getBody()?.getTextEncoding()
        val charset = TextEncoding.fromId(textEncoding)?.charSet
        log.debug(
            "text encoding:$textEncoding charset:${charset?.name()}"
        )
        return charset
    }

    /**
     * Write String into byte array
     *
     *
     * The string will be adjusted to ensure the correct number of bytes are written, If the current value is null
     * or to short the written value will have the 'space' character appended to ensure this. We write this instead of
     * the null character because the null character is likely to confuse the parser into misreading the next field.
     *
     * @return the byte array to be written to the file
     */
    override fun writeByteArray(): ByteArray? {
        val dataBuffer: ByteBuffer?
        val data: ByteArray

        //Create with a series of empty of spaces to try and ensure integrity of field
        val size = getSize()
        if (getValue() == null) {
            log.warn(
                "Value of StringFixedlength Field is null using default value instead"
            )
            data = ByteArray(size)
            (0..<size).forEach { i ->
                data[i] = ' '.code.toByte()
            }
            return data
        }

        try {
            val charset = getTextEncodingCharSet()
            val encoder: CharsetEncoder?
            if (StandardCharsets.UTF_16 == charset) {
                //Note remember LE BOM is ff fe but tis is handled by encoder Unicode char is fe ff
                encoder = StandardCharsets.UTF_16LE.newEncoder()
                dataBuffer = encoder.encode(CharBuffer.wrap("\uFEFF${getValue() as? String}"))
            } else {
                encoder = charset?.newEncoder()
                dataBuffer = encoder?.encode(CharBuffer.wrap(getValue() as? String))
            }
        } catch (ce: CharacterCodingException) {
            log.warn("There was a problem writing the following StringFixedlength Field:${getValue()}:${ce.message}using default value instead")
            data = ByteArray(size)
            (0 until size).forEach { i ->
                data[i] = ' '.code.toByte()
            }
            return data
        }

        // We must return the defined size.
        // To check now because size is in bytes not chars
        if (dataBuffer != null) {
            //Everything ok
            val limit = dataBuffer.limit()
            if (limit == size) {
                data = ByteArray(limit)
                dataBuffer.get(data, 0, limit)
                return data
            } else if (limit > size) {
                log.warn(
                    "There was a problem writing the following StringFixedlength Field:${getValue()} when converted to bytes has length of:${limit} but field was defined with length of:$size too long so stripping extra length"
                )
                data = ByteArray(size)
                dataBuffer.get(data, 0, size)
                return data
            } else {
                log.warn(
                    "There was a problem writing the following StringFixedlength Field:${getValue()} when converted to bytes has length of:${limit} but field was defined with length of:$size too short so padding with spaces to make up extra length"
                )

                data = ByteArray(size)
                dataBuffer.get(data, 0, limit)

                (limit..<size).forEach { i ->
                    data[i] = ' '.code.toByte()
                }
                return data
            }
        } else {
            log.warn(
                "There was a serious problem writing the following StringFixedlength Field:${getValue()}:using default value instead"
            )
            data = ByteArray(size)
            (0..<size).forEach { i ->
                data[i] = ' '.code.toByte()
            }
            return data
        }
    }
}