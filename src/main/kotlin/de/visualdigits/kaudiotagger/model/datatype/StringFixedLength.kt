package de.visualdigits.kaudiotagger.model.datatype

import de.visualdigits.kaudiotagger.model.datatype.types.TextEncoding
import de.visualdigits.kaudiotagger.model.exceptions.InvalidDataTypeException
import de.visualdigits.kaudiotagger.model.frame.framebody.AbstractTagFrameBody
import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.charset.Charset
import java.nio.charset.CharsetEncoder
import java.nio.charset.StandardCharsets

open class StringFixedLength: AbstractString {

    /**
     * Creates a new ObjectStringFixedsize datatype.
     *
     * @param identifier
     * @param frameBody
     * @param size
     * @throws IllegalArgumentException
     */
    constructor(
             identifier: String,
            frameBody: AbstractTagFrameBody,
            size: Int
    ): super(identifier, frameBody) {
        if (size < 0) {
            throw IllegalArgumentException("size is less than zero: " + size);
        }
        this.size = size
    }

    constructor(copyObject: StringFixedLength): super(copyObject) {
        this.size = copyObject.size
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
                "Array length is:${arr.size}offset is:${offset}Size is:$size"
            )

            if (arr.size - offset < size) {
                throw InvalidDataTypeException(
                    "byte array is to small to retrieve string of declared length:$size"
                )
            }
            val str = decoder
                ?.decode(ByteBuffer.wrap(arr, offset, size))
                ?.toString()
            if (str == null) {
                throw NullPointerException("String is null")
            }
            value = str
        } catch (ce: CharacterCodingException) {
            log.error(ce.message)
            value = ""
        }
        log.debug("Read StringFixedLength:$value")
    }

    /**
     * @return the encoding of the frame body this datatype belongs to
     */
    override fun getTextEncodingCharSet(): Charset? {
        val textEncoding = this.frameBody?.getTextEncoding()
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
    override fun writeByteArray(): ByteArray {
        val dataBuffer: ByteBuffer?
        val data: ByteArray

        //Create with a series of empty of spaces to try and ensure integrity of field
        if (value == null) {
            log.warn(
                "Value of StringFixedlength Field is null using default value instead"
            )
            data = ByteArray(size)
            for (i in 0..<size) {
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
                dataBuffer = encoder.encode(CharBuffer.wrap('\ufeff'.toString() + value as String?))
            } else {
                encoder = charset?.newEncoder()
                dataBuffer = encoder?.encode(CharBuffer.wrap(value as? String))
            }
        } catch (ce: CharacterCodingException) {
            log.warn(
                "There was a problem writing the following StringFixedlength Field:$value:${ce.message}using default value instead"
            )
            data = ByteArray(size)
            var i = 0
            while (i < size) {
                data[i] = ' '.code.toByte()
                i++
            }
            return data
        }

        // We must return the defined size.
        // To check now because size is in bytes not chars
        if (dataBuffer != null) {
            //Everything ok
            if (dataBuffer.limit() == size) {
                data = ByteArray(dataBuffer.limit())
                dataBuffer.get(data, 0, dataBuffer.limit())
                return data
            } else if (dataBuffer.limit() > size) {
                log.warn(
                    "There was a problem writing the following StringFixedlength Field:$value when converted to bytes has length of:${dataBuffer.limit()} but field was defined with length of:$size too long so stripping extra length"
                )
                data = ByteArray(size)
                dataBuffer.get(data, 0, size)
                return data
            } else {
                log.warn(
                    "There was a problem writing the following StringFixedlength Field:$value when converted to bytes has length of:${dataBuffer.limit()} but field was defined with length of:$size too short so padding with spaces to make up extra length"
                )

                data = ByteArray(size)
                dataBuffer.get(data, 0, dataBuffer.limit())

                for (i in dataBuffer.limit()..<size) {
                    data[i] = ' '.code.toByte()
                }
                return data
            }
        } else {
            log.warn(
                "There was a serious problem writing the following StringFixedlength Field:$value:using default value instead"
            )
            data = ByteArray(size)
            for (i in 0..<size) {
                data[i] = ' '.code.toByte()
            }
            return data
        }
    }
}