package de.visualdigits.kaudiotagger.model.frame.framebody.id3

import de.visualdigits.kaudiotagger.model.frame.id3.ID3v23StatusFlags
import de.visualdigits.kaudiotagger.model.frame.id3.ID3v24Frame
import de.visualdigits.kaudiotagger.model.kframe.ID3v24KFrame
import de.visualdigits.kaudiotagger.util.FileConstants
import de.visualdigits.kaudiotagger.util.StatusFlags
import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.or

class ID3v24StatusFlags(
    val id3v24Frame: ID3v24Frame? = null,
    flags: Byte = 0
): StatusFlags(flags, flags) {

    companion object {

        const val TYPE_TAGALTERPRESERVATION: String = "typeTagAlterPreservation"
        const val TYPE_FILEALTERPRESERVATION: String = "typeFileAlterPreservation"
        const val TYPE_READONLY: String = "typeReadOnly"

        /**
         * Discard frame if tag altered
         */
        val MASK_TAG_ALTER_PRESERVATION: Byte = FileConstants.BIT6

        /**
         * Discard frame if audio part of file altered
         */
        val MASK_FILE_ALTER_PRESERVATION: Byte = FileConstants.BIT5

        /**
         * Frame tagged as read only
         */
        val MASK_READ_ONLY: Byte = FileConstants.BIT4
    }

    /**
     * Use this constructor when convert a v23 frame
     *
     * @param statusFlags
     */
    constructor(id3v24Frame: ID3v24Frame, statusFlags: StatusFlags?): this(id3v24Frame) {
        originalFlags = convertV3ToV4Flags(statusFlags?.originalFlags?:0.toByte())
        writeFlags = originalFlags
        modifyFlags()
    }

    init {
        modifyFlags()
    }

    /**
     * Makes modifications to flags based on specification and frameid
     */
    fun modifyFlags() {
        val str: String? = id3v24Frame!!.getIdentifier()
        if (ID3v24KFrame.isDiscardedIfFileAltered(str)) {
            writeFlags = writeFlags or  MASK_FILE_ALTER_PRESERVATION.toByte()
            writeFlags = writeFlags and  MASK_TAG_ALTER_PRESERVATION.toByte().inv()
        } else {
            writeFlags = writeFlags and MASK_FILE_ALTER_PRESERVATION.toByte().inv()
            writeFlags = writeFlags and MASK_TAG_ALTER_PRESERVATION.toByte().inv()
        }
    }

    /**
     * Convert V3 Flags to equivalent V4 Flags
     *
     * @param v3Flag
     * @return
     */
    private fun convertV3ToV4Flags(v3Flag: Byte): Byte {
        var v4Flag = 0.toByte()
        if ((v3Flag and ID3v23StatusFlags.MASK_FILE_ALTER_PRESERVATION.toByte()) != 0.toByte()
        ) {
            v4Flag = v4Flag or MASK_FILE_ALTER_PRESERVATION.toByte()
        }
        if ((v3Flag and ID3v23StatusFlags.MASK_TAG_ALTER_PRESERVATION.toByte()) != 0.toByte()) {
            v4Flag = v4Flag or MASK_TAG_ALTER_PRESERVATION.toByte()
        }
        return v4Flag
    }
}