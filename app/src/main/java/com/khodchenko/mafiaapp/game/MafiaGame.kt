package com.khodchenko.mafiaapp.game

import com.khodchenko.mafiaapp.data.Day
import com.khodchenko.mafiaapp.data.GameStage
import com.khodchenko.mafiaapp.data.Player
import com.khodchenko.mafiaapp.data.Role
import com.khodchenko.mafiaapp.data.Team

class MafiaGame(
    var gameStage: GameStage,
    var gameInProgress: Boolean
) {
    private var currentDay: Day = Day(1)
    private lateinit var currentPlayer: Player
    private lateinit var players: List<Player>
    private var blackTeam: Team = Team(Team.TeamColor.BLACK, mutableListOf())
    private var redTeam: Team = Team(Team.TeamColor.RED, mutableListOf())

    private var candidates: MutableMap<Player, List<Player>> = mutableMapOf()

    fun addCandidate(candidate: Player) {
        candidates[candidate] = emptyList()
    }

    fun addVotesForCandidate(candidate: Player, playerList: List<Player>) {
        val uniquePlayerList = playerList.filter { player -> !candidates.values.flatten().any { it.number == player.number } }
        candidates[candidate] = uniquePlayerList
    }

    fun findCandidatesWithLongestVotes(): List<Player> {
        val maxVoteCount = candidates.values.map { it.size }.maxOrNull()

        return maxVoteCount?.let { maxCount ->
            candidates.filterValues { it.size == maxCount }.keys.toList()
        } ?: emptyList()
    }

    fun killPlayer(player: Player) {
        player.isAlive = false
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
        currentPlayer = players.find { it.number == getCurrentDay().number } ?: players.first()
    }

    fun startVote() {
        currentDay.number += 1
        gameStage = GameStage.VOTE
        gameInProgress = true
    }

    fun startRolePickStage() {
        gameStage = GameStage.ROLE_PICK
        gameInProgress = false
    }

    fun getCurrentGameState(): GameStage = gameStage

    fun getCurrentDay(): Day = currentDay

    fun getAllPlayers(): List<Player> = players

    fun getCurrentPlayer(): Player = currentPlayer

    fun setCurrentPlayer(player: Player) {
        currentPlayer = player
    }

    fun initialPlayers(players: List<Player>) {
        players.forEach { player ->
            when (player.role) {
                Role.MAFIA, Role.DON -> blackTeam.players.add(player)
                Role.CIVIL, Role.SHERIFF -> redTeam.players.add(player)
            }
        }
        this.players = players
    }

    fun checkEndGame(): Boolean {
        val aliveBlackTeamSize = blackTeam.players.count { it.isAlive }
        val aliveRedTeamSize = redTeam.players.count { it.isAlive }

        return aliveBlackTeamSize == 0 || aliveBlackTeamSize == aliveRedTeamSize
    }

    fun getWinningTeam(): Team {
        val aliveBlackTeamSize = blackTeam.players.count { it.isAlive }
        val aliveRedTeamSize = redTeam.players.count { it.isAlive }

        return if (aliveBlackTeamSize == 0 || aliveBlackTeamSize == aliveRedTeamSize) redTeam else blackTeam
    }
}
