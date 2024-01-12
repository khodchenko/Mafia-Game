package com.khodchenko.mafiaapp.ui.stage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khodchenko.mafiaapp.R
import com.khodchenko.mafiaapp.data.Day
import com.khodchenko.mafiaapp.data.Player
import com.khodchenko.mafiaapp.data.Role
import com.khodchenko.mafiaapp.data.Team
import com.khodchenko.mafiaapp.ui.PlayerList
import com.khodchenko.mafiaapp.ui.SimpleElevatedButton
import com.khodchenko.mafiaapp.ui.theme.Background

@Composable
fun DayStage(players: MutableList<Player>, currentDay: Day) {

    var showRoles by remember { mutableStateOf(true) }
    var activePlayerIndex by remember { mutableStateOf(0) }
    var votedPlayers by remember { mutableStateOf(mutableListOf<Player>()) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedPlayer by remember { mutableStateOf<Player?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(8.dp)
    ) {
        Column() {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 10.dp)
            ) {
                Text(
                    text = "День: $currentDay",
                    style = MaterialTheme.typography.displayLarge,
                    fontSize = 28.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                        showRoles = !showRoles
                    }
                ) {
                    Icon(
                        painter = if (true) painterResource(id = R.drawable.ic_roles_show_hide)
                        else painterResource(
                            id = R.drawable.ic_roles_show_hide
                        ),
                        contentDescription = null,
                        tint = Color.White, modifier = Modifier.size(120.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color.White)
            )

            PlayerList(
                playersList = players,
                activePlayerIndex = activePlayerIndex,
                onPlayerClick = { clickedIndex ->
                    selectedPlayer = players.find { it.number == clickedIndex }
                    showDialog = true
                },
                showRoles = showRoles
            )
            if (showDialog) {
                PlayerDialog(
                    player = selectedPlayer,
                    activePlayer = players.find { it.number == activePlayerIndex } ?: players[0],
                    onDismiss = { showDialog = false },
                    onVoteClick = {
                        if (!votedPlayers.contains(selectedPlayer)) {
                            votedPlayers.add(selectedPlayer!!)
                        }
                    },
                    onFoulClick = {
                        selectedPlayer?.let {
                            it.fouls += 1
                        }
                    }
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color.White)
            )

            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = "На голосовании: ${votedPlayers.joinToString { it.number.toString() }}",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 22.sp,
                color = Color.White
            )

            Row() {
                Box(modifier = Modifier.clickable {
                    activePlayerIndex = (activePlayerIndex - 1 + players.size) % players.size
                }) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_previous),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(42.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(text = "Предыдущий", color = Color.White, fontSize = 20.sp)
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Box(modifier = Modifier.clickable {
                    activePlayerIndex = (activePlayerIndex + 1) % players.size
                }) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Следующий", color = Color.White, fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(2.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_next),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(42.dp)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                SimpleElevatedButton(buttonText = "Голосование", onClick = { /*TODO*/ }) {

                }
            }
        }
    }
}

@Composable
fun PlayerDialog(
    player: Player?,
    activePlayer: Player,
    onDismiss: () -> Unit,
    onVoteClick: () -> Unit,
    onFoulClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            if (player != null) {
                Text(text = player.name)
            }
        },
        text = {
            Text(text = "Активный игрок сейчас: ${activePlayer.number}:${activePlayer.name}")
        },
        confirmButton = {
            Button(
                onClick = {
                    onDismiss()
                    onVoteClick()
                },
                colors = ButtonDefaults.buttonColors(Background)
            ) {
                Text("Выставить на голосование", color = Color.White)
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismiss()
                    onFoulClick()
                },
                colors = ButtonDefaults.buttonColors(Background)
            ) {
                Text("Дать фол", color = Color.White)
            }
        }
    )
}

@Preview()
@Composable
private fun DayStagePreview() {
    val players = mutableListOf(
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
    val redTeam = Team(Team.TeamColor.BLACK, players)
    DayStage(players, day)
}