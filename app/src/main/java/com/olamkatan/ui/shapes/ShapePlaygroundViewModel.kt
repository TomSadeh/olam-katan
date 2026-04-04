package com.olamkatan.ui.shapes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.olamkatan.OlamKatanApplication
import com.olamkatan.data.content.ShapeDef
import com.olamkatan.data.content.level1Shapes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ShapeRoundState(
    val targetShape: ShapeDef = level1Shapes[0],
    val shapeChoices: List<ShapeDef> = level1Shapes,
    val roundIndex: Int = 0,
    val totalRounds: Int = level1Shapes.size,
    val roundComplete: Boolean = false,
    val sessionComplete: Boolean = false,
    val totalStarsEarned: Int = 0
)

class ShapePlaygroundViewModel(application: Application) : AndroidViewModel(application) {

    private val progressRepository = (application as OlamKatanApplication).progressRepository
    private val rounds = level1Shapes.shuffled()

    private val _state = MutableStateFlow(ShapeRoundState())
    val state: StateFlow<ShapeRoundState> = _state.asStateFlow()

    init {
        startRound(0)
    }

    private fun startRound(index: Int) {
        if (index >= rounds.size) {
            _state.value = _state.value.copy(sessionComplete = true)
            return
        }
        _state.value = ShapeRoundState(
            targetShape = rounds[index],
            shapeChoices = level1Shapes.shuffled(),
            roundIndex = index,
            totalRounds = rounds.size,
            totalStarsEarned = _state.value.totalStarsEarned
        )
    }

    fun onShapeDropped(shapeId: String): Boolean {
        val isCorrect = shapeId == _state.value.targetShape.id
        if (isCorrect) {
            val newStars = _state.value.totalStarsEarned + 1
            _state.value = _state.value.copy(roundComplete = true, totalStarsEarned = newStars)
            viewModelScope.launch { progressRepository.addStars(1) }
        }
        return isCorrect
    }

    fun nextRound() {
        startRound(_state.value.roundIndex + 1)
    }
}
