package com.example.mafiaapplication.ui.stage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mafiaapplication.data.GameStage
import com.example.mafiaapplication.data.GameState
import com.example.mafiaapplication.data.Player
import com.example.mafiaapplication.helpers.SharedPreferencesHelper
import com.khodchenko.mafiaapp.data.Screen
import com.example.mafiaapplication.game.MafiaGame
import com.example.mafiaapplication.ui.element.CustomElevatedButton
import com.example.mafiaapplication.ui.theme.Background
import com.khodchenko.mafiaapp.ui.element.Timer


@Composable
fun LastWordsStage(
    navController: NavController,
    gameState: GameState,
    players: List<Player>
) {
    val game = MafiaGame(gameState, players)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(8.dp)
    ) {
        Column(
            Modifier
                .padding(top = 10.dp)
                .fillMaxSize()
        ) {

            Spacer(modifier = Modifier.weight(1f))

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Последняя речь:",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = game.getPlayerByIndex(game.getCurrentPlayerIndex()).name,
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, bottom = 30.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                CustomElevatedButton("Погнали", enabled = true, onClick = {
                    if (game.getCurrentStage() == GameStage.NIGHT) {
                        game.setStage(GameStage.DAY)
                        navController.previousBackStackEntry
                        navController.navigate(Screen.DayStageScreen.route)
                    } else {
                        game.newDay()
                        game.setStage(GameStage.NIGHT)
                        navController.navigate(Screen.NightStageScreen.route)
                    }
                })
            }

            Timer()
        }
    }
}