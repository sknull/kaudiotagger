package de.visualdigits.kaudiotagger.util

import de.visualdigits.kaudiotagger.model.kframe.ID3v22KFrame
import de.visualdigits.kaudiotagger.model.kframe.ID3v23KFrame
import de.visualdigits.kaudiotagger.model.kframe.ID3v24KFrame
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File

@Disabled("only for local testing")
class JAudioKotlinToolTest {

    @Test
    fun testMetadata() {
        val file = File("M:/Electronic/Tangerine Dream/Alben/1973_Green Desert/01_Green Desert.mp3")
        println(file.getArtworks())
    }

    @Test
    fun determineCommonFields() {
        val v22Entries = ID3v22KFrame.entries.map { e -> e.name}
        val v23Entries = ID3v23KFrame.entries.map { e -> e.name}
        val v24Entries = ID3v24KFrame.entries.map { e -> e.name}

        val common = v22Entries.intersect(v23Entries).intersect(v24Entries)
        common
        println(common.joinToString("\n"))
    }

}