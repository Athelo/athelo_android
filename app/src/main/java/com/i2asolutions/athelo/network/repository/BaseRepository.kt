
package com.i2asolutions.athelo.network.repository

import com.i2asolutions.athelo.BuildConfig
import com.i2asolutions.athelo.extensions.debugPrint
import com.i2asolutions.athelo.utils.UserManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@OptIn(ExperimentalSerializationApi::class)
abstract class BaseRepository<T>(clazz: Class<T>, private val userManager: UserManager) {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        explicitNulls = false
        isLenient = true
    }
    protected val service: T by lazy {
        val httpClient = OkHttpClient.Builder()
        if (BuildConfig.DEBUG)
            httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        httpClient.addInterceptor { chain ->
//            if (NetWorkManager.isDisconnected) {
//                throw NetWorkDisconnectedException()
//            }
            chain.proceed(chain.request().newBuilder().build())
        }
        httpClient.addInterceptor { chain ->
            val request = chain
                .request()
                .newBuilder()
            request.apply {
                addHeader("X-Application-Identifier", BuildConfig.APP_ID)
                addHeader("X-App-Version", BuildConfig.VERSION_NAME)
                addHeader("X-Device-Type", "ANDROID")
                debugPrint(BuildConfig.APP_ID)
                val session = userManager.getFormattedSession()
                if (!session.isNullOrBlank()) {
                    session.let {
                        addHeader("Authorization", it)
                        debugPrint("session: $it")
                    }
                }
            }
            chain.proceed(request.build())
        }
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(httpClient.build())
            .build()
            .create(clazz)
    }
}