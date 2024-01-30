package com.khodchenko.mafiaapp.ui.stage

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.khodchenko.mafiaapp.data.GameStage
import com.khodchenko.mafiaapp.data.Player
import com.khodchenko.mafiaapp.data.Screen
import com.khodchenko.mafiaapp.game.MafiaGame
import com.khodchenko.mafiaapp.ui.CustomElevatedButton
import com.khodchenko.mafiaapp.ui.theme.Background

@Composable
fun VoteStage(navController: NavController, game: MafiaGame) {

    var voters by remember { mutableStateOf(emptyList<Player>()) }

    val nonVotedPlayers = game.getAllPlayers().filter { player ->
        !game.getCandidatesAndVotes().values.flatten().any { it.number == player.number }
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
                text = game.getCurrentPlayer().name,
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp),
                text = "Выберете ниже кто голосует \nпротив этого игрока:",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )

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
                    game.addVotesForCandidate(game.getCurrentPlayer(), voters)

                    Log.d("VoteStage", game.getCandidatesAndVotesLog())
                    if (game.getCandidates().last() == game.getCurrentPlayer()) {
                        if (game.findCandidatesWithLongestVotes().size == 1) {
                            game.killPlayer(game.findCandidatesWithLongestVotes()[0])
                            game.clearVote()
                            game.newDay()
                            game.setStage(GameStage.NIGHT)
                            navController.navigate(Screen.LastWordsScreen.route)
                        } else if (game.findCandidatesWithLongestVotes().size > 1 && game.getCurrentStage() != GameStage.VOTE_2) {
                            game.removeCandidatesExceptMaxVotes()
                            game.setStage(GameStage.VOTE_2)
                            game.setCurrentPlayer(game.getCandidates()[0])
                            game.clearVoters()
                            navController.navigate(Screen.VoteMainStageScreen.route)
                        } else if (game.findCandidatesWithLongestVotes().size > 1 && game.getCurrentStage() == GameStage.VOTE_2) {
                            game.setCurrentPlayer(game.getCandidates()[0])
                            game.clearVoters()
                            game.setStage(GameStage.VOTE_3)
                            navController.navigate(Screen.VoteMainStageScreen.route)
                        }
                        Log.d("VoteStage", "Stage: ${game.getCurrentStage()}")
                    } else {
                        game.getNextCandidateAfterCurrentPlayer()?.let { game.setCurrentPlayer(it) }
                        navController.navigate(Screen.VoteMainStageScreen.route)
                    }
                })

            }
        }
    }
}
