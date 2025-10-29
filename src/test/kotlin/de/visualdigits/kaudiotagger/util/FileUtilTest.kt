package de.visualdigits.kaudiotagger.util

import de.visualdigits.kaudiotagger.util.FileUtil.adjustPadding
import de.visualdigits.kaudiotagger.util.FileUtil.replaceFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Files

class FileUtilTest {

    private var tempDirectory = Files.createTempDirectory("kaudiotagger_").toFile()

    @Test
    fun testReplaceFile() {
        val originalFile = File(tempDirectory, "originalFile.txt")
        val newFile = File(tempDirectory, "newFile.txt")

        originalFile.writeText("originalFile")
        newFile.writeText("newFile")

        replaceFile(originalFile, newFile)

        val contents = originalFile.readText()

        assertEquals("newFile", contents)
    }

    @Test
    fun testAdjustPadding() {
        val paddingFile = File(tempDirectory, "paddingFile.txt")
        paddingFile.writeText("0123456789")

        adjustPadding(paddingFile, 20, 3)

        assertEquals(0.toChar().toString().repeat(20) + "3456789", paddingFile.readText())
    }
}