package com.olamkatan.ui.hub

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.olamkatan.data.models.BilingualText
import com.olamkatan.theme.CloudWhite
import com.olamkatan.ui.common.LocalLanguage
import com.olamkatan.ui.components.ConfettiOverlay

@Composable
fun UnlockCeremony(
    unlockedIslands: List<IslandDef>,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val language = LocalLanguage.current

    AnimatedVisibility(
        visible = unlockedIslands.isNotEmpty(),
        enter = fadeIn() + scaleIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            ConfettiOverlay(show = true)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(32.dp)
            ) {
                Text(
                    text = "\uD83D\uDD13",
                    fontSize = 64.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = BilingualText("אי חדש נפתח!", "New island unlocked!").resolve(language),
                    style = MaterialTheme.typography.headlineLarge,
                    color = CloudWhite
                )
                Spacer(modifier = Modifier.height(8.dp))
                unlockedIslands.forEach { island ->
                    Text(
                        text = "${island.emoji} ${island.name.resolve(language)}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = CloudWhite
                    )
                }
            }
        }
    }
}
