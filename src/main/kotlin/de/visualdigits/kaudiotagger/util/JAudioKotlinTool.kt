package de.visualdigits.kaudiotagger.util

import de.visualdigits.kaudiotagger.model.audiofile.AudioFile
import de.visualdigits.kaudiotagger.model.kfield.AbstractKField
import de.visualdigits.kaudiotagger.model.kfield.BinaryKField
import de.visualdigits.kaudiotagger.model.kfield.CommonKField
import de.visualdigits.kaudiotagger.model.kfield.KFieldKey
import de.visualdigits.kaudiotagger.model.kframe.ID3v22KFrame
import de.visualdigits.kaudiotagger.model.kframe.ID3v23KFrame
import de.visualdigits.kaudiotagger.model.kframe.ID3v24KFrame
import de.visualdigits.kaudiotagger.model.kframe.KFrame
import de.visualdigits.kaudiotagger.model.tag.Tag
import java.awt.image.BufferedImage
import java.io.File
import kotlin.collections.toList
import kotlin.sequences.toList
import kotlin.text.toList
import kotlin.toList

inline fun <reified T : AudioFile<T>> File.toAudioFile(): T? = AudioFile.read<T>(this)

inline fun <reified T : AudioFile<T>> File.getAudioTag(): Tag? = toAudioFile<T>()?.tag

inline fun <reified T : AudioFile<T>> File.tagMap(): Map<KFrame<*>, AbstractKField<*>> {
    return getAudioTag<T>()
        ?.fields
        ?.asSequence()
        ?.toList()
        ?.mapNotNull { tagField -> tagField.toKField() }
        ?.associate { kf -> Pair(kf.id, kf) }
        ?:mapOf()
}

inline fun <reified T : AudioFile<T>> File.commonTagMap(): Map<KFieldKey, AbstractKField<*>> {
    return tagMap<T>()
        .filter { (k, v) -> k.fieldKey != null }
        .map { (k, v) -> Pair(k.fieldKey!!, v) }
        .toMap()
}

inline fun <reified T : AudioFile<T>> File.getArtworks(): List<Pair<BufferedImage, String>> {
    return getAudioTag<T>().artworkList.mapNotNull { aw ->
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
