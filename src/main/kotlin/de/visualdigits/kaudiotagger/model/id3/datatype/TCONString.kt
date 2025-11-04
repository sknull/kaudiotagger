package de.visualdigits.kaudiotagger.model.id3.datatype

import de.visualdigits.kaudiotagger.model.common.frame.framebody.AbstractTagFrameBody

class TCONString : TextEncodedStringSizeTerminated {

    var isNullSeperateMultipleValues = true

    companion object {

        fun splitV23(value: String): List<String> {
            val valuesarray = value
                .replace("(\\(\\d+\\)|\\(RX\\)|\\(CR\\)\\w*)".toRegex(), "\u0000$1")
                .split("\u0000")
                .filter { v -> v.isNotEmpty() }
            var values = valuesarray.toList()
            //Read only list so if empty have to create new list
            if (values.isEmpty()) {
                values = listOf("")
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
    constructor(identifier: String?, frameBody: AbstractTagFrameBody) : super(identifier, frameBody)

    /**
     * Copy constructor
     *
     * @param object
     */
    constructor(copyObject: TCONString): super(copyObject)

    /**
     * Add an additional String to the current String value
     *
     * @param value
     */
    override fun addValue(value: String) {
        // For ID3v24 we separate each value by a null
        if (isNullSeperateMultipleValues) {
            setValue("${getValue()}\u0000$value")
        } else {
            // For ID3v23 if they pass a numeric value in brackets this indicates a mapping to an ID3v2 genre and
            // can be seen as a refinement and therefore do not need the non-standard (for ID3v23) null seperator
            if (value.startsWith("(")) {
                setValue("${getValue()}$value")
            } else {
                setValue("${getValue()}\u0000$value")
            }
        }
    }

    /**
     * How many values are held, each value is separated by a null terminator
     *
     * @return number of values held, usually this will be one.
     */
    override fun getNumberOfValues(): Int {
        return getValues().size
    }

    /**
     * @return list of all values
     */
    override fun getValues(): List<String> {
        return (getValue() as? String)?.let { s ->
            if (isNullSeperateMultipleValues) {
                splitByNullSeperator(s)
            } else {
                splitV23(s)
            }
        }?.toMutableList()
            ?: mutableListOf()
    }

    /**
     * Get the nth value
     *
     * @param index
     * @return the nth value
     */
    override fun getValueAtIndex(index: Int): String? {
        // Split String into separate components
        return getValues()[index]
    }

    /**
     * Get value(s) whilst removing any trailing nulls
     *
     * @return
     */
    override fun getValueWithoutTrailingNull(): String {
        val values = getValues()
        val sb = StringBuffer()
        for (i in values.indices) {
            if (i != 0) {
                sb.append("\u0000")
            }
            sb.append(values[i])
        }
        return sb.toString()
    }
}