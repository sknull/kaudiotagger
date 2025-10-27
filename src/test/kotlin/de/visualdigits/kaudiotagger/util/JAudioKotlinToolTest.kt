package de.visualdigits.kaudiotagger.util

import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.datatype.types.GenericFieldKey
import de.visualdigits.kaudiotagger.model.datatype.types.ID3v22Frames
import de.visualdigits.kaudiotagger.model.datatype.types.ID3v23Frames
import de.visualdigits.kaudiotagger.model.datatype.types.ID3v24Frames
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File
import javax.imageio.ImageIO

@Disabled("only for local testing")
class JAudioKotlinToolTest {

    @Test
    fun testMetadata() {
        val file = File("E:/temp/01_Green Desert.mp3")
        val audioFile = MP3File(file)
        audioFile.getTag()?.also { tag ->
            tag.getArtworkList().firstOrNull()?.also { artwork ->
                val album = tag.getFirst(GenericFieldKey.ALBUM)
                println(album)
                ImageIO.write(artwork.image, artwork.extension, File("e:/temp/$album.${artwork.extension}"))
            }
        }
        println(audioFile)
    }

    @Test
    fun determineCommonFields() {
        val v22Entries = ID3v22Frames.entries.map { e -> e.name}
        val v23Entries = ID3v23Frames.entries.map { e -> e.name}
        val v24Entries = ID3v24Frames.entries.map { e -> e.name}

        val common = v22Entries.intersect(v23Entries).intersect(v24Entries)
        common
        println(common.joinToString("\n"))
    }

}