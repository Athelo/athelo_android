package com.i2asolutions.athelo.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import java.util.*


class DeviceManager(context: Context) {

    private val sharedPrefsFile: String = ".device"
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(sharedPrefsFile, Context.MODE_PRIVATE)

    fun getIdentifier(): String {
        var id = sharedPreferences.getString("identifier", null)
        if (id == null || id.isBlank()) {
            id = UUID.randomUUID().toString()
            saveIdentifier(id)
        }
        return id
    }

    private fun saveIdentifier(id: String) {
        sharedPreferences.edit {
            putString("identifier", id)
        }
    }
}