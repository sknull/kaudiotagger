package de.visualdigits.kaudiotagger.model.audiofile.mp3

import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.types.ID3v2Version
import de.visualdigits.kaudiotagger.util.PadNumberOption
import de.visualdigits.kaudiotagger.util.TagOptionSingleton
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files

abstract class AbstractTestCase {

    private var tempDirectory = Files.createTempDirectory("kaudiotagger_").toFile()

    @BeforeEach
    fun setUp() {
        setOptionsToStandard()
    }

    fun executeAlsoWithMissingResources(): Boolean {
        return false
    }

    /**
     * Copy audiofile to processing dir ready for use in test
     *
     * @param fileName
     * @return
     */
    fun copyAudioToTmp(fileName: String, newFileName: String? = null): File {
        val inputFile = fileResource("testdata", fileName)
        return if (inputFile.exists()) {
            val outputFile = tempFileResource(newFileName?:fileName)
            if (!outputFile.getParentFile().exists()) {
                outputFile.getParentFile().mkdirs()
            }
            copy(inputFile, outputFile)
            outputFile
        } else {
            error("Source file does not exist")
        }
    }

    /**
     * Copy a File
     *
     * @param fromFile The existing File
     * @param toFile   The new File
     * @return `true` if and only if the renaming succeeded;
     * `false` otherwise
     */
    fun copy(fromFile: File, toFile: File) {
        fromFile.copyTo(toFile, true)
    }

    /**
     * Prepends file with tag file in order to create an mp3 with a valid id3
     *
     * @param tagfile
     * @param fileName
     * @return
     */
    fun prependAudioToTmp(tagfile: String?, fileName: String?): File {
        val inputTagFile: File = fileResource("testtagdata", tagfile)
        val inputFile: File = fileResource("testdata", fileName)
        val outputFile: File = tempFileResource(fileName)
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs()
        }
        val result: Boolean = append(inputTagFile, inputFile, outputFile)
        Assertions.assertTrue(result)
        return outputFile
    }

    fun fileResource(directory: String, fileName: String?): File {
        val dir = if (!directory.endsWith("/")) {
            "$directory/"
        } else {
            directory
        }
        val resource = if (fileName == null) {
            ClassLoader.getSystemResource(dir)?.toURI()
        } else {
            ClassLoader.getSystemResource(dir + fileName)?.toURI()
        }
        
        return  resource?.let { r -> File(r) }?:error("File not found: $directory/$fileName")
    }

    fun tempFileResource(fileName: String?): File {
        val file: File
        if (fileName == null) {
            file = tempDirectory
        } else {
            file = File(tempDirectory, fileName)
        }
        
        return file
    }

    private fun append(fromFile1: File, fromFile2: File, toFile: File): Boolean {
        var theByte: Int
        FileInputStream(fromFile1).use { fins ->
            FileInputStream(fromFile2).use { fins2 ->
                FileOutputStream(toFile).use { fouts ->
                    BufferedInputStream(fins).use { inBuffer ->
                        BufferedInputStream(fins2).use { inBuffer2 ->
                            BufferedOutputStream(fouts).use { outBuffer ->
                                while ((inBuffer.read().also { theByte = it }) > -1) {
                                    outBuffer.write(theByte)
                                }
                                while ((inBuffer2.read().also { theByte = it }) > -1) {
                                    outBuffer.write(theByte)
                                }
                            }
                        }
                    }
                }
            }
        }

        // cleanupif files are not the same length
        if ((fromFile1.length() + fromFile2.length()) != toFile.length()) {
            toFile.delete()

            return false
        }

        return true
    }

    private fun setOptionsToStandard() {
        TagOptionSingleton.filenameTagSave = false
        TagOptionSingleton.id3v1Save = true
        TagOptionSingleton.id3v1SaveAlbum = true
        TagOptionSingleton.id3v1SaveArtist = true
        TagOptionSingleton.id3v1SaveComment = true
        TagOptionSingleton.id3v1SaveGenre = true
        TagOptionSingleton.id3v1SaveTitle = true
        TagOptionSingleton.id3v1SaveTrack = true
        TagOptionSingleton.id3v1SaveYear = true
        TagOptionSingleton.id3v2PaddingCopyTag = true
        TagOptionSingleton.id3v2PaddingWillShorten = false
        TagOptionSingleton.id3v2Save = true
        TagOptionSingleton.language = "eng"
        TagOptionSingleton.lyrics3KeepEmptyFieldIfRead = false
        TagOptionSingleton.lyrics3Save = true
        TagOptionSingleton.lyrics3SaveEmptyField = false
        TagOptionSingleton.lyrics3SaveFieldMap = mutableMapOf()
        TagOptionSingleton.numberMP3SyncFrame = 3
        TagOptionSingleton.parenthesisMap = mutableMapOf()
        TagOptionSingleton.replaceWordMap = mutableMapOf()
        TagOptionSingleton.timeStampFormat = 2
        TagOptionSingleton.unsyncTags = false
        TagOptionSingleton.removeTrailingTerminatorOnWrite = true
        TagOptionSingleton.id3v23DefaultTextEncoding = TextEncoding.ISO_8859_1
        TagOptionSingleton.id3v24DefaultTextEncoding = TextEncoding.ISO_8859_1
        TagOptionSingleton.id3v24UnicodeTextEncoding = TextEncoding.UTF_16
        TagOptionSingleton.resetTextEncodingForExistingFrames = false
        TagOptionSingleton.truncateTextWithoutErrors = false
        TagOptionSingleton.padNumbers = false
        TagOptionSingleton.isAPICDescriptionITunesCompatible = false
        TagOptionSingleton.isAndroid = false
        TagOptionSingleton.isEncodeUTF16BomAsLittleEndian = true
        TagOptionSingleton.writeChunkSize = 5000000
        TagOptionSingleton.isWriteMp4GenresAsText = false
        TagOptionSingleton.padNumberTotalLength = PadNumberOption.PAD_ONE_ZERO
        TagOptionSingleton.id3v2Version = ID3v2Version.ID3_V23
        TagOptionSingleton.checkIsWritable = false
        TagOptionSingleton.preserveFileIdentity = true
    }
}