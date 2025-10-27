package de.visualdigits.kaudiotagger.model.common.field

import java.nio.charset.Charset

interface TagTextField : TagField {

    /**
     * Returns the content of the field.
     *
     * @return Content
     */
    fun getContent(): String?

    /**
     * Sets the content of the field.
     *
     * @param content fields content.
     */
    fun setContent(content: String)

    /**
     * Returns the current used charset encoding.
     *
     * @return Charset encoding.
     */
    fun getEncoding(): Charset?

    /**
     * Sets the charset encoding used by the field.
     *
     * @param encoding charset.
     */
    fun setEncoding(encoding: Charset)
}