package com.example.mafiaapplication.game

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mafiaapplication.MainActivity
import com.example.mafiaapplication.data.GameState
import com.example.mafiaapplication.data.Player
import com.example.mafiaapplication.helpers.SharedPreferencesHelper
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
    game: MafiaGame,
    startDestination: String,
    sharedPreferencesHelper: SharedPreferencesHelper,
    gameState: GameState,
    players: List<Player>
) {
    NavHost(navController = navController, startDestination = Screen.StartGameScreen.route) {
        composable(route = Screen.StartGameScreen.route) {
            StartGameStage(navController = navController, game = game)
        }
        composable(route = Screen.RolePickerScreen.route, arguments = listOf()) {
            RolePickerStage(
                navController = navController,
                game = game,
                mainActivity = MainActivity(),
                sharedPreferencesHelper = sharedPreferencesHelper
            )
        }
        composable(route = Screen.NightStageScreen.route, arguments = listOf()) {
            NightStage(
                navController = navController,
                game = game,
                sharedPreferencesHelper = sharedPreferencesHelper
            )
        }
        composable(route = Screen.DayStageScreen.route, arguments = listOf()) {
            DayStage(
                navController = navController,
                game = game,
                sharedPreferencesHelper = sharedPreferencesHelper
            )
        }
        composable(route = Screen.VoteMainStageScreen.route, arguments = listOf()) {
            VoteMainStage(
                navController = navController,
                game = game,
                sharedPreferencesHelper = sharedPreferencesHelper
            )
        }
        composable(route = Screen.VoteStageScreen.route, arguments = listOf()) {
            VoteStage(
                navController = navController,
                game = game,
                sharedPreferencesHelper = sharedPreferencesHelper
            )
        }
        composable(route = Screen.LastWordsScreen.route, arguments = listOf()) {
            LastWordsStage(
                navController = navController,
                game = game,
                sharedPreferencesHelper = sharedPreferencesHelper
            )
        }
        composable(route = Screen.EndGameStageScreen.route, arguments = listOf()) {
            EndGameStage(game = game)
        }
    }
}