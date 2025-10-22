package de.visualdigits.kaudiotagger.model.datatype

import de.visualdigits.kaudiotagger.model.exceptions.InvalidDataTypeException
import de.visualdigits.kaudiotagger.model.frame.framebody.AbstractTagFrameBody
import java.io.ByteArrayOutputStream

class PairedTextEncodedStringNullTerminated: AbstractDataType {

    constructor(identifier: String, frameBody: AbstractTagFrameBody): super(identifier, frameBody) {
        value = ValuePairs()
    }

    constructor(copyObject: TextEncodedStringSizeTerminated): super(copyObject) {
        
        value = ValuePairs()
    }

    constructor(copyObject: PairedTextEncodedStringNullTerminated): super(copyObject)

    /**
     * Returns the size in bytes of this dataType when written to file
     *
     * @return size of this dataType
     */
    override fun getSize(): Int {
        return size
    }

    /**
     * Check the value can be encoded with the specified encoding
     *
     * @return
     */
    fun canBeEncoded(): Boolean {
        for (entry in (value as ValuePairs).mapping) {
            val next =
                TextEncodedStringNullTerminated(
                    identifier,
                    frameBody,
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
                    TextEncodedStringNullTerminated(identifier, frameBody)
                key.readByteArray(arr, offset)
                size += key.getSize()
                offset += key.getSize()
                if (key.getSize() == 0) {
                    break
                }

                try {
                    //Read Value
                    val result =
                        TextEncodedStringNullTerminated(identifier, frameBody)
                    result.readByteArray(arr, offset)
                    size += result.getSize()
                    offset += result.getSize()
                    if (result.getSize() == 0) {
                        break
                    }
                    //Add to value
                    (value as ValuePairs).add(
                        (key.value as String?)!!,
                        (result.value as String?)!!
                    )
                } catch (idte: InvalidDataTypeException) {
                    //Value may not be null terminated if it is the last value
                    //Read Value
                    if (offset >= arr.size) {
                        break
                    }
                    val result =
                        TextEncodedStringSizeTerminated(identifier, frameBody)
                    result.readByteArray(arr, offset)
                    size += result.getSize()
                    offset += result.getSize()
                    if (result.getSize() == 0) {
                        break
                    }
                    //Add to value
                    (value as ValuePairs).add(
                        (key.value as String?)!!,
                        (result.value as String?)!!
                    )
                    break
                }
            } catch (idte: InvalidDataTypeException) {
                break
            }

            if (size == 0) {
                log.warn("No null terminated Strings found")
                throw InvalidDataTypeException("No null terminated Strings found")
            }
        }
        log.debug(
            "Read  PairTextEncodedStringNullTerminated:$value size:$size"
        )
    }

    /**
     * For every String write to byteBuffer
     *
     * @return byteBuffer that should be written to file to persist this dataType.
     */
    override fun writeByteArray(): ByteArray {
        log.debug("Writing PairTextEncodedStringNullTerminated");
        return ByteArrayOutputStream(). use { buffer ->
            var localSize = 0;
            (value as ValuePairs).mapping.forEach { pair ->
                var next = TextEncodedStringNullTerminated(
                        identifier,
                        frameBody,
                        pair.first
                    );
                buffer.write(next.writeByteArray());
                localSize += next.getSize();
                next = TextEncodedStringNullTerminated(
                        identifier,
                        frameBody,
                        pair.second
                    );
                buffer.write(next.writeByteArray());
                localSize += next.getSize();
            }

            //Update size member variable
            size = localSize;

            log.debug("Written PairTextEncodedStringNullTerminated");
            buffer.toByteArray()
        }
    }
}