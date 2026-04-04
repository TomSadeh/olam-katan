package com.olamkatan.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import com.olamkatan.data.models.Language
import java.util.Locale

class TtsManager(context: Context) : TextToSpeech.OnInitListener {

    private val tts = TextToSpeech(context, this)
    private var isReady = false

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isReady = true
            setLanguage(Language.HEBREW)
        }
    }

    fun setLanguage(language: Language) {
        if (!isReady) return
        val locale = when (language) {
            Language.HEBREW -> Locale("he", "IL")
            Language.ENGLISH -> Locale.US
        }
        tts.language = locale
    }

    fun speak(text: String, flush: Boolean = true) {
        if (!isReady) return
        val queueMode = if (flush) TextToSpeech.QUEUE_FLUSH else TextToSpeech.QUEUE_ADD
        tts.speak(text, queueMode, null, text.hashCode().toString())
    }

    fun stop() {
        tts.stop()
    }

    fun shutdown() {
        tts.stop()
        tts.shutdown()
    }
}
