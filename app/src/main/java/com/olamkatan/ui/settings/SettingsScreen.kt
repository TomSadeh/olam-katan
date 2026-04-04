package com.olamkatan.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.olamkatan.data.models.BilingualText
import com.olamkatan.data.models.Language
import com.olamkatan.theme.CloudWhite
import com.olamkatan.theme.EarthBrown
import com.olamkatan.theme.GrassGreen
import com.olamkatan.theme.SkyBlue
import com.olamkatan.theme.SunYellow
import com.olamkatan.ui.common.LocalAppSettings
import com.olamkatan.ui.common.LocalLanguage

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    settingsViewModel: SettingsViewModel = viewModel()
) {
    val language = LocalLanguage.current
    val settings = LocalAppSettings.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SkyBlue)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = BilingualText("הגדרות", "Settings").resolve(language),
                style = MaterialTheme.typography.headlineLarge,
                color = CloudWhite
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(CloudWhite)
                    .padding(24.dp)
                    .width(400.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SettingsToggleRow(
                    label = BilingualText("שפה", "Language").resolve(language),
                    emoji = "\uD83C\uDF10",
                    checked = language == Language.HEBREW,
                    labelOn = "עברית",
                    labelOff = "English",
                    onToggle = { settingsViewModel.toggleLanguage(language) }
                )

                SettingsToggleRow(
                    label = BilingualText("צלילים", "Sounds").resolve(language),
                    emoji = "\uD83D\uDD0A",
                    checked = settings.soundEnabled,
                    labelOn = BilingualText("פועל", "On").resolve(language),
                    labelOff = BilingualText("כבוי", "Off").resolve(language),
                    onToggle = { settingsViewModel.toggleSound(settings.soundEnabled) }
                )

                SettingsToggleRow(
                    label = BilingualText("מוזיקה", "Music").resolve(language),
                    emoji = "\uD83C\uDFB5",
                    checked = settings.musicEnabled,
                    labelOn = BilingualText("פועל", "On").resolve(language),
                    labelOff = BilingualText("כבוי", "Off").resolve(language),
                    onToggle = { settingsViewModel.toggleMusic(settings.musicEnabled) }
                )

                // Time limit row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "⏰", fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = BilingualText("הגבלת זמן", "Time Limit").resolve(language),
                            style = MaterialTheme.typography.titleLarge,
                            color = EarthBrown
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(GrassGreen.copy(alpha = 0.2f))
                            .clickable { settingsViewModel.cycleTimeLimit(settings.timeLimitMinutes) }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = if (settings.timeLimitMinutes == 0)
                                BilingualText("ללא", "Off").resolve(language)
                            else
                                "${settings.timeLimitMinutes} ${BilingualText("דקות", "min").resolve(language)}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = EarthBrown
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .size(120.dp, 60.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(SunYellow)
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = BilingualText("חזרה", "Back").resolve(language),
                    style = MaterialTheme.typography.titleLarge,
                    color = EarthBrown,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun SettingsToggleRow(
    label: String,
    emoji: String,
    checked: Boolean,
    labelOn: String,
    labelOff: String,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = emoji, fontSize = 28.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.titleLarge,
                color = EarthBrown
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = if (checked) labelOn else labelOff,
                style = MaterialTheme.typography.bodyLarge,
                color = EarthBrown
            )
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = checked,
                onCheckedChange = { onToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = CloudWhite,
                    checkedTrackColor = GrassGreen
                )
            )
        }
    }
}
