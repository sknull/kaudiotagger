package de.visualdigits.kaudiotagger.model.frame.lyrics

import de.visualdigits.kaudiotagger.model.exceptions.FieldFrameBodyUnsupported
import de.visualdigits.kaudiotagger.model.exceptions.InvalidTagException
import de.visualdigits.kaudiotagger.model.exceptions.TagException
import de.visualdigits.kaudiotagger.model.frame.AbstractTagFrame
import de.visualdigits.kaudiotagger.model.frame.framebody.AbstractFrameBodyTextInfo
import de.visualdigits.kaudiotagger.model.frame.id3.AbstractID3v2Frame
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
        val text: String?
        val frameIdentifier = frame.getIdentifier()
        if (frameIdentifier?.startsWith("USLT") == true) {
            frameBody = FieldFrameBodyLYR("")
            (frameBody as FieldFrameBodyLYR).addLyric(frame.frameBody as FrameBodyUSLT?)
        } else if (frameIdentifier.startsWith("SYLT")) {
            frameBody = FieldFrameBodyLYR("")
            (frameBody as FieldFrameBodyLYR).addLyric(frame.frameBody as FrameBodySYLT?)
        } else if (frameIdentifier.startsWith("COMM")) {
            text = (frame.frameBody as FrameBodyCOMM).getText()
            frameBody = FieldFrameBodyINF(text)
        } else if (frameIdentifier == "TCOM") {
            textFrame = frame.frameBody as? AbstractFrameBodyTextInfo
            frameBody = FieldFrameBodyAUT("")
            if ((textFrame != null) && (textFrame.getText().length > 0)) {
                frameBody = FieldFrameBodyAUT(textFrame.getText())
            }
        } else if (frameIdentifier == "TALB") {
            textFrame = frame.frameBody as? AbstractFrameBodyTextInfo
            if ((textFrame != null) && (textFrame.getText().length > 0)) {
                frameBody = FieldFrameBodyEAL(textFrame.getText())
            }
        } else if (frameIdentifier == "TPE1") {
            textFrame = frame.frameBody as? AbstractFrameBodyTextInfo
            if ((textFrame != null) && (textFrame.getText().length > 0)) {
                frameBody = FieldFrameBodyEAR(textFrame.getText())
            }
        } else if (frameIdentifier == "TIT2") {
            textFrame = frame.frameBody as? AbstractFrameBodyTextInfo
            if ((textFrame != null) && (textFrame.getText().length > 0)) {
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
    override fun read(byteBuffer: ByteBuffer) {
        val buffer = ByteArray(6)
        // lets scan for a non-zero byte;
        val filePointer: Long
        var b: Byte;
        do {
            b = byteBuffer.get();
        } while (b == 0);
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
        byteBuffer: ByteBuffer?
    ): AbstractLyrics3v2FieldFrameBody {
        val newBody: AbstractLyrics3v2FieldFrameBody
        if (identifier == Lyrics3v2Fields.FIELD_V2_AUTHOR) {
            newBody = FieldFrameBodyAUT(byteBuffer)
        } else if (identifier == Lyrics3v2Fields.FIELD_V2_ALBUM) {
            newBody = FieldFrameBodyEAL(byteBuffer)
        } else if (identifier == Lyrics3v2Fields.FIELD_V2_ARTIST) {
            newBody = FieldFrameBodyEAR(byteBuffer)
        } else if (identifier == Lyrics3v2Fields.FIELD_V2_TRACK) {
            newBody = FieldFrameBodyETT(byteBuffer)
        } else if (identifier == Lyrics3v2Fields.FIELD_V2_IMAGE) {
            newBody = FieldFrameBodyIMG(byteBuffer)
        } else if (identifier == Lyrics3v2Fields.FIELD_V2_INDICATIONS) {
            newBody = FieldFrameBodyIND(byteBuffer)
        } else if (identifier == Lyrics3v2Fields.FIELD_V2_ADDITIONAL_MULTI_LINE_TEXT
        ) {
            newBody = FieldFrameBodyINF(byteBuffer)
        } else if (identifier == Lyrics3v2Fields.FIELD_V2_LYRICS_MULTI_LINE_TEXT
        ) {
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
        return frameBody!!.getSize() + 5 + getIdentifier().length
    }

    /**
     * @return
     */
    override fun getIdentifier(): String {
        if (frameBody == null) {
            return ""
        }
        return frameBody!!.getIdentifier()!!
    }

    /**
     * @param file
     * @throws IOException
     */
    fun write(file: RandomAccessFile) {
        if ((frameBody!!.getSize() > 0) ||
            TagOptionSingleton.lyrics3SaveEmptyField
        ) {
            val buffer = ByteArray(3)
            val str = getIdentifier()
            for (i in 0..<str.length) {
                buffer[i] = str.get(i).code.toByte()
            }
            file.write(buffer, 0, str.length)
            //body.write(file);
        }
    }
}