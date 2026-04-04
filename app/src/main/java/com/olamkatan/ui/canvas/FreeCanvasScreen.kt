package com.olamkatan.ui.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.olamkatan.data.models.BilingualText
import com.olamkatan.theme.EarthBrown
import com.olamkatan.ui.common.LocalLanguage

data class DrawPath(val path: List<Offset>, val color: Color, val strokeWidth: Float)
data class Stamp(val position: Offset, val emoji: String)

private val canvasColors = listOf(
    Color(0xFFEF5350), Color(0xFF42A5F5), Color(0xFFFFEE58),
    Color(0xFF66BB6A), Color(0xFFAB47BC), Color(0xFFFF9800)
)

private val stampEmojis = listOf("⭐", "\uD83C\uDF3B", "\uD83D\uDC1E", "\uD83E\uDD8B", "❤\uFE0F", "\uD83C\uDF08")

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FreeCanvasScreen(onBack: () -> Unit) {
    val language = LocalLanguage.current
    var selectedColor by remember { mutableStateOf(canvasColors[0]) }
    var brushSize by remember { mutableStateOf(8f) }
    var selectedStamp by remember { mutableStateOf<String?>(null) }
    val paths = remember { mutableStateListOf<DrawPath>() }
    val stamps = remember { mutableStateListOf<Stamp>() }
    var currentPath by remember { mutableStateOf(listOf<Offset>()) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Canvas area
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .pointerInput(selectedColor, brushSize, selectedStamp) {
                    if (selectedStamp != null) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                stamps.add(Stamp(offset, selectedStamp!!))
                            },
                            onDrag = { _, _ -> },
                            onDragEnd = {}
                        )
                    } else {
                        detectDragGestures(
                            onDragStart = { offset ->
                                currentPath = listOf(offset)
                            },
                            onDrag = { change, _ ->
                                change.consume()
                                currentPath = currentPath + change.position
                            },
                            onDragEnd = {
                                if (currentPath.size > 1) {
                                    paths.add(DrawPath(currentPath, selectedColor, brushSize))
                                }
                                currentPath = emptyList()
                            }
                        )
                    }
                }
        ) {
            // Draw saved paths
            paths.forEach { drawPath ->
                if (drawPath.path.size > 1) {
                    val path = Path().apply {
                        moveTo(drawPath.path[0].x, drawPath.path[0].y)
                        for (i in 1 until drawPath.path.size) {
                            lineTo(drawPath.path[i].x, drawPath.path[i].y)
                        }
                    }
                    drawPath(path, drawPath.color, style = Stroke(width = drawPath.strokeWidth, cap = StrokeCap.Round))
                }
            }

            // Draw current path
            if (currentPath.size > 1) {
                val path = Path().apply {
                    moveTo(currentPath[0].x, currentPath[0].y)
                    for (i in 1 until currentPath.size) {
                        lineTo(currentPath[i].x, currentPath[i].y)
                    }
                }
                drawPath(path, selectedColor, style = Stroke(width = brushSize, cap = StrokeCap.Round))
            }
        }

        // Stamps drawn as text overlay
        stamps.forEach { stamp ->
            Text(
                text = stamp.emoji,
                fontSize = 32.sp,
                modifier = Modifier.padding(
                    start = (stamp.position.x / 3).dp,
                    top = (stamp.position.y / 3).dp
                )
            )
        }

        // Back button
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .size(56.dp)
                .clip(CircleShape)
                .background(EarthBrown.copy(alpha = 0.7f))
                .clickable { onBack() },
            contentAlignment = Alignment.Center
        ) { Text("◀", fontSize = 24.sp, color = Color.White) }

        // Clear button (long press)
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(56.dp)
                .clip(CircleShape)
                .background(Color.Red.copy(alpha = 0.6f))
                .combinedClickable(
                    onClick = { },
                    onLongClick = {
                        paths.clear()
                        stamps.clear()
                    }
                ),
            contentAlignment = Alignment.Center
        ) { Text("\uD83D\uDDD1\uFE0F", fontSize = 24.sp) }

        // Bottom toolbar
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(Color(0xFFF5F5F5))
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Color picker
            canvasColors.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(if (color == selectedColor && selectedStamp == null) 48.dp else 40.dp)
                        .clip(CircleShape)
                        .background(color)
                        .clickable {
                            selectedColor = color
                            selectedStamp = null
                        }
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Brush sizes
            listOf(6f to 24.dp, 14f to 36.dp).forEach { (size, displaySize) ->
                Box(
                    modifier = Modifier
                        .size(displaySize)
                        .clip(CircleShape)
                        .background(if (brushSize == size && selectedStamp == null) EarthBrown else EarthBrown.copy(alpha = 0.4f))
                        .clickable {
                            brushSize = size
                            selectedStamp = null
                        }
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Stamps
            stampEmojis.forEach { emoji ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (selectedStamp == emoji) Color(0xFFE0E0E0) else Color.Transparent)
                        .clickable { selectedStamp = if (selectedStamp == emoji) null else emoji },
                    contentAlignment = Alignment.Center
                ) { Text(emoji, fontSize = 24.sp) }
            }
        }
    }
}
