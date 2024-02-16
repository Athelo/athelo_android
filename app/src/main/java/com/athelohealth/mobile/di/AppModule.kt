package com.athelohealth.mobile.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.athelohealth.mobile.BuildConfig
import com.athelohealth.mobile.extensions.PREF_NAME
import com.athelohealth.mobile.network.repository.member.MemberRepository
import com.athelohealth.mobile.presentation.model.enums.Enums
import com.athelohealth.mobile.useCase.member.UserAuthorizationTestUseCase
import com.athelohealth.mobile.useCase.websocket.WebSocketSessionUseCases
import com.athelohealth.mobile.utils.DeviceManager
import com.athelohealth.mobile.utils.PreferenceHelper
import com.athelohealth.mobile.utils.UserManager
import com.athelohealth.mobile.utils.app.AppManager
import com.athelohealth.mobile.utils.contentful.ContentfulClient
import com.athelohealth.mobile.utils.fitbit.FitbitConnectionHelper
import com.athelohealth.mobile.websocket.WebSocketManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideUserManager(
        @ApplicationContext appContext: Context,
        authTester: UserAuthorizationTestUseCase
    ): UserManager {
        return UserManager(appContext, authTester)
    }

    @Singleton
    @Provides
    fun provideDeviceManager(@ApplicationContext appContext: Context): DeviceManager {
        return DeviceManager(appContext)
    }


    @Singleton
    @Provides
    fun providePreferenceHelper(@ApplicationContext context: Context) = PreferenceHelper(context)

    @Singleton
    @Provides
    fun provideEnums(): Enums = Enums()

    @Singleton
    @Provides
    fun provideFitbitConnectionHelper(memberRepository: MemberRepository) =
        FitbitConnectionHelper(memberRepository)

    @Singleton
    @Provides
    fun provideWebSocketManager(webSocketUseCases: WebSocketSessionUseCases) =
        WebSocketManager(webSocketUseCases)

    @Singleton
    @Provides
    fun provideAppManager(@ApplicationContext context: Context) = AppManager(context)

    @Singleton
    @Provides
    fun provideContentfulClient() =
        ContentfulClient(
            spaceId = BuildConfig.CONTENTFUL_SPACE_ID,
            spaceToken = BuildConfig.CONTENTFUL_TOKEN_ID
        )

    @Singleton
    @Provides
    fun provideSharePreference(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun providePrefsEditor(pref: SharedPreferences) = pref.edit()
}