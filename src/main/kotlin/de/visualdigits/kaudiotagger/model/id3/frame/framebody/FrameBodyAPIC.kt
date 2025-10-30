package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.datatype.AbstractString
import de.visualdigits.kaudiotagger.model.id3.datatype.ByteArraySizeTerminated
import de.visualdigits.kaudiotagger.model.id3.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.id3.datatype.NumberHashMap
import de.visualdigits.kaudiotagger.model.id3.datatype.StringNullTerminated
import de.visualdigits.kaudiotagger.model.id3.datatype.TextEncodedStringNullTerminated
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import de.visualdigits.kaudiotagger.model.id3.types.ImageFormats
import de.visualdigits.kaudiotagger.model.id3.types.PictureTypes
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.util.TagOptionSingleton
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets


/**
 * Attached picture frame.
 *
 *
 *
 *
 * This frame contains a picture directly related to the audio file.
 * Image format is the MIME type and subtype for the image. In
 * the event that the MIME media type name is omitted, "image/" will be
 * implied. The "image/png" or "image/jpeg" picture format
 * should be used when interoperability is wanted. Description is a
 * short description of the picture, represented as a terminated
 * textstring. The description has a maximum length of 64 characters,
 * but may be empty. There may be several pictures attached to one file,
 * each in their individual "APIC" frame, but only one with the same
 * content descriptor. There may only be one picture with the picture
 * type declared as picture type $01 and $02 respectively. There is the
 * possibility to put only a link to the image file by using the 'MIME
 * type' "-->" and having a complete URL instead of picture data.
 * The use of linked files should however be used sparingly since there
 * is the risk of separation of files.
 *
 * <table border=0 width="70%">
 * <tr><td colspan=2> &lt;Header for 'Attached picture', ID: "APIC"&gt;</td></tr>
 * <tr><td>Text encoding  </td><td>$xx                            </td></tr>
 * <tr><td>MIME type      </td><td>&lt;text string&gt; $00        </td></tr>
 * <tr><td>Picture type   </td><td>$xx                            </td></tr>
 * <tr><td>Description    </td><td>&lt;text string according to encoding&gt; $00 (00)</td></tr>
 * <tr><td>Picture data   </td><td>&lt;binary data&gt;            </td></tr>
</table> *
 *
 * <table border=0 width="70%">
 * <tr><td rowspan=21 valign=top>Picture type:</td>
 * <td>$00 </td><td>Other                                </td></tr>
 * <tr><td>$01 </td><td>32x32 pixels 'file icon' (PNG only)  </td></tr>
 * <tr><td>$02 </td><td>Other file icon                      </td></tr>
 * <tr><td>$03 </td><td>Cover (front)                        </td></tr>
 * <tr><td>$04 </td><td>Cover (back)                         </td></tr>
 * <tr><td>$05 </td><td>Leaflet page                         </td></tr>
 * <tr><td>$06 </td><td>Media (e.g. lable side of CD)        </td></tr>
 * <tr><td>$07 </td><td>Lead artist/lead performer/soloist   </td></tr>
 * <tr><td>$08 </td><td>Artist/performer                     </td></tr>
 * <tr><td>$09 </td><td>Conductor                            </td></tr>
 * <tr><td>$0A </td><td>Band/Orchestra                       </td></tr>
 * <tr><td>$0B </td><td>Composer                             </td></tr>
 * <tr><td>$0C </td><td>Lyricist/text writer                 </td></tr>
 * <tr><td>$0D </td><td>Recording Location                   </td></tr>
 * <tr><td>$0E </td><td>During recording                     </td></tr>
 * <tr><td>$0F </td><td>During performance                   </td></tr>
 * <tr><td>$10 </td><td>Movie/video screen capture           </td></tr>
 * <tr><td>$11 </td><td>A bright coloured fish               </td></tr>
 * <tr><td>$12 </td><td>Illustration                         </td></tr>
 * <tr><td>$13 </td><td>Band/artist logotype                 </td></tr>
 * <tr><td>$14 </td><td>Publisher/Studio logotype            </td></tr>
</table> *
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
class FrameBodyAPIC: AbstractID3v2FrameBody, ID3v24FrameBody, ID3v23FrameBody {

    /**
     * Creates a new FrameBodyAPIC datatype.
     */
    constructor() {
        //Initilise default text encoding
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1.id)
    }

    constructor(body: FrameBodyAPIC) : super(body)

    /**
     * Conversion from v2 PIC to v3/v4 APIC
     *
     * @param body
     */
    constructor(body: FrameBodyPIC) {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, body.getTextEncoding())
        this.setObjectValue(
            DataTypes.OBJ_MIME_TYPE,
            ImageFormats.mimeType(
                body.getObjectValue(DataTypes.OBJ_IMAGE_FORMAT) as String
            )
        )
        this.setObjectValue(
            DataTypes.OBJ_PICTURE_TYPE,
            body.getObjectValue(DataTypes.OBJ_PICTURE_TYPE)
        )
        this.setObjectValue(DataTypes.OBJ_DESCRIPTION, body.getDescription())
        this.setObjectValue(
            DataTypes.OBJ_PICTURE_DATA,
            body.getObjectValue(DataTypes.OBJ_PICTURE_DATA)
        )
    }

    /**
     * Creates a new FrameBodyAPIC datatype.
     *
     * @param textEncoding
     * @param mimeType
     * @param pictureType
     * @param description
     * @param data
     */
    constructor(
        textEncoding: Byte,
        mimeType: String?,
        pictureType: Byte,
        description: String?,
        data: ByteArray?
    ) {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding)
        this.setMimeType(mimeType)
        this.setPictureType(pictureType)
        this.setDescription(description)
        this.setImageData(data)
    }

    constructor(
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(byteBuffer, frameSize)

    override fun getUserFriendlyValue(): String {
        if (this.getImageData() != null) {
            return (this.getMimeType() + ":" + this.getDescription() + ":" + this.getImageData()?.size
                    )
        } else {
            return this.getMimeType() + ":" + this.getDescription() + ":0"
        }
    }

    /**
     * Get Image data
     *
     * @return
     */
    fun getImageData(): ByteArray? {
        return getObjectValue(DataTypes.OBJ_PICTURE_DATA) as? ByteArray
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
     * Get a description of the image
     *
     * @return a description of the image
     */
    fun getDescription(): String? {
        return getObjectValue(DataTypes.OBJ_DESCRIPTION) as? String
    }

    /**
     * Set a description of the image
     *
     * @param description
     */
    fun setDescription(description: String?) {
        setObjectValue(DataTypes.OBJ_DESCRIPTION, description)
    }

    /**
     * Get mimetype
     *
     * @return a description of the image
     */
    fun getMimeType(): String? {
        return getObjectValue(DataTypes.OBJ_MIME_TYPE) as? String
    }

    /**
     * Set mimeType
     *
     * @param mimeType
     */
    fun setMimeType(mimeType: String?) {
        setObjectValue(DataTypes.OBJ_MIME_TYPE, mimeType)
    }

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v24FrameId.ATTACHED_PICTURE.id
    }

    /**
     * Get a description of the image
     *
     * @return a description of the image
     */
    fun getFormatType(): String? {
        return getObjectValue(DataTypes.OBJ_IMAGE_FORMAT) as? String
    }

    /**
     * If the description cannot be encoded using current encoder, change the encoder
     */
    override fun write(tagBuffer: ByteArrayOutputStream) {
        if (TagOptionSingleton.isAPICDescriptionITunesCompatible) {
            this.setTextEncoding(TextEncoding.ISO_8859_1.id)
            if (!(getObject(DataTypes.OBJ_DESCRIPTION) as AbstractString).canBeEncoded()
            ) {
                this.setDescription(null)
            }
        } else {
            if (!(getObject(DataTypes.OBJ_DESCRIPTION) as AbstractString).canBeEncoded()
            ) {
                this.setTextEncoding(TextEncoding.UTF_16.id)
            }
        }
        super.write(tagBuffer)
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
        objectList.add(StringNullTerminated(DataTypes.OBJ_MIME_TYPE, this))
        objectList.add(
            NumberHashMap(
                DataTypes.OBJ_PICTURE_TYPE,
                this,
                PictureTypes.PICTURE_TYPE_FIELD_SIZE
            )
        )
        objectList.add(
            TextEncodedStringNullTerminated(DataTypes.OBJ_DESCRIPTION, this)
        )
        objectList.add(
            ByteArraySizeTerminated(DataTypes.OBJ_PICTURE_DATA, this)
        )
    }

    /**
     * @return the image url if there is otherwise return an empty String
     */
    fun getImageUrl(): String {
        if (isImageUrl()) {
            return String(
                    (getObjectValue(DataTypes.OBJ_PICTURE_DATA) as ByteArray),
                    StandardCharsets.ISO_8859_1
            )
        } else {
            return ""
        }
    }

    /**
     * @return true if imagedata  is held as a url rather than actually being imagedata
     */
    fun isImageUrl(): Boolean {
        return this.getMimeType() != null && this.getMimeType() == IMAGE_IS_URL
    }

    companion object {
        const val IMAGE_IS_URL: String = "-->"
    }
}
