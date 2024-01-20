package com.khodchenko.mafiaapp.ui.stage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khodchenko.mafiaapp.data.Team
import com.khodchenko.mafiaapp.game.MafiaGame
import com.khodchenko.mafiaapp.ui.PlayerList
import com.khodchenko.mafiaapp.ui.theme.Background
import com.khodchenko.mafiaapp.ui.theme.BeautifulBlack

@Composable
fun EndGameStage(game: MafiaGame) {
    val playerList = game.getAllPlayers()
    val bestPlayer = playerList.maxByOrNull { it.score }
    val winnerTeam = game.getWinningTeam()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = if (winnerTeam.color == Team.TeamColor.RED) Background
                else BeautifulBlack
            )
            .padding(8.dp)
    ) {
        Column {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Победила команда:\n" + winnerTeam.color.toString(),
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )

            PlayerList(
                playersList = playerList.toMutableList(),
                activePlayerIndex = bestPlayer?.number
                    ?: 0, onPlayerClick = {}, game = game
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

        }
    }
}
