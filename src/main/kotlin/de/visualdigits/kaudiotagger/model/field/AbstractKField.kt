package de.visualdigits.kaudiotagger.model.field

import de.visualdigits.kaudiotagger.model.frame.KFrame

abstract class AbstractKField<V : Any>(
    val id: KFrame<*>,
    val value: V
) {
}