package com.example.mafiaapplication.ui.element

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mafiaapplication.R
import com.example.mafiaapplication.game.GameState
import com.example.mafiaapplication.helpers.SharedPreferencesHelper
import com.example.mafiaapplication.ui.theme.Background
import com.khodchenko.mafiaapp.data.Screen
import com.khodchenko.mafiaapp.ui.element.PlayerList

@Composable
fun SettingsScreen(
    navController: NavController,
    sharedPreferencesHelper: SharedPreferencesHelper,
    gameState: GameState
) {
    val showDialogRestartGame = remember { mutableStateOf(false) }
    val showDialogChangeDay = remember { mutableStateOf(false) }
    val showDialogResurrectPlayer = remember { mutableStateOf(false) }
    val currentDay = remember { mutableFloatStateOf(gameState.day.toFloat()) }
    val activePlayerIndex = remember { mutableStateOf(-1) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(8.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .fillMaxWidth()
            ) {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier.padding(end = 10.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_previous),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Text(
                    text = "Настройки",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 40.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 10.dp)
            ) {
                Column {
                    Text(
                        text = "Сбросить игру",
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Normal,
                        fontSize = 22.sp,
                        color = Color.White,
                        modifier = Modifier.clickable {
                            showDialogRestartGame.value = true
                        }
                    )

                    Text(
                        text = "Воскресить игрока",
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Normal,
                        fontSize = 22.sp,
                        color = Color.White,
                        modifier = Modifier.clickable {
                            showDialogResurrectPlayer.value = true
                        }
                    )

                    Text(
                        text = "Сменить день",
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Normal,
                        fontSize = 22.sp,
                        color = Color.White,
                        modifier = Modifier.clickable {
                            showDialogChangeDay.value = true
                        }
                    )
                }
            }

            if (showDialogRestartGame.value) {
                AlertDialog(
                    onDismissRequest = { showDialogRestartGame.value = false },
                    title = {
                        Text(text = "Вы хотите сбросить прогресс игры?")
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                restartGame(navController, sharedPreferencesHelper)
                                showDialogRestartGame.value = false
                            },
                            colors = ButtonDefaults.buttonColors(Background)
                        ) {
                            Text("Да", color = Color.White)
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                showDialogRestartGame.value = false
                            },
                            colors = ButtonDefaults.buttonColors(Background)
                        ) {
                            Text("Нет", color = Color.White)
                        }
                    }
                )
            }
            if (showDialogChangeDay.value) {
                AlertDialog(
                    onDismissRequest = { showDialogChangeDay.value = false },
                    title = {
                        Text(text = "Текущий день: ${currentDay.floatValue.toInt()}")
                    },
                    text = {
                        Column {
                            Text("Выберите новый день:")
                            SliderMinimalExample(
                                onSliderValueChanged = { value ->
                                    currentDay.floatValue = value
                                },
                                currentValue = currentDay.floatValue,
                                steps = 9,
                                valueRange = 1f..10f
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                changeDay(
                                    gameState,
                                    currentDay.floatValue.toInt(),
                                    sharedPreferencesHelper
                                )
                                showDialogChangeDay.value = false
                            },
                            colors = ButtonDefaults.buttonColors(Background)
                        ) {
                            Text("Сменить", color = Color.White)
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                showDialogChangeDay.value = false
                            },
                            colors = ButtonDefaults.buttonColors(Background)
                        ) {
                            Text("Отмена", color = Color.White)
                        }
                    }
                )
            }
            if (showDialogResurrectPlayer.value) {
                AlertDialog(
                    onDismissRequest = { showDialogResurrectPlayer.value = false },
                    title = {
                        Text(text = "Выберите игрока:")
                    },
                    text = {
                        PlayerList(
                            playersList = gameState.getAllDeadPlayers(),
                            activePlayerIndex = activePlayerIndex.value,
                            onPlayerClick = { index -> activePlayerIndex.value = index },
                            showRoles = false,
                            gameState = gameState
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                resurrectPlayer(
                                    gameState,
                                    activePlayerIndex.value,
                                    sharedPreferencesHelper
                                )
                                showDialogResurrectPlayer.value = false
                            },
                            colors = ButtonDefaults.buttonColors(Background)
                        ) {
                            Text("Выбрать", color = Color.White)
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                showDialogResurrectPlayer.value = false
                            },
                            colors = ButtonDefaults.buttonColors(Background)
                        ) {
                            Text("Отмена", color = Color.White)
                        }
                    }
                )
            }
        }
    }
}

fun restartGame(navController: NavController, sharedPreferencesHelper: SharedPreferencesHelper) {
    sharedPreferencesHelper.clearSavedGame()
    navController.navigate(Screen.StartGameScreen.route)
}

fun changeDay(gameState: GameState, day: Int, sharedPreferencesHelper: SharedPreferencesHelper) {
    gameState.day = day
    sharedPreferencesHelper.saveGameState(gameState)
}

fun resurrectPlayer(
    gameState: GameState,
    playerId: Int,
    sharedPreferencesHelper: SharedPreferencesHelper
) {
    gameState.players.find { it.id == playerId }?.isAlive = true
    sharedPreferencesHelper.saveGameState(gameState)
}
