package com.khodchenko.mafiaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.khodchenko.mafiaapp.data.GameStage
import com.khodchenko.mafiaapp.game.MafiaGame

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mafiaGame = MafiaGame(
            gameStage = GameStage.START,
            gameInProgress = true
        )

        setContent {
            Navigation(mafiaGame)
        }
    }
}


