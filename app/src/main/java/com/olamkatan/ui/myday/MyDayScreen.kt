package com.olamkatan.ui.myday

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.olamkatan.data.models.BilingualText
import com.olamkatan.navigation.Routes
import com.olamkatan.theme.CloudWhite
import com.olamkatan.theme.EarthBrown
import com.olamkatan.ui.common.LocalLanguage

data class RoomDef(
    val route: String,
    val name: BilingualText,
    val emoji: String,
    val color: Color
)

private val rooms = listOf(
    RoomDef(Routes.MY_DAY_BATH, BilingualText("אמבטיה", "Bath Time"), "\uD83D\uDEC1", Color(0xFF81D4FA)),
    RoomDef(Routes.MY_DAY_BRUSH, BilingualText("צחצוח שיניים", "Brushy Brushy"), "\uD83E\uDEB5", Color(0xFF80CBC4)),
    RoomDef(Routes.MY_DAY_TIDY, BilingualText("סידור חדר", "Tidy Up"), "\uD83E\uDDF8", Color(0xFFFFCC80)),
    RoomDef(Routes.MY_DAY_DRESS, BilingualText("להתלבש", "Getting Dressed"), "\uD83D\uDC55", Color(0xFFEF9A9A)),
    RoomDef(Routes.MY_DAY_TABLE, BilingualText("ארוחה", "Table Time"), "\uD83C\uDF7D\uFE0F", Color(0xFFA5D6A7)),
    RoomDef(Routes.MY_DAY_BEDTIME, BilingualText("שינה", "Bedtime"), "\uD83C\uDF19", Color(0xFF7E57C2))
)

@Composable
fun MyDayScreen(
    onRoomClick: (String) -> Unit,
    onBack: () -> Unit
) {
    val language = LocalLanguage.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFFFE0B2), Color(0xFFFFCC80), Color(0xFFFFB74D))
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

                Text(
                    text = BilingualText("היום שלי", "My Day").resolve(language),
                    style = MaterialTheme.typography.headlineLarge,
                    color = CloudWhite
                )

                Spacer(modifier = Modifier.width(56.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            // House cross-section with rooms
            Text(text = "\uD83C\uDFE0", fontSize = 48.sp)

            Spacer(modifier = Modifier.height(16.dp))

            // Room grid
            Row(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left column: bathroom rooms
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RoomButton(rooms[0], onClick = { onRoomClick(rooms[0].route) })
                    RoomButton(rooms[1], onClick = { onRoomClick(rooms[1].route) })
                }

                // Center column: table + bedtime
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RoomButton(rooms[4], onClick = { onRoomClick(rooms[4].route) })
                    RoomButton(rooms[5], onClick = { onRoomClick(rooms[5].route) })
                }

                // Right column: bedroom rooms
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RoomButton(rooms[2], onClick = { onRoomClick(rooms[2].route) })
                    RoomButton(rooms[3], onClick = { onRoomClick(rooms[3].route) })
                }
            }
        }
    }
}

@Composable
private fun RoomButton(room: RoomDef, onClick: () -> Unit) {
    val language = LocalLanguage.current
    val infiniteTransition = rememberInfiniteTransition(label = "room_${room.route}")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.97f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(tween(2000), RepeatMode.Reverse),
        label = "room_scale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .shadow(8.dp, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .background(room.color),
            contentAlignment = Alignment.Center
        ) {
            Text(text = room.emoji, fontSize = 44.sp)
        }
        Text(
            text = room.name.resolve(language),
            style = MaterialTheme.typography.labelLarge,
            color = CloudWhite
        )
    }
}
