package de.visualdigits.kaudiotagger.model.frame.framebody

import de.visualdigits.kaudiotagger.model.datatype.AbstractString
import de.visualdigits.kaudiotagger.model.datatype.ByteArraySizeTerminated
import de.visualdigits.kaudiotagger.model.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.datatype.NumberHashMap
import de.visualdigits.kaudiotagger.model.datatype.StringFixedLength
import de.visualdigits.kaudiotagger.model.datatype.StringNullTerminated
import de.visualdigits.kaudiotagger.model.datatype.types.ID3v22Frames
import de.visualdigits.kaudiotagger.model.datatype.types.ImageFormats
import de.visualdigits.kaudiotagger.model.datatype.types.PictureTypes
import de.visualdigits.kaudiotagger.model.datatype.types.TextEncoding
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.AbstractID3v2FrameBody
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v22FrameBody
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

/**
 * ID3v22 Attached Picture
 *
 *
 *  This frame contains a picture directly related to the audio file.
 * Image format is preferably "PNG" [PNG] or "JPG" [JFIF]. Description
 * is a short description of the picture, represented as a terminated
 * textstring. The description has a maximum length of 64 characters,
 * but may be empty. There may be several pictures attached to one file,
 * each in their individual "PIC" frame, but only one with the same
 * ontent descriptor. There may only be one picture with the picture
 * type declared as picture type $01 and $02 respectively. There is a
 * possibility to put only a link to the image file by using the image
 * format' "--" and having a complete URL [URL] instead of picture data.
 * The use of linked files should however be used restrictively since
 * there is the risk of separation of files.
 *
 *
 * Attached picture   "PIC"
 * Frame size         $xx xx xx
 * Text encoding      $xx
 * Image format       $xx xx xx
 * Picture type       $xx
 * Description        textstring $00 (00)
 * Picture data       binary data>
 *
 *
 *
 *
 * Picture type:  $00  Other
 * $01  32x32 pixels 'file icon' (PNG only)
 * $02  Other file icon
 * $03  Cover (front)
 * $04  Cover (back)
 * $05  Leaflet page
 * $06  Media (e.g. lable side of CD)
 * $07  Lead artist/lead performer/soloist
 * $08  Artist/performer
 * $09  Conductor
 * $0A  Band/Orchestra
 * $0B  Composer
 * $0C  Lyricist/text writer
 * $0D  Recording Location
 * $0E  During recording
 * $0F  During performance
 * $10  Movie/video screen capture
 * $11  A bright coloured fish
 * $12  Illustration
 * $13  Band/artist logotype
 * $14  Publisher/Studio logotype
 */
class FrameBodyPIC: AbstractID3v2FrameBody, ID3v22FrameBody {
    /**
     * Creates a new FrameBodyPIC datatype.
     */
    constructor() {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1)
    }

    constructor(body: FrameBodyPIC) : super(body)

    /**
     * Creates a new FrameBodyPIC datatype.
     *
     * @param textEncoding
     * @param imageFormat
     * @param pictureType
     * @param description
     * @param data
     */
    constructor(
        textEncoding: Byte,
        imageFormat: String?,
        pictureType: Byte,
        description: String?,
        data: ByteArray?
    ) {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding)
        this.setObjectValue(DataTypes.OBJ_IMAGE_FORMAT, imageFormat)
        this.setPictureType(pictureType)
        setDescription(description)
        setImageData(data)
    }

    /**
     * Conversion from v2 PIC to v3/v4 APIC
     *
     * @param body
     */
    constructor(body: FrameBodyAPIC) {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, body.getTextEncoding())
        this.setObjectValue(
            DataTypes.OBJ_IMAGE_FORMAT,
            ImageFormats.mimeType(
                body.getObjectValue(DataTypes.OBJ_MIME_TYPE) as String?
            )
        )
        this.setObjectValue(
            DataTypes.OBJ_PICTURE_DATA,
            body.getObjectValue(DataTypes.OBJ_PICTURE_DATA)
        )
        setDescription(body.getDescription())
        setImageData(body.getImageData())
    }

    constructor(
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(byteBuffer, frameSize)


    /**
     * Get a description of the image
     *
     * @return a description of the image
     */
    fun getDescription(): String? {
        return getObjectValue(DataTypes.OBJ_DESCRIPTION) as String?
    }

    /**
     * Set a description of the image
     *
     * @param description of the image
     */
    fun setDescription(description: String?) {
        setObjectValue(DataTypes.OBJ_DESCRIPTION, description)
    }

    /**
     * Get Image data
     *
     * @return
     */
    fun getImageData(): ByteArray? {
        return getObjectValue(DataTypes.OBJ_PICTURE_DATA) as ByteArray?
    }

    /**
     * Set imageData
     *
     * @param imageData
     */
    fun setImageData(imageData: ByteArray?) {
        setObjectValue(DataTypes.OBJ_PICTURE_DATA, imageData)
    }

    /**
     * @return picturetype
     */
    fun getPictureType(): Int {
        return (getObjectValue(DataTypes.OBJ_PICTURE_TYPE) as Long).toInt()
    }

    /**
     * Set Picture Type
     *
     * @param pictureType
     */
    fun setPictureType(pictureType: Byte) {
        setObjectValue(DataTypes.OBJ_PICTURE_TYPE, pictureType)
    }

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v22Frames.ATTACHED_PICTURE.id
    }

    /**
     * If the description cannot be encoded using current encoder, change the encoder
     */
    override fun write(tagBuffer: ByteArrayOutputStream) {
        if (!(getObject(DataTypes.OBJ_DESCRIPTION) as AbstractString).canBeEncoded()
        ) {
            this.setTextEncoding(TextEncoding.UTF_16.id)
        }
        super.write(tagBuffer)
    }

    /**
     * Get mimetype
     *
     * @return a description of the image
     */
    fun getMimeType(): String? {
        return getObjectValue(DataTypes.OBJ_MIME_TYPE) as String?
    }

    /**
     * @return the image url if there is otherwise return an empty String
     */
    fun getImageUrl(): String {
        if (isImageUrl()) {
            return String(
                    (getObjectValue(DataTypes.OBJ_PICTURE_DATA) as? ByteArray)?:byteArrayOf(),
                    StandardCharsets.ISO_8859_1
            )
        } else {
            return "";
        }
    }

    fun isImageUrl(): Boolean {
        return this.getFormatType() != null && this.getFormatType() == IMAGE_IS_URL
    }

    /**
     * Get a description of the image
     *
     * @return a description of the image
     */
    fun getFormatType(): String? {
        return getObjectValue(DataTypes.OBJ_IMAGE_FORMAT) as String?
    }

    /**
     *
     */
    override fun setupObjectList() {
        objectList.add(
            NumberHashMap(
                DataTypes.OBJ_TEXT_ENCODING,
                this,
                TextEncoding.TEXT_ENCODING_FIELD_SIZE
            )
        )
        objectList.add(StringFixedLength(DataTypes.OBJ_IMAGE_FORMAT, this, 3))
        objectList.add(
            NumberHashMap(
                DataTypes.OBJ_PICTURE_TYPE,
                this,
                PictureTypes.PICTURE_TYPE_FIELD_SIZE
            )
        )
        objectList.add(StringNullTerminated(DataTypes.OBJ_DESCRIPTION, this))
        objectList.add(
            ByteArraySizeTerminated(DataTypes.OBJ_PICTURE_DATA, this)
        )
    }

    companion object {
        const val IMAGE_IS_URL: String = "-->"
    }
}