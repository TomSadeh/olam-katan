package com.olamkatan.ui.common

import androidx.compose.runtime.compositionLocalOf
import com.olamkatan.audio.SfxPlayer
import com.olamkatan.audio.TtsManager
import com.olamkatan.data.models.AppSettings
import com.olamkatan.data.models.Language

val LocalLanguage = compositionLocalOf { Language.HEBREW }
val LocalAppSettings = compositionLocalOf { AppSettings() }
val LocalTtsManager = compositionLocalOf<TtsManager> { error("No TtsManager provided") }
val LocalSfxPlayer = compositionLocalOf<SfxPlayer> { error("No SfxPlayer provided") }
