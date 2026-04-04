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

val level2CountingRounds = listOf(
    CountingRound(2, BilingualText("תפוחים", "apples"), "\uD83C\uDF4E", Color(0xFFEF5350)),
    CountingRound(3, BilingualText("כוכבים", "stars"), "⭐", Color(0xFFFFD700)),
    CountingRound(4, BilingualText("לבבות", "hearts"), "❤\uFE0F", Color(0xFFE91E63)),
    CountingRound(5, BilingualText("דגים", "fish"), "\uD83D\uDC1F", Color(0xFF42A5F5))
)

val level3CountingRounds = listOf(
    CountingRound(5, BilingualText("ציפורים", "birds"), "\uD83D\uDC26", Color(0xFF42A5F5)),
    CountingRound(6, BilingualText("פרפרים", "butterflies"), "\uD83E\uDD8B", Color(0xFFAB47BC)),
    CountingRound(7, BilingualText("פרחים", "flowers"), "\uD83C\uDF3B", Color(0xFFFFEE58)),
    CountingRound(8, BilingualText("כוכבים", "stars"), "\uD83C\uDF1F", Color(0xFFFFD700)),
    CountingRound(9, BilingualText("לבבות", "hearts"), "\uD83D\uDC9C", Color(0xFF9C27B0)),
    CountingRound(10, BilingualText("תפוחים", "apples"), "\uD83C\uDF4F", Color(0xFF66BB6A))
)

fun countingRoundsForLevel(level: Int): List<CountingRound> = when {
    level >= 3 -> level3CountingRounds
    level >= 2 -> level2CountingRounds
    else -> level1CountingRounds
}

val hebrewNumbers = listOf(
    BilingualText("אחת", "one"),
    BilingualText("שתיים", "two"),
    BilingualText("שלוש", "three"),
    BilingualText("ארבע", "four"),
    BilingualText("חמש", "five"),
    BilingualText("שש", "six"),
    BilingualText("שבע", "seven"),
    BilingualText("שמונה", "eight"),
    BilingualText("תשע", "nine"),
    BilingualText("עשר", "ten")
)
