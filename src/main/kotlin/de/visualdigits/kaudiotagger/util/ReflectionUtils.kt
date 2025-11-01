package de.visualdigits.kaudiotagger.util

import de.visualdigits.kaudiotagger.model.common.exceptions.InvalidFrameException
import de.visualdigits.kaudiotagger.model.common.frame.framebody.AbstractTagFrameBody
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.AbstractID3v2FrameBody
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.reflect.InvocationTargetException
import java.nio.ByteBuffer

@Suppress("UNCHECKED_CAST")
object ReflectionUtils {

    val log: Logger = LoggerFactory.getLogger(javaClass)

    fun classForName(identifier: String?): Class<AbstractTagFrameBody>? {
        return try {
            Class.forName("${AbstractID3v2FrameBody.FRAME_BODY_PACKAGE}.FrameBody$identifier") as Class<AbstractTagFrameBody>
        } catch (e: Exception) {
            log.warn("")
            null
        }
    }

    fun callDefaultConstructor(identifier: String?): AbstractTagFrameBody? {
        return try {
            val clazz = classForName(identifier)
            clazz?.getDeclaredConstructor()?.newInstance()
        } catch (cnfe: ClassNotFoundException) {
            log.error(cnfe.message)
            null
        } catch (ie: InstantiationException) { // Instantiate Interface/Abstract should not happen
            log.error("InstantiationException:$identifier", ie)
            throw java.lang.RuntimeException(ie)
        } catch (iae: IllegalAccessException) { // Private Constructor should not happen
            log.error("IllegalAccessException:$identifier", iae)
            throw java.lang.RuntimeException(iae)
        }
    }

    fun callCopyConstructor(
        identifier: String?,
        body: AbstractID3v2FrameBody,
    ): AbstractTagFrameBody? {
        val clazz = classForName(identifier)
        val constructorParameterTypes = arrayOf<Class<*>>(body.javaClass)
        val constructorParameterValues = arrayOf<Any?>(body)
        val construct = clazz?.getConstructor(*constructorParameterTypes)

        return construct?.newInstance(*constructorParameterValues)
    }

    fun callByteBufferConstructor(
        identifier: String?,
        byteBuffer: ByteBuffer,
        frameSize: Int
    ): AbstractTagFrameBody? {
        return try {
            val clazz = classForName(identifier)
            val constructorParameterTypes = arrayOf<Class<*>>(
                Class.forName("java.nio.ByteBuffer"),
                Integer.TYPE,
            )
            val constructorParameterValues = arrayOf<Any>(byteBuffer, frameSize)
            log.debug("constructorParameterTypes '{}': {}", identifier, constructorParameterTypes.toList())
            log.debug("constructorParameterValues '{}': {}", identifier, constructorParameterValues.toList())
            val construct = clazz?.getConstructor(*constructorParameterTypes)
            construct?.newInstance(*constructorParameterValues)
        } catch (_: ClassNotFoundException) { // No class defined for this frame type,use FrameUnsupported
            log.error("Identifier not recognised: '$identifier' using FrameBodyUnsupported")
            null
        } // propagate it up otherwise mark this frame as invalid // An error has occurred during frame instantiation, if underlying cause is an unchecked exception or error
        catch (e: InvocationTargetException) {
            throw InvalidFrameException("Could not invoke constructor", e)
        } catch (e: Exception) { // No Such Method should not happen
            throw IllegalStateException("Could not construct frame", e)
        }
    }
}