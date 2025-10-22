package de.visualdigits.kaudiotagger.model.kfield

import de.visualdigits.kaudiotagger.model.kframe.KFrame

class BinaryKField(
    id: KFrame<*>,
    value: ByteArray,
    val mimeType: String? = null
) : AbstractKField<ByteArray>(id, value) {

    override fun toString(): String {
        return "$id [$mimeType]: BYTES"
    }
}