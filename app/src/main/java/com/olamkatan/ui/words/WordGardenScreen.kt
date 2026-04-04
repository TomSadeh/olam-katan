package com.olamkatan.ui.words

import android.app.Application
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.olamkatan.OlamKatanApplication
import com.olamkatan.audio.Sfx
import com.olamkatan.data.content.LetterDef
import com.olamkatan.data.content.englishLetters
import com.olamkatan.data.content.hebrewLetters
import com.olamkatan.data.models.BilingualText
import com.olamkatan.data.models.Language
import com.olamkatan.theme.CloudWhite
import com.olamkatan.theme.EarthBrown
import com.olamkatan.ui.common.LocalLanguage
import com.olamkatan.ui.common.LocalSfxPlayer
import com.olamkatan.ui.common.LocalVoicePlayer
import com.olamkatan.ui.components.ConfettiOverlay
import com.olamkatan.ui.components.RoundCompleteSheet
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WordState(
    val targetLetter: LetterDef = hebrewLetters[0],
    val letterChoices: List<LetterDef> = emptyList(),
    val roundIndex: Int = 0,
    val totalRounds: Int = 6,
    val showSuccess: Boolean = false,
    val sessionComplete: Boolean = false,
    val totalStarsEarned: Int = 0
)

class WordGardenViewModel(application: Application) : AndroidViewModel(application) {
    private val progressRepository = (application as OlamKatanApplication).progressRepository
    private val _state = MutableStateFlow(WordState())
    val state: StateFlow<WordState> = _state.asStateFlow()
    private var letters = emptyList<LetterDef>()
    private var currentLanguage = Language.HEBREW

    fun initialize(language: Language) {
        currentLanguage = language
        letters = when (language) {
            Language.HEBREW -> hebrewLetters
            Language.ENGLISH -> englishLetters
        }
        startRound(0)
    }

    private fun startRound(index: Int) {
        if (index >= letters.size) {
            _state.value = _state.value.copy(sessionComplete = true)
            return
        }
        val target = letters[index]
        val distractors = letters.filter { it.id != target.id }.shuffled().take(2)
        _state.value = WordState(
            targetLetter = target,
            letterChoices = (listOf(target) + distractors).shuffled(),
            roundIndex = index,
            totalRounds = letters.size,
            totalStarsEarned = _state.value.totalStarsEarned
        )
    }

    fun onLetterTapped(letterId: String): Boolean {
        val isCorrect = letterId == _state.value.targetLetter.id
        if (isCorrect) {
            val newStars = _state.value.totalStarsEarned + 1
            _state.value = _state.value.copy(showSuccess = true, totalStarsEarned = newStars)
            viewModelScope.launch { progressRepository.addStars(1) }
        }
        return isCorrect
    }

    fun nextRound() {
        startRound(_state.value.roundIndex + 1)
    }
}

@Composable
fun WordGardenScreen(onBack: () -> Unit, viewModel: WordGardenViewModel = viewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val language = LocalLanguage.current
    val voicePlayer = LocalVoicePlayer.current
    val sfxPlayer = LocalSfxPlayer.current
    val scope = rememberCoroutineScope()
    var showRoundSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(language) {
        viewModel.initialize(language)
    }

    LaunchedEffect(state.roundIndex) {
        if (state.letterChoices.isNotEmpty()) {
            delay(500)
            voicePlayer.play("voice_letter_${state.targetLetter.id}", language)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(
            Brush.verticalGradient(listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9), Color(0xFFA5D6A7)))
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Box(
                    modifier = Modifier.size(56.dp).clip(CircleShape).background(EarthBrown.copy(alpha = 0.7f)).clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) { Text("◀", fontSize = 24.sp, color = Color.White) }
                Text(BilingualText("גן המילים", "Word Garden").resolve(language), style = MaterialTheme.typography.headlineMedium, color = EarthBrown)
                Text("⭐ ${state.totalStarsEarned}", fontSize = 24.sp, color = EarthBrown)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Example picture
            if (state.letterChoices.isNotEmpty()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(state.targetLetter.exampleEmoji, fontSize = 64.sp)
                    Text(
                        state.targetLetter.exampleWord.resolve(language),
                        style = MaterialTheme.typography.titleLarge,
                        color = EarthBrown
                    )
                    Text(
                        BilingualText("מצא את: ${state.targetLetter.hebrewLetter}", "Find: ${state.targetLetter.englishLetter}").resolve(language),
                        style = MaterialTheme.typography.headlineMedium,
                        color = EarthBrown
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Letter choices
            Row(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                state.letterChoices.forEach { letter ->
                    val interactionSource = remember(letter.id) { MutableInteractionSource() }
                    val isPressed by interactionSource.collectIsPressedAsState()
                    val scale by animateFloatAsState(
                        targetValue = if (isPressed) 0.85f else 1f,
                        animationSpec = spring(dampingRatio = 0.4f, stiffness = 300f),
                        label = "letter_scale"
                    )

                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .graphicsLayer { scaleX = scale; scaleY = scale }
                            .shadow(8.dp, RoundedCornerShape(24.dp))
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.White)
                            .clickable(interactionSource = interactionSource, indication = null) {
                                val correct = viewModel.onLetterTapped(letter.id)
                                if (correct) {
                                    sfxPlayer.play(Sfx.SUCCESS)
                                    showRoundSuccess = true
                                    scope.launch {
                                        delay(1500)
                                        showRoundSuccess = false
                                        viewModel.nextRound()
                                    }
                                } else {
                                    sfxPlayer.play(Sfx.WRONG_GENTLE)
                                    voicePlayer.play("voice_prompt_try_again", language)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        val displayLetter = when (language) {
                            Language.HEBREW -> letter.hebrewLetter
                            Language.ENGLISH -> letter.englishLetter
                        }
                        Text(displayLetter, fontSize = 56.sp, color = EarthBrown)
                    }
                }
            }

            // Round dots
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(state.totalRounds) { i ->
                    Box(
                        modifier = Modifier
                            .size(12.dp)
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

            Spacer(modifier = Modifier.height(16.dp))
        }

        ConfettiOverlay(show = showRoundSuccess)

        if (state.sessionComplete) {
            RoundCompleteSheet(starsEarned = state.totalStarsEarned, onContinue = onBack, onHome = onBack)
        }
    }
}
