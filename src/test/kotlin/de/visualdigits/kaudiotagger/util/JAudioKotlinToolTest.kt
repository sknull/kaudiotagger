package de.visualdigits.kaudiotagger.util

import de.visualdigits.kaudiotagger.model.audiofile.AudioFile
import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.id3.types.ID3v22Frames
import de.visualdigits.kaudiotagger.model.id3.types.ID3v23Frames
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24Frames
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File

@Disabled("only for local testing")
class JAudioKotlinToolTest {

    @Test
    fun testMetadata() {
        val metaData = scanDirectory(File("m:"))

//        val file = File("E:/temp/01_Green Desert.mp3")
//        val audioFile = MP3File(file)
//        audioFile.getTag()?.also { tag ->
//            tag.getArtworkList().firstOrNull()?.also { artwork ->
//                val album = tag.getFirst(GenericFieldKey.ALBUM)
//                println(album)
//                ImageIO.write(artwork.image, artwork.extension, File("e:/temp/$album.${artwork.extension}"))
//            }
//        }
//        println(audioFile)
    }

    fun scanDirectory(directory: File, metaData: MutableMap<File, MP3File> = LinkedHashMap(), indent: String = ""): Map<File, AudioFile> {
        println("## $indent${directory.canonicalPath}")
        metaData.putAll(directory.listFiles { f -> f.isFile && f.name.endsWith(".mp3", ignoreCase = true) }
            ?.associate { f ->
//                println("## $indent - ${f.name}")
                Pair(f, MP3File(f))
            }
            ?:mapOf()
        )
        directory.listFiles { f -> f.isDirectory }
            ?.forEach { d -> scanDirectory(d, metaData, "$indent  ") }

        return metaData
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