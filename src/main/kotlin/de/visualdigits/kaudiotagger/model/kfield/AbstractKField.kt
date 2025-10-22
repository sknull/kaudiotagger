package de.visualdigits.kaudiotagger.model.kfield

import de.visualdigits.kaudiotagger.model.kframe.KFrame

abstract class AbstractKField<V : Any>(
    val id: KFrame<*>,
    val value: V
) {
}