package com.example.mafiaapplication.ui.stage

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mafiaapplication.game.GameStage
import com.example.mafiaapplication.game.GameState
import com.example.mafiaapplication.data.Player
import com.example.mafiaapplication.data.Role
import com.example.mafiaapplication.game.StatisticManager
import com.example.mafiaapplication.game.Type
import com.khodchenko.mafiaapp.data.Screen
import com.example.mafiaapplication.ui.element.CustomElevatedButton
import com.example.mafiaapplication.ui.theme.Background

@Composable
fun VoteStage(
    navController: NavController,
    gameState: GameState,
    statisticManager: StatisticManager
) {

    var voters by remember { mutableStateOf(emptyList<Player>()) }
    var isAllSelected by remember { mutableStateOf(false) }

    val nonVotedPlayers = gameState.players.filter { player ->
        !gameState.getCandidatesAndVotes().values.flatten().any { it.number == player.number }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(8.dp)
    ) {
        Column {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "На голосовании:",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = gameState.players[gameState.currentPlayerIndex].name,
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp),
                text = "Выберите ниже кто голосует \nпротив этого игрока:",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp, bottom = 2.dp)
                    .clickable {
                        isAllSelected = !isAllSelected
                        voters = if (isAllSelected) {
                            nonVotedPlayers.toList()
                        } else {
                            emptyList()
                        }
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Выбрать всех",
                    modifier = Modifier.padding(end = 4.dp),
                    color = Color.White,
                    fontSize = 28.sp
                )
            }

            LazyColumn {
                items(nonVotedPlayers) { otherPlayer ->
                    val isSelected = voters.contains(otherPlayer)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 2.dp, bottom = 2.dp)
                            .clickable {
                                voters = if (isSelected) {
                                    voters
                                        .toMutableList()
                                        .apply { remove(otherPlayer) }
                                } else {
                                    voters
                                        .toMutableList()
                                        .apply { add(otherPlayer) }
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = null,
                            colors = CheckboxDefaults.colors(
                                checkmarkColor = Background,
                                checkedColor = Color.White,
                                uncheckedColor = Color.White.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier.padding(4.dp)
                        )
                        Text(
                            text = "${otherPlayer.number}:",
                            modifier = Modifier.padding(end = 4.dp),
                            color = Color.White,
                            fontSize = 28.sp
                        )
                        Text(
                            text = otherPlayer.name,
                            color = Color.White,
                            fontSize = 28.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, bottom = 50.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CustomElevatedButton("Голосуем", enabled = true, onClick = {
                    handleVoteButtonClick(navController, gameState, statisticManager, voters)
                })
            }
        }
    }
}

private fun handleVoteButtonClick(
    navController: NavController,
    gameState: GameState,
    statisticManager: StatisticManager,
    voters: List<Player>
) {
    gameState.addVotesForCandidate(
        gameState.players[gameState.currentPlayerIndex],
        voters
    )
    Log.d("VoteStage", gameState.getCandidatesAndVotesLog())

    if (gameState.getCandidates()
            .last() == gameState.players[gameState.currentPlayerIndex]
    ) {
        handleLastCandidate(navController, gameState, statisticManager)
    } else {
        handleNextCandidate(navController, gameState)
    }
}

private fun handleLastCandidate(
    navController: NavController,
    gameState: GameState,
    statisticManager: StatisticManager
) {
    when {
        gameState.findCandidatesWithLongestVotes().size == 1 -> {
            handleSingleCandidate(navController, gameState, statisticManager)
        }
        gameState.findCandidatesWithLongestVotes().size > 1 && gameState.stage != GameStage.VOTE_2 -> {
            handleMultipleCandidatesSecondRound(navController, gameState, statisticManager)
        }
        gameState.findCandidatesWithLongestVotes().size > 1 && gameState.stage == GameStage.VOTE_2 -> {
            handleMultipleCandidatesThirdRound(navController, gameState, statisticManager)
        }
    }
}

private fun handleSingleCandidate(
    navController: NavController,
    gameState: GameState,
    statisticManager: StatisticManager
) {
    val kickedPlayer = gameState.findCandidatesWithLongestVotes()[0]
    gameState.killPlayer(kickedPlayer)
    statisticManager.addEntry(
        "Player ${kickedPlayer.name} выгнан по результатам голосования, он был единственным кандидатом",
        Type.SIMPLE
    )
    gameState.clearVote()
    gameState.newDay()
    gameState.stage = GameStage.NIGHT
    statisticManager.addEntry(
        "Day:${gameState.day} last words of player ${kickedPlayer.number}.${kickedPlayer.name}",
        Type.SIMPLE
    )
    navController.navigate(Screen.LastWordsScreen.route)
}

private fun handleMultipleCandidatesSecondRound(
    navController: NavController,
    gameState: GameState,
    statisticManager: StatisticManager
) {
    gameState.removeCandidatesExceptMaxVotes()
    gameState.stage = GameStage.VOTE_2
    gameState.currentPlayerIndex =
        gameState.players.indexOf(gameState.getCandidates()[0])
    gameState.clearVoters()
    navController.navigate(Screen.VoteMainStageScreen.route)
    statisticManager.addEntry("Day:${gameState.day} кандидатов больше одного, второй тур голосования, проголосовали: ${gameState.getCandidatesAndVotes()}", Type.SIMPLE)
}

private fun handleMultipleCandidatesThirdRound(
    navController: NavController,
    gameState: GameState,
    statisticManager: StatisticManager
) {
    gameState.setCurrentPlayer(gameState.getCandidates()[0])
    gameState.clearVoters()
    gameState.stage = GameStage.VOTE_3
    navController.navigate(Screen.VoteMainStageScreen.route)
    statisticManager.addEntry("Day:${gameState.day} кандидатов больше одного, третий тур голосования, проголосовали: ${gameState.getCandidatesAndVotes()}", Type.SIMPLE)
}

private fun handleNextCandidate(
    navController: NavController,
    gameState: GameState
) {
    gameState.getNextCandidateAfterCurrentPlayer()
        ?.let { gameState.setCurrentPlayer(it) }
    navController.navigate(Screen.VoteMainStageScreen.route)
}

@Preview(showBackground = true)
@Composable
fun VoteStagePreview() {
    val navController = rememberNavController()
    val samplePlayers = listOf(
        Player(1, 0, 1,"Player 1", Role.CIVIL),
        Player(2, 0, 1,"Player 2", Role.CIVIL),
        Player(3, 0, 1,"Player 3", Role.CIVIL)
    )
    val gameState = GameState(
        players = samplePlayers,
        currentPlayerIndex = 0,
        day = 1,
        stage = GameStage.VOTE
    )
    val statisticManager = StatisticManager(LocalContext.current)

    VoteStage(navController, gameState, statisticManager)
}