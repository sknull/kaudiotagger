package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.types.ID3V24Frame
import java.nio.ByteBuffer

/**
 * iTunes grouping field introduced in 12.5.4.42, before that iTunes used TIT1 as is the norm, but it now uses that
 * for Classical Work. Jaudiotagger maps WORK key to TXXX:WORK for work because TIT1 is in use more for GROUPING.
 * Unfortunately TIT1 is defined in ID3 spec to be used for either which is problematic
 *
 * @author : Paul Taylor
 * @author : Eric Farng
 * @version $Id$
 */
class FrameBodyGRP1: AbstractFrameBodyTextInfo, ID3v24FrameBody, ID3v23FrameBody {

    /**
     * Creates a new FrameBodyTBPM datatype.
     */
    constructor()

    constructor(body: FrameBodyGRP1) : super(body)

    /**
     * Creates a new FrameBodyTBPM datatype.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String) : super(textEncoding, text)

    /**
     * Creates a new FrameBodyTBPM datatype.
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
        return ID3V24Frame.ITUNES_GROUPING.id
    }
}
