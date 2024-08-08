package com.example.mafiaapplication.ui.stage

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mafiaapplication.R
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
    val context = LocalContext.current
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
                    onRaiseAll = {
                        raiseAllDialog = false
                        gameState.getCandidates().forEach { candidate ->
                            gameState.killPlayer(candidate)
                            gameState.setCurrentPlayer(candidate)
                            navController.navigate(Screen.LastWordsScreen.route)
                        }
                    },
                    onLeaveAll = {
                        raiseAllDialog = false
                        navController.navigate(Screen.NightStageScreen.route)
                    }
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
                        Log.d("VoteMainStage", "Most votes: ${gameState.findCandidatesWithLongestVotes()}")
                        statisticManager.addEntry("Day:${gameState.day} most votes: ${gameState.findCandidatesWithLongestVotes()}", Type.SIMPLE)
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
fun ShowRaiseAllDialog(onRaiseAll: () -> Unit, onLeaveAll: () -> Unit) {
    AlertDialog(
        containerColor = Color.Black,
        onDismissRequest = {},
        title = { Text("Поднимаем всех?") },
        text = { Text("Выберите действие:") },
        confirmButton = {
            Button(onClick = { onRaiseAll() }) {
                Text("Поднимаем")
            }
        },
        dismissButton = {
            Button(onClick = { onLeaveAll() }) {
                Text("Оставляем в игре")
            }
        }
    )
}

