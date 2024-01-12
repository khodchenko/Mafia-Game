package com.khodchenko.mafiaapp.ui.stage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khodchenko.mafiaapp.data.Day
import com.khodchenko.mafiaapp.data.Player
import com.khodchenko.mafiaapp.data.Role
import com.khodchenko.mafiaapp.ui.theme.Background
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import com.khodchenko.mafiaapp.ui.SimpleElevatedButton

@Composable
fun VoteStage(player: Player, allPlayers: List<Player>) {
    var voters by remember { mutableStateOf(allPlayers.filter { it == player }) }
    val alivePlayers by remember { mutableStateOf(allPlayers.toMutableList()) }

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
                text = "На голосовании:\n${player.name}",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Normal,
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
                items(alivePlayers) { otherPlayer ->
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                SimpleElevatedButton("Голосуем", onClick = { /* Handle button click here */ }) {
                }
            }

        }
    }
}

@Preview
@Composable
private fun VoteStagePreview() {
    val players = mutableListOf<Player>(
        Player(1, "Player 1", Role.MAFIA),
        Player(2, "Player 2", Role.CIVIL),
        Player(3, "Player 3", Role.CIVIL),
        Player(4, "Player 4", Role.CIVIL),
        Player(5, "Player 5", Role.CIVIL),
        Player(6, "Player 6", Role.CIVIL),
        Player(7, "Player 7", Role.CIVIL),
        Player(8, "Player 8", Role.CIVIL),
        Player(9, "Player 9", Role.CIVIL),
        Player(10, "Player 10", Role.CIVIL)
    )
    val day = Day(1)
    VoteStage(players[0], players)
}