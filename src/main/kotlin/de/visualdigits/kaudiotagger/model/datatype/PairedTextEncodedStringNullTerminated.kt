package de.visualdigits.kaudiotagger.model.datatype

import de.visualdigits.kaudiotagger.model.exceptions.InvalidDataTypeException
import de.visualdigits.kaudiotagger.model.frame.framebody.AbstractTagFrameBody
import java.io.ByteArrayOutputStream
import java.io.IOException

class PairedTextEncodedStringNullTerminated: AbstractDataType {

    constructor(identifier: String, frameBody: AbstractTagFrameBody): super(identifier, frameBody) {
        setValue(ValuePairs())
    }

    constructor(copyObject: TextEncodedStringSizeTerminated): super(copyObject) {
        setValue(ValuePairs())
    }

    constructor(copyObject: PairedTextEncodedStringNullTerminated): super(copyObject)

    /**
     * Check the value can be encoded with the specified encoding
     *
     * @return
     */
    fun canBeEncoded(): Boolean {
        for (entry in (getValue() as ValuePairs).mapping) {
            val next =
                TextEncodedStringNullTerminated(
                    identifier,
                    frameBody?:error("No frame body"),
                    entry.second
                )
            if (!next.canBeEncoded()) {
                return false
            }
        }
        return true
    }

    /**
     * Read Null Terminated Strings from the array starting at offset, continue until unable to find any null terminated
     * Strings or until reached the end of the array. The offset should be set to byte after the last null terminated
     * String found.
     *
     * @param arr    to read the Strings from
     * @param offset in the array to start reading from
     * @throws InvalidDataTypeException if unable to find any null terminated Strings
     */
    override fun readByteArray(arr: ByteArray, offset: Int) {
        var offset = offset
        log.debug(
            "Reading PairTextEncodedStringNullTerminated from array from offset:" +
                    offset
        )
        //Continue until unable to read a null terminated String
        while (true) {
            try {
                //Read Key
                val key =
                    TextEncodedStringNullTerminated(identifier, frameBody?:error("No frame body"))
                key.readByteArray(arr, offset)
                addSize(key.getSize())
                offset += key.getSize()
                if (key.getSize() == 0) {
                    break
                }

                try {
                    //Read Value
                    val result =
                        TextEncodedStringNullTerminated(identifier, frameBody?:error("No frame body"))
                    result.readByteArray(arr, offset)
                    addSize(result.getSize())
                    offset += result.getSize()
                    if (result.getSize() == 0) {
                        break
                    }
                    //Add to value
                    (getValue() as ValuePairs).add(
                        (key.getValue() as? String)?:"",
                        (result.getValue() as? String)?:""
                    )
                } catch (idte: InvalidDataTypeException) {
                    //Value may not be null terminated if it is the last value
                    //Read Value
                    if (offset >= arr.size) {
                        break
                    }
                    val result =
                        TextEncodedStringSizeTerminated(identifier, frameBody?:error("No frame body"))
                    result.readByteArray(arr, offset)
                    addSize(result.getSize())
                    offset += result.getSize()
                    if (result.getSize() == 0) {
                        break
                    }
                    //Add to value
                    (getValue() as ValuePairs).add(
                        (key.getValue() as? String)?:"",
                        (result.getValue() as? String)?:""
                    )
                    break
                }
            } catch (idte: InvalidDataTypeException) {
                break
            }

            if (getSize() == 0) {
                log.warn("No null terminated Strings found")
                throw InvalidDataTypeException("No null terminated Strings found")
            }
        }
        log.debug(
            "Read  PairTextEncodedStringNullTerminated:${getValue()} size:${getSize()}"
        )
    }

    /**
     * For every String write to byteBuffer
     *
     * @return byteBuffer that should be written to file to persist this dataType.
     */
    override fun writeByteArray(): ByteArray {
        log.debug("Writing PairTextEncodedStringNullTerminated")

        var localSize = 0
        var buffer = ByteArrayOutputStream()
        try {
            (getValue() as ValuePairs).mapping.forEach { pair ->
                var next = TextEncodedStringNullTerminated(
                        identifier,
                        frameBody,
                        pair.first
                    )
                buffer.write(next.writeByteArray())
                localSize += next.getSize()
                next = TextEncodedStringNullTerminated(
                        identifier,
                frameBody,
                pair.second
                )
                buffer.write(next.writeByteArray())
                localSize += next.getSize()
            }
        } catch (ioe: IOException) {
            //This should never happen because the write is internal with the JVM it is not to a file
            log.error(
                    "IOException in MultipleTextEncodedStringNullTerminated when writing byte array",
                    ioe
            )
            throw RuntimeException(ioe)
        }

        //Update size member variable
        setSize(localSize)

        log.debug("Written PairTextEncodedStringNullTerminated")
        return buffer.toByteArray()
    }
}