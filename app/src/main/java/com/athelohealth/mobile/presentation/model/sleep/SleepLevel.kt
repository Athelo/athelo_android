package com.athelohealth.mobile.presentation.model.sleep

enum class SleepLevel(val wsName: String) {
    Deep("deep"), Light("light"), Rem("rem"), Awake("wake");

    companion object {
        fun fromWsName(name: String) = values().firstOrNull { it.wsName == name } ?: Awake
    }
}