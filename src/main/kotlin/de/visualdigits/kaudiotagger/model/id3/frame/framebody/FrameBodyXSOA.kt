package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.common.types.ID3v23Frames
import java.nio.ByteBuffer

/**
 * Album Sort name, this is what MusicBrainz uses in ID3v23 because TSOA not supported.
 *
 *
 * However iTunes uses TSOA even in ID3v23, so we have two possible options
 */
class FrameBodyXSOA: AbstractFrameBodyTextInfo, ID3v23FrameBody {
    /**
     * Creates a new FrameBodyTSOT datatype.
     */
    constructor()

    constructor(body: FrameBodyXSOA) : super(body)

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
     * @throws InvalidTagException
     */
    constructor(byteBuffer: ByteBuffer?, frameSize: Int) : super(byteBuffer, frameSize)

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v23Frames.ALBUM_SORT_ORDER_MUSICBRAINZ.id
    }
}
