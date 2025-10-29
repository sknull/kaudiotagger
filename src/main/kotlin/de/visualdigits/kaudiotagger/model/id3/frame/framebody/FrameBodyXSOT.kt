package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.types.ID3v23FrameId
import java.nio.ByteBuffer

/**
 * Title Sort name, this is what MusicBrainz uses in ID3v23 because TSOT not supported.
 *
 *
 * However iTunes uses TSOT even in ID3v23, so we have two possible options
 */
class FrameBodyXSOT: AbstractFrameBodyTextInfo, ID3v23FrameBody {
    /**
     * Creates a new FrameBodyTSOT datatype.
     */
    constructor()

    constructor(body: FrameBodyXSOT) : super(body)

    /**
     * Creates a new FrameBodyTSOT datatype.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String) : super(textEncoding, text)

    /**
     * Creates a new FrameBodyTSOT datatype.
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
        return ID3v23FrameId.TITLE_SORT_ORDER_MUSICBRAINZ.id
    }
}
