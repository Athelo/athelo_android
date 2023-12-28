package com.athelohealth.mobile.utils

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.core.net.toUri
import com.athelohealth.mobile.BuildConfig
import net.openid.appauth.*
import net.openid.appauth.AuthorizationException

object GoogleOAuthHelper {

    private var serviceConfig = AuthorizationServiceConfiguration(
        "https://accounts.google.com/o/oauth2/v2/auth".toUri(),
        "https://oauth2.googleapis.com/token".toUri()
    )
    private lateinit var authRequest: AuthorizationRequest

    fun signIn(context: Context, launcher: ActivityResultLauncher<Intent>) {
        authRequest = AuthorizationRequest.Builder(
            serviceConfig,
            BuildConfig.GOOGLE_CLIENT_ID,
            ResponseTypeValues.CODE,
            "${BuildConfig.APPLICATION_ID}://".toUri()
        )
            .setScope("email profile")
            .setLoginHint("Login hint")
            .build()

        val authService = AuthorizationService(context)
        val authIntent = authService.getAuthorizationRequestIntent(authRequest)
        launcher.launch(authIntent)
    }

    fun parseResponse(
        context: Context,
        data: Intent?,
        callback: AuthorizationService.TokenResponseCallback
    ) {
        if (data != null) {
            val resp = AuthorizationResponse.fromIntent(data)
            val err = AuthorizationException.fromIntent(data)
            if (resp != null) {
                exchangeCode(context, resp, callback)
            } else {
                callback.onTokenRequestCompleted(null, err)
            }
        }
    }

    private fun exchangeCode(
        context: Context,
        resp: AuthorizationResponse,
        callback: AuthorizationService.TokenResponseCallback
    ) {
        TokenRequest.Builder(authRequest.configuration, authRequest.clientId)
            .setGrantType(GrantTypeValues.AUTHORIZATION_CODE)
            .setRedirectUri(authRequest.redirectUri)
            .setAuthorizationCode(resp.authorizationCode)
            .build()
        AuthorizationService(context).performTokenRequest(
            resp.createTokenExchangeRequest(),
            callback
        )
    }

}