package de.visualdigits.kaudiotagger.model.audiofile.mp3

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

class MP3FileTest {

    @Test
    fun testReadFileWithID3v22() {
        val file = File(ClassLoader.getSystemResource("mp3/decisions-by-kevin-macleod.mp3").toURI())

        val expected = """AudioFile decisions-by-kevin-macleod.mp3  --------
fileSize:3544412 encoder: startByte:00000000000008a0 numberOfFrames:3389 numberOfFramesEst:3389 timePerFrame:0.026122448979591838 bitrate:320 trackLength:01:28 mpeg frameheader: frame length:1045 version:MPEG-1 layer:Layer 3 channelMode:Stereo noOfSamples:1152 samplingRate:44100 isPadding:true isProtected:false isPrivate:false isCopyrighted:false isOriginal:false isVariableBitRatefalse header as binary:11111111 11111011 11100010 00000000 mp3XingFrame:false mp3VbriFrame:false
[ID3v22Tag:
Tag content [ID3v22Tag]:
	TP1:TextEncoding="ISO-8859-1"; Text="Kevin MacLeod"
	TCM:TextEncoding="ISO-8859-1"; Text="Kevin MacLeod"
	TAL:TextEncoding="ISO-8859-1"; Text="Royalty Free"
	TYE:TextEncoding="ISO-8859-1"; Text="2010"
	TBP:TextEncoding="ISO-8859-1"; Text="80"
	COM:TextEncoding="ISO-8859-1"; Language=""; Description="engiTunPGAP"; Text="0"
	COM:TextEncoding="ISO-8859-1"; Language=""; Description="engiTunNORM"; Text=" 00000085 0000006D 00000D00 00000C12 0000390A 0000390A 00005CD8 0000597A 0000390A 0001236D"
	COM:TextEncoding="ISO-8859-1"; Language=""; Description="engiTunSMPB"; Text=" 00000000 00000210 0000076E 00000000003B8D82 00000000 0035EC15 00000000 00000000 00000000 00000000 00000000 00000000"
	TEN:TextEncoding="ISO-8859-1"; Text="iTunes 9.2.1"
	TT2:TextEncoding="ISO-8859-1"; Text="Decisions"
	TCO:TextEncoding="ISO-8859-1"; Text="TV & Film"

-------------------]
==================="""

        val mP3File = MP3File.read(file)
        assertEquals(expected, mP3File.toString())
    }
}