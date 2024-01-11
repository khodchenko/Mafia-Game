package com.khodchenko.mafiaapp.helpers

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.khodchenko.mafiaapp.R

class SoundPlayer(context: Context) {
    private val mediaPlayer: MediaPlayer = MediaPlayer.create(context, R.raw.shot)

    fun playShootSound() {
        mediaPlayer.start()
    }

    @Composable
    fun DisposableSoundPlayer() {
        val context = LocalContext.current
        val player = remember { SoundPlayer(context) }

        DisposableEffect(player) {
            onDispose {
                player.mediaPlayer.release()
            }
        }
    }
}