package com.khodchenko.mafiaapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.khodchenko.mafiaapp.ui.theme.Background

@Composable
fun PlayerList(playersList: MutableList<Player>, currentDay: Day, activePlayerIndex: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Background)
            .padding(4.dp)
    ) {
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

            LazyColumn {
                items(playersList) { player ->
                    PlayerItem(player, activePlayerIndex == player.number)
                }
            }
        }
    }
}

@Composable
private fun PlayerItem(player: Player, isActive: Boolean) {
    Row(
        modifier = Modifier
            .padding(12.dp)
            .background(if (isActive) Color.Gray else Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${player.number}: ",
            style = MaterialTheme.typography.titleMedium,
            fontSize = 22.sp,
            color = Color.White
        )
        Text(
            text = player.name,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 22.sp,
            color = Color.White
        )
        Text(
            text = player.fouls.toString(),
            style = MaterialTheme.typography.bodySmall,
            fontSize = 22.sp,
            color = Color.White,
            modifier = Modifier.padding(start = 12.dp)
        )
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = player.role.toString(),
            style = MaterialTheme.typography.bodySmall,
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}


@Preview()
@Composable
private fun PlayerListPreview() {
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
    PlayerList(players, day, activePlayerIndex = 1)
}
