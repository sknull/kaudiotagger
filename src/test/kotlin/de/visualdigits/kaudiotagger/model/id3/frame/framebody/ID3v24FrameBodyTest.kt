package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import org.junit.jupiter.api.Test

class ID3v24FrameBodyTest {
    protected val log: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger(javaClass)

    @Test
    fun testBodyImplementationsAreComplete() {
        var success = true
        for (field in ID3v24FrameId::class.java.getDeclaredFields()) {
            if (String::class.java == field.getType() &&
                java.lang.reflect.Modifier.isPublic(field.getModifiers()) &&
                java.lang.reflect.Modifier.isStatic(field.getModifiers()) &&
                java.lang.reflect.Modifier.isFinal(field.getModifiers()) &&
                field.getName().startsWith("FRAME_ID")
            ) {
                val frameID = field.get(null) as? String
                val packageName = ID3v24FrameBody::class.java.getPackage().getName()
                val bodyClass = java.lang.Class.forName(
                    packageName + ".FrameBody" + frameID
                )
                success = success and isCompatible(ID3v24FrameBody::class.java, bodyClass)
            }
        }
        if (success) {
            log.debug("Test was successful.")
        } else {
            log.debug("Test was not successful. Errors haven been reported above.")
        }
    }

    private fun isCompatible(superType: java.lang.Class<*>, subType: java.lang.Class<*>): Boolean {
        var compatible = true
        if (!superType.isAssignableFrom(subType)) {
            compatible = false
            log.error(
                subType.getName() + " does not implement " + superType.getName()
            )
        }
        return compatible
    }

    companion object {
            @JvmStatic
        fun main(args: Array<String>) {
            ID3v24FrameBodyTest().testBodyImplementationsAreComplete()
        }
    }
}
