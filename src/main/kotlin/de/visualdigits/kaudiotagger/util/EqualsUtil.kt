package de.visualdigits.kaudiotagger.util

import java.lang.Double
import java.lang.Float

object EqualsUtil {

    fun areEqual(aThis: Boolean, aThat: Boolean): Boolean {
        return aThis == aThat
    }

    fun areEqual(aThis: Char, aThat: Char): Boolean {
        return aThis == aThat
    }

    fun areEqual(aThis: Long, aThat: Long): Boolean {
        /*
         * Implementation Note
         * Note that byte, short, and int are handled by this method, through
         * implicit conversion.
         */
        return aThis == aThat
    }

    fun areEqual(aThis: Float, aThat: Float): Boolean {
        return Float.floatToIntBits(aThis.toFloat()) == Float.floatToIntBits(aThat.toFloat())
    }

    fun areEqual(aThis: Double, aThat: Double): Boolean {
        return Double.doubleToLongBits(aThis.toDouble()) == Double.doubleToLongBits(aThat.toDouble())
    }

    /**
     * Possibly-null object field.
     *
     *
     * Includes type-safe enumerations and collections, but does not include
     * arrays. See class comment.
     */
    fun areEqual(aThis: Any?, aThat: Any?): Boolean {
        return aThis == aThat
    }
}