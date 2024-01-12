package com.khodchenko.mafiaapp.game

import com.khodchenko.mafiaapp.data.Day
import com.khodchenko.mafiaapp.data.GameStage
import com.khodchenko.mafiaapp.data.Player
import com.khodchenko.mafiaapp.data.Role

class MafiaGame(
    var gameStage: GameStage,
    var gameInProgress: Boolean
) {
    private var currentDay: Day = Day(1)
    private lateinit var currentPlayer: Player
    private lateinit var players : List<Player>

    fun changeGameStage(newGameStage: GameStage) {
        gameStage = newGameStage
    }

    fun startNewDay() {
        val newDayNumber = currentDay.number + 1
        val newDay = Day(newDayNumber)
        currentDay = newDay
        gameInProgress = true
        currentPlayer = players.find { it.number == newDayNumber } ?: players.first()
    }

    fun startRolePickRandom(player: Player) {
        currentPlayer = player
        gameStage = GameStage.ROLE_PICK_RANDOM
        gameInProgress = false
    }

    fun startNight() {
        gameStage = GameStage.NIGHT
        gameInProgress = true
    }

    fun startDay() {
        gameStage = GameStage.DAY
        gameInProgress = true
    }

    fun startVote() {
        gameStage = GameStage.VOTE
        gameInProgress = true
    }

    fun startRolePickStage() {
        gameStage = GameStage.ROLE_PICK
        gameInProgress = false
    }

    fun assignRole(player: Player, role: Role) {
        players = players.map {
            if (it.number == player.number) it.copy(role = role) else it
        }
        gameStage = GameStage.NIGHT
        gameInProgress = true
    }

    fun getCurrentGameState(): GameStage {
        return gameStage
    }

    fun getCurrentDay(): Day {
        return currentDay
    }

    fun getAllPlayers(): List<Player> {
        return players
    }

    fun getCurrentPlayer(): Player {
        return currentPlayer
    }

    fun setCurrentPlayer(player: Player) {
        currentPlayer = player
    }

    fun initialPlayers(players: List<Player>) {
        this.players = players
    }
}
