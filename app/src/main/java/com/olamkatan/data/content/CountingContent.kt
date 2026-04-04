package com.olamkatan.data.content

import androidx.compose.ui.graphics.Color
import com.olamkatan.data.models.BilingualText

data class CountingRound(
    val targetNumber: Int,
    val itemName: BilingualText,
    val itemEmoji: String,
    val itemColor: Color
)

val level1CountingRounds = listOf(
    CountingRound(1, BilingualText("פרח", "flower"), "\uD83C\uDF3B", Color(0xFFFFEE58)),
    CountingRound(2, BilingualText("פרפרים", "butterflies"), "\uD83E\uDD8B", Color(0xFFAB47BC)),
    CountingRound(3, BilingualText("חיפושיות", "ladybugs"), "\uD83D\uDC1E", Color(0xFFEF5350)),
    CountingRound(4, BilingualText("פרחים", "flowers"), "\uD83C\uDF3A", Color(0xFFEC407A))
)

val hebrewNumbers = listOf(
    BilingualText("אחת", "one"),
    BilingualText("שתיים", "two"),
    BilingualText("שלוש", "three"),
    BilingualText("ארבע", "four")
)
