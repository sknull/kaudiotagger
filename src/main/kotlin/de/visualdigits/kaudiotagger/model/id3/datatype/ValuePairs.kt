package de.visualdigits.kaudiotagger.model.id3.datatype

class ValuePairs(
    val mapping: MutableList<Pair<String, String>> = mutableListOf()
) {

    constructor(copyObject: ValuePairs): this(copyObject.mapping.toMutableList())

    fun add(pair: Pair<String, String>) {
        mapping.add(pair)
    }

    /**
     * Add String Data type to the value list
     *
     * @param value to add to the list
     */
    fun add(key: String, value: String) {
        mapping.add(Pair(key, value))
    }

    /**
     * @return no of values
     */
    fun getNumberOfPairs(): Int {
        return mapping.size
    }

    /**
     * @return no of values
     */
    fun getNumberOfValues(): Int {
        return mapping.size
    }
}