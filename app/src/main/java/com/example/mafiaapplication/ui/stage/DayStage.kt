package com.example.mafiaapplication.ui.stage

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mafiaapplication.R
import com.example.mafiaapplication.game.GameStage
import com.example.mafiaapplication.game.GameState
import com.example.mafiaapplication.data.Player
import com.example.mafiaapplication.game.StatisticManager
import com.example.mafiaapplication.game.Type
import com.example.mafiaapplication.helpers.SharedPreferencesHelper
import com.khodchenko.mafiaapp.data.Screen
import com.example.mafiaapplication.ui.element.CustomElevatedButton
import com.example.mafiaapplication.ui.element.HeaderBanner
import com.example.mafiaapplication.ui.theme.Background
import com.khodchenko.mafiaapp.ui.element.PlayerDialog
import com.example.mafiaapplication.ui.element.PlayerList
import com.khodchenko.mafiaapp.ui.element.Timer


@Composable
fun DayStage(
    navController: NavController,
    gameState: GameState,
    sharedPreferencesHelper: SharedPreferencesHelper,
    statisticManager: StatisticManager
) {

    val context = LocalContext.current
    val showRoles = remember { mutableStateOf(false) }
    var activePlayerIndex by remember { mutableIntStateOf(gameState.currentPlayerIndex) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedPlayer by remember { mutableStateOf<Player?>(null) }
    var counterHelper: Int = 0

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.Center)
        ) {
            HeaderBanner(
                leftIconId = R.drawable.ic_settings,
                leftIconClick = {
                    navController.navigate(Screen.SettingsScreen.route)
                },
                text = "День: ${gameState.day}",
                rightIconId = if (showRoles.value) R.drawable.ic_roles_show_hide else R.drawable.ic_roles_show_hide,
                rightIconClick = {
                    showRoles.value = !showRoles.value
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color.White)
            )

            PlayerList(
                playersList = gameState.getAllAlivePlayers(),
                activePlayerIndex = activePlayerIndex,
                onPlayerClick = { clickedIndex ->
                    selectedPlayer = gameState.getAllAlivePlayers()[clickedIndex]
                    showDialog = true
                },
                showRoles = showRoles.value, gameState = gameState
            )
            if (showDialog) {
                PlayerDialog(
                    player = selectedPlayer,
                    activePlayer = gameState.getAllAlivePlayers()
                        .find { it.number == activePlayerIndex }
                        ?: gameState.getAllAlivePlayers()[0],
                    onDismiss = { showDialog = false },
                    onVoteClick = {
                        if (!gameState.getCandidates().contains(selectedPlayer)) {
                            gameState.addCandidate(
                                gameState.players[activePlayerIndex],
                                selectedPlayer!!
                            )
                            Log.d(
                                "DayStage",
                                "Кандидат $selectedPlayer выставлен игроком ${gameState.players[activePlayerIndex].name} ${gameState.players[activePlayerIndex].score}"
                            )
                            statisticManager.addEntry(
                                "Кандидат ${selectedPlayer!!.number}.${selectedPlayer!!.name} выставлен игроком ${gameState.players[activePlayerIndex].number}.${gameState.players[activePlayerIndex].name}",
                                Type.SIMPLE
                            )
                            Log.d(
                                "DayStage",
                                "Add ${selectedPlayer!!.name} to Candidates: ${gameState.getCandidates()} "
                            )
                        } else {
                            Log.d("DayStage", "Кандидат $selectedPlayer уже выставлен")
                        }
                    },
                    onFoulClick = {
                        selectedPlayer?.let {
                            it.fouls += 1
                            Toast.makeText(
                                context,
                                "Выдан фол игроку ${it.name}",
                                Toast.LENGTH_SHORT
                            ).show()
                            statisticManager.addEntry(
                                "Выдан ${it.fouls}-й фол игроку ${it.name}",
                                Type.SIMPLE
                            )
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
                text = "На голосовании: ${
                    gameState.getCandidates().joinToString { it.number.toString() }
                }",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 22.sp,
                color = Color.White
            )

            Row() {
                Box(modifier = Modifier.clickable {
                    counterHelper -= 1
                    activePlayerIndex =
                        (activePlayerIndex - 1 + gameState.getAllAlivePlayers().size) % gameState.getAllAlivePlayers().size
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

                    if (counterHelper < gameState.getAllAlivePlayers().size) counterHelper += 1 else Toast.makeText(
                        context,
                        "All players passed!",
                        Toast.LENGTH_SHORT
                    ).show()

                    activePlayerIndex =
                        (activePlayerIndex + 1) % gameState.getAllAlivePlayers().size
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
                CustomElevatedButton(buttonText = "Голосование", enabled = true, onClick = {
                    gameState.getCandidates().firstOrNull()
                        ?.let { gameState.setCurrentPlayer(it) }
                    Log.d("DayStage", "Current player: ${gameState.currentPlayerIndex}")
                    gameState.stage = GameStage.VOTE
                    statisticManager.addEntry("Day:${gameState.day} Vote started", Type.MAIN)
                    navController.navigate(Screen.VoteMainStageScreen.route)
                    sharedPreferencesHelper.saveGameState(gameState)
                })
            }

            Timer()
        }

    }
}