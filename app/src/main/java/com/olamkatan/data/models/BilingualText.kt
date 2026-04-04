package com.olamkatan.data.models

data class BilingualText(
    val he: String,
    val en: String
) {
    fun resolve(language: Language): String = when (language) {
        Language.HEBREW -> he
        Language.ENGLISH -> en
    }
}
