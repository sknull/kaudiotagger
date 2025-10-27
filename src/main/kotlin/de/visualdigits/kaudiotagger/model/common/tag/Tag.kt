package de.visualdigits.kaudiotagger.model.common.tag

import de.visualdigits.kaudiotagger.model.common.types.GenericFieldKey
import de.visualdigits.kaudiotagger.model.common.field.TagField
import de.visualdigits.kaudiotagger.model.images.Artwork

/**
 * This interface represents the basic data structure for the default
 * audio library functionality.<br>
 * <p>
 * Some audio file tagging systems allow to specify multiple values for one type
 * of information. The artist for example. Some songs may be a cooperation of
 * two or more artists. Sometimes a tagging user wants to specify them in the
 * tag without making one long text string.<br>
 * <p>
 * The addField() method can be used for this but it is possible the underlying implementation
 * does not support that kind of storing multiple values and will just overwrite the existing value<br>
 * <br>
 * <b>Code Examples:</b><br>
 *
 * <pre>
 * <code>
 * AudioFile file = AudioFileIO.read(new File(&quot;C:\\test.mp3&quot;));
 *
 * Tag tag = file.getTag();
 * </code>
 * </pre>
 *
 * @author Raphael Slinckx
 * @author Paul Taylor
 */
interface Tag {

    /**
     * Create the field based on the generic key and add it to the tag
     *
     *
     * This is handled differently by different formats
     *
     * @param genericKey
     * @param value
     * @throws KeyNotFoundException
     * @throws FieldDataInvalidException
     */
    fun addField(genericKey: GenericFieldKey, vararg value: String)

    /**
     * Delete any fields with this key
     *
     * @param GenericFieldKey
     * @throws KeyNotFoundException
     */
    fun deleteField(genericKey: GenericFieldKey)

    /**
     * Delete any fields with this Flac (Vorbis Comment) id
     *
     * @param key
     * @throws KeyNotFoundException
     */
    fun deleteField(key: String)


    /**
     * Returns `true`, if at least one of the contained
     * [fields][TagField] is a common field ([TagField.isCommon]).
     *
     * @return `true` if a [common][TagField.isCommon]
     * field is present.
     */
    fun hasCommonFields(): Boolean

    /**
     * Determines whether the tag has at least one field with the specified field key.
     *
     * @param GenericFieldKey
     * @return
     */
    fun hasField(fieldKey: GenericFieldKey): Boolean

    /**
     * Determines whether the tag has at least one field with the specified
     * &quot;id&quot;.
     *
     * @param id The field id to look for.
     * @return `true` if tag contains a [TagField] with the
     * given [id][TagField.getIdentifier].
     */
    fun hasField(id: String): Boolean

    /**
     * Determines whether the tag has no fields specified.<br></br>
     *
     * @return `true` if tag contains no field.
     */
    fun isEmpty(): Boolean

    /**
     * Retrieve all String values that exist for this generic key
     *
     * @param id
     * @return
     * @throws KeyNotFoundException
     */
    fun getAll(id: GenericFieldKey): List<String>

    fun getFirst(genericKey: GenericFieldKey): String?

    /**
     * Delete any instance of tag fields used to store artwork
     *
     *
     * We need this additional deleteField method because in some formats artwork can be stored
     * in multiple fields
     *
     * @throws KeyNotFoundException
     */
    fun deleteArtworkField()

    /**
     * Create artwork field based on the data in artwork
     *
     * @param artwork
     * @return suitable tagfield for this format that represents the artwork data
     * @throws FieldDataInvalidException
     */
    fun createField(artwork: Artwork): TagField

    /**
     * Create artwork field based on the data in artwork and then add it to the tag itself
     *
     * @param artwork
     * @throws FieldDataInvalidException
     */
    fun addField(artwork: Artwork)

    /**
     * Adds a field to the structure, used internally by the library<br></br>
     *
     * @param field The field to add.
     * @throws FieldDataInvalidException
     */
    fun addField(field: TagField)

    /**
     * Create a new field based on generic key, used internally by the library
     *
     *
     * Only textual data supported at the moment. The genericKey will be mapped
     * to the correct implementation key and return a TagField.
     *
     *
     * Usually the value field should only be one value, but certain fields may require more than one value
     * currently the only field to require this is the MUSICIAN field, it should contain instrument and then
     * performer name
     *
     * @param genericKey is the generic key
     * @param value      to store
     * @return
     * @throws KeyNotFoundException
     * @throws FieldDataInvalidException
     */
    fun createField(genericKey: GenericFieldKey, vararg value: String): TagField

    /**
     * Creates isCompilation field
     *
     *
     * It is useful to have this method because it handles ensuring that the correct value to represent a boolean
     * is stored in the underlying field format.
     *
     * @param value
     * @return
     * @throws KeyNotFoundException
     * @throws FieldDataInvalidException
     */
    fun createCompilationField(value: Boolean): TagField

    /**
     * @return a list of all artwork in this file using the format independent Artwork class
     */
    fun getArtworkList(): List<Artwork>

    /**
     * @return first artwork or null if none exist
     */
    fun getFirstArtwork(): Artwork? = getArtworkList().firstOrNull()
}