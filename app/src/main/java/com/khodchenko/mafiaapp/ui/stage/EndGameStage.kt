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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khodchenko.mafiaapp.data.Day
import com.khodchenko.mafiaapp.data.Player
import com.khodchenko.mafiaapp.data.Role
import com.khodchenko.mafiaapp.data.Team
import com.khodchenko.mafiaapp.ui.PlayerList
import com.khodchenko.mafiaapp.ui.theme.Background
import com.khodchenko.mafiaapp.ui.theme.BeautifulBlack

@Composable
fun EndGameStage(playerList: MutableList<Player>, currentDay: Day, winnerTeam: Team) {
    val bestPlayer = playerList.maxByOrNull { it.score }
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
                playersList = playerList,
                currentDay = currentDay,
                activePlayerIndex = bestPlayer?.number
                    ?: 0
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

@Preview()
@Composable
private fun EndGameStagePreview() {
    val players = mutableListOf<Player>(
        Player(1, "Player 1", Role.MAFIA),
        Player(2, "Player 2", Role.CIVIL),
        Player(3, "Player 3", Role.CIVIL),
        Player(4, "Player 4", Role.CIVIL),
        Player(5, "Player 5", Role.CIVIL),
        Player(6, "Player 6", Role.CIVIL),
        Player(7, "Player 7", Role.CIVIL),
        Player(8, "Player 8", Role.CIVIL),
        Player(9, "Player 9", Role.CIVIL),
        Player(10, "Player 10", Role.CIVIL)
    )
    val day = Day(1)
    val redTeam = Team(Team.TeamColor.BLACK, players)
    EndGameStage(players, day, redTeam)
}