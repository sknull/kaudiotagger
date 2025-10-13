package de.visualdigits.kaudiotagger.util

import de.visualdigits.kaudiotagger.model.field.AbstractKField
import de.visualdigits.kaudiotagger.model.field.BinaryKField
import de.visualdigits.kaudiotagger.model.field.CommonKField
import de.visualdigits.kaudiotagger.model.field.KFieldKey
import de.visualdigits.kaudiotagger.model.frame.ID3v22KFrame
import de.visualdigits.kaudiotagger.model.frame.ID3v23KFrame
import de.visualdigits.kaudiotagger.model.frame.ID3v24KFrame
import de.visualdigits.kaudiotagger.model.frame.KFrame
import org.jaudiotagger.audio.AudioFile
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.Tag
import org.jaudiotagger.tag.TagField
import org.jaudiotagger.tag.id3.ID3v22Frame
import org.jaudiotagger.tag.id3.ID3v23Frame
import org.jaudiotagger.tag.id3.ID3v24Frame
import org.jaudiotagger.tag.id3.framebody.FrameBodyAPIC
import org.jaudiotagger.tag.id3.framebody.FrameBodyPIC
import org.jaudiotagger.tag.id3.valuepair.ImageFormats
import java.awt.image.BufferedImage
import java.io.File

fun File.toAudioFile(): AudioFile = AudioFileIO.read(this)

fun File.getAudioTag(): Tag = toAudioFile().tag

fun File.tagMap(): Map<KFrame<*>, AbstractKField<*>> {
    return getAudioTag()
        .fields
        ?.asSequence()
        ?.toList()
        ?.mapNotNull { tagField -> tagField.toKField() }
        ?.associate { kf -> Pair(kf.id, kf) }
        ?:mapOf()
}

fun File.commonTagMap(): Map<KFieldKey, AbstractKField<*>> {
    return tagMap()
        .filter { (k, v) -> k.fieldKey != null }
        .map { (k, v) -> Pair(k.fieldKey!!, v) }
        .toMap()
}

fun File.getArtworks(): List<Pair<BufferedImage, String>> {
    return getAudioTag().artworkList.mapNotNull { aw ->
        when (aw.mimeType) {
            "image/jpeg" -> "jpg"
            "image/jpg" -> "jpg"
            "image/png" -> "png"
            else -> null
        }?.let { extension -> Pair(aw.image, extension) }
    }
}

fun TagField.toKField(): AbstractKField<*>? {
    return when (this) {
        is ID3v22Frame -> {
            if (isCommon) {
                ID3v22KFrame.fromId(id)?.let { kf -> CommonKField(kf, content) }
            } else if (isBinary) {
                ID3v22KFrame.fromId(id)?.let { kf ->
                    val mimeType = when(body) {
                        is FrameBodyPIC -> ImageFormats.getMimeTypeForFormat ((body as FrameBodyPIC).formatType)
                        is FrameBodyAPIC -> (body as FrameBodyAPIC).mimeType
                        else -> null
                    }
                    BinaryKField(kf, rawContent, mimeType)
                }
            } else {
                null
            }
        }
        is ID3v23Frame -> {
            if (isCommon) {
                ID3v23KFrame.fromId(id)?.let { kf -> CommonKField(kf, content) }
            } else if (isBinary) {
                ID3v23KFrame.fromId(id)?.let { kf ->
                    val mimeType = when(body) {
                        is FrameBodyPIC -> ImageFormats.getMimeTypeForFormat ((body as FrameBodyPIC).formatType)
                        is FrameBodyAPIC -> (body as FrameBodyAPIC).mimeType
                        else -> null
                    }
                    BinaryKField(kf, rawContent, mimeType)
                }
            } else {
                null
            }
        }
        is ID3v24Frame -> {
            if (isCommon) {
                ID3v24KFrame.fromId(id)?.let { kf -> CommonKField(kf, content) }
            } else if (isBinary) {
                ID3v24KFrame.fromId(id)?.let { kf ->
                    val mimeType = when(body) {
                        is FrameBodyPIC -> ImageFormats.getMimeTypeForFormat ((body as FrameBodyPIC).formatType)
                        is FrameBodyAPIC -> (body as FrameBodyAPIC).mimeType
                        else -> null
                    }
                    BinaryKField(kf, rawContent, mimeType)
                }
            } else {
                null
            }
        }
        else -> null
    }
}
