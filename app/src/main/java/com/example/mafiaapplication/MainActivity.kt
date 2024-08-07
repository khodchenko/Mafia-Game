package com.example.mafiaapplication

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.example.mafiaapplication.ui.theme.MafiaApplicationTheme
import com.example.mafiaapplication.game.GameStage
import com.example.mafiaapplication.game.Navigation
import com.example.mafiaapplication.helpers.SharedPreferencesHelper
import com.khodchenko.mafiaapp.data.Screen

class MainActivity : ComponentActivity() {
    @SuppressLint("MutableCollectionMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MafiaApplicationTheme(darkTheme = isSystemInDarkTheme()) {
                val sharedPreferencesHelper = SharedPreferencesHelper(this)
                val navController = rememberNavController()

                val gameState by remember { mutableStateOf(sharedPreferencesHelper.loadGameState()) }

                val startDestination = when (gameState.stage) {
                    GameStage.NIGHT -> Screen.NightStageScreen.route
                    GameStage.DAY -> Screen.DayStageScreen.route
                    GameStage.ROLE_PICK -> Screen.RolePickerScreen.route
                    GameStage.START -> Screen.StartGameScreen.route

                    else -> Screen.StartGameScreen.route
                }

                Navigation(
                    navController = navController,
                    startDestination = startDestination,
                    sharedPreferencesHelper = sharedPreferencesHelper,
                    gameState = gameState
                )
            }
        }
    }
}