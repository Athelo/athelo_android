package com.athelohealth.mobile.presentation.ui.share.appointment.joinAppointment

import android.Manifest
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.BuildConfig
import com.athelohealth.mobile.R
import com.athelohealth.mobile.databinding.AppointmentMeetingRoomBinding
import com.athelohealth.mobile.extensions.debugPrint
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseFragment
import com.athelohealth.mobile.utils.routeToBackScreen
import com.opentok.android.BaseVideoRenderer
import com.opentok.android.OpentokError
import com.opentok.android.Publisher
import com.opentok.android.PublisherKit
import com.opentok.android.Session
import com.opentok.android.Stream
import com.opentok.android.Subscriber
import com.opentok.android.SubscriberKit
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks


@AndroidEntryPoint
class JoinAppointmentFragment : BaseFragment<JoinAppointmentViewModel>(),
    PermissionCallbacks {

    override val viewModel: JoinAppointmentViewModel by viewModels()

    private var session: Session? = null
    private var publisher: Publisher? = null
    private var subscriber: Subscriber? = null
    private val permissions = arrayOf(
        Manifest.permission.INTERNET,
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )

    private lateinit var _binding: AppointmentMeetingRoomBinding
    private val binding get() = _binding

    override fun createContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AppointmentMeetingRoomBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions()
        binding.ivBack.setOnClickListener {
            routeToBackScreen()
        }
    }

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) { event ->
            when (event) {
                is JoinAppointmentEffect.BackPressEffect -> routeToBackScreen()
                is JoinAppointmentEffect.JoinAppointmentTokenEffect -> {
                    if (event.token.isNotEmpty()) {
                        initializeSession(
                            viewModel.sessionId.value,
                            event.token
                        )
                    }
                }
            }
        }
    }

    @AfterPermissionGranted(PERMISSIONS_REQUEST_CODE)
    private fun requestPermissions() {
        if (EasyPermissions.hasPermissions(requireContext(), *permissions)) {
            viewModel.getToken()
        } else {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.rationale_video_app),
                PERMISSIONS_REQUEST_CODE,
                *permissions
            )
        }
    }

    private val publisherListener: PublisherKit.PublisherListener = object :
        PublisherKit.PublisherListener {
        override fun onStreamCreated(publisherKit: PublisherKit, stream: Stream) {
            debugPrint("onStreamCreated: Publisher Stream Created. Own stream ${stream.streamId}")
        }

        override fun onStreamDestroyed(publisherKit: PublisherKit, stream: Stream) {
            debugPrint("onStreamDestroyed: Publisher Stream Destroyed. Own stream ${stream.streamId}")
        }

        override fun onError(publisherKit: PublisherKit, opentokError: OpentokError) {
            viewModel.sendBaseEvent(
                BaseEvent.DisplayError(
                    "PublisherKit onError: ${opentokError.message}"
                )
            )
        }
    }

    private val sessionListener: Session.SessionListener = object : Session.SessionListener {
        override fun onConnected(session: Session) {
            debugPrint("onConnected: Connected to session: ${session.sessionId}")
            publisher = Publisher.Builder(requireContext()).build()
            publisher?.setPublisherListener(publisherListener)
            publisher?.renderer?.setStyle(
                BaseVideoRenderer.STYLE_VIDEO_SCALE,
                BaseVideoRenderer.STYLE_VIDEO_FILL
            )

//            viewModel.setPublisherView(publisher?.view)
            binding.publisherContainer.addView(publisher?.view)

            if (publisher?.view is GLSurfaceView) {
                (publisher?.view as GLSurfaceView).setZOrderOnTop(true)
            }
            session.publish(publisher)
        }

        override fun onDisconnected(session: Session) {
            debugPrint("onDisconnected: Disconnected from session: ${session.sessionId}")
        }

        override fun onStreamReceived(session: Session, stream: Stream) {
            debugPrint("onStreamReceived: New Stream Received ${stream.streamId} in session: ${session.sessionId}")
            if (subscriber == null) {
                subscriber = Subscriber.Builder(requireContext(), stream).build().also {
                    it.renderer?.setStyle(
                        BaseVideoRenderer.STYLE_VIDEO_SCALE,
                        BaseVideoRenderer.STYLE_VIDEO_FILL
                    )

                    it.setSubscriberListener(subscriberListener)
                }

                session.subscribe(subscriber)
//                viewModel.setSubscriberView(publisher?.view)
                binding.subscriberContainer.addView(subscriber?.view)
            }
        }

        override fun onStreamDropped(session: Session, stream: Stream) {
            debugPrint("onStreamDropped: Stream Dropped: ${stream.streamId} in session: ${session.sessionId}")
            if (subscriber != null) {
                subscriber = null
//                viewModel.removeAllViewOfSubscriber()
                binding.subscriberContainer.removeAllViews()
            }
        }

        override fun onError(session: Session, opentokError: OpentokError) {
            viewModel.sendBaseEvent(
                BaseEvent.DisplayError(
                    "Session error: ${opentokError.message}"
                )
            )
        }
    }
    var subscriberListener: SubscriberKit.SubscriberListener = object :
        SubscriberKit.SubscriberListener {
        override fun onConnected(subscriberKit: SubscriberKit) {
            debugPrint("onConnected: Subscriber connected. Stream: ${subscriberKit.stream.streamId}")
        }

        override fun onDisconnected(subscriberKit: SubscriberKit) {
            debugPrint("onDisconnected: Subscriber disconnected. Stream: ${subscriberKit.stream.streamId}")
        }

        override fun onError(subscriberKit: SubscriberKit, opentokError: OpentokError) {
            viewModel.sendBaseEvent(
                BaseEvent.DisplayError(
                    "SubscriberKit onError: ${opentokError.message}"
                )
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        debugPrint("onPermissionsGranted:$requestCode: $perms")
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        debugPrint("onPermissionsDenied: $requestCode: $perms")
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    private fun initializeSession(sessionId: String, token: String) {

        /*
        The context used depends on the specific use case, but usually, it is desired for the session to
        live outside of the Activity e.g: live between activities. For a production applications,
        it's convenient to use Application context instead of Activity context.
         */
        session = Session.Builder(requireContext(), BuildConfig.OPEN_TOK_API_KEY, sessionId).build().also {
            it.setSessionListener(sessionListener)
            it.connect(token)
        }
    }

    override fun onResume() {
        super.onResume()
        session?.onResume()
    }

    override fun onPause() {
        super.onPause()
        session?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        session?.disconnect()
    }

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 124
    }
}