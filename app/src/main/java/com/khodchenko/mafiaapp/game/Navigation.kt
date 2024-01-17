package com.khodchenko.mafiaapp.game

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.khodchenko.mafiaapp.data.Screen
import com.khodchenko.mafiaapp.helpers.BluetoothHelper
import com.khodchenko.mafiaapp.ui.stage.DayStage
import com.khodchenko.mafiaapp.ui.stage.EndGameStage
import com.khodchenko.mafiaapp.ui.stage.NightStage
import com.khodchenko.mafiaapp.ui.stage.RolePickerStage
import com.khodchenko.mafiaapp.ui.stage.StartGameStage
import com.khodchenko.mafiaapp.ui.stage.VoteStage


@Composable
fun Navigation(game: MafiaGame, bluetoothHelper: BluetoothHelper) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.StartGameScreen.route) {
        composable(route = Screen.StartGameScreen.route) {
            StartGameStage(navController = navController)
        }
        composable(route = Screen.RolePickerScreen.route, arguments = listOf()) {
           RolePickerStage(navController = navController, game = game)
        }
        composable(route = Screen.NightStageScreen.route, arguments = listOf()) {
            NightStage(navController = navController, game = game)
        }
        composable(route = Screen.DayStageScreen.route, arguments = listOf()) {
            DayStage(navController = navController, game = game)
        }
        composable(route = Screen.VoteStageScreen.route, arguments = listOf()) {
            VoteStage(navController = navController, game = game)
        }
        composable(route = Screen.EndGameStageScreen.route, arguments = listOf()) {
            EndGameStage(game = game)
        }

    }
}