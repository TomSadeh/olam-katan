package com.olamkatan

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.olamkatan.audio.MusicPlayer
import com.olamkatan.audio.Sfx
import com.olamkatan.audio.SfxPlayer
import com.olamkatan.audio.TtsManager
import com.olamkatan.audio.VoicePlayer
import com.olamkatan.data.ProgressRepository
import com.olamkatan.data.SettingsRepository
import com.olamkatan.data.models.Language
import com.olamkatan.navigation.OlamKatanNavGraph
import com.olamkatan.theme.OlamKatanTheme
import com.olamkatan.ui.common.AppViewModel
import com.olamkatan.ui.common.LocalAppSettings
import com.olamkatan.ui.common.LocalLanguage
import com.olamkatan.ui.common.LocalMusicPlayer
import com.olamkatan.ui.common.LocalSfxPlayer
import com.olamkatan.ui.common.LocalTtsManager
import com.olamkatan.ui.common.LocalVoicePlayer
import com.olamkatan.ui.components.TimeLimitOverlay
import kotlinx.coroutines.delay

@Composable
fun OlamKatanApp(
    ttsManager: TtsManager,
    sfxPlayer: SfxPlayer,
    musicPlayer: MusicPlayer,
    voicePlayer: VoicePlayer,
    settingsRepository: SettingsRepository,
    progressRepository: ProgressRepository
) {
    val appViewModel: AppViewModel = viewModel(
        factory = AppViewModel.Factory(settingsRepository, progressRepository)
    )
    val settings by appViewModel.settings.collectAsStateWithLifecycle()
    val language = settings.language
    val layoutDirection = when (language) {
        Language.HEBREW -> LayoutDirection.Rtl
        Language.ENGLISH -> LayoutDirection.Ltr
    }

    LaunchedEffect(Unit) {
        Sfx.preloadAll(sfxPlayer)
        musicPlayer.play(R.raw.music_hub)
    }

    LaunchedEffect(language) {
        ttsManager.setLanguage(language)
    }

    LaunchedEffect(settings.soundEnabled) {
        sfxPlayer.enabled = settings.soundEnabled
        ttsManager.enabled = settings.soundEnabled
        voicePlayer.enabled = settings.soundEnabled
    }

    LaunchedEffect(settings.musicEnabled) {
        musicPlayer.setEnabled(settings.musicEnabled)
    }

    // Time limit tracking
    var showTimeLimit by remember { mutableStateOf(false) }
    var sessionStartTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(settings.timeLimitMinutes) {
        if (settings.timeLimitMinutes > 0) {
            sessionStartTime = System.currentTimeMillis()
            showTimeLimit = false
            while (true) {
                delay(30_000)
                val elapsed = System.currentTimeMillis() - sessionStartTime
                val limitMs = settings.timeLimitMinutes * 60_000L
                if (elapsed >= limitMs) {
                    showTimeLimit = true
                    break
                }
            }
        }
    }

    OlamKatanTheme {
        CompositionLocalProvider(
            LocalLanguage provides language,
            LocalAppSettings provides settings,
            LocalTtsManager provides ttsManager,
            LocalSfxPlayer provides sfxPlayer,
            LocalMusicPlayer provides musicPlayer,
            LocalVoicePlayer provides voicePlayer,
            LocalLayoutDirection provides layoutDirection
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                val navController = rememberNavController()
                OlamKatanNavGraph(navController = navController)

                if (showTimeLimit) {
                    TimeLimitOverlay(
                        onDismiss = {
                            showTimeLimit = false
                            sessionStartTime = System.currentTimeMillis()
                        }
                    )
                }
            }
        }
    }
}
