package com.kt.myproject.utils

import android.annotation.TargetApi
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.kt.myproject.repository.model.Media
import java.util.*

class Utils {
    private val isNotPending: String
        private get() = if (Build.VERSION.SDK_INT <= 28) MediaStore.Images.Media.DATA + " NOT NULL" else MediaStore.MediaColumns.IS_PENDING + " != 1"

    @TargetApi(16)
    private fun getWidthColumn(orientation: Int): String {
        return if (orientation == 0 || orientation == 180) MediaStore.Images.Media.WIDTH else MediaStore.Images.Media.HEIGHT
    }

    @TargetApi(16)
    private fun getHeightColumn(orientation: Int): String {
        return if (orientation == 0 || orientation == 180) MediaStore.Images.Media.HEIGHT else MediaStore.Images.Media.WIDTH
    }

    fun getMediaInBucket(
        context: Context,
        bucketId: String,
        contentUri: Uri,
        isImage: Boolean
    ): List<Media> {
        val media: MutableList<Media> = LinkedList()
        var selection = MediaStore.Images.Media.BUCKET_ID + " = ? AND " + isNotPending
        var selectionArgs: Array<String>? = arrayOf(bucketId)
        val sortBy = MediaStore.Images.Media.DATE_MODIFIED + " DESC"
        val projection: Array<String>
        projection = if (isImage) {
            arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.DATE_MODIFIED,
                MediaStore.Images.Media.ORIENTATION,
                MediaStore.Images.Media.WIDTH,
                MediaStore.Images.Media.HEIGHT,
                MediaStore.Images.Media.SIZE
            )
        } else {
            arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.DATE_MODIFIED,
                MediaStore.Images.Media.WIDTH,
                MediaStore.Images.Media.HEIGHT,
                MediaStore.Images.Media.SIZE,
                MediaStore.Video.Media.DURATION
            )
        }
        if (Media.ALL_MEDIA_BUCKET_ID == bucketId) {
            selection = isNotPending
            selectionArgs = null
        }
        context.contentResolver.query(contentUri, projection, selection, selectionArgs, sortBy)
            .use { cursor ->
                while (cursor != null && cursor.moveToNext()) {
                    val rowId = cursor.getLong(cursor.getColumnIndexOrThrow(projection[0]))
                    val uri = ContentUris.withAppendedId(contentUri, rowId)
                    val mimetype =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE))
                    val date =
                        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED))
                    val orientation =
                        if (isImage) cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.ORIENTATION)) else 0
                    val width =
                        cursor.getInt(cursor.getColumnIndexOrThrow(getWidthColumn(orientation)))
                    val height =
                        cursor.getInt(cursor.getColumnIndexOrThrow(getHeightColumn(orientation)))
                    val size =
                        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE))
                    val duration =
                        if (!isImage) cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))
                            .toLong() else 0.toLong()
                    media.add(
                        Media(
                            uri,
                            mimetype,
                            date,
                            width,
                            height,
                            size,
                            duration,
                            false,
                            false
                        )
                    )
                }
            }
        return media
    }
}