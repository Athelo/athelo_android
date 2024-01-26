@file:Suppress("unused")

package com.athelohealth.mobile.utils

import android.content.Context
import android.net.Uri
import android.webkit.URLUtil
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import com.athelohealth.mobile.MainNavigationDirections
import com.athelohealth.mobile.R
import com.athelohealth.mobile.extensions.*
import com.athelohealth.mobile.presentation.model.calendar.Day
import com.athelohealth.mobile.presentation.model.feedback.FeedbackScreenType
import com.athelohealth.mobile.presentation.model.health.Symptom
import com.athelohealth.mobile.presentation.model.home.Tabs
import com.athelohealth.mobile.presentation.model.news.Category
import com.athelohealth.mobile.presentation.ui.containerWithTab.TabsFragment
import com.athelohealth.mobile.presentation.ui.patient.wellbeing.WellbeingFragmentDirections
import com.athelohealth.mobile.presentation.ui.share.authorization.landing.AuthorizationLandingFragmentDirections
import com.athelohealth.mobile.presentation.ui.share.authorization.signInWithEmail.SignInWithEmailFragmentDirections
import com.athelohealth.mobile.presentation.ui.share.splash.SplashFragmentDirections
import com.athelohealth.mobile.utils.consts.*

internal const val DEEPLINK_MY_PROFILE = "atheloapp://myProfile"
internal const val DEEPLINK_LOG_OUT = "atheloapp://logOut"
internal const val DEEPLINK_AUTHORIZATION = "atheloapp://authorization"
internal const val DEEPLINK_EDIT_PROFILE = "atheloapp://editProfile?editMode=%b"
internal const val DEEPLINK_CHANGE_PASSWORD = "atheloapp://changePassword"
internal const val DEEPLINK_WEB_VIEW = "atheloapp://webView?url=%s"
internal const val DEEPLINK_ASK_QUESTION = "atheloapp://askQuestion"
internal const val DEEPLINK_FEEDBACK = "atheloapp://feedback?type=%d"
internal const val DEEPLINK_CHAT_LIST = "atheloapp://chatList"
internal const val DEEPLINK_CONNECT_FITBIT = "atheloapp://connectFitbit?skip=%b"
internal const val DEEPLINK_CATEGORY_PICKER = "atheloapp://categoryPicker"
internal const val DEEPLINK_NEWS_DETAIL = "atheloapp://newsDetail?id=%s"
internal const val DEEPLINK_WELLBEING = "atheloapp://trackWellbeing"
internal const val DEEPLINK_CHAT = "atheloapp://chat?conversationId=%s&group=%b"
internal const val DEEPLINK_RECOMMENDATION_SYMPTOM = "atheloapp://symptomRecommendation?id=%d"
internal const val DEEPLINK_MY_SYMPTOMS = "atheloapp://mySymptoms"
internal const val DEEPLINK_SYMPTOM_DETAIL = "atheloapp://symptomDetail?id=%d"
internal const val DEEPLINK_NEWS = "atheloapp://newsList"
internal const val DEEPLINK_SETTINGS = "atheloapp://settings"
internal const val DEEPLINK_SYMPTOM_CHRONOLOGY = "atheloapp://symptomChronology"
internal const val DEEPLINK_MY_DEVICE = "atheloapp://myDevice"
internal const val DEEPLINK_SLEEP_DETAILS = "atheloapp://sleepDetails"
internal const val DEEPLINK_INVITATION_CODE = "atheloapp://invitationCode"
internal const val DEEPLINK_ACTIVITY_STEPS = "atheloapp://activitySteps"
internal const val DEEPLINK_ACTIVITY_HRV = "atheloapp://activityHrv"
internal const val DEEPLINK_ACTIVITY_HEART_RATE = "atheloapp://activityHeartRate"
internal const val DEEPLINK_ACTIVITY_EXERCISE = "atheloapp://activityExercise"
internal const val DEEPLINK_MY_WARDS = "atheloapp://myWards"
internal const val DEEPLINK_PATIENT_LIST = "atheloapp://patientList?initialFlow=%b"
internal const val DEEPLINK_CAREGIVER_LIST = "atheloapp://caregiverList?initialFlow=%b"
internal const val DEEPLINK_MESSAGES = "atheloapp://messages"
internal const val DEEPLINK_FIND_CAREGIVER = "atheloapp://findCaregiver"
internal const val DEEPLINK_SELECT_ROLE = "atheloapp://selectRole?initialFlow=%b"
internal const val LOST_CAREGIVER_ACCESS = "atheloapp://lost_caregiver_access"
internal const val MY_CAREGIVERS = "atheloapp://myCaregiversList"
internal const val SCHEDULE_MY_APPOINTMENT = "atheloapp://scheduleAppointment"

fun Fragment.routeToBackScreen() =
    requireActivity().onBackPressed()

internal fun Fragment.routeToHome() {
    navigateTo(MainNavigationDirections.actionOpenHomeScreen(Tabs.Home))
}

internal fun Fragment.routeToHomeWithTab(tab: Tabs) {
    navigateTo(MainNavigationDirections.actionOpenHomeScreen(tab))
}

internal fun Fragment.routeToAuthorization() {
    navigateTo(DEEPLINK_AUTHORIZATION, splashNavOptionWithPushAnimation)
}

internal fun Fragment.routeToMyProfile() {
    navigateTo(DEEPLINK_MY_PROFILE)
}

internal fun Fragment.routeToScheduleMyAppointment() {
    navigateTo(SCHEDULE_MY_APPOINTMENT)
}

internal fun Fragment.routeToLogout() {
    navigateTo(DEEPLINK_LOG_OUT)
}

internal fun Fragment.routeToEditProfile(enableEditMode: Boolean) {
    navigateTo(DEEPLINK_EDIT_PROFILE.format(enableEditMode))
}

internal fun Fragment.routeToChangePassword() {
    navigateTo(DEEPLINK_CHANGE_PASSWORD)
}

internal fun Fragment.routeToWebView(url: String) = openURL(url)

internal fun Fragment.routeToMail(email: String) = openMail(email)

internal fun Fragment.routeAskQuestion() = navigateTo(DEEPLINK_ASK_QUESTION)

internal fun Fragment.routeToFeedback(type: FeedbackScreenType) =
    navigateTo(DEEPLINK_FEEDBACK.format(type.ordinal), defaultNavOptionWithPushAnimation.build())

internal fun Fragment.routeToChatList() =
    (parentFragment?.parentFragment as? TabsFragment)?.routeToCommunity() ?: navigateTo(
        MainNavigationDirections.actionOpenHomeScreen(Tabs.Community)
    ) /*navigateTo(DEEPLINK_CHAT_LIST)*/

internal fun Fragment.routeToConnectFitbitScreen(showSkipButton: Boolean = false) =
    navigateTo(
        DEEPLINK_CONNECT_FITBIT.format(showSkipButton),
        if (showSkipButton) splashNavOptionWithPushAnimation else defaultNavOptionWithPushAnimation.build()
    )


internal fun Fragment.routeToCategoryFilterPicker(selected: List<Category>) =
    navigateTo(MainNavigationDirections.actionOpenCategoryFilter(selected.toTypedArray()))

internal fun Fragment.routeToNews() =
    (parentFragment?.parentFragment as? TabsFragment)?.routeToNews() ?: navigateTo(
        MainNavigationDirections.actionOpenHomeScreen(Tabs.News)
    )
/* navigateTo(DEEPLINK_NEWS)*/

internal fun Fragment.routeToNewsDetail(newsId: String) =
    navigateTo(DEEPLINK_NEWS_DETAIL.format(newsId))

internal fun Fragment.routeToChat(conversationId: Int, isGroup: Boolean = true) =
    navigateTo(DEEPLINK_CHAT.format(conversationId, isGroup))

internal fun Fragment.routeToTrackWellbeing() = navigateTo(DEEPLINK_WELLBEING)

internal fun Fragment.routeToAddSymptom(symptom: Symptom, date: Day) = navigateTo(
    WellbeingFragmentDirections.actionWellbeingFragmentToAddSymptomDialogFragment(
        symptom,
        date
    )
)

internal fun Fragment.routeToInfoSymptom(symptom: Symptom) = navigateTo(
    WellbeingFragmentDirections.actionWellbeingFragmentToInfoSymptomDialogFragment(symptom)
)

internal fun Fragment.routeToRecommendationSymptom(symptomId: Int) =
    navigateTo(DEEPLINK_RECOMMENDATION_SYMPTOM.format(symptomId))

internal fun Fragment.routeToMySymptoms() = navigateTo(DEEPLINK_MY_SYMPTOMS)

internal fun Fragment.routeToSymptomDetail(symptomId: Int) =
    navigateTo(DEEPLINK_SYMPTOM_DETAIL.format(symptomId))

internal fun Fragment.routeToPrivacyPolicyScreen() =
    navigateTo("atheloapp://info?type=PrivacyPolicy")/*navigateTo(SplashNavigationDirections.actionOpenAppInfoFragment(AppInfoScreenType.PrivacyPolicy))*/

internal fun Fragment.routeToTermsOfUseScreen() =
    navigateTo("atheloapp://info?type=TermsOfUse") /* navigateTo(SplashNavigationDirections.actionOpenAppInfoFragment(AppInfoScreenType.TermsOfUse))*/

internal fun Fragment.routeToAboutScreen() =
    navigateTo("atheloapp://info?type=About") /*navigateTo(SplashNavigationDirections.actionOpenAppInfoFragment(AppInfoScreenType.About))*/

internal fun Fragment.routeToSettings() = navigateTo(DEEPLINK_SETTINGS)

internal fun Fragment.routeToForgotPassword(email: String) = navigateTo(
    SignInWithEmailFragmentDirections.actionSignInWithEmailFragmentToForgotPasswordFragment(email)
)

internal fun Fragment.routeToAdditionalInformation() = navigateTo(
    "atheloapp://additionalInfo",
    splashNavOptionWithPushAnimation
)

internal fun Fragment.routeToTutorial() =
    navigateTo(SplashFragmentDirections.actionSplashFragmentToTutorialFragment())

internal fun Fragment.routeToSignIn() = navigateTo(
    AuthorizationLandingFragmentDirections.actionAuthorizationLandingFragmentToSignInWithEmailFragment()
)

internal fun Fragment.routeToSignUp() = navigateTo(
    AuthorizationLandingFragmentDirections.actionAuthorizationLandingFragmentToSignUpFragment()
)

internal fun Fragment.routeToDeeplink(deeplink: String) = navigateTo(deeplink)

internal fun Fragment.routeToSymptomChronologyScreen() = navigateTo(DEEPLINK_SYMPTOM_CHRONOLOGY)

internal fun Fragment.routeToMyDevice() = navigateTo(DEEPLINK_MY_DEVICE)

internal fun Fragment.routeToSleepDetails() = navigateTo(DEEPLINK_SLEEP_DETAILS)
internal fun Fragment.routeToInvitationCode() {
    navigateTo(DEEPLINK_INVITATION_CODE)
}

internal fun Fragment.routeToActivitySteps() = navigateTo(DEEPLINK_ACTIVITY_STEPS)

internal fun Fragment.routeToActivityHrv() = navigateTo(DEEPLINK_ACTIVITY_HRV)

internal fun Fragment.routeToActivityHeartRate() = navigateTo(DEEPLINK_ACTIVITY_HEART_RATE)

internal fun Fragment.routeToActivityExercise() = navigateTo(DEEPLINK_ACTIVITY_EXERCISE)
internal fun Fragment.routeToMyWards() = navigateTo(DEEPLINK_MY_WARDS)
internal fun Fragment.routeToCaregiverList(initialFlow: Boolean = false) =
    navigateTo(DEEPLINK_CAREGIVER_LIST.format(initialFlow))

internal fun Fragment.routeToPatientList(initialFlow: Boolean = false) =
    navigateTo(DEEPLINK_PATIENT_LIST.format(initialFlow))

internal fun Fragment.routeToMessages() = navigateTo(DEEPLINK_MESSAGES)
internal fun Fragment.routeToFindCaregiver() {
    navigateTo(DEEPLINK_FIND_CAREGIVER)
}

internal fun Fragment.routeToSelectRole(initialFlow: Boolean = false) = navigateTo(
    DEEPLINK_SELECT_ROLE.format(initialFlow),
    if (initialFlow) splashNavOptionWithPushAnimation else defaultNavOptionWithPushAnimation.build()
)

internal fun Fragment.routeToLostCaregiverAccess() = navigateTo(LOST_CAREGIVER_ACCESS)
internal fun Fragment.routeToMyCaregivers() = navigateTo(MY_CAREGIVERS)

internal fun Fragment.navigateToInAppBrowser(url: String) {
    if (!URLUtil.isValidUrl(url)) {
        Toast.makeText(requireContext(),requireContext().getString(R.string.invalid_url, url) ,Toast.LENGTH_SHORT).show()
        return
    }
    CustomTabsIntent.Builder().build().launchUrl(requireContext(), Uri.parse(url))
}
internal fun navigateToInAppBrowser(context: Context, url: String) {
    if (!URLUtil.isValidUrl(url)) {
        Toast.makeText(context,context.getString(R.string.invalid_url, url) ,Toast.LENGTH_SHORT).show()
        return
    }
    CustomTabsIntent.Builder().build().launchUrl(context, Uri.parse(url))
}
