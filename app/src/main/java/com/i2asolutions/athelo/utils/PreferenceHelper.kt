package com.i2asolutions.athelo.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.i2asolutions.athelo.utils.PreferenceHelper.Keys.DISPLAY_CHAT_HELLO
import com.i2asolutions.athelo.utils.PreferenceHelper.Keys.DISPLAY_CHAT_LANDING
import com.i2asolutions.athelo.utils.PreferenceHelper.Keys.DISPLAY_TUTORIAL
import com.i2asolutions.athelo.utils.PreferenceHelper.Keys.HIDE_PIN
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class PreferenceHelper @Inject constructor(@ApplicationContext context: Context) {
    private val preference: SharedPreferences =
        context.getSharedPreferences(MEMBER_PREF_NAME, Context.MODE_PRIVATE)

    var userId: Int?
        get() {
            val userId = preference.getInt(Keys.USER_ID_KEY, -1)
            return if (userId > 0) userId else null
        }
        set(value) = preference.edit {
            if (value == null) {
                remove(Keys.USER_ID_KEY)
            } else {
                preference.edit { putInt(Keys.USER_ID_KEY, value) }
            }
        }
    var showChatGroupLanding: Boolean
        get() = preference.getBoolean(DISPLAY_CHAT_LANDING, true)
        set(value) = preference.edit { putBoolean(DISPLAY_CHAT_LANDING, value) }
    var showTutorial: Boolean
        get() = preference.getBoolean(DISPLAY_TUTORIAL, true)
        set(value) = preference.edit { putBoolean(DISPLAY_TUTORIAL, value) }
    var hidePin: Boolean
        get() = preference.getBoolean(HIDE_PIN, false)
        set(value) = preference.edit { putBoolean(HIDE_PIN, value) }

    fun shouldShowHello(convId: Int): Boolean =
        preference.getBoolean(DISPLAY_CHAT_HELLO + convId, true)

    fun setShowHello(convId: Int, value: Boolean = false) =
        preference.edit { putBoolean(DISPLAY_CHAT_HELLO + convId, value) }


    companion object {
        private const val MEMBER_PREF_NAME = "userData"
    }

    object Keys {
        internal const val USER_ID_KEY = "user_id_key"
        internal const val DISPLAY_TUTORIAL = "display_tutorial"
        internal const val HIDE_PIN = "hide_pin"
        internal const val DISPLAY_CHAT_LANDING = "display_chat_landing"
        internal const val DISPLAY_CHAT_HELLO = "display_chat_hello_"
    }
}