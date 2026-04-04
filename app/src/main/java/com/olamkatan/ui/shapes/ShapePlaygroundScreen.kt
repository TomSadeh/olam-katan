package com.olamkatan.ui.shapes

import androidx.compose.foundation.Canvas
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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.olamkatan.audio.Sfx
import com.olamkatan.data.content.ShapeDef
import com.olamkatan.data.models.BilingualText
import com.olamkatan.theme.CloudWhite
import com.olamkatan.theme.EarthBrown
import com.olamkatan.ui.common.LocalLanguage
import com.olamkatan.ui.common.LocalSfxPlayer
import com.olamkatan.ui.common.LocalVoicePlayer
import com.olamkatan.ui.components.ConfettiOverlay
import com.olamkatan.ui.components.DragDropState
import com.olamkatan.ui.components.DragTarget
import com.olamkatan.ui.components.DropZone
import com.olamkatan.ui.components.LocalDragDropState
import com.olamkatan.ui.components.RoundCompleteSheet
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ShapePlaygroundScreen(
    onBack: () -> Unit,
    viewModel: ShapePlaygroundViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
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
            listOf("voice_prompt_drag_it", "voice_shape_${state.targetShape.id}"),
            language
        )
    }

    CompositionLocalProvider(LocalDragDropState provides dragDropState) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFBBDEFB), Color(0xFF90CAF9), Color(0xFF64B5F6))
                    )
                )
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top bar
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
                        Text("◀", fontSize = 24.sp, color = Color.White)
                    }

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

                    Text("⭐ ${state.totalStarsEarned}", fontSize = 24.sp, color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = BilingualText(
                        "גרור את ה${state.targetShape.name.he}!",
                        "Drag the ${state.targetShape.name.en}!"
                    ).resolve(language),
                    style = MaterialTheme.typography.headlineMedium,
                    color = CloudWhite
                )

                // Target outline (drop zone) in center
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    DropZone(zoneId = "target") {
                        Canvas(modifier = Modifier.size(150.dp)) {
                            drawShapeOutline(state.targetShape, size)
                        }
                    }
                }

                // Draggable shapes at bottom
                if (!state.roundComplete) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        state.shapeChoices.forEach { shape ->
                            DragTarget(
                                itemId = shape.id,
                                onDragEnd = { dropTargetId ->
                                    if (dropTargetId == "target") {
                                        val correct = viewModel.onShapeDropped(shape.id)
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
                                    }
                                }
                            ) {
                                Canvas(modifier = Modifier.size(100.dp)) {
                                    drawShapeFilled(shape, size)
                                }
                            }
                        }
                    }
                }
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
}

private fun DrawScope.drawShapeOutline(shape: ShapeDef, canvasSize: Size) {
    val center = Offset(canvasSize.width / 2, canvasSize.height / 2)
    val radius = canvasSize.minDimension / 2 * 0.8f
    when (shape.sides) {
        0 -> drawCircle(shape.color, radius, center, style = Stroke(width = 4f))
        else -> {
            val path = polygonPath(center, radius, shape.sides)
            drawPath(path, shape.color, style = Stroke(width = 4f))
        }
    }
}

private fun DrawScope.drawShapeFilled(shape: ShapeDef, canvasSize: Size) {
    val center = Offset(canvasSize.width / 2, canvasSize.height / 2)
    val radius = canvasSize.minDimension / 2 * 0.8f
    when (shape.sides) {
        0 -> drawCircle(shape.color, radius, center)
        else -> {
            val path = polygonPath(center, radius, shape.sides)
            drawPath(path, shape.color)
        }
    }
}

private fun polygonPath(center: Offset, radius: Float, sides: Int): Path {
    val path = Path()
    val angleStep = 360f / sides
    for (i in 0 until sides) {
        val angle = (angleStep * i - 90f) * Math.PI.toFloat() / 180f
        val x = center.x + cos(angle) * radius
        val y = center.y + sin(angle) * radius
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    return path
}
