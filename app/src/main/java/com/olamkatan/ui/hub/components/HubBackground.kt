package com.olamkatan.ui.hub.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path

@Composable
fun HubBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "clouds")
    val cloudOffset by infiniteTransition.animateFloat(
        initialValue = -0.1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 60000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "cloud_drift"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Sky gradient
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF4FC3F7),
                    Color(0xFF81D4FA),
                    Color(0xFFB3E5FC)
                )
            )
        )

        // Sun
        drawCircle(
            color = Color(0xFFFFD54F),
            radius = h * 0.08f,
            center = Offset(w * 0.9f, h * 0.12f)
        )
        drawCircle(
            color = Color(0x40FFD54F),
            radius = h * 0.12f,
            center = Offset(w * 0.9f, h * 0.12f)
        )

        // Clouds
        val cloudY1 = h * 0.15f
        val cloudY2 = h * 0.22f
        val cloudX1 = w * cloudOffset
        val cloudX2 = w * ((cloudOffset + 0.5f) % 1.1f)
        drawCloud(cloudX1, cloudY1, w * 0.08f)
        drawCloud(cloudX2, cloudY2, w * 0.06f)

        // Rolling hills (back)
        val hillPath = Path().apply {
            moveTo(0f, h * 0.65f)
            cubicTo(w * 0.15f, h * 0.50f, w * 0.35f, h * 0.55f, w * 0.5f, h * 0.60f)
            cubicTo(w * 0.65f, h * 0.65f, w * 0.85f, h * 0.50f, w, h * 0.58f)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(hillPath, Color(0xFF66BB6A))

        // Rolling hills (front)
        val frontHillPath = Path().apply {
            moveTo(0f, h * 0.75f)
            cubicTo(w * 0.2f, h * 0.65f, w * 0.4f, h * 0.72f, w * 0.6f, h * 0.68f)
            cubicTo(w * 0.8f, h * 0.64f, w * 0.9f, h * 0.72f, w, h * 0.70f)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(frontHillPath, Color(0xFF4CAF50))

        // Ground
        drawRect(
            color = Color(0xFF388E3C),
            topLeft = Offset(0f, h * 0.85f),
            size = Size(w, h * 0.15f)
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawCloud(
    x: Float,
    y: Float,
    radius: Float
) {
    val cloudColor = Color(0xDDFFFFFF)
    drawCircle(cloudColor, radius, Offset(x, y))
    drawCircle(cloudColor, radius * 0.8f, Offset(x - radius * 0.7f, y + radius * 0.1f))
    drawCircle(cloudColor, radius * 0.7f, Offset(x + radius * 0.8f, y + radius * 0.15f))
    drawCircle(cloudColor, radius * 0.6f, Offset(x - radius * 1.2f, y + radius * 0.3f))
    drawCircle(cloudColor, radius * 0.65f, Offset(x + radius * 1.3f, y + radius * 0.3f))
}
