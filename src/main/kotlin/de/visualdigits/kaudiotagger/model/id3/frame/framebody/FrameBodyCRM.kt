package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.datatype.ByteArraySizeTerminated
import de.visualdigits.kaudiotagger.model.id3.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.id3.datatype.StringNullTerminated
import de.visualdigits.kaudiotagger.model.id3.types.ID3v22FrameId
import java.nio.ByteBuffer

/**
 * Encrypted meta frame
 *
 *
 * This frame contains one or more encrypted frames. This enables
 * protection of copyrighted information such as pictures and text, that
 * people might want to pay extra for. Since standardisation of such an
 * encryption scheme is beyond this document, all "CRM" frames begin with
 * a terminated string with a URL [URL] containing an email address, or a
 * link to a location where an email adress can be found, that belongs to
 * the organisation responsible for this specific encrypted meta frame.
 *
 *
 * Questions regarding the encrypted frame should be sent to the
 * indicated email address. If a $00 is found directly after the 'Frame
 * size', the whole frame should be ignored, and preferably be removed.
 * The 'Owner identifier' is then followed by a short content description
 * and explanation as to why it's encrypted. After the
 * 'content/explanation' description, the actual encrypted block follows.
 *
 *
 * When an ID3v2 decoder encounters a "CRM" frame, it should send the
 * datablock to the 'plugin' with the corresponding 'owner identifier'
 * and expect to receive either a datablock with one or several ID3v2
 * frames after each other or an error. There may be more than one "CRM"
 * frames in a tag, but only one with the same 'owner identifier'.
 *
 *
 * Encrypted meta frame  "CRM"
 * Frame size            $xx xx xx
 * Owner identifier      <textstring> $00 (00)
 * Content/explanation   <textstring> $00 (00)
 * Encrypted datablock   <binary data>
</binary></textstring></textstring> */
class FrameBodyCRM

    : AbstractID3v2FrameBody, ID3v22FrameBody {
    /**
     * Creates a new FrameBodyCRM datatype.
     */
    constructor()

    constructor(body: FrameBodyCRM) : super(body)

    /**
     * Creates a new FrameBodyCRM datatype.
     *
     * @param owner
     * @param description
     * @param data
     */
    constructor(owner: String?, description: String?, data: ByteArray?) {
        this.setObjectValue(DataTypes.OBJ_OWNER, owner)
        this.setObjectValue(DataTypes.OBJ_DESCRIPTION, description)
        this.setObjectValue(DataTypes.OBJ_ENCRYPTED_DATABLOCK, data)
    }

    /**
     * Creates a new FrameBodyCRM datatype.
     *
     * @param byteBuffer
     * @param frameSize
     */
    constructor(byteBuffer: ByteBuffer?, frameSize: Int) : super(byteBuffer, frameSize)

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v22FrameId.ENCRYPTED_FRAME.id
    }

    fun getOwner(description: String?) {
        setObjectValue(DataTypes.OBJ_OWNER, description)
    }

    override fun setupObjectList() {
        objectList.add(StringNullTerminated(DataTypes.OBJ_OWNER, this))
        objectList.add(StringNullTerminated(DataTypes.OBJ_DESCRIPTION, this))
        objectList.add(
            ByteArraySizeTerminated(DataTypes.OBJ_ENCRYPTED_DATABLOCK, this)
        )
    }
}
