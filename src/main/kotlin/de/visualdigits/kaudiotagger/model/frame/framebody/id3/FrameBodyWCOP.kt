package de.visualdigits.kaudiotagger.model.frame.framebody.id3

import de.visualdigits.kaudiotagger.model.datatype.types.ID3v24Frames
import de.visualdigits.kaudiotagger.model.frame.framebody.AbstractFrameBodyUrlLink
import java.nio.ByteBuffer

/**
 * Copyright/Legal information URL link frames.
 *
 * The 'Copyright/Legal information' frame is a URL pointing at a webpage where the terms of use and ownership of the file is described.
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
class FrameBodyWCOP: AbstractFrameBodyUrlLink, ID3v24FrameBody, ID3v23FrameBody {
    /**
     * Creates a new FrameBodyWCOP datatype.
     */
    constructor()

    /**
     * Creates a new FrameBodyWCOP datatype.
     *
     * @param urlLink
     */
    constructor(urlLink: String) : super(urlLink)

    constructor(body: FrameBodyWCOP) : super(body)

    /**
     * Creates a new FrameBodyWCOP datatype.
     *
     * @param byteBuffer
     * @param frameSize
     * @throws java.io.IOException
     * @throws InvalidTagException
     */
    constructor(byteBuffer: ByteBuffer?, frameSize: Int) : super(byteBuffer, frameSize)

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v24Frames.URL_COPYRIGHT.id
    }
}
