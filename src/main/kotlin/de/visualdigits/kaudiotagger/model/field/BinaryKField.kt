package de.visualdigits.kaudiotagger.model.field

import de.visualdigits.kaudiotagger.model.frame.KFrame

class BinaryKField(
    id: KFrame<*>,
    value: ByteArray,
    val mimeType: String? = null
) : AbstractKField<ByteArray>(id, value) {

    override fun toString(): String {
        return "$id [$mimeType]: BYTES"
    }
}