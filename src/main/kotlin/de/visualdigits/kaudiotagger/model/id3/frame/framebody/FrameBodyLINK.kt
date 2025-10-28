package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.common.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.common.datatype.StringFixedLength
import de.visualdigits.kaudiotagger.model.common.datatype.StringNullTerminated
import de.visualdigits.kaudiotagger.model.common.datatype.StringSizeTerminated
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24Frames
import java.nio.ByteBuffer

/**
 * Linked information frame.
 *
 *
 *
 *
 * To keep space waste as low as possible this frame may be used to link
 * information from another ID3v2 tag that might reside in another audio
 * file or alone in a binary file. It is recommended that this method is
 * only used when the files are stored on a CD-ROM or other
 * circumstances when the risk of file seperation is low. The frame
 * contains a frame identifier, which is the frame that should be linked
 * into this tag, a URL field, where a reference to the file where
 * the frame is given, and additional ID data, if needed. Data should be
 * retrieved from the first tag found in the file to which this link
 * points. There may be more than one "LINK" frame in a tag, but only
 * one with the same contents. A linked frame is to be considered as
 * part of the tag and has the same restrictions as if it was a physical
 * part of the tag (i.e. only one "RVRB" frame allowed, whether it's
 * linked or not).
 *
 * <table border=0 width="70%">
 * <tr><td>&lt;Header for 'Linked information', ID: "LINK"&gt;   </td></tr>
 * <tr><td>Frame identifier      </td><td>$xx xx xx              </td></tr>
 * <tr><td>URL                   </td><td>&lt;text string&gt; $00</td></tr>
 * <tr><td>ID and additional data</td><td>&lt;text string(s)&gt; </td></tr>
</table> *
 *
 *
 * Frames that may be linked and need no additional data are "IPLS",
 * "MCID", "ETCO", "MLLT", "SYTC", "RVAD", "EQUA", "RVRB", "RBUF", the
 * text information frames and the URL link frames.
 *
 *
 * The "TXXX", "APIC", "GEOB" and "AENC" frames may be linked with
 * the content descriptor as additional ID data.
 *
 *
 * The "COMM", "SYLT" and "USLT" frames may be linked with three bytes
 * of language descriptor directly followed by a content descriptor as
 * additional ID data.
 *
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
class FrameBodyLINK

    : AbstractID3v2FrameBody, ID3v24FrameBody, ID3v23FrameBody {
    /**
     * Creates a new FrameBodyLINK datatype.
     */
    constructor()

    constructor(body: FrameBodyLINK) : super(body)

    /**
     * Creates a new FrameBodyLINK datatype.
     *
     * @param frameIdentifier
     * @param url
     * @param additionalData
     */
    constructor(
        frameIdentifier: String?,
        url: String?,
        additionalData: String?
    ) {
        this.setObjectValue(DataTypes.OBJ_DESCRIPTION, frameIdentifier)
        this.setObjectValue(DataTypes.OBJ_URL, url)
        this.setObjectValue(DataTypes.OBJ_ID, additionalData)
    }

    /**
     * Creates a new FrameBodyLINK datatype.
     *
     * @param byteBuffer
     * @param frameSize
     * @throws InvalidTagException if unable to create framebody from buffer
     */
    constructor(byteBuffer: ByteBuffer?, frameSize: Int) : super(byteBuffer, frameSize)

    val additionalData: String?
        /**
         * @return
         */
        get() = getObjectValue(DataTypes.OBJ_ID) as String?

    /**
     * @param additionalData
     */
    fun getAdditionalData(additionalData: String?) {
        setObjectValue(DataTypes.OBJ_ID, additionalData)
    }

    val frameIdentifier: String?
        /**
         * @return
         */
        get() = getObjectValue(DataTypes.OBJ_DESCRIPTION) as String?

    /**
     * @param frameIdentifier
     */
    fun getFrameIdentifier(frameIdentifier: String?) {
        setObjectValue(DataTypes.OBJ_DESCRIPTION, frameIdentifier)
    }

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v24Frames.LINKED_INFO.id
    }

    /**
     *
     */
    override fun setupObjectList() {
        objectList.add(StringFixedLength(DataTypes.OBJ_DESCRIPTION, this, 4))
        objectList.add(StringNullTerminated(DataTypes.OBJ_URL, this))
        objectList.add(StringSizeTerminated(DataTypes.OBJ_ID, this))
    }
}
