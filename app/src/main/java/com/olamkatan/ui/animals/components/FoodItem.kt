package com.olamkatan.ui.animals.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.olamkatan.data.content.FoodDef
import com.olamkatan.theme.EarthBrown
import com.olamkatan.ui.common.LocalLanguage
import com.olamkatan.ui.components.DragTarget

@Composable
fun FoodItem(
    food: FoodDef,
    onDropped: (dropTargetId: String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val language = LocalLanguage.current

    DragTarget(
        itemId = food.id,
        onDragEnd = onDropped,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(105.dp)
                .shadow(8.dp, RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            food.color.copy(alpha = 0.4f),
                            food.color.copy(alpha = 0.15f)
                        )
                    )
                )
                .border(
                    width = 2.dp,
                    color = Color.White.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(24.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (food.drawableRes != 0) {
                    Image(
                        painter = painterResource(id = food.drawableRes),
                        contentDescription = food.name.en,
                        modifier = Modifier.size(60.dp)
                    )
                } else {
                    Text(text = food.emoji, fontSize = 48.sp)
                }
                Text(
                    text = food.name.resolve(language),
                    fontSize = 12.sp,
                    color = EarthBrown,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}
