package com.khodchenko.mafiaapp.ui.stage

import android.util.Log
import android.widget.Toast
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.khodchenko.mafiaapp.R
import com.khodchenko.mafiaapp.data.GameStage
import com.khodchenko.mafiaapp.data.Player
import com.khodchenko.mafiaapp.data.Screen
import com.khodchenko.mafiaapp.game.MafiaGame
import com.khodchenko.mafiaapp.ui.element.CustomElevatedButton
import com.khodchenko.mafiaapp.ui.element.PlayerDialog
import com.khodchenko.mafiaapp.ui.element.PlayerList
import com.khodchenko.mafiaapp.ui.element.Timer
import com.khodchenko.mafiaapp.ui.theme.Background


@Composable
fun DayStage(navController: NavController, game: MafiaGame) {

    val context = LocalContext.current
    var showRoles by remember { mutableStateOf(false) }
    var activePlayerIndex by remember { mutableIntStateOf(0) }

    var showDialog by remember { mutableStateOf(false) }
    var selectedPlayer by remember { mutableStateOf<Player?>(null) }
    val currentDay = game.getCurrentDay()
    val players = game.getAllPlayers().filter { it.isAlive }


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
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "День: $currentDay",
                   modifier = Modifier.padding(start = 60.dp),
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = {
                        showRoles = !showRoles
                    }
                        ,
                    modifier = Modifier.padding(end = 10.dp)
                ) {
                    Icon(
                        painter = if (showRoles) painterResource(id = R.drawable.ic_roles_show_hide)
                        else painterResource(
                            id = R.drawable.ic_roles_show_hide
                        ),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(120.dp)
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
                playersList = players.toMutableList(),
                activePlayerIndex = activePlayerIndex,
                onPlayerClick = { clickedIndex ->
                    selectedPlayer = players.find { it.number == clickedIndex + 1 }
                    showDialog = true
                },
                showRoles = showRoles, game = game
            )
            if (showDialog) {
                PlayerDialog(
                    player = selectedPlayer,
                    activePlayer = players.find { it.number == activePlayerIndex } ?: players[0],
                    onDismiss = { showDialog = false },
                    onVoteClick = {
                        if (!game.getCandidates().contains(selectedPlayer)) {
                            game.addCandidate(selectedPlayer!!)
                            Log.d("DayStage", "Add ${selectedPlayer!!.name} to Candidates: ${game.getCandidates()} ")
                        } else {
                            Log.d("DayStage", "Кандидат $selectedPlayer уже выставлен")
                        }
                    },
                    onFoulClick = {
                        selectedPlayer?.let {
                            it.fouls += 1
                            Toast.makeText(context, "Выдан фол игроку ${it.name}", Toast.LENGTH_SHORT).show()
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
                text = "На голосовании: ${game.getCandidates().joinToString { it.number.toString() }}",
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
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                CustomElevatedButton(buttonText = "Голосование",enabled = true, onClick = {
                    game.getCandidates().firstOrNull()?.let { game.setCurrentPlayer(it) }
                    Log.d("DayStage", "Current player: ${game.getCurrentPlayer()}")
                    game.setStage(GameStage.VOTE)
                    navController.navigate(Screen.VoteMainStageScreen.route)
                })
            }

            Timer()
        }
    }
}