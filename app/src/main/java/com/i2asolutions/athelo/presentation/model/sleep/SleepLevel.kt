package com.i2asolutions.athelo.presentation.model.sleep

enum class SleepLevel(val wsName: String) {
    Deep("deep"), Light("light"), Rem("rem"), Awake("wake");

    companion object {
        fun fromWsName(name: String) = values().firstOrNull { it.wsName == name } ?: Awake
    }
}