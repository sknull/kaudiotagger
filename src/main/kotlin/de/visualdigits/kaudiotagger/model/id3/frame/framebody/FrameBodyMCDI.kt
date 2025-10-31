package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.datatype.ByteArraySizeTerminated
import de.visualdigits.kaudiotagger.model.id3.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import java.nio.ByteBuffer

/**
 * Music CD identifier frame.
 *
 *
 *
 *
 * This frame is intended for music that comes from a CD, so that the CD
 * can be identified in databases such as the CDDB. The frame
 * consists of a binary dump of the Table Of Contents, TOC, from the CD,
 * which is a header of 4 bytes and then 8 bytes/track on the CD plus 8
 * bytes for the 'lead out' making a maximum of 804 bytes. The offset to
 * the beginning of every track on the CD should be described with a
 * four bytes absolute CD-frame address per track, and not with absolute
 * time. This frame requires a present and valid "TRCK" frame, even if
 * the CD's only got one track. There may only be one "MCDI" frame in
 * each tag.
 *
 * <table border=0 width="70%">
 * <tr><td colspan=2> &lt;Header for 'Music CD identifier', ID: "MCDI"&gt;</td></tr>
 * <tr><td>CD TOC</td><td>&lt;binary data&gt;</td></tr>
</table> *
 *
 *
 * For more details, please refer to the ID3 specifications:
 *
 *  * [ID3 v2.3.0 Spec](http:// www.id3.org/id3v2.3.0.txt)
 *
 *
 * @author : Paul Taylor
 * @author : Eric Farng
 * @version $Id$
 */
class FrameBodyMCDI: AbstractID3v2FrameBody, ID3v24FrameBody, ID3v23FrameBody {
    /**
     * Creates a new FrameBodyMCDI datatype.
     */
    constructor() {
        this.setObjectValue(DataTypes.OBJ_DATA, ByteArray(0))
    }

    constructor(body: FrameBodyMCDI) : super(body)

    /**
     * Creates a new FrameBodyMCDI datatype.
     *
     * @param cdTOC
     */
    constructor(cdTOC: ByteArray?) {
        this.setObjectValue(DataTypes.OBJ_DATA, cdTOC)
    }

    /**
     * Creates a new FrameBodyMCDI datatype.
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
        return ID3v24FrameId.MUSIC_CD_ID.id
    }

    override fun setupObjectList() {
        objectList.add(ByteArraySizeTerminated(DataTypes.OBJ_DATA, this))
    }
}
