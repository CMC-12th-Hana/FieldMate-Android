package com.hana.fieldmate.ui.component.imagepicker

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import java.util.*

private val projection = arrayOf(
    MediaStore.Images.Media._ID,
    MediaStore.Images.Media.DISPLAY_NAME,
    MediaStore.Images.Media.DATE_TAKEN
)

internal object ImageLoader {

    fun insertImage(context: Context): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "umuljeong-${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        return context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
    }

    fun deleteImage(context: Context, uri: Uri) {
        context.contentResolver.delete(uri, null, null)
    }

    fun load(context: Context): List<ImageInfo> {
        val images = ArrayList<ImageInfo>()
        val query = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null, null, "${MediaStore.Images.Media.DATE_TAKEN} DESC"
        )

        query?.use { cursor ->
            cursor.moveToFirst()

            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val dateTakenColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
            val displayNameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)

            var id = cursor.getLong(idColumn)
            var dateTaken = Date(cursor.getLong(dateTakenColumn))
            var displayName = cursor.getString(displayNameColumn)
            var contentUri = Uri.withAppendedPath(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                id.toString()
            )

            images += ImageInfo(id, displayName, dateTaken, contentUri)

            while (cursor.moveToNext()) {
                id = cursor.getLong(idColumn)
                dateTaken = Date(cursor.getLong(dateTakenColumn))
                displayName = cursor.getString(displayNameColumn)
                contentUri = Uri.withAppendedPath(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id.toString()
                )

                images += ImageInfo(id, displayName, dateTaken, contentUri)
            }

            cursor.close()
        }

        return images
    }
}