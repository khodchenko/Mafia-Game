package com.khodchenko.mafiaapp.game

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mafiaapplication.MainActivity
import com.khodchenko.mafiaapp.data.Screen
import com.khodchenko.mafiaapp.helpers.BluetoothHelper
import com.example.mafiaapplication.ui.stage.DayStage
import com.example.mafiaapplication.ui.stage.EndGameStage
import com.example.mafiaapplication.ui.stage.LastWordsStage
import com.example.mafiaapplication.ui.stage.NightStage
import com.example.mafiaapplication.ui.stage.RolePickerStage
import com.example.mafiaapplication.ui.stage.StartGameStage
import com.example.mafiaapplication.ui.stage.VoteMainStage
import com.example.mafiaapplication.ui.stage.VoteStage


@Composable
fun Navigation(game: MafiaGame, bluetoothHelper: BluetoothHelper) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.StartGameScreen.route) {
        composable(route = Screen.StartGameScreen.route) {
            StartGameStage(navController = navController, game = game)
        }
        composable(route = Screen.RolePickerScreen.route, arguments = listOf()) {
           RolePickerStage(navController = navController, game = game, mainActivity = MainActivity())
        }
        composable(route = Screen.NightStageScreen.route, arguments = listOf()) {
            NightStage(navController = navController, game = game)
        }
        composable(route = Screen.DayStageScreen.route, arguments = listOf()) {
            DayStage(navController = navController, game = game)
        }
        composable(route = Screen.VoteMainStageScreen.route, arguments = listOf()) {
            VoteMainStage(navController = navController, game = game)
        }
        composable(route = Screen.VoteStageScreen.route, arguments = listOf()) {
            VoteStage(navController = navController, game = game)
        }
        composable(route = Screen.LastWordsScreen.route, arguments = listOf()) {
            LastWordsStage(navController = navController, game = game)
        }
        composable(route = Screen.EndGameStageScreen.route, arguments = listOf()) {
            EndGameStage(game = game)
        }

    }
}