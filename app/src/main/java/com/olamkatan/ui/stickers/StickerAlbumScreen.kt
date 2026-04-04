package com.olamkatan.ui.stickers

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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.olamkatan.OlamKatanApplication
import com.olamkatan.audio.Sfx
import com.olamkatan.data.models.BilingualText
import com.olamkatan.data.models.GameProgress
import com.olamkatan.theme.CloudWhite
import com.olamkatan.theme.EarthBrown
import com.olamkatan.ui.common.LocalLanguage
import com.olamkatan.ui.common.LocalSfxPlayer
import com.olamkatan.ui.common.LocalTtsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class StickerDef(
    val id: String,
    val name: BilingualText,
    val emoji: String,
    val starsRequired: Int
)

val allStickers = listOf(
    StickerDef("cow", BilingualText("פרה", "Cow"), "\uD83D\uDC04", 0),
    StickerDef("dog", BilingualText("כלב", "Dog"), "\uD83D\uDC36", 2),
    StickerDef("cat", BilingualText("חתול", "Cat"), "\uD83D\uDC31", 4),
    StickerDef("rainbow", BilingualText("קשת", "Rainbow"), "\uD83C\uDF08", 6),
    StickerDef("star", BilingualText("כוכב", "Star"), "⭐", 8),
    StickerDef("flower", BilingualText("פרח", "Flower"), "\uD83C\uDF3B", 10),
    StickerDef("butterfly", BilingualText("פרפר", "Butterfly"), "\uD83E\uDD8B", 14),
    StickerDef("heart", BilingualText("לב", "Heart"), "❤\uFE0F", 18),
    StickerDef("music", BilingualText("מוזיקה", "Music"), "\uD83C\uDFB5", 22),
    StickerDef("sun", BilingualText("שמש", "Sun"), "☀\uFE0F", 26),
    StickerDef("moon", BilingualText("ירח", "Moon"), "\uD83C\uDF19", 30),
    StickerDef("rocket", BilingualText("טיל", "Rocket"), "\uD83D\uDE80", 40),
    StickerDef("crown", BilingualText("כתר", "Crown"), "\uD83D\uDC51", 50),
    StickerDef("trophy", BilingualText("גביע", "Trophy"), "\uD83C\uDFC6", 60),
    StickerDef("diamond", BilingualText("יהלום", "Diamond"), "\uD83D\uDC8E", 80),
    StickerDef("unicorn", BilingualText("חד קרן", "Unicorn"), "\uD83E\uDD84", 100)
)

class StickerAlbumViewModel(application: Application) : AndroidViewModel(application) {
    private val progressRepository = (application as OlamKatanApplication).progressRepository
    val progress: StateFlow<GameProgress> = MutableStateFlow(GameProgress()).also { flow ->
        // Collect in init
    }

    // Use the app-level progress
    val appProgress = progressRepository.progress
}

@Composable
fun StickerAlbumScreen(onBack: () -> Unit, viewModel: StickerAlbumViewModel = viewModel()) {
    val language = LocalLanguage.current
    val ttsManager = LocalTtsManager.current
    val sfxPlayer = LocalSfxPlayer.current
    val progress by viewModel.appProgress.collectAsStateWithLifecycle(GameProgress())

    Box(
        modifier = Modifier.fillMaxSize().background(
            Brush.verticalGradient(listOf(Color(0xFFF3E5F5), Color(0xFFE1BEE7), Color(0xFFCE93D8)))
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
                Text(BilingualText("אלבום מדבקות", "Sticker Album").resolve(language), style = MaterialTheme.typography.headlineMedium, color = CloudWhite)
                Text("⭐ ${progress.totalStars}", fontSize = 24.sp, color = CloudWhite)
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(allStickers) { sticker ->
                    val isUnlocked = progress.totalStars >= sticker.starsRequired
                    val interactionSource = remember(sticker.id) { MutableInteractionSource() }
                    val isPressed by interactionSource.collectIsPressedAsState()
                    val scale by animateFloatAsState(
                        targetValue = if (isPressed) 0.85f else 1f,
                        animationSpec = spring(dampingRatio = 0.4f, stiffness = 300f),
                        label = "sticker_scale"
                    )

                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .graphicsLayer { scaleX = scale; scaleY = scale }
                            .shadow(4.dp, RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isUnlocked) Color.White else Color.Gray.copy(alpha = 0.3f))
                            .clickable(interactionSource = interactionSource, indication = null) {
                                if (isUnlocked) {
                                    sfxPlayer.play(Sfx.POP)
                                    ttsManager.speakBilingual(sticker.name, language)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isUnlocked) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(sticker.emoji, fontSize = 40.sp)
                                Text(sticker.name.resolve(language), fontSize = 10.sp, color = EarthBrown)
                            }
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("\uD83D\uDD12", fontSize = 28.sp)
                                Text("⭐ ${sticker.starsRequired}", fontSize = 10.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}
