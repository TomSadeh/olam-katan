package com.olamkatan.ui.hub.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.olamkatan.data.models.Language
import com.olamkatan.theme.CloudWhite
import com.olamkatan.theme.EarthBrown
import com.olamkatan.ui.common.LocalLanguage
import com.olamkatan.ui.hub.IslandDef
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun IslandButton(
    island: IslandDef,
    isUnlocked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val language = LocalLanguage.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scope = rememberCoroutineScope()

    // Idle breathing animation
    val infiniteTransition = rememberInfiniteTransition(label = "breathe_${island.id}")
    val breatheScale by infiniteTransition.animateFloat(
        initialValue = 0.97f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathe_scale_${island.id}"
    )

    // Press scale animation
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(dampingRatio = 0.4f, stiffness = 300f),
        label = "press_scale"
    )

    // Wobble for locked islands
    var wobbleRotation by remember { mutableFloatStateOf(0f) }
    val wobbleAnim by animateFloatAsState(
        targetValue = wobbleRotation,
        animationSpec = spring(dampingRatio = 0.3f, stiffness = 400f),
        label = "wobble"
    )

    Column(
        modifier = modifier
            .graphicsLayer {
                scaleX = breatheScale * pressScale
                scaleY = breatheScale * pressScale
                rotationZ = wobbleAnim
            }
            .alpha(if (isUnlocked) 1f else 0.5f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .shadow(8.dp, CircleShape)
                .clip(CircleShape)
                .background(if (isUnlocked) island.color else Color.Gray)
                .clickable(interactionSource = interactionSource, indication = null) {
                    if (isUnlocked) {
                        onClick()
                    } else {
                        scope.launch {
                            wobbleRotation = 8f
                            delay(100)
                            wobbleRotation = -8f
                            delay(100)
                            wobbleRotation = 0f
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            if (island.drawableRes != 0) {
                Image(
                    painter = painterResource(id = island.drawableRes),
                    contentDescription = island.name.en,
                    modifier = Modifier.size(70.dp)
                )
            } else {
                Text(text = island.emoji, fontSize = 40.sp)
            }
            if (!isUnlocked) {
                Text(text = "\uD83D\uDD12", fontSize = 24.sp, modifier = Modifier.align(Alignment.BottomEnd).padding(4.dp))
            }
        }

        Text(
            text = island.name.resolve(language),
            style = MaterialTheme.typography.labelLarge,
            color = CloudWhite,
            textAlign = TextAlign.Center
        )

        if (!isUnlocked) {
            Text(
                text = "⭐ ${island.starsToUnlock}",
                style = MaterialTheme.typography.bodyLarge,
                color = EarthBrown,
                fontSize = 14.sp
            )
        }
    }
}
