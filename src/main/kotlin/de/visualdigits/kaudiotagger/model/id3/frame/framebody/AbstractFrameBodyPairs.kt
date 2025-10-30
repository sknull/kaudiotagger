package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.id3.datatype.NumberHashMap
import de.visualdigits.kaudiotagger.model.id3.datatype.PairedTextEncodedStringNullTerminated
import de.visualdigits.kaudiotagger.model.id3.datatype.ValuePairs
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.StringTokenizer

abstract class AbstractFrameBodyPairs: AbstractID3v2FrameBody, ID3v24FrameBody {

    /**
     * Creates a new AbstractFrameBodyPairs datatype.
     */
    constructor() {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1.id)
    }

    /**
     * Creates a new AbstractFrameBodyPairs data type.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String) {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding)
        setText(text)
    }

    constructor(
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(byteBuffer, frameSize)

    /**
     * Parse text as a null separated pairing of function and name
     *
     * @param text
     */
    fun addPair(text: String) {
        val stz = StringTokenizer(text, "\u0000")
        if (stz.countTokens() == 2) {
            addPair(stz.nextToken(), stz.nextToken())
        } else {
            addPair("", text)
        }
    }

    /**
     * Add pair
     *
     * @param function
     * @param name
     */
    fun addPair(function: String?, name: String) {
        if (function == null) {
            return
        }
        val value = (getObject(
                DataTypes.OBJ_TEXT
            ) as? PairedTextEncodedStringNullTerminated)?.getValue() as? ValuePairs
        value?.add(function, name)
    }

    /**
     * Remove all Pairs
     */
    fun resetPairs() {
        val value =
            (getObject(
                DataTypes.OBJ_TEXT
            ) as? PairedTextEncodedStringNullTerminated)?.getValue() as? ValuePairs
        value?.mapping?.clear()
    }

    /**
     * Because have a text encoding we need to check the data values do not contain characters that cannot be encoded in
     * current encoding before we write data. If they do change the encoding.
     */
    override fun write(tagBuffer: ByteArrayOutputStream) {
        if (!(getObject(
                DataTypes.OBJ_TEXT
            ) as PairedTextEncodedStringNullTerminated).canBeEncoded()
        ) {
            this.setTextEncoding(TextEncoding.UTF_16.id)
        }
        super.write(tagBuffer)
    }

    /**
     * Consists of a text encoding , and then a series of null terminated Strings, there should be an even number
     * of Strings as they are paired as involvement/involvee
     */
    override fun setupObjectList() {
        objectList.add(
            NumberHashMap(
                DataTypes.OBJ_TEXT_ENCODING,
                this,
                TextEncoding.TEXT_ENCODING_FIELD_SIZE
            )
        )
        objectList.add(
            PairedTextEncodedStringNullTerminated(DataTypes.OBJ_TEXT, this)
        )
    }

    fun getPairing(): ValuePairs? {
        return getObject(
            DataTypes.OBJ_TEXT
        )?.getValue() as? ValuePairs
    }

    /**
     * Get key at index
     *
     * @param index
     * @return value at index
     */
    fun getKeyAtIndex(index: Int): String {
        val text: PairedTextEncodedStringNullTerminated =
            getObject(DataTypes.OBJ_TEXT) as PairedTextEncodedStringNullTerminated
        return (text.getValue() as ValuePairs).mapping[index].first
    }

    /**
     * Get value at index
     *
     * @param index
     * @return value at index
     */
    fun getValueAtIndex(index: Int): String {
        val text: PairedTextEncodedStringNullTerminated =
            getObject(DataTypes.OBJ_TEXT) as PairedTextEncodedStringNullTerminated
        return (text.getValue() as ValuePairs).mapping[index].second
    }

    override fun getUserFriendlyValue(): String {
        return getText()
    }

    fun getText(): String {
        val text: PairedTextEncodedStringNullTerminated =
            getObject(DataTypes.OBJ_TEXT) as PairedTextEncodedStringNullTerminated
        val sb = StringBuilder()
        var count = 1
        for (entry in (text.getValue() as ValuePairs).mapping) {
            sb.append(entry.first + '\u0000' + entry.second)
            if (count != getNumberOfPairs()) {
                sb.append('\u0000')
            }
            count++
        }
        return sb.toString()
    }

    /**
     * Set the text, decoded as pairs of involvee - involvement
     *
     * @param text
     */
    fun setText(text: String) {
        val value: ValuePairs =
            ValuePairs()
        val stz = StringTokenizer(text, "\u0000")

        while (stz.hasMoreTokens()) {
            val key = stz.nextToken()
            if (stz.hasMoreTokens()) {
                value.add(key, stz.nextToken())
            }
        }
        setObjectValue(DataTypes.OBJ_TEXT, value)
    }

    /**
     * @return number of text pairs
     */
    fun getNumberOfPairs(): Int {
        val text: PairedTextEncodedStringNullTerminated =
            getObject(DataTypes.OBJ_TEXT) as PairedTextEncodedStringNullTerminated
        return (text.getValue() as ValuePairs).getNumberOfPairs()
    }
}