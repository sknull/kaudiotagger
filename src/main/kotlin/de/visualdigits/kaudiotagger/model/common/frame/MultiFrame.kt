package de.visualdigits.kaudiotagger.model.common.frame

import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.frame.AbstractID3v2Frame
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.charset.Charset

class MultiFrame : AbstractID3v2Frame {

    val frames: MutableSet<AbstractID3v2Frame> = mutableSetOf()

    constructor(
        identifier: String?,
            frames: Set<AbstractID3v2Frame> = setOf()
    ): super(identifier) {
        this.frames.addAll(frames)
    }

    constructor(
        identifier: String?,
        vararg frames: AbstractID3v2Frame
    ): this(identifier, frames.toSet())

    fun addFrame(frame: AbstractID3v2Frame) {
        frames.add(frame)
    }

    /**
     * Returns the content of the underlying frames in order.
     *
     * @return Content
     */
    override fun getContent(): String {
        return frames.joinToString("") { f -> f.getContent()?:"" }
    }

    /**
     * Sets the content of the field.
     *
     * @param content fields content.
     */
    override fun setContent(content: String) {
        // to be implemented
    }

    /**
     * Returns the current used charset encoding.
     *
     * @return Charset encoding.
     */
    override fun getEncoding(): Charset {
        return frames.firstOrNull()
            ?.frameBody?.getTextEncoding()
            ?.let { te ->
                TextEncoding.fromId(te)
            }
            ?.charSet
            ?: TextEncoding.ISO_8859_1.charSet
    }

    /**
     * Sets the charset encoding used by the field.
     *
     * @param encoding charset.
     */
    override fun setEncoding(encoding: Charset) {
        // to be implemented
    }

    override fun getFrameIdSize(): Int {
        return frames.firstOrNull()?.getFrameIdSize() ?:0
    }

    override fun getFrameSizeSize(): Int {
        return frames.firstOrNull()?.getFrameSizeSize() ?:0
    }

    override fun getFrameHeaderSize(): Int {
        return frames.firstOrNull()?.getFrameHeaderSize() ?:0
    }

    override fun isCommon(): Boolean {
        return false
    }

    override fun isEmpty(): Boolean {
        return frames.isEmpty() && frames.all { frame -> frame.isEmpty() }
    }

    override fun getRawContent(): ByteArray {
        return getContent().toByteArray(getEncoding())
    }

    override fun write(tagBuffer: ByteArrayOutputStream) {
        frames.forEach { frame -> frame.write(tagBuffer) }
    }

    override fun getSize(): Int {
        return frames.sumOf { frame -> frame.getSize() }
    }

    override fun read(byteBuffer: ByteBuffer?): Boolean {
        return false
    }

    override fun createStructure() {
        frames.forEach { frame -> frame.createStructure() }
    }
}