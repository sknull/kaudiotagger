package de.visualdigits.kaudiotagger.model.tag.images

import de.visualdigits.kaudiotagger.model.datatype.types.ImageFormats
import de.visualdigits.kaudiotagger.model.datatype.types.PictureTypes
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import javax.imageio.ImageIO


/**
 * Represents artwork in a format independent way
 */
class Artwork(
    var binaryData: ByteArray? = null,
    var mimeType: String? = null,
    var description: String? = null,
    var isLinked: Boolean = false,
    var imageUrl: String? = null,
    var pictureType: Int = -1,
    var width: Int = 0,
    var height: Int = 0
) {

    companion object {

        /**
         * Create Artwork from File
         *
         * @param file
         * @return
         * @throws IOException
         */
        fun createArtworkFromFile(file: File): Artwork {
            val artwork = Artwork()
            artwork.setFromFile(file)
            return artwork
        }

        fun createLinkedArtworkFromURL(url: String): Artwork {
            val artwork = Artwork()
            artwork.setLinkedFromURL(url)
            return artwork
        }

        /**
         * Create artwork from Flac block
         *
         * @param coverArt
         * @return
         */
        fun createArtworkFromMetadataBlockDataPicture(
            coverArt: MetadataBlockDataPicture
        ): Artwork {
            val artwork = Artwork()
            artwork.setFromMetadataBlockDataPicture(coverArt)
            return artwork
        }
    }

    /**
     * Create Artwork from File
     *
     * @param file
     * @throws IOException
     */
    fun setFromFile(file: File) {
        val imageFile = RandomAccessFile(file, "r")
        val imagedata = ByteArray(imageFile.length().toInt())
        imageFile.read(imagedata)
        imageFile.close()

        binaryData = imagedata
        mimeType = ImageFormats.mimeTypeFromBinarySignature(imagedata)
        description = ""
        pictureType = PictureTypes.DEFAULT_ID
    }

    /**
     * Create Linked Artwork from URL
     *
     * @param url
     */
    fun setLinkedFromURL(url: String) {
        isLinked = true
        imageUrl = url
    }

    /**
     * Populate Artwork from MetadataBlockDataPicture as used by Flac and VorbisComment
     *
     * @param coverArt
     */
    fun setFromMetadataBlockDataPicture(
        coverArt: MetadataBlockDataPicture
    ) {
        mimeType = coverArt.getMimeType()
        description = coverArt.getDescription()
        pictureType = coverArt.pictureType
        if (coverArt.isImageUrl()) {
            isLinked = coverArt.isImageUrl()
            imageUrl = coverArt.getImageUrl()
        } else {
            binaryData = coverArt.imageData
        }
        width = coverArt.width
        height = coverArt.height
    }

    /**
     * Should be called when you wish to prime the artwork for saving
     *
     * @return
     */
    fun setImageFromData(): Boolean {
        try {
            val image: BufferedImage = getImage()
            width = image.getWidth()
            height = image.getHeight()
        } catch (ioe: IOException) {
            return false
        }
        return true
    }

    fun getImage(): BufferedImage {
        val iis = ImageIO.createImageInputStream(
            ByteArrayInputStream(binaryData)
        )
        return ImageIO.read(iis)
    }
}
