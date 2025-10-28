package de.visualdigits.kaudiotagger.model.common.field

interface TagField {
    /**
     * This method copies the data of the given field to the current data.<br></br>
     *
     * @param field The field containing the data to be taken.
     */
    fun copyContent(field: TagField)

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
     * Determines whether the represented field contains (is made up of) binary
     * data, instead of text data.<br></br>
     * Software can identify fields to be displayed because they are human
     * readable if this method returns `false`.
     *
     * @return `true` if field represents binary data (not human
     * readable).
     */
    fun isBinary(): Boolean

    /**
     * This method will set the field to represent binary data.<br></br>
     *
     *
     * Some implementations may support conversions.<br></br>
     * As of now (Octobre 2005) there is no implementation really using this
     * method to perform useful operations.
     *
     * @param b `true`, if the field contains binary data.
     * //@deprecated As for now is of no use. Implementations should use another
     * //            way of setting this property.
     */
    fun isBinary(b: Boolean)

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