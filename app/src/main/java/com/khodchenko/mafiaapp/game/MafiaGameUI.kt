package com.khodchenko.mafiaapp.game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun MafiaGameUI(game: MafiaGame) {
    var gameState by remember(game) { mutableStateOf(game.getCurrentGameState()) }
//
//    LaunchedEffect(gameState) {
//        gameState = game.getCurrentGameState()
//    }
//    // Отображение текущего экрана в зависимости от стадии игры
//    when (gameState) {
//        GameStage.NIGHT -> {
//            NightStage(
//                players = game.getAllPlayers().toMutableList(),
//                currentDay = game.currentDay
//            )
//        }
//
//        GameStage.DAY -> {
//            DayStage(players = game.getAlivePlayers(), currentDay = game.currentDay)
//        }
//
//        GameStage.VOTE -> {
//            // Добавьте код для стадии голосования при необходимости
//        }
//
//        GameStage.ROLE_PICK -> {
//            // Отображение UI для стадии выбора ролей
////            RolePickerStage(players = game.getAllPlayers().toMutableList()) { player, role ->
////                game.assignRole(player, role)
////            }
//        }
//
//        GameStage.ROLE_PICK_RANDOM -> {
//            RolePickerRandom(player = game.getCurrentPlayer())
//        }
//
//        GameStage.GAME_OVER -> {
//            // Отображение UI для экрана окончания игры
//            EndGameStage(
//                playerList = game.getAllPlayers().toMutableList(), currentDay = game.currentDay,
//                winnerTeam = game.winnerTeam!!
//            )
//        }
//
//        GameStage.START -> {
//            StartGameStageUI(navController = )
//        }
//    }
}