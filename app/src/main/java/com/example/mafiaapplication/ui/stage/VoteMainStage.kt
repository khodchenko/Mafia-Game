package com.example.mafiaapplication.ui.stage

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mafiaapplication.R
import com.example.mafiaapplication.data.Player
import com.example.mafiaapplication.game.*
import com.example.mafiaapplication.ui.element.*
import com.example.mafiaapplication.ui.theme.Background
import com.khodchenko.mafiaapp.data.Screen
import com.khodchenko.mafiaapp.ui.element.Timer

@Composable
fun VoteMainStage(
    navController: NavController,
    gameState: GameState,
    statisticManager: StatisticManager
) {
    val showRoles = remember { mutableStateOf(false) }
    var raiseAllDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Background)
            .padding(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.Center)
        ) {
            VoteHeader(
                navController = navController,
                gameState = gameState,
                showRoles = showRoles
            )

            Divider()

            VoteDetails(gameState)

            PlayerListSection(
                gameState = gameState,
                showRoles = showRoles.value
            )

            Spacer(modifier = Modifier.weight(1f))

            VoteFooter(
                navController = navController,
                gameState = gameState,
                statisticManager = statisticManager,
                raiseAllDialog = raiseAllDialog,
                setRaiseAllDialog = { raiseAllDialog = it }
            )

            Timer(30000)

            if (raiseAllDialog) {
                ShowRaiseAllDialog(
                    nonVotedPlayers = gameState.getAllAlivePlayers(),
                    onRaiseAll = {
                        raiseAllDialog = false
                        gameState.getCandidates().forEach { candidate ->
                            //todo сделать каждому кандидату отдельный экран LastWords
                            gameState.killPlayer(candidate)
                            gameState.setCurrentPlayer(candidate)
                            navController.navigate(Screen.LastWordsScreen.route)
                        }
                    },
                    onLeaveAll = {
                        raiseAllDialog = false
                        navController.navigate(Screen.NightStageScreen.route)
                    },
                    gameState = gameState,
                    statisticManager = statisticManager
                )
            }
        }
    }
}

@Composable
fun VoteHeader(
    navController: NavController,
    gameState: GameState,
    showRoles: MutableState<Boolean>
) {
    HeaderBanner(
        leftIconId = R.drawable.ic_settings,
        leftIconClick = {
            navController.navigate(Screen.SettingsScreen.route)
        },
        text = "Голосование: ${gameState.day}",
        rightIconId = if (showRoles.value) R.drawable.ic_roles_show_hide else R.drawable.ic_roles_show_hide,
        rightIconClick = {
            showRoles.value = !showRoles.value
        }
    )
}

@Composable
fun Divider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(Color.White)
    )
}

@Composable
fun VoteDetails(gameState: GameState) {
    Column {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = when (gameState.stage) {
                GameStage.VOTE -> "Фаза 1"
                GameStage.VOTE_2 -> "Фаза 2"
                GameStage.VOTE_3 -> "Фаза 3"
                else -> ""
            },
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "В живых: ${gameState.getAllAlivePlayers().size}",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "На голосовании:",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PlayerListSection(
    gameState: GameState,
    showRoles: Boolean
) {
    PlayerList(
        playersList = gameState.getCandidates().toMutableList(),
        activePlayerIndex = gameState.currentPlayerIndex,
        showRoles = showRoles,
        onPlayerClick = {},
        showVotes = true,
        gameState = gameState
    )
}

@Composable
fun VoteFooter(
    navController: NavController,
    gameState: GameState,
    statisticManager: StatisticManager,
    raiseAllDialog: Boolean,
    setRaiseAllDialog: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        CustomElevatedButton("К голосованию", true) {
            if (!gameState.checkFaults()) {
                when {
                    gameState.getCandidates().isEmpty() -> {
                        Log.d("VoteMainStage", "End of stage.")
                        statisticManager.addEntry("Day:${gameState.day} end of voting", Type.SIMPLE)
                        Log.d(
                            "VoteMainStage",
                            "Most votes: ${gameState.findCandidatesWithLongestVotes()}"
                        )
                        statisticManager.addEntry(
                            "Day:${gameState.day} most votes: ${gameState.findCandidatesWithLongestVotes()}",
                            Type.SIMPLE
                        )
                        gameState.newDay()
                        gameState.stage = GameStage.NIGHT
                        navController.navigate(Screen.NightStageScreen.route)
                        //Toast.makeText(context, "End of voting", Toast.LENGTH_SHORT).show()
                    }

                    gameState.stage != GameStage.VOTE_3 -> {
                        Log.d("VoteMainStage", "Current player: ${gameState.currentPlayerIndex}")
                        statisticManager.addEntry(
                            "Day:${gameState.day} vote for player ${gameState.players[gameState.currentPlayerIndex].number}.${gameState.players[gameState.currentPlayerIndex].name}",
                            Type.SIMPLE
                        )
                        navController.navigate(Screen.VoteStageScreen.route)
                    }

                    else -> setRaiseAllDialog(true)
                }
            } else {
                gameState.clearVote()
                navController.navigate(Screen.LastWordsScreen.route)
                statisticManager.addEntry(
                    "Day:${gameState.day} last words of player ${gameState.players[gameState.currentPlayerIndex].number}.${gameState.players[gameState.currentPlayerIndex].name}",
                    Type.SIMPLE
                )
            }
        }
    }
}

@Composable
fun ShowRaiseAllDialog(
    nonVotedPlayers: List<Player>,
    onRaiseAll: () -> Unit,
    onLeaveAll: () -> Unit,
    gameState: GameState,
    statisticManager: StatisticManager
) {
    var selectedPlayers by remember { mutableStateOf(emptyList<Player>()) }

    AlertDialog(
        onDismissRequest = {},
        title = { Text("Поднимаем всех?", color = Color.White) },
        text = {
            Column {
                Text("Выберите игроков:", color = Color.White)
                LazyColumn {
                    items(nonVotedPlayers) { player ->
                        val isSelected = selectedPlayers.contains(player)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                                .clickable {
                                    selectedPlayers = if (isSelected) {
                                        selectedPlayers
                                            .toMutableList()
                                            .apply { remove(player) }
                                    } else {
                                        selectedPlayers
                                            .toMutableList()
                                            .apply { add(player) }
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
                                text = "${player.number}:",
                                modifier = Modifier.padding(end = 4.dp),
                                color = Color.White,
                                fontSize = 28.sp
                            )
                            Text(
                                text = player.name,
                                color = Color.White,
                                fontSize = 28.sp
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (selectedPlayers.size > nonVotedPlayers.size / 2) {
                        onRaiseAll()
                        for (player in selectedPlayers) {
                            for (candidate in gameState.getCandidates()) {
                                gameState.awardPointsForVoting(player, candidate)
                            }
                        }
                        statisticManager.addEntry(
                            "Day:${gameState.day} за поднятие кандидатов ${gameState.getCandidates()}, проголосовали: $selectedPlayers",
                            Type.SIMPLE
                        )
                    } else {
                        onLeaveAll()
                    }
                }
            ) {
                Text("Подтвердить")
            }
        },
        dismissButton = {
            Button(onClick = { /* Do something to dismiss dialog */ }) {
                Text("Отмена")
            }
        },
        containerColor = Color.Black
    )
}


