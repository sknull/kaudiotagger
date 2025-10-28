package de.visualdigits.kaudiotagger.util

import de.visualdigits.kaudiotagger.model.audiofile.AudioFile
import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.id3.types.ID3V22Frame
import de.visualdigits.kaudiotagger.model.id3.types.ID3V23Frame
import de.visualdigits.kaudiotagger.model.id3.types.ID3V24Frame
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File

@Disabled("only for local testing")
class JAudioKotlinToolTest {

    @Test
    fun testRead() {
        val file = File("M:/_Free Music/freemusicarchive.org/BODYSURFER/2018_Digital Prints/02_Wants To Know.mp3")
        println(MP3File.read(file))
    }

    @Test
    fun testMetadata() {
        val file = File("E:/temp/decisions-by-kevin-macleod.mp3")
        val audioFile = MP3File.read(file)

        val expected = """AudioFile E:\temp\01_Green Desert.mp3  --------
fileSize:46813950 encoder:LAME3.99r startByte:000000000000b386 numberOfFrames:44757 numberOfFramesEst:44796 timePerFrame:0.026122448979591838 bitrate:320 trackLength:19:29 mpeg frameheader: frame length:1044 version:MPEG-1 layer:Layer 3 channelMode:Joint Stereo noOfSamples:1152 samplingRate:44100 isPadding:false isProtected:false isPrivate:false isCopyrighted:false isOriginal:false isVariableBitRatefalse header as binary:11111111 11111011 11100000 01100100xingheader vbr:false frameCountEnabled:true frameCount:44757 audioSizeEnabled:true audioFileSize:46767542 mp3VbriFrame:false
Tag content:
	TALB:TextEncoding="UTF-16"; Text="Green Desert"
	TPE1:TextEncoding="UTF-16"; Text="Tangerine Dream"
	TPE2:TextEncoding="UTF-16"; Text="Tangerine Dream"
	TCON:TextEncoding="UTF-16"; Text="Electronic"
	TPUB:TextEncoding="UTF-16"; Text="Esoteric Recordings"
	TIT2:TextEncoding="UTF-16"; Text="Green Desert"
	TRCK:TextEncoding="ISO-8859-1"; Text="01"
	TYER:TextEncoding="ISO-8859-1"; Text="1973"
	APIC:TextEncoding="ISO-8859-1"; MIMEType="image/jpeg"; PictureType="Cover (front)"; Description=""; PictureData="43590 bytes"
	TBPM:TextEncoding="UTF-16"; Text="129.61"

-------------------""".trimIndent()

        val actual = audioFile.toString()

        println(actual)
        assertEquals(expected, actual)
    }

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
        val v22Entries = ID3V22Frame.entries.map { e -> e.name}
        val v23Entries = ID3V23Frame.entries.map { e -> e.name}
        val v24Entries = ID3V24Frame.entries.map { e -> e.name}

        val common = v22Entries.intersect(v23Entries).intersect(v24Entries)
        common
        println(common.joinToString("\n"))
    }

}