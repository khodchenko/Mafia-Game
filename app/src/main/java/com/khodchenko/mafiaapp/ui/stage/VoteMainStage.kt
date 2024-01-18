package com.khodchenko.mafiaapp.ui.stage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khodchenko.mafiaapp.R
import com.khodchenko.mafiaapp.data.Player
import com.khodchenko.mafiaapp.data.Role
import com.khodchenko.mafiaapp.ui.CustomElevatedButton
import com.khodchenko.mafiaapp.ui.PlayerList

import com.khodchenko.mafiaapp.ui.theme.Background

@Composable
fun VoteMainStage(playerList: List<Player>) {
    var showRoles by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Background)
            .padding(16.dp)
    ) {

        Column() {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Голосование за день:0",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "В живых игроков:0",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "На голосовании:",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )

            Row() {
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                        showRoles = !showRoles
                    }
                ) {
                    Icon(
                        painter = if (showRoles) painterResource(id = R.drawable.ic_roles_show_hide)
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

            PlayerList(
                playersList = playerList.toMutableList(),
                activePlayerIndex = 0,
                showRoles = showRoles,
                onPlayerClick = {

                })

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CustomElevatedButton("К голосованию", true) {

                }
            }
        }
    }
}

@Preview
@Composable
fun VoteMainStageUI() {
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

    VoteMainStage(players)
}