package com.khodchenko.mafiaapp.ui.stage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khodchenko.mafiaapp.R
import com.khodchenko.mafiaapp.data.Day
import com.khodchenko.mafiaapp.data.Player
import com.khodchenko.mafiaapp.data.Role
import com.khodchenko.mafiaapp.data.Team

@Composable
fun DayStage(currentDay: Day) {
    Column() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 10.dp)
        ) {
            Text(
                text = "День: $currentDay",
                style = MaterialTheme.typography.displayLarge,
                fontSize = 28.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {

                }
            ) {
                Icon(
                    painter = if (true) painterResource(id = R.drawable.ic_roles_show_hide)
                    else painterResource(
                        id = R.drawable.ic_roles_show_hide
                    ),
                    contentDescription = null,
                    tint = Color.White, modifier = Modifier.size(120.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(Color.White)
        )
    }
}

@Preview()
@Composable
private fun DayStagePreview() {
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
    DayStage(day)
}