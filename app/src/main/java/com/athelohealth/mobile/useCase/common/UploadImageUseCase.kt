package com.athelohealth.mobile.useCase.common

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import androidx.core.net.toFile
import com.athelohealth.mobile.extensions.getImageRotation
import com.athelohealth.mobile.network.dto.common.InputStreamRequestBodyDto
import com.athelohealth.mobile.network.repository.common.CommonRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject


class UploadImageUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: CommonRepository
) {

    suspend operator fun invoke(uri: Uri): Int? {
        val contentResolver = context.contentResolver
        val body =
            getMultipartBody(uri, contentResolver) ?: throw Exception("Unknown exception")
        return repository.updateUserImage(body).id
    }

    @SuppressLint("Range")
    private fun getMultipartBody(uri: Uri, contentResolver: ContentResolver): MultipartBody.Part? {
        try {
            when (uri.scheme) {
                "file" -> {
                    val rotation = uri.path?.getImageRotation() ?: 0f
                    return if (rotation == 0f) {
                        val file = uri.toFile()
                        val requestFile = file.asRequestBody("image/*".toMediaType())
                        MultipartBody.Part.createFormData("image", file.name, requestFile)
                    } else {
                        val file = uri.toFile()
                        val requestBody =
                            InputStreamRequestBodyDto("image/*".toMediaType(), contentResolver, uri)
                        MultipartBody.Part.createFormData("image", file.name, requestBody)
                    }
                }
                "content" -> {
                    var result: String? = null
                    val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
                    cursor.use {
                        if (cursor != null && cursor.moveToFirst()) {
                            result =
                                cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                        }
                    }
                    if (result == null) {
                        result = uri.path
                        val cut = result?.lastIndexOf('/') ?: -1
                        if (cut != -1) {
                            result = result?.substring(cut + 1)
                        }
                    }
                    val requestBody =
                        InputStreamRequestBodyDto("image/*".toMediaType(), contentResolver, uri)
                    return MultipartBody.Part.createFormData("image", result, requestBody)
                }
                else -> return null
            }
        } catch (e: Exception) {
            return null
        }
    }
}