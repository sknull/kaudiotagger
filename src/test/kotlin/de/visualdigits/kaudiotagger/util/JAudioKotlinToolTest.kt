package de.visualdigits.kaudiotagger.util

import de.visualdigits.kaudiotagger.model.audiofile.AudioFile
import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.id3.types.ID3v22FrameId
import de.visualdigits.kaudiotagger.model.id3.types.ID3v23FrameId
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File

@Disabled("only for local testing")
class JAudioKotlinToolTest : AbstractTestCase() {

    @Test
    fun scanDirectory() {
        scanDirectory(File("m:"))
    }

    fun scanDirectory(directory: File, metaData: MutableMap<File, MP3File> = LinkedHashMap(), indent: String = ""): Map<File, AudioFile> {
        println("## $indent${directory.canonicalPath}")
        metaData.putAll(directory.listFiles { f -> f.isFile && f.name.endsWith(".mp3", ignoreCase = true) }
            ?.associate { f ->
//                println("## $indent - ${f.name}")
                val mP3File = MP3File.read(f)
                Pair(f, mP3File)
            }
            ?:mapOf()
        )
        directory.listFiles { f -> f.isDirectory }
            ?.forEach { d -> scanDirectory(d, metaData, "$indent  ") }

        return metaData
    }

    @Test
    fun determineCommonFields() {
        val v22Entries = ID3v22FrameId.entries.map { e -> e.name}
        val v23Entries = ID3v23FrameId.entries.map { e -> e.name}
        val v24Entries = ID3v24FrameId.entries.map { e -> e.name}

        val common = v22Entries.intersect(v23Entries).intersect(v24Entries)
        common
        println(common.joinToString("\n"))
    }

}