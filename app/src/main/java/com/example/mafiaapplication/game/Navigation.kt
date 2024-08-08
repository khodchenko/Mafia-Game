package com.example.mafiaapplication.game

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mafiaapplication.helpers.SharedPreferencesHelper
import com.example.mafiaapplication.ui.element.SettingsScreen
import com.khodchenko.mafiaapp.data.Screen
import com.example.mafiaapplication.ui.stage.DayStage
import com.example.mafiaapplication.ui.stage.EndGameStage
import com.example.mafiaapplication.ui.stage.LastWordsStage
import com.example.mafiaapplication.ui.stage.NightStage
import com.example.mafiaapplication.ui.stage.RolePickerStage
import com.example.mafiaapplication.ui.stage.StartGameStage
import com.example.mafiaapplication.ui.stage.VoteMainStage
import com.example.mafiaapplication.ui.stage.VoteStage

@Composable
fun Navigation(
    navController: NavHostController,
    startDestination: String,
    sharedPreferencesHelper: SharedPreferencesHelper,
    gameState: GameState,
    statisticManager : StatisticManager
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = Screen.StartGameScreen.route) {
            StartGameStage(navController = navController, gameState = gameState)
        }
        composable(route = Screen.RolePickerScreen.route, arguments = listOf()) {
            RolePickerStage(
                navController = navController,
                gameState = gameState,
                sharedPreferencesHelper = sharedPreferencesHelper
            )
        }
        composable(route = Screen.NightStageScreen.route, arguments = listOf()) {
            NightStage(
                navController = navController,
                gameState = gameState,
                sharedPreferencesHelper = sharedPreferencesHelper,
                statisticManager = statisticManager
            )
        }
        composable(route = Screen.DayStageScreen.route, arguments = listOf()) {
            DayStage(
                navController = navController,
                gameState = gameState,
                sharedPreferencesHelper = sharedPreferencesHelper,
                statisticManager = statisticManager
            )
        }
        composable(route = Screen.VoteMainStageScreen.route, arguments = listOf()) {
            VoteMainStage(
                navController = navController,
                gameState = gameState,
               statisticManager = statisticManager
            )
        }
        composable(route = Screen.VoteStageScreen.route, arguments = listOf()) {
            VoteStage(
                navController = navController,
                gameState = gameState,
                statisticManager = statisticManager
            )
        }
        composable(route = Screen.LastWordsScreen.route, arguments = listOf()) {
            LastWordsStage(
                navController = navController,
                gameState = gameState,
                statisticManager = statisticManager
            )
        }
        composable(route = Screen.EndGameStageScreen.route, arguments = listOf()) {
            EndGameStage(
                gameState = gameState,
                statisticManager = statisticManager
            )
        }
        composable(route = Screen.SettingsScreen.route, arguments = listOf()) {
            SettingsScreen(
                navController = navController,
                sharedPreferencesHelper = sharedPreferencesHelper,
                gameState = gameState
            )
        }
    }
}

