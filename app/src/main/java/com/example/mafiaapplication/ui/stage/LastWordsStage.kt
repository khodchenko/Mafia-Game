package com.example.mafiaapplication.ui.stage

import android.util.Log
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mafiaapplication.game.GameStage
import com.example.mafiaapplication.game.GameState
import com.example.mafiaapplication.data.Player
import com.example.mafiaapplication.ui.element.BestMovePanel
import com.khodchenko.mafiaapp.data.Screen
import com.example.mafiaapplication.ui.element.CustomElevatedButton
import com.example.mafiaapplication.ui.theme.Background
import com.khodchenko.mafiaapp.ui.element.Timer


@Composable
fun LastWordsStage(
    navController: NavController,
    gameState: GameState
) {
    var selectedPlayersBestMove = remember { mutableStateListOf<Player>() }
    val isFirstKilledOrVotedOutPlayer = remember {
        gameState.players.count { !it.isAlive } == 1
    }

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
                modifier = Modifier.fillMaxWidth(),
                text = "Последняя речь:",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = gameState.players[gameState.currentPlayerIndex].name,
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = if (gameState.checkEndGame()) "С правом обьявления победы противоположной команды." else "",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )

            if (isFirstKilledOrVotedOutPlayer) {
                BestMovePanel(players = gameState.players) { selectedPlayers ->
                    selectedPlayersBestMove = selectedPlayers.toMutableStateList()
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, bottom = 30.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                CustomElevatedButton("Погнали", enabled = true, onClick = {
                    if (gameState.checkEndGame()) {
                        gameState.stage = GameStage.GAME_OVER
                        gameState.awardPointsToWinningTeam()
                        navController.navigate(Screen.EndGameStageScreen.route)
                    } else if (gameState.stage == GameStage.NIGHT) {
                        gameState.stage = GameStage.DAY
                        navController.navigate(Screen.DayStageScreen.route)
                    } else {
                        gameState.newDay()
                        gameState.stage = GameStage.NIGHT
                        navController.navigate(Screen.NightStageScreen.route)
                    }

                    if (isFirstKilledOrVotedOutPlayer && selectedPlayersBestMove.size == 3) {
                        gameState.awardPointsForBestMove(gameState.players[gameState.currentPlayerIndex], selectedPlayersBestMove)
                        Log.d("LastWordsStage", "Best move: ${gameState.players[gameState.currentPlayerIndex].score} candidates: $selectedPlayersBestMove")
                    }
                })
            }

            Timer()
        }
    }
}