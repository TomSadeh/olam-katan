package com.olamkatan.data.models

data class AppSettings(
    val language: Language = Language.HEBREW,
    val gender: Gender = Gender.GIRL,
    val soundEnabled: Boolean = true,
    val musicEnabled: Boolean = true,
    val timeLimitMinutes: Int = 0, // 0 = no limit
    val companionEnabled: Boolean = true,
    val companionAddress: String = DEFAULT_COMPANION_ADDRESS
)

/** The family PC running brain/doorbell.py, on the home network. */
const val DEFAULT_COMPANION_ADDRESS = "192.168.1.151:8790"

enum class Language {
    HEBREW, ENGLISH
}

enum class Gender {
    BOY, GIRL
}
