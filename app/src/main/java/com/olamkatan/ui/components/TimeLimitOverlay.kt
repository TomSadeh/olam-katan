package com.olamkatan.ui.components

import androidx.compose.foundation.background
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

@Composable
fun TimeLimitOverlay(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val language = LocalLanguage.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF81D4FA)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Text("\uD83D\uDC4B", fontSize = 80.sp)

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = BilingualText("הגיע הזמן להפסקה!", "Time for a break!").resolve(language),
                style = MaterialTheme.typography.headlineLarge,
                color = CloudWhite
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = BilingualText("נתראה בקרוב! 😊", "See you soon! 😊").resolve(language),
                style = MaterialTheme.typography.headlineMedium,
                color = CloudWhite
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Long press to dismiss (parent only)
            LongPressGate(
                holdDurationMs = 2000L,
                onUnlocked = onDismiss
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            Color.White.copy(alpha = 0.3f),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = BilingualText("לחץ ארוך לסגירה", "Long press to dismiss").resolve(language),
                        style = MaterialTheme.typography.bodyLarge,
                        color = CloudWhite
                    )
                }
            }
        }
    }
}
