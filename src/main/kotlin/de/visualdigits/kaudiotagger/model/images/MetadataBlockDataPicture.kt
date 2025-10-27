package de.visualdigits.kaudiotagger.model.images

import de.visualdigits.kaudiotagger.model.common.types.GenericFieldKey
import de.visualdigits.kaudiotagger.model.common.types.PictureTypes
import de.visualdigits.kaudiotagger.model.common.exceptions.InvalidFrameException
import de.visualdigits.kaudiotagger.model.common.field.TagField
import de.visualdigits.kaudiotagger.util.Utils
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.charset.StandardCharsets


/**
 * Picture Block
 *
 *
 *
 *
 * pThis block is for storing pictures associated with the file, most commonly cover art from CDs.
 * There may be more than one PICTURE block in a file. The picture format is similar to the APIC frame in ID3v2.
 * The PICTURE block has a type, MIME type, and UTF-8 description like ID3v2, and supports external linking via URL
 * (though this is discouraged). The differences are that there is no uniqueness constraint on the description field,
 * and the MIME type is mandatory. The FLAC PICTURE block also includes the resolution, color depth, and palette size
 * so that the client can search for a suitable picture without having to scan them all
 *
 *
 * Format:
 * Size in bits Info
 * 32 The picture type according to the ID3v2 APIC frame: (There may only be one each of picture type 1 and 2 in a file)
 * 32 	The length of the MIME type string in bytes.
 * n*8 	The MIME type string, in printable ASCII characters 0x20-0x7e. The MIME type may also be -- to signify that the data part is a URL of the picture instead of the picture data itself.
 * 32 	The length of the description string in bytes.
 * n*8 	The description of the picture, in UTF-8.
 * 32 	The width of the picture in pixels.
 * 32 	The height of the picture in pixels.
 * 32 	The color depth of the picture in bits-per-pixel.
 * 32 	For indexed-color pictures (e.g. GIF), the number of colors used, or 0 for non-indexed pictures.
 * 32 	The length of the picture data in bytes.
 * n*8 	The binary picture data.
 */
class MetadataBlockDataPicture : MetadataBlockData, TagField {
    
    val log = LoggerFactory.getLogger(javaClass)

    var pictureType: Int = 0
    var mimeType: String? = ""
    var description: String? = null
    var width: Int = 0
    var height: Int = 0
    var colourDepth: Int = 0
    var indexedColourCount: Int = 0
    var lengthOfPictureInBytes = 0
    var imageData: ByteArray? = null

    /**
     * Initialize MetaBlockDataPicture from byteBuffer
     *
     * @param rawdata
     * @throws IOException
     * @throws InvalidFrameException
     */
    constructor(rawdata: ByteBuffer) {
        initFromByteBuffer(rawdata)
    }

    private fun initFromByteBuffer(rawdata: ByteBuffer) {
        //Picture Type
        pictureType = rawdata.getInt()
        if (pictureType >= PictureTypes.getSize()) {
            throw InvalidFrameException(
                "PictureType was:" +
                        pictureType +
                        "but the maximum allowed is " +
                        (PictureTypes.getSize() - 1)
            )
        }

        //MimeType
        val mimeTypeSize = rawdata.getInt()
        mimeType = getString(
            rawdata,
            mimeTypeSize,
            StandardCharsets.ISO_8859_1.name()
        )

        //Description
        val descriptionSize = rawdata.getInt()
        description = getString(
            rawdata,
            descriptionSize,
            StandardCharsets.UTF_8.name()
        )

        //Image width
        width = rawdata.getInt()

        //Image height
        height = rawdata.getInt()

        //Colour Depth
        colourDepth = rawdata.getInt()

        //Indexed Colour Count
        this.indexedColourCount = rawdata.getInt()

        lengthOfPictureInBytes = rawdata.getInt()
        //ImageData
        imageData = ByteArray(lengthOfPictureInBytes)
        rawdata.get(imageData)

        log.debug("Read image:" + this)
    }

    private fun getString(rawdata: ByteBuffer, length: Int, charset: String): String {
        val tempbuffer = ByteArray(length)
        rawdata.get(tempbuffer)
        return String(tempbuffer, charset(charset))
    }

    /**
     * Construct picture block by reading from file, the header informs us how many bytes we should be reading from
     *
     * @param header
     * @param fc
     * @throws IOException
     * @throws InvalidFrameException
     */
    //TODO check for buffer underflows see http://research.eeye.com/html/advisories/published/AD20071115.html
    constructor(header: MetadataBlockHeader, fc: FileChannel) {
        val rawdata = ByteBuffer.allocate(header.dataLength)
        val bytesRead = fc.read(rawdata)
        if (bytesRead < header.dataLength) {
            throw IOException(
                "Unable to read required number of databytes read:" +
                        bytesRead +
                        ":required:" +
                        header.dataLength
            )
        }
        rawdata.rewind()
        initFromByteBuffer(rawdata)
    }

    /**
     * Construct new MetadataPicture block
     *
     * @param imageData
     * @param pictureType
     * @param mimeType
     * @param description
     * @param width
     * @param height
     * @param colourDepth
     * @param indexedColouredCount
     */
    constructor(
        imageData: ByteArray,
        pictureType: Int,
        mimeType: String?,
        description: String,
        width: Int,
        height: Int,
        colourDepth: Int,
        indexedColouredCount: Int
    ) {
        //Picture Type
        this.pictureType = pictureType

        //MimeType
        if (mimeType != null) {
            this.mimeType = mimeType
        }

        //Description
        this.description = description

        this.width = width

        this.height = height

        this.colourDepth = colourDepth

        this.indexedColourCount = indexedColouredCount
        //ImageData
        this.imageData = imageData
    }

    override fun length(): Int {
        return getBytes().limit()
    }

    override fun getBytes(): ByteBuffer {
        try {
            val baos = ByteArrayOutputStream()
            baos.write(Utils.getSizeBEInt32(pictureType))
            baos.write(Utils.getSizeBEInt32(mimeType?.length?:0))
            mimeType?.toByteArray(StandardCharsets.ISO_8859_1)?.also { ba -> baos.write(ba) }
            baos.write(Utils.getSizeBEInt32(description?.length?:0))
            description?.toByteArray(StandardCharsets.UTF_8)?.also { ba -> baos.write(ba) }
            baos.write(Utils.getSizeBEInt32(width))
            baos.write(Utils.getSizeBEInt32(height))
            baos.write(Utils.getSizeBEInt32(colourDepth))
            baos.write(Utils.getSizeBEInt32(this.indexedColourCount))
            baos.write(Utils.getSizeBEInt32(imageData?.size?:0))
            baos.write(imageData)
            return ByteBuffer.wrap(baos.toByteArray())
        } catch (ioe: IOException) {
            throw RuntimeException(ioe.message)
        }
    }

    /**
     * @return the image url if there is otherwise return an empty String
     */
    fun getImageUrl(): String {
        if (isImageUrl()) {
            return imageData?.let { id -> String(id, StandardCharsets.ISO_8859_1) }?:""
        } else {
            return ""
        }
    }

    /**
     * @return true if imagedata  is held as a url rather than actually being imagedata
     */
    fun isImageUrl(): Boolean {
        return mimeType == IMAGE_IS_URL
    }

    override fun toString(): String {
        return ("${PictureTypes.fromId(pictureType)?.friendlyName}:$mimeType:$description:width:$width:height:$height:colourdepth:$colourDepth:indexedColourCount:${this.indexedColourCount}:image size in bytes:$lengthOfPictureInBytes/${imageData?.size}")
    }

    /**
     * This method copies the data of the given field to the current data.<br></br>
     *
     * @param field The field containing the data to be taken.
     */
    override fun copyContent(field: TagField) {
        throw UnsupportedOperationException()
    }

    /**
     * Returns the Id of the represented tag field.<br></br>
     * This value should uniquely identify a kind of tag data, like title.
     * [org.jaudiotagger.audio.generic.AbstractTag] will use the &quot;id&quot; to summarize multiple
     * fields.
     *
     * @return Unique identifier for the fields type. (title, artist...)
     */
    override fun getIdentifier(): String {
        return GenericFieldKey.COVER_ART.name
    }

    /**
     * This method delivers the binary representation of the fields data in
     * order to be directly written to the file.<br></br>
     *
     * @return Binary data representing the current tag field.<br></br>
     * @throws UnsupportedEncodingException Most tag data represents text. In some cases the underlying
     * implementation will need to convert the text data in java to
     * a specific charset encoding. In these cases an
     * [UnsupportedEncodingException] may occur.
     */
    override fun getRawContent(): ByteArray? {
        return getBytes().array()
    }

    /**
     * Determines whether the represented field contains (is made up of) binary
     * data, instead of text data.<br></br>
     * Software can identify fields to be displayed because they are human
     * readable if this method returns `false`.
     *
     * @return `true` if field represents binary data (not human
     * readable).
     */
    override fun isBinary(): Boolean {
        return true
    }

    /**
     * This method will set the field to represent binary data.<br></br>
     *
     *
     * Some implementations may support conversions.<br></br>
     * As of now (Octobre 2005) there is no implementation really using this
     * method to perform useful operations.
     *
     * @param b `true`, if the field contains binary data.
     */
    @Deprecated(
        """As for now is of no use. Implementations should use another
      way of setting this property."""
    )
    override fun isBinary(b: Boolean) {
        //Do nothing, always true
    }

    /**
     * Identifies a field to be of common use.<br></br>
     *
     *
     * Some software may differ between common and not common fields. A common
     * one is for sure the title field. A web link may not be of common use for
     * tagging. However some file formats, or future development of users
     * expectations will make more fields common than now can be known.
     *
     * @return `true` if the field is of common use.
     */
    override fun isCommon(): Boolean {
        return true
    }

    /**
     * Determines whether the content of the field is empty.<br></br>
     *
     * @return `true` if no data is stored (or empty String).
     */
    override fun isEmpty(): Boolean {
        return false
    }

    companion object {
        const val IMAGE_IS_URL: String = "-->"
    }
}
