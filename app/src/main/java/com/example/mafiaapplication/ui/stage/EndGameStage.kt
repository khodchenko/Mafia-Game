package com.example.mafiaapplication.ui.stage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mafiaapplication.game.GameState
import com.example.mafiaapplication.game.StatisticManager
import com.example.mafiaapplication.game.StatisticRow
import com.example.mafiaapplication.ui.theme.Background
import com.example.mafiaapplication.ui.theme.BeautifulBlack
import com.example.mafiaapplication.ui.element.PlayerList

@Composable
fun EndGameStage(
    gameState: GameState,
    statisticManager: StatisticManager
) {
    val players = gameState.players
    val bestPlayer = players.maxByOrNull { it.score }
    val winnerTeam = gameState.getWinningTeam()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = if (winnerTeam == "Red") Background
                else BeautifulBlack
            )
            .padding(8.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "Победила команда:\n$winnerTeam",
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center
        )

        PlayerList(
            playersList = players.toMutableList(),
            activePlayerIndex = bestPlayer?.number ?: 0,
            onPlayerClick = {},
            showScores = true,
            gameState = gameState
        )

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "Лучший игрок:\n" + bestPlayer?.name,
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        StatisticScreen(statisticManager = statisticManager)
    }
}


@Composable
fun StatisticScreen(statisticManager: StatisticManager) {
    val entries = statisticManager.getAllEntries()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        entries.forEach { entry ->
            StatisticRow(entry = entry)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}