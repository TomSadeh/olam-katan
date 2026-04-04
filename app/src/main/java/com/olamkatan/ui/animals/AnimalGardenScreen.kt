package com.olamkatan.ui.animals

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.olamkatan.audio.Sfx
import com.olamkatan.data.models.BilingualText
import com.olamkatan.theme.EarthBrown
import com.olamkatan.theme.GrassGreen
import com.olamkatan.ui.animals.components.AnimalDisplay
import com.olamkatan.ui.animals.components.FoodItem
import com.olamkatan.ui.common.LocalLanguage
import com.olamkatan.ui.common.LocalSfxPlayer
import com.olamkatan.ui.common.LocalTtsManager
import com.olamkatan.ui.components.ConfettiOverlay
import com.olamkatan.ui.components.DragDropState
import com.olamkatan.ui.components.DropZone
import com.olamkatan.ui.components.LocalDragDropState
import com.olamkatan.ui.components.RoundCompleteSheet
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AnimalGardenScreen(
    onBack: () -> Unit,
    animalViewModel: AnimalGardenViewModel = viewModel()
) {
    val state by animalViewModel.state.collectAsStateWithLifecycle()
    val language = LocalLanguage.current
    val ttsManager = LocalTtsManager.current
    val sfxPlayer = LocalSfxPlayer.current
    val scope = rememberCoroutineScope()
    val dragDropState = remember { DragDropState() }

    var showRoundSuccess by remember { mutableStateOf(false) }

    // Speak animal name when a new round starts
    LaunchedEffect(state.roundIndex) {
        delay(500)
        ttsManager.speakBilingual(
            BilingualText(
                "מי רוצה אוכל? זה ${state.currentAnimal.name.he}!",
                "Who's hungry? It's a ${state.currentAnimal.name.en}!"
            ),
            language
        )
    }

    CompositionLocalProvider(LocalDragDropState provides dragDropState) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF81C784), Color(0xFF4CAF50), Color(0xFF388E3C))
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top bar: back button + round dots
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(EarthBrown.copy(alpha = 0.7f))
                            .clickable { onBack() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "◀",
                            fontSize = 24.sp,
                            color = Color.White
                        )
                    }

                    // Round indicator dots
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        repeat(state.totalRounds) { i ->
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            i < state.roundIndex -> Color(0xFFFFD700)
                                            i == state.roundIndex -> Color.White
                                            else -> Color.White.copy(alpha = 0.4f)
                                        }
                                    )
                            )
                        }
                    }

                    // Star count for this session
                    Text(
                        text = "⭐ ${state.totalStarsEarned}",
                        fontSize = 24.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Animal in center with drop zone
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    DropZone(
                        zoneId = state.currentAnimal.id,
                        highlightColor = GrassGreen
                    ) {
                        AnimalDisplay(animal = state.currentAnimal)
                    }
                }

                // Food tray at bottom
                AnimatedVisibility(
                    visible = !state.roundComplete,
                    enter = fadeIn() + scaleIn()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        state.foodChoices.forEach { food ->
                            FoodItem(
                                food = food,
                                onDropped = { dropTargetId ->
                                    if (dropTargetId == state.currentAnimal.id) {
                                        val correct = animalViewModel.onFoodDropped(food.id)
                                        if (correct) {
                                            sfxPlayer.play(Sfx.SUCCESS)
                                            showRoundSuccess = true
                                            scope.launch {
                                                delay(1500)
                                                showRoundSuccess = false
                                                if (state.roundIndex + 1 < state.totalRounds) {
                                                    animalViewModel.nextRound()
                                                }
                                            }
                                        } else {
                                            sfxPlayer.play(Sfx.WRONG_GENTLE)
                                            ttsManager.speakBilingual(
                                                BilingualText("לא, ננסה שוב!", "Nope, try again!"),
                                                language
                                            )
                                        }
                                    } else {
                                        sfxPlayer.play(Sfx.WRONG_GENTLE)
                                    }
                                }
                            )
                        }
                    }
                }
            }

            // Success confetti per round
            ConfettiOverlay(show = showRoundSuccess)

            // Session complete overlay
            if (state.sessionComplete) {
                RoundCompleteSheet(
                    starsEarned = state.totalStarsEarned,
                    onContinue = {
                        // Restart
                        // For now, just go back
                        onBack()
                    },
                    onHome = onBack
                )
            }
        }
    }
}
