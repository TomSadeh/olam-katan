package com.olamkatan.data.models

data class AppSettings(
    val language: Language = Language.HEBREW,
    val soundEnabled: Boolean = true,
    val musicEnabled: Boolean = true,
    val timeLimitMinutes: Int = 0 // 0 = no limit
)

enum class Language {
    HEBREW, ENGLISH
}
