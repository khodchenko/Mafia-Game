package com.khodchenko.mafiaapp

sealed class Screen(val route: String) {
    object StartGameScreen : Screen("start_screen")
    object RolePickerScreen : Screen("role_picker_screen")
}