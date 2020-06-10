package com.mercadopago.android.px.internal.core

import android.os.Parcelable
import com.mercadopago.android.px.internal.util.ParcelableUtil
import java.io.*

class FileManager(private val rootCacheDir: File) {

    fun create(fileName: String): File {
        val fileNameBuilder = StringBuilder()
        fileNameBuilder.append(rootCacheDir.path)
        fileNameBuilder.append(File.separator)
        fileNameBuilder.append(fileName)
        return File(fileNameBuilder.toString())
    }

    @Synchronized
    fun writeToFile(file: File, fileContent: String) = file.writeText(fileContent)

    @Synchronized
    fun writeToFile(file: File, fileContent: Parcelable) = file.writeBytes(ParcelableUtil.marshall(fileContent))

    @Synchronized
    fun readText(file: File): String {
        return if (file.exists()) file.readText() else ""
    }

    @Synchronized
    fun <T>readParcelable(file: File, creator: Parcelable.Creator<T>): T? {
        return if (file.exists()) ParcelableUtil.unmarshall(file.readBytes(), creator) else null
    }

    @Synchronized
    fun exists(file: File) = file.exists()

    @Synchronized
    fun removeFile(file: File) = exists(file) && file.delete()
}