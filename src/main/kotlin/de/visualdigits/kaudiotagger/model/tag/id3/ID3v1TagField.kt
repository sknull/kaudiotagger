package de.visualdigits.kaudiotagger.model.tag.id3

import de.visualdigits.kaudiotagger.model.datatype.types.ID3v1FieldKey
import de.visualdigits.kaudiotagger.model.field.TagField
import de.visualdigits.kaudiotagger.model.field.TagTextField
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.Locale


/**
 * This class encapsulates the name and content of a tag entry in id3 fields
 * <br></br>
 *
 * @author @author Raphael Slinckx (KiKiDonK)
 * @author Christian Laireiter (liree)
 */
class ID3v1TagField : TagTextField {

    /**
     * Stores the id (name) of the tag field. <br></br>
     */
    val id: String

    /**
     * If `true`, the id of the current encapsulated tag field is
     * specified as a common field. <br></br>
     * Example is "ARTIST" which should be interpreted by any application as the
     * artist of the media content. <br></br>
     * Will be set during construction with [.checkCommon].
     */
    var common = false

    /**
     * Stores the content of the tag field. <br></br>
     */
    private var content: String? = null

    /**
     * Creates an instance.
     *
     * @param raw Raw byte data of the tagfield.
     */
    constructor(raw: ByteArray) {
        val field = String(raw, StandardCharsets.ISO_8859_1)

        val i = field.indexOf('=')
        if (i == -1) {
            //Beware that ogg ID, must be capitalized and contain no space..
            this.id = "ERRONEOUS"
            this.content = field
        } else {
            this.id = field.substring(0, i).uppercase(Locale.getDefault())
            if (field.length > i) {
                this.content = field.substring(i + 1)
            } else {
                //We have "XXXXXX=" with nothing after the "="
                this.content = ""
            }
        }
        checkCommon()
    }

    /**
     * This method examines the ID of the current field and modifies
     * [.common]in order to reflect if the tag id is a commonly used one.
     * <br></br>
     */
    private fun checkCommon() {
        this.common =
            id == ID3v1FieldKey.TITLE.name ||
                    id == ID3v1FieldKey.ALBUM.name ||
                    id == ID3v1FieldKey.ARTIST.name ||
                    id == ID3v1FieldKey.GENRE.name ||
                    id == ID3v1FieldKey.YEAR.name ||
                    id == ID3v1FieldKey.COMMENT.name ||
                    id == ID3v1FieldKey.TRACK.name
    }

    /**
     * Creates an instance.
     *
     * @param fieldId      ID (name) of the field.
     * @param fieldContent Content of the field.
     */
    constructor(fieldId: String, fieldContent: String) {
        this.id = fieldId.uppercase(Locale.getDefault())
        this.content = fieldContent
        checkCommon()
    }

    override fun copyContent(field: TagField) {
        if (field is TagTextField) {
            this.content = field.getContent()
        }
    }

    override fun getIdentifier(): String {
        return id
    }

    override fun getEncoding(): Charset {
        return StandardCharsets.ISO_8859_1
    }

    override fun setEncoding(s: Charset) {
        //Do nothing, encoding is always ISO-8859-1 for this tag
    }

    override fun getRawContent(): ByteArray {
        val size = ByteArray(4)
        val idBytes = this.id.toByteArray(StandardCharsets.ISO_8859_1)
        val contentBytes = this.content?.toByteArray(StandardCharsets.ISO_8859_1)
        val b = ByteArray(4 + idBytes.size + 1 + (contentBytes?.size?:0))

        val length = idBytes.size + 1 + (contentBytes?.size?:0)
        size[3] = ((length and -0x1000000) shr 24).toByte()
        size[2] = ((length and 0x00FF0000) shr 16).toByte()
        size[1] = ((length and 0x0000FF00) shr 8).toByte()
        size[0] = (length and 0x000000FF).toByte()

        var offset = 0
        copy(size, b, offset)
        offset += 4
        copy(idBytes, b, offset)
        offset += idBytes.size
        b[offset] = 0x3D.toByte()
        offset++ // "="
        copy(contentBytes, b, offset)

        return b
    }

    /**
     * This method will copy all bytes of `src` to `dst`
     * at the specified location.
     *
     * @param src       bytes to copy.
     * @param dst       where to copy to.
     * @param dstOffset at which position of `dst` the data should be
     * copied.
     */
    fun copy(src: ByteArray?, dst: ByteArray, dstOffset: Int) {
        src?.also { s -> System.arraycopy(s, 0, dst, dstOffset, src.size) }
    }

    override fun isBinary(): Boolean {
        return false
    }

    override fun isBinary(b: Boolean) {
        //Do nothing, always false
    }

    override fun isCommon(): Boolean {
        return common
    }

    override fun isEmpty(): Boolean {
        return "" == this.content
    }

    override fun getContent(): String? {
        return content
    }

    override fun setContent(s: String) {
        this.content = s
    }
}
