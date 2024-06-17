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

    private var randomPlayerRoles: Boolean = false
    private var generateDumbPlayersList: Boolean = false
    private var numbersOfPlayers: Int = 10

    fun getNumberOfPlayers(): Int = numbersOfPlayers
    fun setNumberOfPlayers(numberOfPlayers: Int) {
        numbersOfPlayers = numberOfPlayers
    }

    fun onOffRandomPlayerRoles() {
        randomPlayerRoles = !randomPlayerRoles
    }

    fun addCandidate(candidate: Player) {
        candidates[candidate] = emptyList()
    }

    fun addVotesForCandidate(candidate: Player, playerList: List<Player>) {
        val uniquePlayerList = playerList.filter { player ->
            !candidates.values.flatten().any { it.number == player.number }
        }
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
        val candidateKeys = candidates.keys.toList()
        val currentIndex = candidateKeys.indexOf(currentPlayer)

        return candidateKeys.getOrNull(currentIndex + 1)
    }

    fun getVotersByCandidate(candidate: Player): MutableList<Player>? {
        return candidates[candidate]?.toMutableList()
    }

    fun removeCandidatesExceptMaxVotes() {
        val maxVotes = candidates.values.maxOfOrNull { it.size }

        maxVotes?.let { maxCount ->
            val candidatesWithMaxVotes =
                candidates.filterValues { it.size == maxCount }.keys.toList()
            candidates = candidates.filterKeys { it in candidatesWithMaxVotes }.toMutableMap()
        }
    }

    fun clearVoters() {
        for (candidate in candidates.keys) {
            candidates[candidate] = emptyList()
        }
    }

    fun clearVote() {
        candidates = mutableMapOf()
    }

    fun checkFaults() : Boolean {
        val alivePlayers = getAllAlivePlayers()

        for (player in alivePlayers) {
            if (player.fouls >= 4) {
                killPlayer(player)
                currentPlayer = player
                return true
            }
        }

        return false
    }

    fun newDay() {
        currentDay += 1
    }

    fun killPlayer(player: Player) {
        player.isAlive = false
    }

    fun getCurrentStage(): GameStage {
        return gameStage
    }

    fun setStage(stage: GameStage) {
        gameStage = stage
    }

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

    fun getGenerateDumbPlayersList(): Boolean {
        return generateDumbPlayersList
    }

    fun onOffGenerateDumbPlayersList() {
        generateDumbPlayersList = !generateDumbPlayersList
    }

    fun awardPointsToWinningTeam() {
        val winningTeam = getWinningTeam()
        winningTeam.players.forEach { player ->
            player.score += 1.0
        }
    }

}
