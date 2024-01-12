package com.khodchenko.mafiaapp.game

import com.khodchenko.mafiaapp.data.Day
import com.khodchenko.mafiaapp.data.GameStage
import com.khodchenko.mafiaapp.data.Player
import com.khodchenko.mafiaapp.data.Role
import com.khodchenko.mafiaapp.data.Team

class MafiaGame(
    var currentDay: Day,
    var gameStage: GameStage,
    var gameInProgress: Boolean,
    var winnerTeam: Team?
) {

    private lateinit var currentPlayer: Player
    private var players : List<Player>
    init {
        players = mutableListOf()
    }

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

    fun getAllPlayers(): List<Player> {
        return players
    }

    fun getCurrentPlayer(): Player {
        return currentPlayer
    }

    fun getAlivePlayers(): MutableList<Player> {
        return players.filter { it.isAlive }.toMutableList()
    }

    private fun generateInitialPlayers(): MutableList<Player> {
        return mutableListOf(
            Player(1, "Player 1", Role.MAFIA),
            Player(2, "Player 2", Role.CIVIL),
            // добавьте остальных игроков
        )
    }
}
