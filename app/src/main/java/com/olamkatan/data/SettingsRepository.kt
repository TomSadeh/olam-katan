package com.olamkatan.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.olamkatan.data.models.AppSettings
import com.olamkatan.data.models.Gender
import com.olamkatan.data.models.Language
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepository(private val context: Context) {

    private object Keys {
        val LANGUAGE = stringPreferencesKey("language")
        val GENDER = stringPreferencesKey("gender")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val MUSIC_ENABLED = booleanPreferencesKey("music_enabled")
        val TIME_LIMIT = intPreferencesKey("time_limit_minutes")
    }

    val settings: Flow<AppSettings> = context.settingsDataStore.data.map { prefs ->
        AppSettings(
            language = Language.valueOf(prefs[Keys.LANGUAGE] ?: Language.HEBREW.name),
            gender = Gender.valueOf(prefs[Keys.GENDER] ?: Gender.GIRL.name),
            soundEnabled = prefs[Keys.SOUND_ENABLED] ?: true,
            musicEnabled = prefs[Keys.MUSIC_ENABLED] ?: true,
            timeLimitMinutes = prefs[Keys.TIME_LIMIT] ?: 0
        )
    }

    suspend fun updateLanguage(language: Language) {
        context.settingsDataStore.edit { it[Keys.LANGUAGE] = language.name }
    }

    suspend fun updateSound(enabled: Boolean) {
        context.settingsDataStore.edit { it[Keys.SOUND_ENABLED] = enabled }
    }

    suspend fun updateMusic(enabled: Boolean) {
        context.settingsDataStore.edit { it[Keys.MUSIC_ENABLED] = enabled }
    }

    suspend fun updateTimeLimit(minutes: Int) {
        context.settingsDataStore.edit { it[Keys.TIME_LIMIT] = minutes }
    }

    suspend fun updateGender(gender: Gender) {
        context.settingsDataStore.edit { it[Keys.GENDER] = gender.name }
    }
}
