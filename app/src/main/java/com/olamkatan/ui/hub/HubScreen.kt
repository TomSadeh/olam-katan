package com.olamkatan.ui.hub

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.olamkatan.data.models.BilingualText
import com.olamkatan.theme.EarthBrown
import com.olamkatan.ui.hub.components.HubBackground
import com.olamkatan.ui.hub.components.IslandButton
import com.olamkatan.ui.hub.components.StarCounter
import com.olamkatan.ui.common.LocalLanguage
import androidx.compose.material3.Text

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HubScreen(
    onIslandClick: (String) -> Unit,
    onSettingsClick: () -> Unit,
    onStickersClick: () -> Unit
) {
    val hubViewModel: HubViewModel = viewModel()
    val progress by hubViewModel.progress.collectAsStateWithLifecycle()
    val layoutDirection = LocalLayoutDirection.current

    Box(modifier = Modifier.fillMaxSize()) {
        HubBackground()

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val density = LocalDensity.current
            val maxWidthPx = with(density) { maxWidth.toPx() }
            val maxHeightPx = with(density) { maxHeight.toPx() }

            allIslands.forEach { island ->
                val fx = if (layoutDirection == LayoutDirection.Rtl) {
                    1f - island.fractionalX
                } else {
                    island.fractionalX
                }
                val offsetX = with(density) { (maxWidthPx * fx - 50.dp.toPx()).toDp() }
                val offsetY = with(density) { (maxHeightPx * island.fractionalY - 50.dp.toPx()).toDp() }

                IslandButton(
                    island = island,
                    isUnlocked = true,
                    onClick = { onIslandClick(island.route) },
                    modifier = Modifier.offset(x = offsetX, y = offsetY)
                )
            }
        }

        // Star counter — top center
        StarCounter(
            stars = progress.totalStars,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        )

        // Sticker album button — top start
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .size(56.dp)
                .clip(CircleShape)
                .background(EarthBrown.copy(alpha = 0.7f))
                .combinedClickable(onClick = { onStickersClick() }),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "📖", fontSize = 28.sp)
        }

        // Settings gear — top end (long-press to open)
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(56.dp)
                .clip(CircleShape)
                .background(EarthBrown.copy(alpha = 0.7f))
                .combinedClickable(
                    onClick = { },
                    onLongClick = { onSettingsClick() }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "⚙️", fontSize = 28.sp)
        }

        // Quit button — bottom start (double-tap to exit)
        val activity = LocalContext.current as? Activity
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .size(56.dp)
                .clip(CircleShape)
                .background(EarthBrown.copy(alpha = 0.7f))
                .combinedClickable(
                    onClick = { },
                    onDoubleClick = { activity?.finishAndRemoveTask() }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "\uD83D\uDEAA", fontSize = 28.sp)
        }

        // Canvas button — bottom end
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(56.dp)
                .clip(CircleShape)
                .background(EarthBrown.copy(alpha = 0.7f))
                .combinedClickable(onClick = {
                    onIslandClick("canvas")
                }),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "\uD83C\uDFA8", fontSize = 28.sp)
        }

    }
}
