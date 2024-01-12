package com.khodchenko.mafiaapp

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.khodchenko.mafiaapp.ui.stage.RolePickerStage
import com.khodchenko.mafiaapp.ui.stage.StartGameStage

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.StartGameScreen.route) {
        composable(route = Screen.StartGameScreen.route) {
            StartGameStage(navController = navController)
        }
        composable(route = Screen.RolePickerScreen.route, arguments = listOf()) {
           RolePickerStage()
        }
    }
}