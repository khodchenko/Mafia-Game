package com.khodchenko.mafiaapp.game

import com.khodchenko.mafiaapp.data.GameStage
import com.khodchenko.mafiaapp.data.Player
import com.khodchenko.mafiaapp.data.Role
import com.khodchenko.mafiaapp.data.Team

class MafiaGame(
    var gameStage: GameStage,
    var gameInProgress: Boolean
) {
    private var currentDay: Int = 1
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

    fun getCandidates(): MutableList<Player> {
        return candidates.keys.toMutableList()
    }

    fun getCandidatesAndVotes(): MutableMap<Player, List<Player>> = candidates

    fun getCandidatesAndVotesLog(): String {
        val logBuilder = StringBuilder()

        for ((candidate, voters) in candidates) {
            val voterNumbers = voters.map { it.number }
            val voterList = if (voterNumbers.isEmpty()) "No votes" else "Votes: $voterNumbers"
            val logMessage = "Candidate ${candidate.number}: $voterList"
            logBuilder.appendLine(logMessage)
        }

        return logBuilder.toString()
    }

    fun getNextCandidateAfterCurrentPlayer(): Player? {
        val candidateKeys = candidates.keys.toMutableList()
        val currentIndex = candidateKeys.indexOf(currentPlayer)

        return when {
            candidateKeys.isEmpty() -> null
            currentIndex == -1 || currentIndex == candidateKeys.size - 1 -> candidateKeys.first()
            else -> candidateKeys[currentIndex + 1]
        }
    }

    fun getVotersByCandidate(candidate: Player): MutableList<Player>? {
        return candidates[candidate]?.toMutableList()
    }

    fun newDay(){
      currentDay += 1
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
        currentPlayer = players.find { it.number == getCurrentDay() } ?: players.first()
    }

    fun startVote() {
        gameStage = GameStage.VOTE
        gameInProgress = true
    }

    fun startRolePickStage() {
        gameStage = GameStage.ROLE_PICK
        gameInProgress = false
    }

    fun getCurrentGameState(): GameStage = gameStage

    fun getCurrentDay(): Int = currentDay

    fun getAllPlayers(): List<Player> = players

    fun getAllAlivePlayers(): List<Player> = players.filter { it.isAlive }

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
