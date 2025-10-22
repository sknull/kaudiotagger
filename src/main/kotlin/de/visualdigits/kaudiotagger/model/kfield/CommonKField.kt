package de.visualdigits.kaudiotagger.model.kfield

import de.visualdigits.kaudiotagger.model.kframe.KFrame

class CommonKField(
    id: KFrame<*>,
    value: String
) : AbstractKField<String>(id, value) {

    override fun toString(): String {
            return "$id: $value"
    }

}