package com.i2asolutions.athelo.di

import android.content.Context
import com.i2asolutions.athelo.BuildConfig
import com.i2asolutions.athelo.network.repository.member.MemberRepository
import com.i2asolutions.athelo.presentation.model.enums.Enums
import com.i2asolutions.athelo.useCase.member.UserAuthorizationTestUseCase
import com.i2asolutions.athelo.useCase.websocket.WebSocketSessionUseCases
import com.i2asolutions.athelo.utils.DeviceManager
import com.i2asolutions.athelo.utils.PreferenceHelper
import com.i2asolutions.athelo.utils.UserManager
import com.i2asolutions.athelo.utils.app.AppManager
import com.i2asolutions.athelo.utils.contentful.ContentfulClient
import com.i2asolutions.athelo.utils.fitbit.FitbitConnectionHelper
import com.i2asolutions.athelo.websocket.WebSocketManager
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
}