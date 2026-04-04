package com.olamkatan.audio

import android.content.Context
import android.media.MediaPlayer
import com.olamkatan.data.models.Language

class VoicePlayer(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    var enabled = true

    fun play(voiceKey: String, language: Language, onComplete: (() -> Unit)? = null) {
        if (!enabled) {
            onComplete?.invoke()
            return
        }
        stop()
        val suffix = when (language) {
            Language.HEBREW -> "_he"
            Language.ENGLISH -> "_en"
        }
        val resName = "${voiceKey}$suffix"
        val resId = context.resources.getIdentifier(resName, "raw", context.packageName)
        if (resId == 0) {
            onComplete?.invoke()
            return
        }
        mediaPlayer = MediaPlayer.create(context, resId)?.apply {
            setOnCompletionListener {
                it.release()
                mediaPlayer = null
                onComplete?.invoke()
            }
            start()
        } ?: run {
            onComplete?.invoke()
            null
        }
    }

    fun playSequence(voiceKeys: List<String>, language: Language, onAllComplete: (() -> Unit)? = null) {
        if (voiceKeys.isEmpty()) {
            onAllComplete?.invoke()
            return
        }
        play(voiceKeys.first(), language) {
            playSequence(voiceKeys.drop(1), language, onAllComplete)
        }
    }

    fun stop() {
        mediaPlayer?.apply {
            if (isPlaying) stop()
            release()
        }
        mediaPlayer = null
    }

    fun release() {
        stop()
    }
}
