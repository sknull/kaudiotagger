package de.visualdigits.kaudiotagger.model.datatype

import de.visualdigits.kaudiotagger.model.exceptions.InvalidDataTypeException
import de.visualdigits.kaudiotagger.model.frame.framebody.AbstractTagFrameBody
import de.visualdigits.kaudiotagger.util.TagOptionSingleton
import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.charset.Charset
import java.nio.charset.CharsetDecoder
import java.nio.charset.CodingErrorAction
import java.nio.charset.StandardCharsets

open class TextEncodedStringNullTerminated : AbstractString {

    /**
     * Creates a new TextEncodedStringNullTerminated datatype.
     *
     * @param identifier identifies the frame type
     * @param frameBody
     */
    constructor(
        identifier: String,
        frameBody: AbstractTagFrameBody?
    ) : super(identifier, frameBody)

    /**
     * Creates a new TextEncodedStringNullTerminated datatype, with value
     *
     * @param identifier
     * @param frameBody
     * @param value
     */
    constructor(
        identifier: String,
        frameBody: AbstractTagFrameBody?,
        value: String
    ) : super(identifier, frameBody, value)

    constructor(
        copyObject: TextEncodedStringNullTerminated
    ) : super(copyObject)

    /**
     * Read a string from buffer upto null character (if exists)
     *
     *
     * Must take into account the text encoding defined in the Encoding Object
     * ID3 Text Frames often allow multiple strings separated by the null char
     * appropriate for the encoding.
     *
     * @param arr    this is the buffer for the frame
     * @param offset this is where to start reading in the buffer for this field
     */
    override fun readByteArray(arr: ByteArray, offset: Int) {
        if (offset >= arr.size) {
            throw InvalidDataTypeException("Unable to find null terminated string")
        }
        val bufferSize: Int

        log.debug("Reading from array starting from offset:$offset")
        var size: Int

        //Get the Specified Decoder
        val charset: Charset = getTextEncodingCharSet()!!

        //We only want to load up to null terminator, data after this is part of different
        //field and it may not be possible to decode it so do the check before we do
        //do the decoding,encoding dependent.
        val buffer = ByteBuffer.wrap(arr, offset, arr.size - offset)
        var endPosition = 0

        //Latin-1 and UTF-8 strings are terminated by a single-byte null,
        //while UTF-16 and its variants need two bytes for the null terminator.
        val nullIsOneByte = StandardCharsets.ISO_8859_1 == charset || StandardCharsets.UTF_8 == charset
        var isNullTerminatorFound = false
        while (buffer.hasRemaining()) {
            var nextByte = buffer.get()
            if (nextByte.toInt() == 0x00) {
                if (nullIsOneByte) {
                    buffer.mark()
                    buffer.reset()
                    endPosition = buffer.position() - 1
                    log.debug("Null terminator found starting at:$endPosition")
                    isNullTerminatorFound = true
                    break
                } else {
                    // Looking for two-byte null
                    if (buffer.hasRemaining()) {
                        nextByte = buffer.get()
                        if (nextByte.toInt() == 0x00) {
                            buffer.mark()
                            buffer.reset()
                            endPosition = buffer.position() - 2
                            log.debug("UTF16:Null terminator found starting  at:$endPosition")
                            isNullTerminatorFound = true
                            break
                        } else {
                            //Nothing to do, we have checked 2nd value of pair it was not a null terminator
                            //so will just start looking again in next invocation of loop
                        }
                    } else {
                        buffer.mark()
                        buffer.reset()
                        endPosition = buffer.position() - 1
                        log.warn("UTF16:Should be two null terminator marks but only found one starting at:$endPosition")
                        isNullTerminatorFound = true
                        break
                    }
                }
            } else {
                //If UTF16, we should only be looking on 2 byte boundaries
                if (!nullIsOneByte) {
                    if (buffer.hasRemaining()) {
                        buffer.get()
                    }
                }
            }
        }

        if (!isNullTerminatorFound) {
            throw InvalidDataTypeException("Unable to find null terminated string")
        }

        log.debug("End Position is:${endPosition}Offset:$offset")

        //Set Size so offset is ready for next field (includes the null terminator)
        size = endPosition - offset
        size++
        if (!nullIsOneByte) {
            size++
        }
        setSize(size)

        //Decode buffer if runs into problems should throw exception which we
        //catch and then set value to empty string. (We don't read the null terminator
        //because we dont want to display this)
        bufferSize = endPosition - offset
        log.debug("Text size is:$bufferSize")
        if (bufferSize == 0) {
            setValue("")
        } else {
            //Decode sliced inBuffer
            val inBuffer = ByteBuffer.wrap(arr, offset, bufferSize).slice()
            val outBuffer = CharBuffer.allocate(bufferSize)

            val decoder: CharsetDecoder = getCorrectDecoder(inBuffer!!)!!
            val coderResult = decoder.decode(inBuffer, outBuffer, true)
            if (coderResult.isError) {
                log.warn("Problem decoding text encoded null terminated string:$coderResult")
            }
            decoder.flush(outBuffer)
            outBuffer.flip()
            setValue(outBuffer.toString())
        }
        //Set Size so offset is ready for next field (includes the null terminator)
        log.debug("Read NullTerminatedString:{} size inc terminator:{}", getValue(), size)
    }

    /**
     * Write String into byte array, adding a null character to the end of the String
     *
     * @return the data as a byte array in format to write to file
     */
    override fun writeByteArray(): ByteArray {
        log.debug("Writing NullTerminatedString.{}", getValue())
        val data: ByteArray?
        //Write to buffer using the CharSet defined by getTextEncodingCharSet()
        //Add a null terminator which will be encoded based on encoding.
        val charset = getTextEncodingCharSet()?:error("No charset found")
        try {
            if (StandardCharsets.UTF_16 == charset) {
                if (TagOptionSingleton.isEncodeUTF16BomAsLittleEndian) {
                    val encoder = StandardCharsets.UTF_16LE.newEncoder()
                    encoder.onMalformedInput(CodingErrorAction.IGNORE)
                    encoder.onUnmappableCharacter(CodingErrorAction.IGNORE)

                    //Note remember LE BOM is ff fe but this is handled by encoder Unicode char is fe ff
                    val bb = encoder.encode(
                        CharBuffer.wrap("\uFEFF${getValue() as String?}\u0000")
                    )
                    data = ByteArray(bb.limit())
                    bb.get(data, 0, bb.limit())
                } else {
                    val encoder = StandardCharsets.UTF_16BE.newEncoder()
                    encoder.onMalformedInput(CodingErrorAction.IGNORE)
                    encoder.onUnmappableCharacter(CodingErrorAction.IGNORE)

                    //Note  BE BOM will leave as fe ff
                    val bb = encoder.encode(
                        CharBuffer.wrap("\uFEFF${getValue() as String?}\u0000")
                    )
                    data = ByteArray(bb.limit())
                    bb.get(data, 0, bb.limit())
                }
            } else {
                val encoder = charset.newEncoder()
                encoder.onMalformedInput(CodingErrorAction.IGNORE)
                encoder.onUnmappableCharacter(CodingErrorAction.IGNORE)

                val bb = encoder.encode(
                    CharBuffer.wrap("${getValue() as String?}\u0000")
                )
                data = ByteArray(bb.limit())
                bb.get(data, 0, bb.limit())
            }
        } catch (ce: CharacterCodingException) { //https://bitbucket.org/ijabz/jaudiotagger/issue/1/encoding-metadata-to-utf-16-can-fail-if
            log.error("${ce.message}:${charset.name()}:${getValue()}")
            throw RuntimeException(ce)
        }
        setValue(data.size)

        return data
    }
}