package com.olamkatan.data.content

import androidx.compose.ui.graphics.Color
import com.olamkatan.data.models.BilingualText

data class ShapeDef(
    val id: String,
    val name: BilingualText,
    val color: Color,
    val sides: Int
)

val level1Shapes = listOf(
    ShapeDef("circle", BilingualText("עיגול", "Circle"), Color(0xFFEF5350), 0),
    ShapeDef("square", BilingualText("ריבוע", "Square"), Color(0xFF42A5F5), 4),
    ShapeDef("triangle", BilingualText("משולש", "Triangle"), Color(0xFFFFEE58), 3),
    ShapeDef("star", BilingualText("כוכב", "Star"), Color(0xFFAB47BC), 5)
)
