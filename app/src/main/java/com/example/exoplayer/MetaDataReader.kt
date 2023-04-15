package com.example.exoplayer

import android.app.Application
import android.net.Uri
import android.provider.MediaStore
import java.io.File

data class MetaData(
    val fileName: String
)

interface MetaDataReader {
    fun getMetaDataFromUri(contentUri: Uri): MetaData?
}

class MetaDataReaderImpl(
    private val app: Application
) : MetaDataReader {

    override fun getMetaDataFromUri(contentUri: Uri): MetaData? {
        if (contentUri.scheme != "content") {
            return null
        }
        val fileName = app.contentResolver
            .query(
                contentUri,
                arrayOf(MediaStore.Video.VideoColumns.DISPLAY_NAME),
                null,
                null,
                null
            )
            ?.use { cursor ->
                val index = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DISPLAY_NAME)
                if (index == -1 || !cursor.moveToFirst()) {
                    null
                } else {
                    cursor.getString(index)
                }
            }
        return fileName?.let { fileName ->
            MetaData(
                fileName = File(fileName).name ?: return null
            )
        }
    }
}
