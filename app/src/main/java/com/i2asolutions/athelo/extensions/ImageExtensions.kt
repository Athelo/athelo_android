package com.i2asolutions.athelo.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import java.io.ByteArrayOutputStream
import java.io.IOException

fun String.createStreamByteFromImage(stream: ByteArrayOutputStream) {
    var photoBitmap: Bitmap = BitmapFactory.decodeFile(this)
    val imageRotation = getImageRotation()
    if (imageRotation != 0f) photoBitmap = photoBitmap.getBitmapRotatedByDegree(imageRotation)
    photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
}

fun String.getImageRotation(): Float {
    var exif: ExifInterface? = null
    var exifRotation = 0
    try {
        exif = ExifInterface(this)
        exifRotation =
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return if (exif == null) 0f else exifToDegrees(exifRotation).toFloat()
}

private fun exifToDegrees(rotation: Int): Int {
    if (rotation == ExifInterface.ORIENTATION_ROTATE_90) return 90 else if (rotation == ExifInterface.ORIENTATION_ROTATE_180) return 180 else if (rotation == ExifInterface.ORIENTATION_ROTATE_270) return 270
    return 0
}

fun Bitmap.getBitmapRotatedByDegree(rotationDegree: Float): Bitmap {
    val matrix = Matrix()
    matrix.preRotate(rotationDegree)
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}