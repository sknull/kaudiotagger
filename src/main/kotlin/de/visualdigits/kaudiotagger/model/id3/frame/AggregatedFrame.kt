package de.visualdigits.kaudiotagger.model.id3.frame

import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import java.io.ByteArrayOutputStream
import java.nio.charset.Charset

/**
 * Required when a single generic field maps to multiple ID3 Frames
 */
abstract class AggregatedFrame : AbstractID3v2Frame() {

    protected var frames = LinkedHashSet<AbstractID3v2Frame>()

    fun addFrame(frame: AbstractID3v2Frame?) {
        frame?.also { f -> frames.add(f) }
    }

    fun getFrames(): MutableSet<AbstractID3v2Frame> {
        return frames
    }

    /**
     * Returns the content of the underlying frames in order.
     *
     * @return Content
     */
    override fun getContent(): String {
        val sb = StringBuilder()
        for (next in frames) {
            sb.append(next.getContent())
        }
        return sb.toString()
    }

    override fun setContent(content: String) {
        // to be implemented
    }

    /**
     * Returns the current used charset encoding.
     *
     * @return Charset encoding.
     */
    override fun getEncoding(): Charset {
        val textEncoding = frames
            .iterator()
            .next()
            .frameBody
            ?.getTextEncoding()
        return TextEncoding.fromId(textEncoding)?.charSet?: TextEncoding.ISO_8859_1.charSet
    }

    override fun isCommon(): Boolean {
        return true
    }

    override fun isEmpty(): Boolean {
        return false
    }

    override fun getIdentifier(): String? {
        return frames.joinToString("") { f -> f.getIdentifier()?:"" }
    }

    override fun getSize(): Int {
        return frames.sumOf { frame -> frame.getSize() }
    }

    override fun getRawContent(): ByteArray? {
        return null
    }

    override fun write(tagBuffer: ByteArrayOutputStream) {
        frames.forEach { frame -> frame.write(tagBuffer) }
    }
}
