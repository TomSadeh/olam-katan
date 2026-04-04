package com.olamkatan.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.olamkatan.OlamKatanApplication
import com.olamkatan.data.models.Language
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val settingsRepository = (application as OlamKatanApplication).settingsRepository

    fun toggleLanguage(current: Language) {
        viewModelScope.launch {
            val next = when (current) {
                Language.HEBREW -> Language.ENGLISH
                Language.ENGLISH -> Language.HEBREW
            }
            settingsRepository.updateLanguage(next)
        }
    }

    fun toggleSound(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.updateSound(!enabled) }
    }

    fun toggleMusic(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.updateMusic(!enabled) }
    }
}
