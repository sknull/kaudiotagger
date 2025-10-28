package de.visualdigits.kaudiotagger.model.id3.tag

import de.visualdigits.kaudiotagger.model.id3.frame.ID3v23Frame
import de.visualdigits.kaudiotagger.model.id3.types.ID3v23Frames
import de.visualdigits.kaudiotagger.util.FileConstants
import de.visualdigits.kaudiotagger.util.StatusFlags

class ID3v23StatusFlags: StatusFlags {

    companion object {

        const val TYPE_TAGALTERPRESERVATION: String = "typeTagAlterPreservation"
        const val TYPE_FILEALTERPRESERVATION: String = "typeFileAlterPreservation"
        const val TYPE_READONLY: String = "typeReadOnly"

        /**
         * Discard frame if tag altered
         */
        val MASK_TAG_ALTER_PRESERVATION: Int = FileConstants.BIT7

        /**
         * Discard frame if audio file part altered
         */
        val MASK_FILE_ALTER_PRESERVATION: Int = FileConstants.BIT6

        /**
         * Frame tagged as read only
         */
        val MASK_READ_ONLY: Int = FileConstants.BIT5
    }

    var id3v23Frame: ID3v23Frame? = null

    /**
     * Use this constructor when convert a v24 frame
     *
     * @param statusFlags
     */
    constructor(id3v23Frame: ID3v23Frame, statusFlags: StatusFlags?): this(id3v23Frame) {
        originalFlags = convertV4ToV3Flags(statusFlags?.originalFlags?:0)
        writeFlags = originalFlags
        modifyFlags()
    }

    constructor(
        id3v23Frame: ID3v23Frame? = null,
        flags: Int = 0
    ): super(flags, flags) {
        this.id3v23Frame = id3v23Frame
        modifyFlags()
    }

    fun modifyFlags() {
        val str = id3v23Frame?.getIdentifier()
        if (ID3v23Frames.isDiscardedIfFileAltered(str)) {
            writeFlags = writeFlags or MASK_FILE_ALTER_PRESERVATION
            writeFlags = writeFlags and MASK_TAG_ALTER_PRESERVATION.inv()
        } else {
            writeFlags = writeFlags and MASK_FILE_ALTER_PRESERVATION.inv()
            writeFlags = writeFlags and MASK_TAG_ALTER_PRESERVATION.inv()
        }
    }

    private fun convertV4ToV3Flags(v4Flag: Int): Int {
        var v3Flag = 0
        if ((v4Flag and ID3v24StatusFlags.MASK_FILE_ALTER_PRESERVATION) != 0
        ) {
            v3Flag = v3Flag or MASK_FILE_ALTER_PRESERVATION
        }
        if ((v4Flag and ID3v24StatusFlags.MASK_TAG_ALTER_PRESERVATION) != 0) {
            v3Flag = v3Flag or MASK_TAG_ALTER_PRESERVATION
        }
        return v3Flag
    }
}