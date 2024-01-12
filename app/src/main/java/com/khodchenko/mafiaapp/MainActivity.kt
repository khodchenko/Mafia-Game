package com.khodchenko.mafiaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.khodchenko.mafiaapp.data.Day
import com.khodchenko.mafiaapp.data.GameStage
import com.khodchenko.mafiaapp.game.MafiaGame

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mafiaGame = MafiaGame(
            currentDay = Day(1),
            gameStage = GameStage.START,
            gameInProgress = true,
            winnerTeam = null
        )

        setContent {
            Navigation()
        }
    }
}


