package com.athelohealth.mobile.di

import com.athelohealth.mobile.network.repository.application.ApplicationRepository
import com.athelohealth.mobile.network.repository.application.ApplicationRepositoryImpl
import com.athelohealth.mobile.network.repository.caregiver.CaregiverRepository
import com.athelohealth.mobile.network.repository.caregiver.CaregiverRepositoryImpl
import com.athelohealth.mobile.network.repository.chat.ChatRepository
import com.athelohealth.mobile.network.repository.chat.ChatRepositoryImpl
import com.athelohealth.mobile.network.repository.common.CommonRepository
import com.athelohealth.mobile.network.repository.common.CommonRepositoryImpl
import com.athelohealth.mobile.network.repository.deviceConfig.DeviceConfigRepository
import com.athelohealth.mobile.network.repository.deviceConfig.DeviceConfigRepositoryImpl
import com.athelohealth.mobile.network.repository.health.HealthRepository
import com.athelohealth.mobile.network.repository.health.HealthRepositoryImpl
import com.athelohealth.mobile.network.repository.member.MemberRepository
import com.athelohealth.mobile.network.repository.member.MemberRepositoryImpl
import com.athelohealth.mobile.network.repository.news.NewsRepository
import com.athelohealth.mobile.network.repository.news.NewsRepositoryImpl
import com.athelohealth.mobile.utils.UserManager
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

    @Singleton
    @Provides
    fun provideCaregiverRepository(userManager: UserManager): CaregiverRepository {
        return CaregiverRepositoryImpl(userManager)
    }
}