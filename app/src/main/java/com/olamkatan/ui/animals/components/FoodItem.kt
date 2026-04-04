package com.olamkatan.ui.animals.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.olamkatan.data.content.FoodDef
import com.olamkatan.ui.components.DragTarget

@Composable
fun FoodItem(
    food: FoodDef,
    onDropped: (dropTargetId: String?) -> Unit,
    modifier: Modifier = Modifier
) {
    DragTarget(
        itemId = food.id,
        onDragEnd = onDropped,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .shadow(6.dp, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .background(food.color.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = food.emoji, fontSize = 48.sp)
        }
    }
}
