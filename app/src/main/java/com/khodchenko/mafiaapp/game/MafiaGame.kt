package com.khodchenko.mafiaapp.game

import com.khodchenko.mafiaapp.data.Day
import com.khodchenko.mafiaapp.data.GameStage
import com.khodchenko.mafiaapp.data.Player
import com.khodchenko.mafiaapp.data.Role
import com.khodchenko.mafiaapp.data.Team

class MafiaGame(
    var currentDay: Day,
    var players: List<Player>,
    var currentPlayer: Player,
    var gameStage: GameStage,
    var gameInProgress: Boolean,
    var winnerTeam: Team?
) {

    init {
        // Начальная настройка игры
        val initialDay = Day(1)
        val initialPlayers = generateInitialPlayers()
        gameStage = GameStage.NIGHT // начинаем с ночной стадии
    }
    // Реализуйте методы для обновления состояния игры

    fun startNewDay() {
        val newDayNumber = currentDay.number + 1
        val newDay = Day(newDayNumber)
        currentDay = newDay
        gameInProgress = true
    }

    fun startRolePickRandom(player: Player) {
        currentPlayer = player
        gameStage = GameStage.ROLE_PICK_RANDOM
        gameInProgress = false
    }

    fun startNight() {
        currentPlayer = players[0]
        gameStage = GameStage.NIGHT
        gameInProgress = true
    }

    fun startDay() {
        currentPlayer = players[0]
        gameStage = GameStage.DAY
        gameInProgress = true
    }

    fun startVote() {
        currentPlayer = players[0]
        gameStage = GameStage.VOTE
        gameInProgress = true
    }

    fun startRolePickStage() {
        currentPlayer = players[0]
        gameStage = GameStage.ROLE_PICK
        gameInProgress = false
    }

    fun assignRole(player: Player, role: Role) {
        players = players.map {
            if (it.number == player.number) it.copy(role = role) else it
        }
        gameStage = GameStage.NIGHT // после выбора ролей переходим на ночную стадию
        gameInProgress = true
    }

    // Метод для получения текущего состояния игры
    fun getCurrentGameState(): GameStage {
        return gameStage
    }

    // Реализуйте функцию для генерации начальных игроков
    private fun generateInitialPlayers(): MutableList<Player> {
        return mutableListOf<Player>()
    }
}
