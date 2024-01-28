package com.khodchenko.mafiaapp.ui.stage

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.khodchenko.mafiaapp.data.GameStage
import com.khodchenko.mafiaapp.data.Screen
import com.khodchenko.mafiaapp.game.MafiaGame
import com.khodchenko.mafiaapp.helpers.SoundPlayer
import com.khodchenko.mafiaapp.ui.PlayerList
import com.khodchenko.mafiaapp.ui.Timer
import com.khodchenko.mafiaapp.ui.theme.Background
import com.khodchenko.mafiaapp.ui.theme.BeautifulBlack


@Composable
fun NightStage(navController: NavController, game: MafiaGame) {
    var activePlayerIndex by remember { mutableIntStateOf(11) }
    val soundPlayer = SoundPlayer(LocalContext.current)
    val currentDay = game.getCurrentDay()
    val players = game.getAllPlayers()


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BeautifulBlack)
            .padding(8.dp)
            .clickable {
                activePlayerIndex = 11
            }
    ) {
        Column(modifier = Modifier.fillMaxHeight()) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Ночь:$currentDay",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Мафия совершает выстрел...",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )

            PlayerList(
                playersList = players,
                activePlayerIndex = activePlayerIndex,
                onPlayerClick = { clickedPlayerIndex ->
                    Log.d("NightStage", "NightStage: activePlayerIndex = $activePlayerIndex")
                    activePlayerIndex = clickedPlayerIndex
                },
                Background,
                game = game
            )

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 36.dp)
            ) {
                Button(
                    onClick = {
                        players.find { it.number == activePlayerIndex + 1 }
                            ?.let { game.killPlayer(it) }
                        soundPlayer.playShootSound()

                        if (game.checkEndGame()) {
                            game.setStage(GameStage.GAME_OVER)
                            navController.navigate(Screen.EndGameStageScreen.route)
                        } else if (activePlayerIndex == 11) {
                            game.getAllAlivePlayers().find { it.number == game.getCurrentDay() }
                                ?.let { game.setCurrentPlayer(it) }
                            game.setStage(GameStage.DAY)
                            navController.navigate(Screen.DayStageScreen.route)
                        } else {
                            game.setCurrentPlayer(players[activePlayerIndex])
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
