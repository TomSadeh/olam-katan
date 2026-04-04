package com.olamkatan.ui.hub

import androidx.compose.ui.graphics.Color
import com.olamkatan.data.models.BilingualText
import com.olamkatan.navigation.Routes
import com.olamkatan.theme.CoralPink
import com.olamkatan.theme.EarthBrown
import com.olamkatan.theme.GrassGreen
import com.olamkatan.theme.SoftPurple
import com.olamkatan.theme.SunYellow
import com.olamkatan.theme.WarmOrange

data class IslandDef(
    val id: String,
    val name: BilingualText,
    val route: String,
    val color: Color,
    val emoji: String,
    val starsToUnlock: Int,
    val fractionalX: Float,
    val fractionalY: Float
)

val allIslands = listOf(
    IslandDef(
        id = "animals",
        name = BilingualText("גן החיות", "Animal Garden"),
        route = Routes.ANIMALS,
        color = GrassGreen,
        emoji = "\uD83D\uDC04",
        starsToUnlock = 0,
        fractionalX = 0.12f,
        fractionalY = 0.35f
    ),
    IslandDef(
        id = "colors",
        name = BilingualText("צבעים", "Color Splash"),
        route = Routes.COLORS,
        color = CoralPink,
        emoji = "\uD83C\uDFA8",
        starsToUnlock = 4,
        fractionalX = 0.32f,
        fractionalY = 0.28f
    ),
    IslandDef(
        id = "shapes",
        name = BilingualText("צורות", "Shapes"),
        route = Routes.SHAPES,
        color = SoftPurple,
        emoji = "\uD83D\uDD37",
        starsToUnlock = 10,
        fractionalX = 0.50f,
        fractionalY = 0.32f
    ),
    IslandDef(
        id = "counting",
        name = BilingualText("ספירה", "Counting"),
        route = Routes.COUNTING,
        color = SunYellow,
        emoji = "\uD83C\uDF3B",
        starsToUnlock = 18,
        fractionalX = 0.68f,
        fractionalY = 0.28f
    ),
    IslandDef(
        id = "music",
        name = BilingualText("מוזיקה", "Music"),
        route = Routes.MUSIC,
        color = WarmOrange,
        emoji = "\uD83C\uDFB5",
        starsToUnlock = 40,
        fractionalX = 0.85f,
        fractionalY = 0.35f
    ),
    IslandDef(
        id = "words",
        name = BilingualText("מילים", "Words"),
        route = Routes.WORDS,
        color = CoralPink,
        emoji = "\uD83D\uDCD6",
        starsToUnlock = 60,
        fractionalX = 0.88f,
        fractionalY = 0.60f
    ),
    IslandDef(
        id = "myday",
        name = BilingualText("היום שלי", "My Day"),
        route = Routes.MY_DAY,
        color = EarthBrown,
        emoji = "\uD83C\uDFE0",
        starsToUnlock = 25,
        fractionalX = 0.50f,
        fractionalY = 0.70f
    )
)
