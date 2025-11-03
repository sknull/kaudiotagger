package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.common.types.GenericFieldKey
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future

class ConcurrentWritesTest: AbstractTestCase() {

    companion object {
        const val THREADS = 100
    }
    
    private val files = Array<File?>(THREADS) { null }

    @BeforeEach
    override fun setUp() {
        super.setUp()
        (0..< THREADS).forEach { counter ->
            files[counter] = copyAudioToTmp(
                "testV25.mp3",
                ConcurrentWritesTest::class.java.getSimpleName() + "-" + counter + ".mp3"
            )
        }
    }

    @AfterEach
    fun tearDown() {
        for (file in files) file!!.delete()
    }

    @Test
    fun testConcurrentWrites() {
        val executor = Executors.newCachedThreadPool()
        val results = ArrayList<Future<Boolean>>(files.size)
        files.forEach { file ->
            results.add(executor.submit(WriteFileCallable(file!!)))
        }

        results.forEach { result ->
            assertTrue(result.get())
        }
    }

    private class WriteFileCallable(val file: File) : Callable<Boolean> {

        override fun call(): Boolean {
            var audiofile = MP3File.read(file)
            audiofile
                .getTagAndConvertOrCreateAndSetDefault()
                .setField(GenericFieldKey.CUSTOM1, file.getName())
            audiofile.commit()
            audiofile = MP3File.read(file)
            assertEquals(
                file.getName(),
                audiofile.getTag()?.getFirst(GenericFieldKey.CUSTOM1)
            )
            return true
        }
    }
}
