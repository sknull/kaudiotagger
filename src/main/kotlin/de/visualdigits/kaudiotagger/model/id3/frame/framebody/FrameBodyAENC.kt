package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.common.datatype.ByteArraySizeTerminated
import de.visualdigits.kaudiotagger.model.common.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.common.datatype.NumberFixedLength
import de.visualdigits.kaudiotagger.model.common.datatype.StringNullTerminated
import de.visualdigits.kaudiotagger.model.id3.types.ID3V24Frame
import java.nio.ByteBuffer

/**
 * Audio encryption Frame.
 *
 *
 *
 *
 * This frame indicates if the actual audio stream is encrypted, and by
 * whom. Since standardisation of such encrypion scheme is beyond this
 * document, all "AENC" frames begin with a terminated string with a
 * URL containing an email address, or a link to a location where an
 * email address can be found, that belongs to the organisation
 * responsible for this specific encrypted audio file. Questions
 * regarding the encrypted audio should be sent to the email address
 * specified. If a $00 is found directly after the 'Frame size' and the
 * audiofile indeed is encrypted, the whole file may be considered
 * useless.
 *
 *
 * After the 'Owner identifier', a pointer to an unencrypted part of the
 * audio can be specified. The 'Preview start' and 'Preview length' is
 * described in frames. If no part is unencrypted, these fields should
 * be left zeroed. After the 'preview length' field follows optionally a
 * datablock required for decryption of the audio. There may be more
 * than one "AENC" frames in a tag, but only one with the same 'Owner
 * identifier'.
 *
 * <table border=0 width="70%">
 * <tr><td colspan=2>&lt;Header for 'Audio encryption', ID: "AENC"&gt;</td></tr>
 * <tr><td>Owner identifier  </td><td>&lt;text string&gt; $00    </td></tr>
 * <tr><td>Preview start     </td><td>$xx xx                     </td></tr>
 * <tr><td>Preview length    </td><td>$xx xx                     </td></tr>
 * <tr><td>Encryption info   </td><td>&lt;binary data&gt;        </td></tr>
</table> *
 *
 *
 * For more details, please refer to the ID3 specifications:
 *
 *  * [ID3 v2.3.0 Spec](http://www.id3.org/id3v2.3.0.txt)
 *
 *
 * @author : Paul Taylor
 * @author : Eric Farng
 * @version $Id$
 */
class FrameBodyAENC: AbstractID3v2FrameBody, ID3v24FrameBody, ID3v23FrameBody {
    /**
     * Creates a new FrameBodyAENC datatype.
     */
    constructor() {
        this.setObjectValue(DataTypes.OBJ_OWNER, "")
        this.setObjectValue(DataTypes.OBJ_PREVIEW_START, 0.toShort())
        this.setObjectValue(DataTypes.OBJ_PREVIEW_LENGTH, 0.toShort())
        this.setObjectValue(DataTypes.OBJ_ENCRYPTION_INFO, ByteArray(0))
    }

    constructor(body: FrameBodyAENC) : super(body)

    /**
     * Creates a new FrameBodyAENC datatype.
     *
     * @param owner
     * @param previewStart
     * @param previewLength
     * @param data
     */
    constructor(
        owner: String?,
        previewStart: Short,
        previewLength: Short,
        data: ByteArray?
    ) {
        this.setObjectValue(DataTypes.OBJ_OWNER, owner)
        this.setObjectValue(DataTypes.OBJ_PREVIEW_START, previewStart)
        this.setObjectValue(DataTypes.OBJ_PREVIEW_LENGTH, previewLength)
        this.setObjectValue(DataTypes.OBJ_ENCRYPTION_INFO, data)
    }

    /**
     * Creates a new FrameBodyAENC datatype.
     *
     * @param byteBuffer
     * @param frameSize
     * @throws InvalidTagException if unable to create framebody from buffer
     */
    constructor(byteBuffer: ByteBuffer?, frameSize: Int) : super(byteBuffer, frameSize)

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3V24Frame.AUDIO_ENCRYPTION.id
    }

    /**
     * @param description
     */
    fun getOwner(description: String?) {
        setObjectValue(DataTypes.OBJ_OWNER, description)
    }

    /**
     *
     */
    override fun setupObjectList() {
        objectList.add(StringNullTerminated(DataTypes.OBJ_OWNER, this))
        objectList.add(NumberFixedLength(DataTypes.OBJ_PREVIEW_START, this, 2))
        objectList.add(
            NumberFixedLength(DataTypes.OBJ_PREVIEW_LENGTH, this, 2)
        )
        objectList.add(
            ByteArraySizeTerminated(DataTypes.OBJ_ENCRYPTION_INFO, this)
        )
    }
}
