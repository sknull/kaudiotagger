package de.visualdigits.kaudiotagger.model.tag.id3

import de.visualdigits.kaudiotagger.model.datatype.types.FieldKey
import de.visualdigits.kaudiotagger.model.datatype.types.GenericFieldKey

/**
 * Created by Paul on 09/11/2016.
 */
object ID3NumberTotalFields {

    val numberField: Set<FieldKey> = setOf(
        GenericFieldKey.TRACK,
        GenericFieldKey.DISC_NO,
        GenericFieldKey.MOVEMENT_NO,
    )
    val totalField: Set<FieldKey> = setOf(
        GenericFieldKey.TRACK_TOTAL,
        GenericFieldKey.DISC_TOTAL,
        GenericFieldKey.MOVEMENT_TOTAL,
    )

    fun isNumber(fieldKey: FieldKey?): Boolean {
        return numberField.contains(fieldKey)
    }

    fun isTotal(fieldKey: FieldKey?): Boolean {
        return totalField.contains(fieldKey)
    }
}
