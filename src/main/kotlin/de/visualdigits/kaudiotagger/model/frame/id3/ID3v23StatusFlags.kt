package de.visualdigits.kaudiotagger.model.frame.id3

import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v24StatusFlags
import de.visualdigits.kaudiotagger.model.kframe.ID3v23KFrame
import de.visualdigits.kaudiotagger.util.FileConstants
import de.visualdigits.kaudiotagger.util.StatusFlags
import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.or

class ID3v23StatusFlags(
    val id3v23Frame: ID3v23Frame? = null, 
    flags: Byte = 0
): StatusFlags(flags, flags) {

    companion object {

        const val TYPE_TAGALTERPRESERVATION: String = "typeTagAlterPreservation"
        const val TYPE_FILEALTERPRESERVATION: String = "typeFileAlterPreservation"
        const val TYPE_READONLY: String = "typeReadOnly"

        /**
         * Discard frame if tag altered
         */
        val MASK_TAG_ALTER_PRESERVATION: Byte = FileConstants.BIT7

        /**
         * Discard frame if audio file part altered
         */
        val MASK_FILE_ALTER_PRESERVATION: Byte = FileConstants.BIT6

        /**
         * Frame tagged as read only
         */
        val MASK_READ_ONLY: Byte = FileConstants.BIT5
    }

    /**
     * Use this constructor when convert a v24 frame
     *
     * @param statusFlags
     */
    constructor(id3v23Frame: ID3v23Frame, statusFlags: StatusFlags?): this(id3v23Frame) {
        originalFlags = convertV4ToV3Flags(statusFlags?.originalFlags?:0.toByte())
        writeFlags = originalFlags
        modifyFlags()
    }

    init {
        modifyFlags()
    }

    fun modifyFlags() {
        val str = id3v23Frame?.getIdentifier()
        if (ID3v23KFrame.isDiscardedIfFileAltered(str)) {
            writeFlags = writeFlags or MASK_FILE_ALTER_PRESERVATION
            writeFlags = writeFlags and MASK_TAG_ALTER_PRESERVATION.inv()
        } else {
            writeFlags = writeFlags and MASK_FILE_ALTER_PRESERVATION.inv()
            writeFlags = writeFlags and MASK_TAG_ALTER_PRESERVATION.inv()
        }
    }

    private fun convertV4ToV3Flags(v4Flag: Byte): Byte {
        var v3Flag = 0.toByte()
        if ((v4Flag and ID3v24StatusFlags.MASK_FILE_ALTER_PRESERVATION) != 0.toByte()
        ) {
            v3Flag = v3Flag or MASK_FILE_ALTER_PRESERVATION
        }
        if ((v4Flag and ID3v24StatusFlags.MASK_TAG_ALTER_PRESERVATION) != 0.toByte()) {
            v3Flag = v3Flag or MASK_TAG_ALTER_PRESERVATION
        }
        return v3Flag
    }
}