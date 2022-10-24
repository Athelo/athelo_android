package com.i2asolutions.athelo.utils

import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.i2asolutions.athelo.extensions.debugPrint
import com.i2asolutions.athelo.presentation.model.member.AuthorizationState
import com.i2asolutions.athelo.presentation.model.member.Token
import com.i2asolutions.athelo.presentation.model.member.User
import com.i2asolutions.athelo.useCase.member.UserAuthorizationTestUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class UserManager internal constructor(
    context: Context,
    private val authTester: UserAuthorizationTestUseCase
) {

    private val mainKey =
        MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
    private val sharedPrefsFile: String = ".user"
    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        sharedPrefsFile,
        mainKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val _loginState: MutableStateFlow<AuthorizationState> =
        MutableStateFlow(AuthorizationState.Unknown)
    val logInState: StateFlow<AuthorizationState> = _loginState

    private val _user: MutableStateFlow<User?> =
        MutableStateFlow(null)
    val user: StateFlow<User?> = _user

    var lastUserLocation: Location? = null
        set(value) {
            field = value
            debugPrint("#UserLocation", "update ${value.toString()}")
        }

    fun checkLogInState() {
        if (!isLoggedIn()) {
            _loginState.tryEmit(AuthorizationState.Unauthorized)
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                val result = authTester()
                _loginState.tryEmit(
                    if (result) AuthorizationState.Authorized
                    else AuthorizationState.Unauthorized
                )
            }
        }
    }

    suspend fun storeUser(response: User?) {
        val userJson = if (response != null) Json.encodeToString(response) else null
        storeOrRemoveUser(userJson)
        _user.emit(response)
    }

    fun storeSession(response: Token?) {
        val session = response?.accessToken
        val tokenType = response?.tokenType
        storeSession(session)
        storeTokenType(tokenType)
    }

    fun clearSession() {
        storeSession(session = null)
        storeTokenType(null)
    }

    fun getFormattedSession(): String? {
        val session = getSession() ?: return null
        val type = getTokenType() ?: return null
        if (session.isBlank()) return null
        if (type.isBlank()) return null
        return "$type $session"
    }

    fun isLoggedIn(): Boolean {
        return !getFormattedSession().isNullOrBlank()
    }

    suspend fun logout() {
        clearSession()
        storeOrRemoveUser(null)
        storeUserEmail(null)
        _loginState.emit(AuthorizationState.Unknown)
    }

    fun storeUserEmail(email: String?) {
        sharedPreferences.edit {
            if (email == null) {
                remove("userEmail")
            } else {
                putString("userEmail", email)
            }
            apply()
        }
    }

    fun loadUserEmail(): String? =
        sharedPreferences.getString("userEmail", null)

    private fun storeTokenType(tokenType: String?) {
        if (tokenType == null)
            sharedPreferences.edit {
                remove("tokenType")
                apply()
            }
        else {
            sharedPreferences.edit {
                putString("tokenType", tokenType)
                apply()
            }
        }
    }

    private fun getTokenType(): String? {
        return sharedPreferences.getString("tokenType", null)
    }

    private fun storeSession(session: String?) {
        if (session == null)
            sharedPreferences.edit {
                remove("session")
                apply()
            }
        else {
            sharedPreferences.edit {
                putString("session", session)
                apply()
            }
        }
        _loginState.tryEmit(
            if (!session.isNullOrBlank()) AuthorizationState.Authorized
            else AuthorizationState.Unauthorized
        )
    }

    private fun getSession(): String? {
        return sharedPreferences.getString("session", null)
    }

    private fun storeOrRemoveUser(user: String?) {
        if (user == null)
            sharedPreferences.edit { remove("user") }
        else {
            sharedPreferences.edit { putString("user", user) }
        }
    }


    internal suspend fun getUser(): User? {
        val userJson = sharedPreferences.getString("user", null)
        return try {
            if (userJson == null) null
            else Json.decodeFromString<User>(userJson)
        } catch (e: Exception) {
            null
        }.also { _user.emit(it) }
    }
}