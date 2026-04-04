package com.olamkatan.ui.counting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.olamkatan.audio.Sfx
import com.olamkatan.theme.CloudWhite
import com.olamkatan.ui.common.LocalLanguage
import com.olamkatan.ui.common.LocalSfxPlayer
import com.olamkatan.ui.common.LocalVoicePlayer
import com.olamkatan.ui.components.ConfettiOverlay
import com.olamkatan.ui.components.GameBackground
import com.olamkatan.ui.components.GameTopBar
import com.olamkatan.ui.components.RoundCompleteSheet
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CountingGardenScreen(
    onBack: () -> Unit,
    viewModel: CountingGardenViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val language = LocalLanguage.current
    val voicePlayer = LocalVoicePlayer.current
    val sfxPlayer = LocalSfxPlayer.current
    val scope = rememberCoroutineScope()
    var showRoundSuccess by remember { mutableStateOf(false) }

    if (state.isLoading) return

    LaunchedEffect(state.roundIndex) {
        delay(500)
        voicePlayer.play("voice_prompt_count_along", language)
    }

    GameBackground(
        colors = listOf(Color(0xFFC8E6C9), Color(0xFFA5D6A7), Color(0xFF81C784))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GameTopBar(
                onBack = onBack,
                roundIndex = state.roundIndex,
                totalRounds = state.totalRounds,
                starsEarned = state.totalStarsEarned
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Big number display
            Text(
                text = "${state.currentCount}",
                fontSize = 80.sp,
                color = CloudWhite,
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Garden area — shows placed items
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF4CAF50).copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(state.currentCount) { i ->
                        AnimatedVisibility(
                            visible = true,
                            enter = scaleIn() + fadeIn()
                        ) {
                            Text(
                                text = state.currentRound.itemEmoji,
                                fontSize = 48.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tap button to add items
            if (!state.roundComplete) {
                val interactionSource = remember { MutableInteractionSource() }
                val isPressed by interactionSource.collectIsPressedAsState()
                val tapScale by animateFloatAsState(
                    targetValue = if (isPressed) 0.85f else 1f,
                    animationSpec = spring(dampingRatio = 0.4f, stiffness = 300f),
                    label = "tap_scale"
                )

                Box(
                    modifier = Modifier
                        .graphicsLayer { scaleX = tapScale; scaleY = tapScale }
                        .size(120.dp)
                        .shadow(8.dp, CircleShape)
                        .clip(CircleShape)
                        .background(state.currentRound.itemColor)
                        .clickable(interactionSource = interactionSource, indication = null) {
                            val newCount = viewModel.onItemTapped()
                            sfxPlayer.play(Sfx.POP)
                            if (newCount > 0) {
                                voicePlayer.play("voice_number_$newCount", language)
                            } else {
                                // Round complete
                                voicePlayer.play("voice_number_${state.currentRound.targetNumber}", language)
                                sfxPlayer.play(Sfx.SUCCESS)
                                showRoundSuccess = true
                                scope.launch {
                                    delay(1500)
                                    showRoundSuccess = false
                                    viewModel.nextRound()
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = state.currentRound.itemEmoji, fontSize = 56.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        ConfettiOverlay(show = showRoundSuccess)

        if (state.sessionComplete) {
            RoundCompleteSheet(
                starsEarned = state.totalStarsEarned,
                onContinue = onBack,
                onHome = onBack
            )
        }
    }
}
