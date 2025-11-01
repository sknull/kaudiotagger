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
    fun testRead() {
        val file = File("M:/_Free Music/freemusicarchive.org/BODYSURFER/2018_Digital Prints/02_Wants To Know.mp3")
        println(MP3File.read(file))
    }

    @Test
    fun testMetadata() {
        val file = File("E:/temp/decisions-by-kevin-macleod.mp3")
        val audioFile = MP3File.read(file)

        val expected = """AudioFile decisions-by-kevin-macleod.mp3  --------
fileSize:3544412 encoder: startByte:00000000000008a0 numberOfFrames:3389 numberOfFramesEst:3389 timePerFrame:0.026122448979591838 bitrate:320 trackLength:01:28 mpeg frameheader: frame length:1045 version:MPEG-1 layer:Layer 3 channelMode:Stereo noOfSamples:1152 samplingRate:44100 isPadding:true isProtected:false isPrivate:false isCopyrighted:false isOriginal:false isVariableBitRatefalse header as binary:11111111 11111011 11100010 00000000 mp3XingFrame:false mp3VbriFrame:false
[ID3v22Tag:
	TP1:TextEncoding="ISO-8859-1"; Text="Kevin MacLeod"
	TCM:TextEncoding="ISO-8859-1"; Text="Kevin MacLeod"
	TAL:TextEncoding="ISO-8859-1"; Text="Royalty Free"
	TYE:TextEncoding="ISO-8859-1"; Text="2010"
	TBP:TextEncoding="ISO-8859-1"; Text="80"
	COM:TextEncoding="ISO-8859-1"; Language="English"; Description="iTunPGAP"; Text="0"
	COM:TextEncoding="ISO-8859-1"; Language="English"; Description="iTunNORM"; Text=" 00000085 0000006D 00000D00 00000C12 0000390A 0000390A 00005CD8 0000597A 0000390A 0001236D"
	COM:TextEncoding="ISO-8859-1"; Language="English"; Description="iTunSMPB"; Text=" 00000000 00000210 0000076E 00000000003B8D82 00000000 0035EC15 00000000 00000000 00000000 00000000 00000000 00000000"
	TEN:TextEncoding="ISO-8859-1"; Text="iTunes 9.2.1"
	TT2:TextEncoding="ISO-8859-1"; Text="Decisions"
	TCO:TextEncoding="ISO-8859-1"; Text="TV & Film"
-------------------]
=============""".trimIndent()

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
        val v22Entries = ID3v22FrameId.entries.map { e -> e.name}
        val v23Entries = ID3v23FrameId.entries.map { e -> e.name}
        val v24Entries = ID3v24FrameId.entries.map { e -> e.name}

        val common = v22Entries.intersect(v23Entries).intersect(v24Entries)
        common
        println(common.joinToString("\n"))
    }

}