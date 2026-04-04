package com.olamkatan.data.content

import androidx.compose.ui.graphics.Color
import com.olamkatan.data.models.BilingualText

data class AnimalDef(
    val id: String,
    val name: BilingualText,
    val sound: BilingualText,
    val emoji: String,
    val color: Color,
    val foodId: String
)

data class FoodDef(
    val id: String,
    val name: BilingualText,
    val emoji: String,
    val color: Color
)

val level1Animals = listOf(
    AnimalDef(
        id = "cow",
        name = BilingualText("פרה", "Cow"),
        sound = BilingualText("מוּ!", "Moo!"),
        emoji = "\uD83D\uDC04",
        color = Color(0xFFF5F5DC),
        foodId = "grass"
    ),
    AnimalDef(
        id = "dog",
        name = BilingualText("כלב", "Dog"),
        sound = BilingualText("הב הב!", "Woof!"),
        emoji = "\uD83D\uDC36",
        color = Color(0xFFD2B48C),
        foodId = "bone"
    ),
    AnimalDef(
        id = "cat",
        name = BilingualText("חתול", "Cat"),
        sound = BilingualText("מיאו!", "Meow!"),
        emoji = "\uD83D\uDC31",
        color = Color(0xFFFFB347),
        foodId = "fish"
    ),
    AnimalDef(
        id = "chicken",
        name = BilingualText("תרנגולת", "Chicken"),
        sound = BilingualText("קוקוריקו!", "Cock-a-doodle-doo!"),
        emoji = "\uD83D\uDC14",
        color = Color(0xFFFFF8DC),
        foodId = "seeds"
    )
)

val level1Foods = listOf(
    FoodDef(
        id = "grass",
        name = BilingualText("עשב", "Grass"),
        emoji = "\uD83C\uDF3F",
        color = Color(0xFF4CAF50)
    ),
    FoodDef(
        id = "bone",
        name = BilingualText("עצם", "Bone"),
        emoji = "\uD83E\uDDB4",
        color = Color(0xFFF5F5DC)
    ),
    FoodDef(
        id = "fish",
        name = BilingualText("דג", "Fish"),
        emoji = "\uD83D\uDC1F",
        color = Color(0xFF64B5F6)
    ),
    FoodDef(
        id = "seeds",
        name = BilingualText("גרעינים", "Seeds"),
        emoji = "\uD83C\uDF3E",
        color = Color(0xFFFFD54F)
    )
)
