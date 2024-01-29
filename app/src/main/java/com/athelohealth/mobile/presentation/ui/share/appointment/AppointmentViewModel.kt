package com.athelohealth.mobile.presentation.ui.share.appointment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.member.LoadCachedUserUseCase
import com.athelohealth.mobile.useCase.member.LoadMyProfileUseCase
import com.athelohealth.mobile.useCase.member.StoreUserUseCase
import com.athelohealth.mobile.utils.AuthorizationException
import com.athelohealth.mobile.utils.contentful.ContentfulClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AppointmentViewModel @Inject constructor(
    private val loadMyProfileUseCase: LoadMyProfileUseCase,
    private val storeProfile: StoreUserUseCase,
    private val loadCachedUserUseCase: LoadCachedUserUseCase
): BaseViewModel<AppointmentEvent, AppointmentEffect, AppointmentViewState>(AppointmentViewState()) {

    private val _isAppointmentListEmpty = MutableLiveData(false)
    val isAppointmentListEmpty: LiveData<Boolean> get() = _isAppointmentListEmpty

    override fun pauseLoadingState() {
        notifyStateChange(currentState.copy(isLoading = false))
    }

    override fun loadData() {
        notifyStateChange(currentState.copy(isLoading = false))
        launchRequest {
//            val user =
//                loadCachedUserUseCase() ?: loadMyProfileUseCase().also { storeProfile(it) } ?: throw AuthorizationException()
          //  newsList = contentfulClient.getAllNews()
          //  _contentfulViewState.emit(currentNewsList())
//            notifyStateChange(
//                currentState.copy(
//                    initialized = true,
//                    isLoading = false,
//                    user = user
//                )
//            )
        }
    }

    override fun handleEvent(event: AppointmentEvent) {
        when(event) {
            is AppointmentEvent.MenuClick -> {
                notifyEffectChanged(AppointmentEffect.ShowMenuScreen)
            }
            is AppointmentEvent.MyProfileClick -> {
                notifyEffectChanged(AppointmentEffect.ShowMyProfileScreen)
            }
            is AppointmentEvent.ScheduleMyAppointmentClick -> {
                notifyEffectChanged(AppointmentEffect.ShowScheduleMyAppointment)
            }
        }
    }
}