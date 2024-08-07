package com.example.mafiaapplication.game

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mafiaapplication.data.Player
import com.example.mafiaapplication.data.Role
import com.example.mafiaapplication.data.Scores

@Entity(tableName = "game_state")
data class GameState(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var stage: GameStage,
    var day: Int,
    var players: List<Player>,
    var currentPlayerIndex: Int,
    var randomPlayerRoles: Boolean = false,
    var generateDumbPlayersList: Boolean = false,
    var numbersOfPlayers: Int = 10
) {
    private var candidates: MutableMap<Player, List<Player>> = mutableMapOf()

    private fun getUniquePlayers(playerList: List<Player>): List<Player> {
        return playerList.filter { player ->
            !candidates.values.flatten().any { it.number == player.number }
        }
    }

    fun addCandidate(player: Player, candidate: Player) {
        awardPointsForMakeCandidate(player, candidate)
        candidates[candidate] = emptyList()
    }

    fun addVotesForCandidate(candidate: Player, playerList: List<Player>) {
        val uniquePlayerList = getUniquePlayers(playerList)
        uniquePlayerList.forEach { player ->
            awardPointsForVoting(player, candidate)
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
        return if (players.isEmpty()) {
            null
        } else {
            val nextIndex = (currentPlayerIndex + 1) % players.size
            players.getOrNull(nextIndex)
        }
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

    fun checkFaults(): Boolean {
        val alivePlayers = getAllAlivePlayers()

        for (player in alivePlayers) {
            if (player.fouls >= 4) {
                killPlayer(player)
                deductionPointsForDisqualification(player)
                currentPlayerIndex = players.indexOf(player)
                return true
            }
        }
        return false
    }

    fun newDay() {
        day += 1
    }

    fun killPlayer(player: Player): Boolean {
        return if (player.isAlive) {
            player.isAlive = false
            true
        } else {
            false
        }
    }

    fun getAllAlivePlayers(): List<Player> = players.filter { it.isAlive }

    fun getAllDeadPlayers(): List<Player> = players.filter { !it.isAlive }

    fun setCurrentPlayer(player: Player) {
        currentPlayerIndex = players.indexOf(player)
    }

    fun checkEndGame(): Boolean {
        val aliveBlackTeamSize =
            players.count { it.role == Role.MAFIA && it.isAlive || it.role == Role.DON && it.isAlive }
        val aliveRedTeamSize =
            players.count { it.role == Role.CIVIL && it.isAlive || it.role == Role.SHERIFF && it.isAlive }

        return aliveBlackTeamSize == 0 || aliveBlackTeamSize == aliveRedTeamSize
    }

    fun getWinningTeam(): String {
        val aliveBlackTeamSize = players.count { it.role == Role.MAFIA || it.role == Role.DON }
        val aliveRedTeamSize = players.count { it.role == Role.CIVIL || it.role == Role.SHERIFF }

        return if (aliveBlackTeamSize == 0 || aliveBlackTeamSize == aliveRedTeamSize) "Red" else "Black"
    }

    fun awardPointsToWinningTeam() {
        val winningTeam = getWinningTeam()
        if (winningTeam == "Black") {
            players.forEach { player ->
                if (player.role == Role.MAFIA || player.role == Role.DON) {
                    player.score += Scores.WIN_TEAM_SCORE
                }
            }
        } else if (winningTeam == "Red") {
            players.forEach { player ->
                if (player.role == Role.CIVIL || player.role == Role.SHERIFF) {
                    player.score += Scores.WIN_TEAM_SCORE
                }
            }
        }
    }

    private fun deductionPointsForDisqualification(player: Player) {
        player.score -= Scores.DISQUALIFICATION_SCORE
    }

    fun awardPointsForBestMove(player: Player, listOfPlayers: List<Player>) {
        if (player.role == Role.CIVIL || player.role == Role.SHERIFF) {
            if (listOfPlayers.count { it.role == Role.MAFIA || it.role == Role.DON } == 2) {
                player.score += Scores.BEST_MOVE_2_SCORE
            } else if (listOfPlayers.count { it.role == Role.MAFIA || it.role == Role.DON } == 3) {
                player.score += Scores.BEST_MOVE_3_SCORE
            }
        }
    }

    private fun awardPointsForMakeCandidate(player: Player, candidate: Player) {
        if (player.role == Role.CIVIL || player.role == Role.SHERIFF && candidate.role == Role.MAFIA || candidate.role == Role.DON) {
            player.score += Scores.MAKE_CANDIDATE_OPPOSITE_TEAM_SCORE
        } else if (player.role == Role.MAFIA || player.role == Role.DON && candidate.role == Role.CIVIL || candidate.role == Role.SHERIFF) {
            player.score += Scores.MAKE_CANDIDATE_OPPOSITE_TEAM_SCORE
        } else {
            player.score -= Scores.MAKE_CANDIDATE_SAME_TEAM_SCORE
        }
    }

    private fun awardPointsForVoting(player: Player, candidate: Player){
        if (player.role == Role.CIVIL || player.role == Role.SHERIFF && candidate.role == Role.MAFIA || candidate.role == Role.DON) {
            player.score += Scores.VOTING_OPPOSITE_TEAM_SCORE
        } else if (player.role == Role.MAFIA || player.role == Role.DON && candidate.role == Role.CIVIL || candidate.role == Role.SHERIFF) {
            player.score += Scores.VOTING_OPPOSITE_TEAM_SCORE
        } else {
            player.score -= Scores.VOTING_SAME_TEAM_SCORE
        }
    }
}

enum class GameStage {
    START,
    NIGHT,
    DAY,
    VOTE,
    VOTE_2,
    VOTE_3,
    ROLE_PICK_RANDOM,
    ROLE_PICK,
    GAME_OVER
}