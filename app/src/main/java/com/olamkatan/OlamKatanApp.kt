package com.olamkatan

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.olamkatan.audio.Sfx
import com.olamkatan.audio.SfxPlayer
import com.olamkatan.audio.TtsManager
import com.olamkatan.data.ProgressRepository
import com.olamkatan.data.SettingsRepository
import com.olamkatan.data.models.Language
import com.olamkatan.navigation.OlamKatanNavGraph
import com.olamkatan.theme.OlamKatanTheme
import com.olamkatan.ui.common.AppViewModel
import com.olamkatan.ui.common.LocalAppSettings
import com.olamkatan.ui.common.LocalLanguage
import com.olamkatan.ui.common.LocalSfxPlayer
import com.olamkatan.ui.common.LocalTtsManager

@Composable
fun OlamKatanApp(
    ttsManager: TtsManager,
    sfxPlayer: SfxPlayer,
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
    }

    LaunchedEffect(language) {
        ttsManager.setLanguage(language)
    }

    LaunchedEffect(settings.soundEnabled) {
        sfxPlayer.enabled = settings.soundEnabled
        ttsManager.enabled = settings.soundEnabled
    }

    OlamKatanTheme {
        CompositionLocalProvider(
            LocalLanguage provides language,
            LocalAppSettings provides settings,
            LocalTtsManager provides ttsManager,
            LocalSfxPlayer provides sfxPlayer,
            LocalLayoutDirection provides layoutDirection
        ) {
            val navController = rememberNavController()
            OlamKatanNavGraph(navController = navController)
        }
    }
}
