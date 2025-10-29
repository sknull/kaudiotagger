package de.visualdigits.kaudiotagger.model.id3.datatype

interface HashMapInterface<K, V> {

    /**
     * @return a mapping between the key within the frame and the value
     */
    fun getKeyToValue(): Map<K, V>

    /**
     * @return a mapping between the value to the key within the frame
     */
    fun getValueToKey(): Map<V, K>
}