package io.github.tomsadeh.puki.data.models

data class AppSettings(
    val language: Language = Language.HEBREW,
    val gender: Gender = Gender.GIRL,
    val soundEnabled: Boolean = true,
    val musicEnabled: Boolean = true,
    /** Volumes, 0..1: music, voices (recorded lines and Puki), sound effects. */
    val musicVolume: Float = 1f,
    val voiceVolume: Float = 1f,
    val sfxVolume: Float = 1f,
    val timeLimitMinutes: Int = 0, // 0 = no limit
    val childName: String = "",
    val onboardingDone: Boolean = false,
    /** False until the stored settings have been read (so the welcome screen doesn't flash). */
    val loaded: Boolean = false,
    val companionEnabled: Boolean = true,
    val companionAddress: String = DEFAULT_COMPANION_ADDRESS,
    /** Shown by brain/doorbell.py on the PC; without it the PC won't answer. */
    val companionCode: String = "",
    /** The PC's HTTPS certificate fingerprint (hex); empty until scanned or learned. */
    val companionPin: String = "",
    /** Puki thinks on this device instead of the PC (high-end phones, models downloaded). */
    val brainOnDevice: Boolean = false,
    /** After a game's question, Puki listens a few seconds for the answer said out loud. */
    val answersOutLoud: Boolean = true,
    /** Islands a parent took off the map (fewer choices at two), by id. */
    val hiddenIslands: Set<String> = emptySet()
)

/** The family PC running brain/doorbell.py, on the home network. */
const val DEFAULT_COMPANION_ADDRESS = "192.168.1.151:8790"

enum class Language {
    HEBREW, ENGLISH
}

enum class Gender {
    BOY, GIRL
}
