
package com.athelohealth.mobile.network.repository

import com.athelohealth.mobile.BuildConfig
import com.athelohealth.mobile.extensions.debugPrint
import com.athelohealth.mobile.utils.UserManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

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
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
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
//                addHeader("X-Application-Identifier", BuildConfig.APP_ID)
//                addHeader("X-App-Version", BuildConfig.VERSION_NAME)
//                addHeader("X-Device-Type", "ANDROID")
                debugPrint(BuildConfig.APP_ID)
                val session = userManager.getFormattedSession()
                if (!session.isNullOrBlank()) {
                    session.let {
                        addHeader("Authorization", "Bearer $it")
                        debugPrint("session: $it")
                    }
                }
            }
            chain.proceed(request.build())
        }
        if (BuildConfig.DEBUG)
            httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(httpClient.build())
            .build()
            .create(clazz)
    }
}