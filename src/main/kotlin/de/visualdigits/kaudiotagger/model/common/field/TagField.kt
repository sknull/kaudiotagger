package de.visualdigits.kaudiotagger.model.common.field

interface TagField {

    /**
     * Returns the Id of the represented tag field.<br></br>
     * This value should uniquely identify a kind of tag data, like title.
     * [AbstractTag] will use the &quot;id&quot; to summarize multiple
     * fields.
     *
     * @return Unique identifier for the fields type. (title, artist...)
     */
    fun getIdentifier(): String?

    /**
     * This method delivers the binary representation of the fields data in
     * order to be directly written to the file.<br></br>
     *
     * @return Binary data representing the current tag field.<br></br>
     * implementation will need to convert the text data in java to
     * a specific charset encoding. In these cases an
     * [java.io.UnsupportedEncodingException] may occur.
     */
    fun getRawContent(): ByteArray?

    /**
     * Identifies a field to be of common use.<br></br>
     *
     *
     * Some software may differ between common and not common fields. A common
     * one is for sure the title field. A web link may not be of common use for
     * tagging. However some file formats, or future development of users
     * expectations will make more fields common than now can be known.
     *
     * @return `true` if the field is of common use.
     */
    fun isCommon(): Boolean

    /**
     * Determines whether the content of the field is empty.<br></br>
     *
     * @return `true` if no data is stored (or empty String).
     */
    fun isEmpty(): Boolean
}