package com.khodchenko.mafiaapp.game

import androidx.compose.runtime.Composable
import com.khodchenko.mafiaapp.data.GameStage
import com.khodchenko.mafiaapp.ui.stage.DayStage
import com.khodchenko.mafiaapp.ui.stage.EndGameStage
import com.khodchenko.mafiaapp.ui.stage.NightStage
import com.khodchenko.mafiaapp.ui.stage.RolePickerRandom
import com.khodchenko.mafiaapp.ui.stage.RolePickerStage
import com.khodchenko.mafiaapp.ui.stage.StartGameStageUI

@Composable
fun MafiaGameUI(game: MafiaGame) {
    val gameState = game.getCurrentGameState()

    when (gameState) {
        GameStage.NIGHT -> {
            NightStage(game.players.toMutableList(), game.currentDay)
        }

        GameStage.DAY -> {
            DayStage(game.currentDay)
        }

        GameStage.VOTE -> {

        }

        GameStage.ROLE_PICK -> {
            // Отображение UI для стадии выбора ролей
            RolePickerStage(game.players.toMutableList()) { player, role ->
                game.assignRole(player, role)
            }
        }

        GameStage.ROLE_PICK_RANDOM -> {
            RolePickerRandom(player = game.currentPlayer)
        }

        GameStage.GAME_OVER -> {
            // Отображение UI для экрана окончания игры
            EndGameStage(
                game.players.toMutableList(), game.currentDay,
                game.winnerTeam!!
            )
        }

        else -> {
            // Отображение UI для стартового экрана
            StartGameStageUI()
        }
    }
}
