package com.example.mafiaapplication.ui.stage

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mafiaapplication.R
import com.example.mafiaapplication.data.GameStage
import com.example.mafiaapplication.ui.theme.Background
import com.example.mafiaapplication.ui.theme.BeautifulBlack
import com.example.mafiaapplication.data.GameState
import com.example.mafiaapplication.helpers.SharedPreferencesHelper
import com.example.mafiaapplication.ui.element.HeaderBanner
import com.khodchenko.mafiaapp.data.Screen
import com.khodchenko.mafiaapp.helpers.SoundPlayer
import com.khodchenko.mafiaapp.ui.element.PlayerList
import com.khodchenko.mafiaapp.ui.element.Timer

@Composable
fun NightStage(
    navController: NavController,
    gameState: GameState,
    sharedPreferencesHelper: SharedPreferencesHelper
) {
    var activePlayerIndex by remember { mutableIntStateOf(11) }
    val soundPlayer = SoundPlayer(LocalContext.current)
    val showRoles = remember { mutableStateOf(false) }

    val players = gameState.players

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BeautifulBlack)
            .padding(4.dp)
            .clickable {
                activePlayerIndex = 11
            }
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
                text = "Ночь: ${gameState.day}",
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

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                text = "Мафия совершает выстрел...",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            PlayerList(
                playersList = players,
                activePlayerIndex = activePlayerIndex,
                onPlayerClick = { clickedPlayerIndex ->
                    Log.d("NightStage", "NightStage: activePlayerIndex = $activePlayerIndex")
                    activePlayerIndex = clickedPlayerIndex
                },
                Background,
                gameState = gameState,
                showRoles = showRoles.value
            )

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 36.dp)
            ) {
                Button(
                    onClick = {
                        if (activePlayerIndex in players.indices) {
                            players[activePlayerIndex].let { player ->
                                val result = gameState.killPlayer(player)
                                if (result) {
                                    soundPlayer.playShootSound()
                                    Log.d(
                                        "NightStage",
                                        "Player ${players[activePlayerIndex]} killed"
                                    )
                                } else {
                                    soundPlayer.playShootSound()
                                    Log.d(
                                        "NightStage",
                                        "Player ${players[activePlayerIndex]} already dead"
                                    )
                                }
                            }
                        }

                        sharedPreferencesHelper.saveGameState(gameState)

                        if (activePlayerIndex == 11) {
                            gameState.currentPlayerIndex = gameState.day - 1 //todo it can make bug
                            gameState.stage = GameStage.DAY
                            navController.navigate(Screen.DayStageScreen.route)
                        } else {
                            gameState.currentPlayerIndex = activePlayerIndex
                            navController.navigate(Screen.LastWordsScreen.route)
                        }
                    },
                    modifier = Modifier.align(Alignment.Center),
                    colors = ButtonDefaults.buttonColors(Color.White)
                ) {
                    Text(text = "ВЫСТРЕЛ", color = Color.Black, fontSize = 24.sp)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Timer()
        }
    }
}
