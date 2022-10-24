package com.i2asolutions.athelo.di

import com.i2asolutions.athelo.network.repository.application.ApplicationRepository
import com.i2asolutions.athelo.network.repository.application.ApplicationRepositoryImpl
import com.i2asolutions.athelo.network.repository.chat.ChatRepository
import com.i2asolutions.athelo.network.repository.chat.ChatRepositoryImpl
import com.i2asolutions.athelo.network.repository.common.CommonRepository
import com.i2asolutions.athelo.network.repository.common.CommonRepositoryImpl
import com.i2asolutions.athelo.network.repository.deviceConfig.DeviceConfigRepository
import com.i2asolutions.athelo.network.repository.deviceConfig.DeviceConfigRepositoryImpl
import com.i2asolutions.athelo.network.repository.health.HealthRepository
import com.i2asolutions.athelo.network.repository.health.HealthRepositoryImpl
import com.i2asolutions.athelo.network.repository.member.MemberRepository
import com.i2asolutions.athelo.network.repository.member.MemberRepositoryImpl
import com.i2asolutions.athelo.network.repository.news.NewsRepository
import com.i2asolutions.athelo.network.repository.news.NewsRepositoryImpl
import com.i2asolutions.athelo.utils.UserManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideMemberRepository(userManager: UserManager): MemberRepository {
        return MemberRepositoryImpl(userManager)
    }

    @Singleton
    @Provides
    fun provideCommonRepository(userManager: UserManager): CommonRepository {
        return CommonRepositoryImpl(userManager)
    }

    @Singleton
    @Provides
    fun provideHealthRepository(userManager: UserManager): HealthRepository {
        return HealthRepositoryImpl(userManager)
    }

    @Singleton
    @Provides
    fun provideApplicationRepository(userManager: UserManager): ApplicationRepository {
        return ApplicationRepositoryImpl(userManager)
    }

    @Singleton
    @Provides
    fun provideChatRepository(userManager: UserManager): ChatRepository {
        return ChatRepositoryImpl(userManager)
    }

    @Singleton
    @Provides
    fun provideNewsRepository(userManager: UserManager): NewsRepository {
        return NewsRepositoryImpl(userManager)
    }

    @Singleton
    @Provides
    fun provideDeviceConfigRepository(userManager: UserManager): DeviceConfigRepository {
        return DeviceConfigRepositoryImpl(userManager)
    }
}