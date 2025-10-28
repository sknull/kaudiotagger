package de.visualdigits.kaudiotagger.model.common.frame

import de.visualdigits.kaudiotagger.model.common.field.TagField
import de.visualdigits.kaudiotagger.model.common.field.TagTextField
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.frame.AbstractID3v2Frame
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

abstract class AggregatedFrame() : TagTextField {

    //TODO rather than just maintaining insertion order we want to define a preset order
    private var frames: MutableSet<AbstractID3v2Frame> = mutableSetOf()

    fun addFrame(frame: AbstractID3v2Frame) {
        frames.add(frame)
    }

    fun getFrames(): MutableSet<AbstractID3v2Frame> {
        return frames
    }

    /**
     * Returns the content of the underlying frames in order.
     *
     * @return Content
     */
    override fun getContent(): String? {
        val sb = StringBuilder()
        for (next in frames) {
            sb.append(next.getContent())
        }
        return sb.toString()
    }

    /**
     * Sets the content of the field.
     *
     * @param content fields content.
     */
    override fun setContent(content: String) {
    }

    /**
     * Returns the current used charset encoding.
     *
     * @return Charset encoding.
     */
    override fun getEncoding(): Charset? {
        val textEncoding = frames
            .iterator()
            .next()
            .frameBody
            ?.getTextEncoding()
            ?: TextEncoding.ISO_8859_1.id
        return TextEncoding.fromId(textEncoding)?.charSet
    }

    /**
     * Sets the charset encoding used by the field.
     *
     * @param encoding charset.
     */
    override fun setEncoding(encoding: Charset) {
    }

    override fun copyContent(field: TagField) {
    }

    override fun getIdentifier(): String? {
        val sb = StringBuilder()
        for (next in frames) {
            sb.append(next.getIdentifier())
        }
        return sb.toString()
    }

    override fun isCommon(): Boolean {
        return true
    }

    override fun isBinary(): Boolean {
        return false
    }

    override fun isBinary(b: Boolean) {
    }

    override fun isEmpty(): Boolean {
        return false
    }

    override fun getRawContent(): ByteArray? {
        throw UnsupportedEncodingException()
    }
}