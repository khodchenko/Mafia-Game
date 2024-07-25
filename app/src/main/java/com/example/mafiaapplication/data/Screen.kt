package com.khodchenko.mafiaapp.data

sealed class Screen(val route: String) {
    object StartGameScreen : Screen("start_screen")
    object RolePickerScreen : Screen("role_picker_screen")
    object NightStageScreen : Screen("night_stage_screen")
    object DayStageScreen : Screen("day_stage_screen")
    object VoteMainStageScreen : Screen("vote_main_stage_screen")
    object VoteStageScreen : Screen("vote_stage_screen")
    object LastWordsScreen : Screen("last_words_screen")
    object EndGameStageScreen : Screen("end_game_screen")
}