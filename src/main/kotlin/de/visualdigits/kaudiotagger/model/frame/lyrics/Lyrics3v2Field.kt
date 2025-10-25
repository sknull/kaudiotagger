package de.visualdigits.kaudiotagger.model.frame.lyrics

import de.visualdigits.kaudiotagger.model.datatype.types.Lyrics3v2Fields
import de.visualdigits.kaudiotagger.model.frame.framebody.FieldFrameBodyUnsupported
import de.visualdigits.kaudiotagger.model.exceptions.InvalidTagException
import de.visualdigits.kaudiotagger.model.exceptions.TagException
import de.visualdigits.kaudiotagger.model.frame.AbstractTagFrame
import de.visualdigits.kaudiotagger.model.frame.framebody.AbstractFrameBodyTextInfo
import de.visualdigits.kaudiotagger.model.frame.framebody.FrameBodyCOMM
import de.visualdigits.kaudiotagger.model.frame.framebody.FrameBodySYLT
import de.visualdigits.kaudiotagger.model.frame.framebody.FrameBodyUSLT
import de.visualdigits.kaudiotagger.model.frame.id3.AbstractID3v2Frame
import de.visualdigits.kaudiotagger.model.tag.lyrics3.AbstractLyrics3v2FieldFrameBody
import de.visualdigits.kaudiotagger.model.tag.lyrics3.FieldFrameBodyAUT
import de.visualdigits.kaudiotagger.model.tag.lyrics3.FieldFrameBodyEAL
import de.visualdigits.kaudiotagger.model.tag.lyrics3.FieldFrameBodyEAR
import de.visualdigits.kaudiotagger.model.tag.lyrics3.FieldFrameBodyETT
import de.visualdigits.kaudiotagger.model.tag.lyrics3.FieldFrameBodyIMG
import de.visualdigits.kaudiotagger.model.tag.lyrics3.FieldFrameBodyIND
import de.visualdigits.kaudiotagger.model.tag.lyrics3.FieldFrameBodyINF
import de.visualdigits.kaudiotagger.model.tag.lyrics3.FieldFrameBodyLYR
import de.visualdigits.kaudiotagger.util.TagOptionSingleton
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.ByteBuffer

class Lyrics3v2Field: AbstractTagFrame {

    /**
     * Creates a new Lyrics3v2Field datatype.
     */
    constructor()

    constructor(copyObject: Lyrics3v2Field) : super(copyObject)

    /**
     * Creates a new Lyrics3v2Field datatype.
     *
     * @param body
     */
    constructor(body: AbstractLyrics3v2FieldFrameBody) {
        this.frameBody = body
    }

    /**
     * Creates a new Lyrics3v2Field datatype.
     *
     * @param frame
     * @throws TagException
     */
    constructor(frame: AbstractID3v2Frame) {
        val textFrame: AbstractFrameBodyTextInfo?
        val frameIdentifier = frame.getIdentifier()?:error("No frame identifier")
        if (frameIdentifier?.startsWith("USLT") == true) {
            frameBody = FieldFrameBodyLYR("")
            (frameBody as FieldFrameBodyLYR).addLyric(frame.frameBody as FrameBodyUSLT)
        } else if (frameIdentifier.startsWith("SYLT")) {
            frameBody = FieldFrameBodyLYR("")
            (frameBody as FieldFrameBodyLYR).addLyric(frame.frameBody as FrameBodySYLT)
        } else if (frameIdentifier.startsWith("COMM")) {
            val text = (frame.frameBody as FrameBodyCOMM).getText()
            frameBody = FieldFrameBodyINF(text)
        } else if (frameIdentifier == "TCOM") {
            textFrame = frame.frameBody as? AbstractFrameBodyTextInfo
            frameBody = FieldFrameBodyAUT("")
            if ((textFrame != null) && (textFrame.getText().isNotEmpty())) {
                frameBody = FieldFrameBodyAUT(textFrame.getText())
            }
        } else if (frameIdentifier == "TALB") {
            textFrame = frame.frameBody as? AbstractFrameBodyTextInfo
            if ((textFrame != null) && (textFrame.getText().isNotEmpty())) {
                frameBody = FieldFrameBodyEAL(textFrame.getText())
            }
        } else if (frameIdentifier == "TPE1") {
            textFrame = frame.frameBody as? AbstractFrameBodyTextInfo
            if ((textFrame != null) && (textFrame.getText().isNotEmpty())) {
                frameBody = FieldFrameBodyEAR(textFrame.getText())
            }
        } else if (frameIdentifier == "TIT2") {
            textFrame = frame.frameBody as? AbstractFrameBodyTextInfo
            if ((textFrame != null) && (textFrame.getText().isNotEmpty())) {
                frameBody = FieldFrameBodyETT(textFrame.getText())
            }
        } else {
            throw TagException(
                "Cannot createField Lyrics3v2 field from given ID3v2 frame"
            )
        }
    }

    /**
     * Creates a new Lyrics3v2Field datatype.
     *
     * @param byteBuffer
     * @throws InvalidTagException
     */
    constructor(byteBuffer: ByteBuffer) {
        this.read(byteBuffer)
    }

    /**
     * @param byteBuffer
     * @throws InvalidTagException
     * @throws IOException
     */
    override fun read(byteBuffer: ByteBuffer?) {
        if (byteBuffer == null) {
            return
        }
        val buffer = ByteArray(6)
        // lets scan for a non-zero byte;
        val filePointer: Long
        var b: Byte;
        do {
            b = byteBuffer.get();
        } while (b.toInt() == 0);
        byteBuffer.position(byteBuffer.position() - 1);
        // read the 3 character ID
        byteBuffer.get(buffer, 0, 3);
        val identifier = String(buffer, 0, 3);
        // is this a valid identifier?
        if (!Lyrics3v2Fields.isLyrics3v2FieldIdentifier(identifier)) {
            throw InvalidTagException(
                "$identifier is not a valid ID3v2.4 frame"
            );
        }
        frameBody = readBody(identifier, byteBuffer);
    }

    /**
     * Read a Lyrics3 Field from a file.
     *
     * @param identifier
     * @param byteBuffer
     * @return
     * @throws InvalidTagException
     */
    private fun readBody(
        identifier: String,
        byteBuffer: ByteBuffer
    ): AbstractLyrics3v2FieldFrameBody {
        val newBody: AbstractLyrics3v2FieldFrameBody
        if (identifier == Lyrics3v2Fields.AUTHOR.id) {
            newBody = FieldFrameBodyAUT(byteBuffer)
        } else if (identifier == Lyrics3v2Fields.ALBUM.id) {
            newBody = FieldFrameBodyEAL(byteBuffer)
        } else if (identifier == Lyrics3v2Fields.ARTIST.id) {
            newBody = FieldFrameBodyEAR(byteBuffer)
        } else if (identifier == Lyrics3v2Fields.TRACK.id) {
            newBody = FieldFrameBodyETT(byteBuffer)
        } else if (identifier == Lyrics3v2Fields.IMAGE.id) {
            newBody = FieldFrameBodyIMG(byteBuffer)
        } else if (identifier == Lyrics3v2Fields.INDICATIONS.id) {
            newBody = FieldFrameBodyIND(byteBuffer)
        } else if (identifier == Lyrics3v2Fields.ADDITIONAL_MULTI_LINE_TEXT.id) {
            newBody = FieldFrameBodyINF(byteBuffer)
        } else if (identifier == Lyrics3v2Fields.LYRICS_MULTI_LINE_TEXT.id) {
            newBody = FieldFrameBodyLYR(byteBuffer)
        } else {
            newBody = FieldFrameBodyUnsupported(byteBuffer)
        }
        return newBody
    }

    /**
     * @return
     */
    override fun getSize(): Int {
        return (frameBody?.getSize()?:0) + 5 + (getIdentifier()?.length?:0)
    }

    /**
     * @return
     */
    override fun getIdentifier(): String? {
        return frameBody?.getIdentifier()?:""
    }

    /**
     * @param file
     * @throws IOException
     */
    fun write(file: RandomAccessFile) {
        if (((frameBody?.getSize()?:0) > 0) ||
            TagOptionSingleton.lyrics3SaveEmptyField
        ) {
            val buffer = ByteArray(3)
            val str = getIdentifier()
            for (i in 0..< (str?.length?:0)) {
                str?.get(i)?.code?.toByte()?.also { b -> buffer[i] = b }
            }
            file.write(buffer, 0, (str?.length?:0))
            //body.write(file);
        }
    }
}