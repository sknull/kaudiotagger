package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import java.nio.ByteBuffer

/**
 * File type Text information frame.
 *
 *
 * The 'File type' frame indicates which type of audio this tag defines.
 * The following type and refinements are defined:
 *
 * <table border=0 width="70%">
 * <tr><td>MPG</td><td rowspan=8>&nbsp;</td><td width="100%">MPEG Audio</td></tr>
 * <tr><td align=right>/1   </td><td>MPEG 1/2 layer I           </td></tr>
 * <tr><td align=right>/2   </td><td>MPEG 1/2 layer II          </td></tr>
 * <tr><td align=right>/3   </td><td>MPEG 1/2 layer III         </td></tr>
 * <tr><td align=right>/2.5 </td><td>MPEG 2.5                   </td></tr>
 * <tr><td align=right>/AAC </td><td>Advanced audio compression </td></tr>
 * <tr><td>VQF</td><td>Transform-domain Weighted Interleave Vector Quantization</td></tr>
 * <tr><td>PCM              </td><td>Pulse Code Modulated audio </td></tr>
</table> *
 *
 * but other types may be used, not for these types though. This is used
 * in a similar way to the predefined types in the "TMED" frame, but
 * without parentheses. If this frame is not present audio type is
 * assumed to be "MPG".
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
class FrameBodyTFLT: AbstractFrameBodyTextInfo, ID3v24FrameBody, ID3v23FrameBody {
    /**
     * Creates a new FrameBodyTFLT datatype.
     */
    constructor()

    constructor(body: FrameBodyTFLT) : super(body)

    /**
     * Creates a new FrameBodyTFLT datatype.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String) : super(textEncoding, text)

    /**
     * Creates a new FrameBodyTFLT datatype.
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
        return ID3v24FrameId.FILE_TYPE.id
    }
}
