package com.athelohealth.mobile.presentation.model.activity

sealed interface ActivityScreen {
    class Steps(val value: String, val data: List<Float>)
    class Activity(val value: String, val data: List<Float>)
    class HeartRate(val value: String, val data: List<Float>)
    class HeartRateVariability(val value: String, val data: List<Float>)
}