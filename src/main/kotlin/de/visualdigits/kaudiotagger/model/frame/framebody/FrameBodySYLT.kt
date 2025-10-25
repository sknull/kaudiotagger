package de.visualdigits.kaudiotagger.model.frame.framebody

import de.visualdigits.kaudiotagger.model.datatype.ByteArraySizeTerminated
import de.visualdigits.kaudiotagger.model.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.datatype.NumberHashMap
import de.visualdigits.kaudiotagger.model.datatype.StringHashMap
import de.visualdigits.kaudiotagger.model.datatype.StringNullTerminated
import de.visualdigits.kaudiotagger.model.datatype.types.EventTimingTimestampTypes
import de.visualdigits.kaudiotagger.model.datatype.types.Languages
import de.visualdigits.kaudiotagger.model.datatype.types.SynchronisedLyricsContentType
import de.visualdigits.kaudiotagger.model.datatype.types.TextEncoding
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.AbstractID3v2FrameBody
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v23FrameBody
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v24FrameBody
import de.visualdigits.kaudiotagger.model.datatype.types.ID3v24Frames
import de.visualdigits.kaudiotagger.model.frame.framebody.AbstractTagFrameBody
import java.nio.ByteBuffer

/**
 * Synchronised lyrics3/text frame.
 *
 *
 *
 *
 * This is another way of incorporating the words, said or sung lyrics3,
 * in the audio file as text, this time, however, in sync with the
 * audio. It might also be used to describing events e.g. occurring on a
 * stage or on the screen in sync with the audio. The header includes a
 * content descriptor, represented with as terminated textstring. If no
 * descriptor is entered, 'Content descriptor' is $00 (00) only.
 *
 * <center><table border=0 width="70%">
 * <tr><td colspan=2>&lt;Header for 'Synchronised lyrics3/text', ID: "SYLT"&gt;</td></tr>
 * <tr><td>Text encoding</td><td width="80%">$xx</td></tr>
 * <tr><td>Language</td><td>$xx xx xx</td></tr>
 * <tr><td>Time stamp format</td><td>$xx</td></tr>
 * <tr><td>Content type</td><td>$xx</td></tr>
 * <tr><td>Content descriptor</td><td>&lt;text string according to encoding&gt; $00 (00)</td></tr>
</table></center> *
 *
 * <center><table border=0 width="70%">
 * <tr><td rowspan=2 valign=top>Encoding:</td><td>$00</td><td>ISO-8859-1 character set is used => $00 is sync identifier.</td></tr>
 * <tr><td>$01</td><td>Unicode character set is used => $00 00 is sync identifier.</td></tr>
</table></center> *
 *
 * <center><table border=0 width="70%">
 * <tr><td rowspan=7 valign=top>Content type:</td><td>$00</td><td width="80%">is other</td></tr>
 * <tr><td>$01</td><td>is lyrics3</td></tr>
 * <tr><td>$02</td><td>is text transcription</td></tr>
 * <tr><td>$03</td><td>is movement/part name (e.g. "Adagio")</td></tr>
 * <tr><td>$04</td><td>is events (e.g. "Don Quijote enters the stage")</td></tr>
 * <tr><td>$05</td><td>is chord (e.g. "Bb F Fsus")</td></tr>
 * <tr><td>$06</td><td>is trivia/'pop up' information</td></tr>
</table></center> *
 *
 *
 * Time stamp format is:
 *
 *
 * $01 Absolute time, 32 bit sized, using MPEG frames as unit<br></br>
 * $02 Absolute time, 32 bit sized, using milliseconds as unit
 *
 *
 * Abolute time means that every stamp contains the time from the
 * beginning of the file.
 *
 *
 * The text that follows the frame header differs from that of the
 * unsynchronised lyrics3/text transcription in one major way. Each
 * syllable (or whatever size of text is considered to be convenient by
 * the encoder) is a null terminated string followed by a time stamp
 * denoting where in the sound file it belongs. Each sync thus has the
 * following structure:
 *
 * <table border=0 width="70%">
 * <tr><td colspan=2>Terminated text to be synced (typically a syllable)</td></tr>
 * <tr><td nowrap>Sync identifier (terminator to above string)</td><td width="80%">$00 (00)</td></tr>
 * <tr><td>Time stamp</td><td>$xx (xx ...)</td></tr>
</table> *
 *
 *
 * The 'time stamp' is set to zero or the whole sync is omitted if
 * located directly at the beginning of the sound. All time stamps
 * should be sorted in chronological order. The sync can be considered
 * as a validator of the subsequent string.
 *
 *
 * Newline ($0A) characters are allowed in all "SYLT" frames and should
 * be used after every entry (name, event etc.) in a frame with the
 * content type $03 - $04.
 *
 *
 * A few considerations regarding whitespace characters: Whitespace
 * separating words should mark the beginning of a new word, thus
 * occurring in front of the first syllable of a new word. This is also
 * valid for new line characters. A syllable followed by a comma should
 * not be broken apart with a sync (both the syllable and the comma
 * should be before the sync).
 *
 *
 * An example: The "USLT" passage
 *
 *
 * "Strangers in the night" $0A "Exchanging glances"
 *
 * would be "SYLT" encoded as:
 *
 *
 * "Strang" $00 xx xx "ers" $00 xx xx " in" $00 xx xx " the" $00 xx xx
 * " night" $00 xx xx 0A "Ex" $00 xx xx "chang" $00 xx xx "ing" $00 xx
 * xx "glan" $00 xx xx "ces" $00 xx xx
 *
 * There may be more than one "SYLT" frame in each tag, but only one
 * with the same language and content descriptor.
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
class FrameBodySYLT: AbstractID3v2FrameBody, ID3v24FrameBody, ID3v23FrameBody {
    /**
     * Creates a new FrameBodySYLT datatype.
     */
    constructor()

    /**
     * Copy Constructor
     *
     * @param body
     */
    constructor(body: FrameBodySYLT) : super(body)

    /**
     * Creates a new FrameBodySYLT datatype.
     *
     * @param textEncoding
     * @param language
     * @param timeStampFormat
     * @param contentType
     * @param description
     * @param lyrics
     */
    constructor(
        textEncoding: Int,
        language: String,
        timeStampFormat: Int,
        contentType: Int,
        description: String,
        lyrics: ByteArray
    ) {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding)
        setObjectValue(DataTypes.OBJ_LANGUAGE, language)
        setObjectValue(DataTypes.OBJ_TIME_STAMP_FORMAT, timeStampFormat)
        setObjectValue(DataTypes.OBJ_CONTENT_TYPE, contentType)
        setObjectValue(DataTypes.OBJ_DESCRIPTION, description)
        setObjectValue(DataTypes.OBJ_DATA, lyrics)
    }

    constructor(
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(byteBuffer, frameSize)

    constructor(
        identifier: String? = null,
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(identifier, byteBuffer, frameSize)

    val language: String?
        /**
         * @return language code
         */
        get() = getObjectValue(DataTypes.OBJ_LANGUAGE) as String?

    val timeStampFormat: Int
        /**
         * @return timestamp format key
         */
        get() = (getObjectValue(DataTypes.OBJ_TIME_STAMP_FORMAT) as Number
                ).toInt()

    val contentType: Int
        /**
         * @return content type key
         */
        get() = (getObjectValue(DataTypes.OBJ_CONTENT_TYPE) as Number).toInt()

    val description: String?
        /**
         * @return description
         */
        get() = getObjectValue(DataTypes.OBJ_DESCRIPTION) as String?

    /**
     * @return frame identifier
     */
    override fun getIdentifier(): String {
        return ID3v24Frames.SYNC_LYRIC.id
    }

    var lyrics: ByteArray?
        /**
         * Get lyrics3
         *
         *
         * TODO:better format
         *
         * @return lyrics3
         */
        get() = this.getObjectValue(DataTypes.OBJ_DATA) as ByteArray?
        /**
         * Set lyrics3
         *
         *
         * TODO:provide a more user friendly way of adding lyrics3
         *
         * @param data
         */
        set(data) {
            this.setObjectValue(DataTypes.OBJ_DATA, data)
        }

    /**
     * Setup Object List
     */
    override fun setupObjectList() {
        objectList.add(
            NumberHashMap(
                DataTypes.OBJ_TEXT_ENCODING,
                this,
                TextEncoding.TEXT_ENCODING_FIELD_SIZE
            )
        )
        objectList.add(
            StringHashMap(
                DataTypes.OBJ_LANGUAGE,
                this,
                Languages.LANGUAGE_FIELD_SIZE
            )
        )
        objectList.add(
            NumberHashMap(
                DataTypes.OBJ_TIME_STAMP_FORMAT,
                this,
                EventTimingTimestampTypes.TIMESTAMP_KEY_FIELD_SIZE
            )
        )
        objectList.add(
            NumberHashMap(
                DataTypes.OBJ_CONTENT_TYPE,
                this,
                SynchronisedLyricsContentType.CONTENT_KEY_FIELD_SIZE
            )
        )
        objectList.add(StringNullTerminated(DataTypes.OBJ_DESCRIPTION, this))

        //TODO:This hold the actual lyrics3
        objectList.add(ByteArraySizeTerminated(DataTypes.OBJ_DATA, this))
    }
}
