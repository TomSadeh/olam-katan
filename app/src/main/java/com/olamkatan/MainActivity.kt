package com.olamkatan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.olamkatan.audio.SfxPlayer
import com.olamkatan.audio.TtsManager

class MainActivity : ComponentActivity() {

    private lateinit var ttsManager: TtsManager
    private lateinit var sfxPlayer: SfxPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        ttsManager = TtsManager(this)
        sfxPlayer = SfxPlayer(this)

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
                settingsRepository = app.settingsRepository,
                progressRepository = app.progressRepository
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ttsManager.shutdown()
        sfxPlayer.release()
    }
}
