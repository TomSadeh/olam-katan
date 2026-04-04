package com.olamkatan.data.content

import androidx.compose.ui.graphics.Color
import com.olamkatan.data.models.BilingualText

data class ColorDef(
    val id: String,
    val name: BilingualText,
    val color: Color
)

val level1Colors = listOf(
    ColorDef("red", BilingualText("אדום", "Red"), Color(0xFFEF5350)),
    ColorDef("blue", BilingualText("כחול", "Blue"), Color(0xFF42A5F5)),
    ColorDef("yellow", BilingualText("צהוב", "Yellow"), Color(0xFFFFEE58)),
    ColorDef("green", BilingualText("ירוק", "Green"), Color(0xFF66BB6A))
)
