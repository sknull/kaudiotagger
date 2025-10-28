package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.common.datatype.ByteArraySizeTerminated
import de.visualdigits.kaudiotagger.model.common.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.id3.types.ID3V24Frame
import java.nio.ByteBuffer

/**
 * Equalisation (2)
 *
 *
 * This is another subjective, alignment frame. It allows the user to
 * predefine an equalisation curve within the audio file. There may be
 * more than one "EQU2" frame in each tag, but only one with the same
 * identification string.
 *
 *
 * &lt;Header of 'Equalisation (2)', ID: "EQU2"&gt;
 * Interpolation method  $xx
 * Identification        &lt;text string&gt; $00
 *
 *
 * The 'interpolation method' describes which method is preferred when
 * an interpolation between the adjustment point that follows. The
 * following methods are currently defined:
 *
 *
 * $00  Band
 * No interpolation is made. A jump from one adjustment level to
 * another occurs in the middle between two adjustment points.
 * $01  Linear
 * Interpolation between adjustment points is linear.
 *
 *
 * The 'identification' string is used to identify the situation and/or
 * device where this adjustment should apply. The following is then
 * repeated for every adjustment point
 *
 *
 * Frequency          $xx xx
 * Volume adjustment  $xx xx
 *
 *
 * The frequency is stored in units of 1/2 Hz, giving it a range from 0
 * to 32767 Hz.
 *
 *
 * The volume adjustment is encoded as a fixed point decibel value, 16
 * bit signed integer representing (adjustment*512), giving +/- 64 dB
 * with a precision of 0.001953125 dB. E.g. +2 dB is stored as $04 00
 * and -2 dB is $FC 00.
 *
 *
 * Adjustment points should be ordered by frequency and one frequency
 * should only be described once in the frame.
 */
class FrameBodyEQU2

    : AbstractID3v2FrameBody, ID3v24FrameBody {
    /**
     * Creates a new FrameBodyEQU2 datatype.
     */
    constructor()

    constructor(body: FrameBodyEQU2) : super(body)

    /**
     * Creates a new FrameBodyEQU2 datatype.
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
        return ID3V24Frame.EQUALISATION2.id
    }

    /**
     *
     */
    override fun setupObjectList() {
        objectList.add(ByteArraySizeTerminated(DataTypes.OBJ_DATA, this))
    }
}
