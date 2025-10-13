package de.visualdigits.kaudiotagger.model.field

import de.visualdigits.kaudiotagger.model.frame.KFrame

class CommonKField(
    id: KFrame<*>,
    value: String
) : AbstractKField<String>(id, value) {

    override fun toString(): String {
            return "$id: $value"
    }

}