package com.olamkatan.ui.common

import androidx.compose.runtime.compositionLocalOf
import com.olamkatan.audio.MusicPlayer
import com.olamkatan.audio.SfxPlayer
import com.olamkatan.audio.TtsManager
import com.olamkatan.audio.VoicePlayer
import com.olamkatan.data.models.AppSettings
import com.olamkatan.data.models.Gender
import com.olamkatan.data.models.Language

val LocalLanguage = compositionLocalOf { Language.HEBREW }
val LocalGender = compositionLocalOf { Gender.GIRL }
val LocalAppSettings = compositionLocalOf { AppSettings() }
val LocalTtsManager = compositionLocalOf<TtsManager> { error("No TtsManager provided") }
val LocalSfxPlayer = compositionLocalOf<SfxPlayer> { error("No SfxPlayer provided") }
val LocalMusicPlayer = compositionLocalOf<MusicPlayer> { error("No MusicPlayer provided") }
val LocalVoicePlayer = compositionLocalOf<VoicePlayer> { error("No VoicePlayer provided") }
