package com.olamkatan.data.content

import androidx.compose.ui.graphics.Color
import com.olamkatan.data.models.BilingualText

data class InstrumentDef(
    val id: String,
    val name: BilingualText,
    val emoji: String,
    val color: Color,
    val noteIndex: Int // maps to a pitch for simon-says
)

val instruments = listOf(
    InstrumentDef("drum", BilingualText("תוף", "Drum"), "\uD83E\uDD41", Color(0xFFEF5350), 0),
    InstrumentDef("xylophone", BilingualText("קסילופון", "Xylophone"), "\uD83C\uDFB5", Color(0xFF42A5F5), 1),
    InstrumentDef("tambourine", BilingualText("תוף מרים", "Tambourine"), "\uD83E\uDE98", Color(0xFFFFEE58), 2),
    InstrumentDef("maracas", BilingualText("מרקסים", "Maracas"), "\uD83C\uDF89", Color(0xFF66BB6A), 3)
)
