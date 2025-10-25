package de.visualdigits.kaudiotagger.model.frame.framebody

import de.visualdigits.kaudiotagger.model.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.datatype.NumberFixedLength
import de.visualdigits.kaudiotagger.model.datatype.NumberVariableLength
import de.visualdigits.kaudiotagger.model.datatype.StringNullTerminated
import de.visualdigits.kaudiotagger.model.datatype.types.ID3v24Frames
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.AbstractID3v2FrameBody
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v23FrameBody
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v24FrameBody
import java.nio.ByteBuffer

/**
 * Popularimeter frame.
 *
 *
 *
 *
 * The purpose of this frame is to specify how good an audio file is.
 * Many interesting applications could be found to this frame such as a
 * playlist that features better audiofiles more often than others or it
 * could be used to profile a person's taste and find other 'good' files
 * by comparing people's profiles. The frame is very simple. It contains
 * the email address to the user, one rating byte and a four byte play
 * counter, intended to be increased with one for every time the file is
 * played. The email is a terminated string. The rating is 1-255 where
 * 1 is worst and 255 is best. 0 is unknown. If no personal counter is
 * wanted it may be omitted. When the counter reaches all one's, one
 * byte is inserted in front of the counter thus making the counter
 * eight bits bigger in the same away as the play counter ("PCNT").
 * There may be more than one "POPM" frame in each tag, but only one
 * with the same email address.
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
 * TODO : Counter should be optional, whereas we always expect it although allow a size of zero
 * needs testing.
 */
class FrameBodyPOPM: AbstractID3v2FrameBody, ID3v24FrameBody, ID3v23FrameBody {

    /**
     * Creates a new FrameBodyPOPM datatype.
     */
    constructor() {
        this.setObjectValue(DataTypes.OBJ_EMAIL, "")
        this.setObjectValue(DataTypes.OBJ_RATING, 0L)
        this.setObjectValue(DataTypes.OBJ_COUNTER, 0L)
    }

    constructor(body: FrameBodyPOPM) : super(body)

    /**
     * Creates a new FrameBodyPOPM datatype.
     *
     * @param emailToUser
     * @param rating
     * @param counter
     */
    constructor(emailToUser: String, rating: Long, counter: Long) {
        this.setObjectValue(DataTypes.OBJ_EMAIL, emailToUser)
        this.setObjectValue(DataTypes.OBJ_RATING, rating)
        this.setObjectValue(DataTypes.OBJ_COUNTER, counter)
    }

    constructor(
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(byteBuffer, frameSize)

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v24Frames.POPULARIMETER.id
    }

    override fun getUserFriendlyValue(): String {
        return this.emailToUser + ":" + this.rating + ":" + this.counter
    }

    var emailToUser: String?
        /**
         * @return the memail of the user who rated this
         */
        get() = getObjectValue(DataTypes.OBJ_EMAIL) as String?
        /**
         * @param description
         */
        set(description) {
            setObjectValue(DataTypes.OBJ_EMAIL, description)
        }

    var rating: Long
        /**
         * @return the rating given to this file
         */
        get() = (getObjectValue(DataTypes.OBJ_RATING) as Number).toLong()
        /**
         * Set the rating given to this file
         *
         * @param rating
         */
        set(rating) {
            setObjectValue(DataTypes.OBJ_RATING, rating)
        }

    var counter: Long
        /**
         * @return the play count of this file
         */
        get() = (getObjectValue(DataTypes.OBJ_COUNTER) as Number).toLong()
        /**
         * Set the play counter of this file
         *
         * @param counter
         */
        set(counter) {
            setObjectValue(DataTypes.OBJ_COUNTER, counter)
        }

    fun parseString(data: String) {
        try {
            val value = data.toInt()
            this.rating = value.toLong()
            this.emailToUser = MEDIA_MONKEY_NO_EMAIL
        } catch (nfe: NumberFormatException) {
        }
    }

    /**
     *
     */
    override fun setupObjectList() {
        objectList.add(StringNullTerminated(DataTypes.OBJ_EMAIL, this))
        objectList.add(
            NumberFixedLength(DataTypes.OBJ_RATING, this, RATING_FIELD_SIZE)
        )
        objectList.add(
            NumberVariableLength(
                DataTypes.OBJ_COUNTER,
                this,
                COUNTER_MINIMUM_FIELD_SIZE
            )
        )
    }

    companion object {
        const val MEDIA_MONKEY_NO_EMAIL: String = "no@email"
        private const val RATING_FIELD_SIZE = 1
        private const val COUNTER_MINIMUM_FIELD_SIZE = 0
    }
}
