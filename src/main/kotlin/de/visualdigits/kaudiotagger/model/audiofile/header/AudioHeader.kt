package de.visualdigits.kaudiotagger.model.audiofile.header

interface AudioHeader {

    fun getBitRate(): String

    fun isVariableBitRate(): Boolean
}