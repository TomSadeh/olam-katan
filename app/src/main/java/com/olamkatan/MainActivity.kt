package com.olamkatan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.olamkatan.audio.MusicPlayer
import com.olamkatan.audio.SfxPlayer
import com.olamkatan.audio.TtsManager
import com.olamkatan.audio.VoicePlayer

class MainActivity : ComponentActivity() {

    private lateinit var ttsManager: TtsManager
    private lateinit var sfxPlayer: SfxPlayer
    private lateinit var musicPlayer: MusicPlayer
    private lateinit var voicePlayer: VoicePlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        ttsManager = TtsManager(this)
        sfxPlayer = SfxPlayer(this)
        musicPlayer = MusicPlayer(this)
        voicePlayer = VoicePlayer(this)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        val app = application as OlamKatanApplication

        setContent {
            OlamKatanApp(
                ttsManager = ttsManager,
                sfxPlayer = sfxPlayer,
                musicPlayer = musicPlayer,
                voicePlayer = voicePlayer,
                settingsRepository = app.settingsRepository,
                progressRepository = app.progressRepository
            )
        }
    }

    override fun onPause() {
        super.onPause()
        musicPlayer.pause()
    }

    override fun onResume() {
        super.onResume()
        musicPlayer.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        ttsManager.shutdown()
        sfxPlayer.release()
        musicPlayer.release()
        voicePlayer.release()
    }
}
