package com.olamkatan.ui.animals

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.olamkatan.audio.Sfx
import com.olamkatan.theme.GrassGreen
import com.olamkatan.ui.animals.components.AnimalDisplay
import com.olamkatan.ui.animals.components.FoodItem
import com.olamkatan.ui.common.LocalLanguage
import com.olamkatan.ui.common.LocalSfxPlayer
import com.olamkatan.ui.common.LocalVoicePlayer
import com.olamkatan.ui.components.ConfettiOverlay
import com.olamkatan.ui.components.DragDropState
import com.olamkatan.ui.components.DropZone
import com.olamkatan.ui.components.GameBackground
import com.olamkatan.ui.components.GameTopBar
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
    val voicePlayer = LocalVoicePlayer.current
    val sfxPlayer = LocalSfxPlayer.current
    val scope = rememberCoroutineScope()
    val dragDropState = remember { DragDropState() }

    var showRoundSuccess by remember { mutableStateOf(false) }

    if (state.isLoading) return

    LaunchedEffect(state.roundIndex) {
        delay(500)
        voicePlayer.playSequence(
            listOf("voice_prompt_whos_hungry", "voice_animal_${state.currentAnimal.id}"),
            language
        )
    }

    CompositionLocalProvider(LocalDragDropState provides dragDropState) {
        GameBackground(
            colors = listOf(Color(0xFF81C784), Color(0xFF66BB6A), Color(0xFF4CAF50), Color(0xFF388E3C))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GameTopBar(
                    onBack = onBack,
                    roundIndex = state.roundIndex,
                    totalRounds = state.totalRounds,
                    starsEarned = state.totalStarsEarned
                )

                Spacer(modifier = Modifier.height(8.dp))

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
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
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
                                                animalViewModel.nextRound()
                                            }
                                        } else {
                                            sfxPlayer.play(Sfx.WRONG_GENTLE)
                                            voicePlayer.play("voice_prompt_try_again", language)
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

            ConfettiOverlay(show = showRoundSuccess)

            if (state.sessionComplete) {
                RoundCompleteSheet(
                    starsEarned = state.totalStarsEarned,
                    onContinue = { onBack() },
                    onHome = onBack
                )
            }
        }
    }
}
