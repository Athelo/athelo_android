package com.i2asolutions.athelo.network.dto.common

import android.content.ContentResolver
import android.net.Uri
import com.i2asolutions.athelo.extensions.createStreamByteFromImage
import com.i2asolutions.athelo.extensions.getImageRotation
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source
import java.io.ByteArrayOutputStream
import java.io.IOException


class InputStreamRequestBodyDto(
    private val contentType: MediaType,
    private val contentResolver: ContentResolver,
    private val uri: Uri
) : RequestBody() {
    override fun contentType() = contentType

    override fun contentLength(): Long = -1

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val rotation = uri.path?.getImageRotation() ?: 0f
        if (rotation == 0f) {
            val input = contentResolver.openInputStream(uri)
            input?.use { sink.writeAll(it.source()) }
                ?: throw IOException("Could not open $uri")
        } else {
            val bos = ByteArrayOutputStream()
            uri.path?.createStreamByteFromImage(bos)
            bos.use { sink.write(it.toByteArray()) }
        }
    }
}
