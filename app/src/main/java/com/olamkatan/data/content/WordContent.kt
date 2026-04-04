package com.olamkatan.data.content

import com.olamkatan.data.models.BilingualText

data class LetterDef(
    val id: String,
    val hebrewLetter: String,
    val englishLetter: String,
    val name: BilingualText,
    val exampleWord: BilingualText,
    val exampleEmoji: String
)

val hebrewLetters = listOf(
    LetterDef("alef", "א", "A", BilingualText("אָלֶף", "Alef"), BilingualText("אריה", "Lion"), "\uD83E\uDD81"),
    LetterDef("bet", "ב", "B", BilingualText("בֵּית", "Bet"), BilingualText("בית", "House"), "\uD83C\uDFE0"),
    LetterDef("gimel", "ג", "G", BilingualText("גִּימֶל", "Gimel"), BilingualText("גמל", "Camel"), "\uD83D\uDC2B"),
    LetterDef("dalet", "ד", "D", BilingualText("דָּלֶת", "Dalet"), BilingualText("דג", "Fish"), "\uD83D\uDC1F"),
    LetterDef("he", "ה", "H", BilingualText("הֵא", "He"), BilingualText("הר", "Mountain"), "⛰\uFE0F"),
    LetterDef("vav", "ו", "V", BilingualText("וָו", "Vav"), BilingualText("ורד", "Rose"), "\uD83C\uDF39")
)

val englishLetters = listOf(
    LetterDef("a", "א", "A", BilingualText("אֵי", "A"), BilingualText("תפוח", "Apple"), "\uD83C\uDF4E"),
    LetterDef("b", "ב", "B", BilingualText("בִּי", "B"), BilingualText("כדור", "Ball"), "⚽"),
    LetterDef("c", "ג", "C", BilingualText("סִי", "C"), BilingualText("חתול", "Cat"), "\uD83D\uDC31"),
    LetterDef("d", "ד", "D", BilingualText("דִי", "D"), BilingualText("כלב", "Dog"), "\uD83D\uDC36"),
    LetterDef("e", "ה", "E", BilingualText("אִי", "E"), BilingualText("פיל", "Elephant"), "\uD83D\uDC18"),
    LetterDef("f", "ו", "F", BilingualText("אֶף", "F"), BilingualText("דג", "Fish"), "\uD83D\uDC1F")
)
