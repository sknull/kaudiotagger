package de.visualdigits.kaudiotagger.model.common.datatype

import de.visualdigits.kaudiotagger.model.common.frame.framebody.AbstractTagFrameBody
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.util.TagOptionSingleton
import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.charset.Charset
import java.nio.charset.CharsetEncoder
import java.nio.charset.CodingErrorAction
import java.nio.charset.StandardCharsets

open class TextEncodedStringSizeTerminated : AbstractString {

    companion object {

        /**
         * Split the values separated by null character
         *
         * @param value the raw value
         * @return list of values, guaranteed to be at least one value
         */
        fun splitByNullSeperator(value: String): MutableList<String> {
            val valuesarray = value.split("\\u0000")
            var values = valuesarray.toMutableList()
            //Read only list so if empty have to create new list
            if (values.isEmpty()) {
                values = mutableListOf("")
            }

            return values
        }
    }

    /**
     * Creates a new empty TextEncodedStringSizeTerminated datatype.
     *
     * @param identifier identifies the frame type
     * @param frameBody
     */
    constructor(
        identifier: String?,
        frameBody: AbstractTagFrameBody
    ) : super(identifier, frameBody)

    /**
     * Copy constructor
     *
     * @param `object`
     */
    constructor(
        copyObject: TextEncodedStringSizeTerminated
    ) : super(copyObject)

    /**
     * Read a 'n' bytes from buffer into a String where n is the framesize - offset
     * so therefore cannot use this if there are other objects after it because it has no
     * delimiter.
     *
     *
     * Must take into account the text encoding defined in the Encoding Object
     * ID3 Text Frames often allow multiple strings seperated by the null char
     * appropriate for the encoding.
     *
     * @param arr    this is the buffer for the frame
     * @param offset this is where to start reading in the buffer for this field
     */
    override fun readByteArray(arr: ByteArray, offset: Int) {
        log.debug("Reading from array from offset:$offset")

        //Decode sliced inBuffer
        val inBuffer = ByteBuffer.wrap(arr, offset, arr.size - offset).slice()
        val outBuffer = CharBuffer.allocate(arr.size - offset)
        val decoder = getCorrectDecoder(inBuffer)
        val coderResult = decoder?.decode(inBuffer, outBuffer, true)
        if (coderResult?.isError == true) {
            log.warn("Decoding error:$coderResult")
        }
        decoder?.flush(outBuffer)
        outBuffer.flip()

        //If using UTF16 with BOM we then search through the text removing any BOMs that could exist
        //for multiple values, BOM could be Big Endian or Little Endian
        setValue(if (StandardCharsets.UTF_16 == getTextEncodingCharSet()) {
            outBuffer.toString().replace("\ufeff", "").replace("\ufffe", "")
        } else {
            outBuffer.toString()
        })
        //SetSize, important this is correct for finding the next datatype
        setSize(arr.size - offset)
        log.debug("Read SizeTerminatedString:{} size:{}", getValue(), getSize())
    }

    override fun getTextEncodingCharSet(): Charset? {
        val textEncoding = getBody()?.getTextEncoding()?:error("No text encoding found")
        val charset = TextEncoding.fromId(textEncoding)?.charSet
        log.debug("text encoding:$textEncoding charset:${charset?.name()}")

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
        val data: ByteArray
        //Try and write to buffer using the CharSet defined by getTextEncodingCharSet()
        val charset = getTextEncodingCharSet()?:error("No charset found")
        try {
            stripTrailingNull()

            //Special Handling because there is no UTF16 BOM LE charset
            val stringValue = getValue() as String
            var actualCharSet: Charset? = null
            if (StandardCharsets.UTF_16 == charset) {
                actualCharSet = if (TagOptionSingleton.isEncodeUTF16BomAsLittleEndian) {
                    StandardCharsets.UTF_16LE
                } else {
                    StandardCharsets.UTF_16BE
                }
            }

            //Ensure large enough for any encoding
            val outputBuffer = ByteBuffer.allocate(
                (stringValue.length + 3) * 3
            )

            //Ensure each string (if multiple values) is written with BOM by writing separately
            val values = splitByNullSeperator(stringValue)
            checkTrailingNull(values, stringValue)

            //For each value
            for (i in values.indices) {
                val next = values[i]

                if (StandardCharsets.UTF_16LE == actualCharSet) {
                    outputBuffer.put(writeStringUTF16LEBOM(next, i, values.size))
                } else if (StandardCharsets.UTF_16BE == actualCharSet) {
                    outputBuffer.put(writeStringUTF16BEBOM(next, i, values.size))
                } else {
                    val charsetEncoder = charset.newEncoder()
                    charsetEncoder.onMalformedInput(CodingErrorAction.IGNORE)
                    charsetEncoder.onUnmappableCharacter(CodingErrorAction.IGNORE)
                    outputBuffer.put(writeString(charsetEncoder, next, i, values.size))
                }
            }
            outputBuffer.flip()
            data = ByteArray(outputBuffer.limit())
            outputBuffer.rewind()
            outputBuffer.get(data, 0, outputBuffer.limit())
            setValue(data.size)
        } catch (ce: CharacterCodingException) { //https://bitbucket.org/ijabz/jaudiotagger/issue/1/encoding-metadata-to-utf-16-can-fail-if
            log.error("${ce.message}:$charset:${getValue()}")
            throw RuntimeException(ce)
        }
        return data
    }

    /**
     * Write String using specified encoding
     *
     *
     * When this is called multiple times, all but the last value has a trailing null
     *
     * @param encoder
     * @param next
     * @param i
     * @param noOfValues
     * @return
     * @throws CharacterCodingException
     */
    fun writeString(
        encoder: CharsetEncoder,
        next: String,
        i: Int,
        noOfValues: Int
    ): ByteBuffer {
        val bb: ByteBuffer
        if ((i + 1) == noOfValues) {
            bb = encoder.encode(CharBuffer.wrap(next))
        } else {
            bb = encoder.encode(CharBuffer.wrap(next + '\u0000'))
        }
        bb.rewind()
        return bb
    }

    /**
     * Write String in UTF-LEBOM format
     *
     *
     * When this is called multiple times, all but the last value has a trailing null
     *
     *
     * Remember we are using this charset because the charset that writes BOM does it the wrong way for us
     * so we use this none and then manually add the BOM ourselves.
     *
     * @param next
     * @param i
     * @param noOfValues
     * @return
     * @throws CharacterCodingException
     */
    fun writeStringUTF16LEBOM(
        next: String,
        i: Int,
        noOfValues: Int
    ): ByteBuffer {
        val encoder = StandardCharsets.UTF_16LE.newEncoder()
        encoder.onMalformedInput(CodingErrorAction.IGNORE)
        encoder.onUnmappableCharacter(CodingErrorAction.IGNORE)

        val bb: ByteBuffer
        //Note remember LE BOM is ff fe but this is handled by encoder Unicode char is fe ff
        if ((i + 1) == noOfValues) {
            bb = encoder.encode(CharBuffer.wrap('\ufeff'.toString() + next))
        } else {
            bb = encoder.encode(CharBuffer.wrap('\ufeff'.toString() + next + '\u0000'))
        }
        bb.rewind()
        return bb
    }

    /**
     * Write String in UTF-BEBOM format
     *
     *
     * When this is called multiple times, all but the last value has a trailing null
     *
     * @param next
     * @param i
     * @param noOfValues
     * @return
     * @throws CharacterCodingException
     */
    fun writeStringUTF16BEBOM(
        next: String,
        i: Int,
        noOfValues: Int
    ): ByteBuffer {
        val encoder = StandardCharsets.UTF_16BE.newEncoder()
        encoder.onMalformedInput(CodingErrorAction.IGNORE)
        encoder.onUnmappableCharacter(CodingErrorAction.IGNORE)

        val bb: ByteBuffer
        //Add BOM
        if ((i + 1) == noOfValues) {
            bb = encoder.encode(CharBuffer.wrap('\ufeff'.toString() + next))
        } else {
            bb = encoder.encode(CharBuffer.wrap('\ufeff'.toString() + next + '\u0000'))
        }
        bb.rewind()
        return bb
    }

    /**
     * Removing trailing null from end of String, this should not be there but some applications continue to write
     * this unnecessary null char.
     */
    fun stripTrailingNull() {
        if (TagOptionSingleton.removeTrailingTerminatorOnWrite) {
            var stringValue = getValue() as String
            if (stringValue.isNotEmpty()) {
                if (stringValue.get(stringValue.length - 1) == '\u0000') {
                    stringValue = stringValue.take(stringValue.length - 1)
                    setValue(stringValue)
                }
            }
        }
    }

    /**
     * Because nulls are stripped we need to check if not removing trailing nulls whether the original
     * value ended with a null and if so add it back in.
     *
     * @param values
     * @param stringValue
     */
    fun checkTrailingNull(values: MutableList<String>, stringValue: String) {
        if (!TagOptionSingleton.removeTrailingTerminatorOnWrite) {
            if (stringValue.isNotEmpty() &&
                stringValue[stringValue.length - 1] == '\u0000'
            ) {
                val lastVal = values[values.size - 1]
                val newLastVal = "$lastVal\u0000"
                values[values.size - 1] = newLastVal
            }
        }
    }

    /**
     * Add an additional String to the current String value
     *
     * @param value
     */
    open fun addValue(value: String) {
        setValue("${value}\u0000$value")
    }

    /**
     * How many values are held, each value is separated by a null terminator
     *
     * @return number of values held, usually this will be one.
     */
    open fun getNumberOfValues(): Int {
        return splitByNullSeperator((getValue() as String)).size
    }

    /**
     * Get the nth value
     *
     * @param index
     * @return the nth value
     * @throws IndexOutOfBoundsException if value does not exist
     */
    open fun getValueAtIndex(index: Int): String? {
        //Split String into separate components
        val values: MutableList<*> = splitByNullSeperator(getValue() as String)
        return values[index] as String
    }

    /**
     * @return list of all values
     */
    open fun getValues(): MutableList<String> {
        return splitByNullSeperator(getValue() as String)
    }

    /**
     * Get value(s) whilst removing any trailing nulls
     *
     * @return
     */
    open fun getValueWithoutTrailingNull(): String {
        return (getValue() as String).substringBefore(0.toChar())
    }
}