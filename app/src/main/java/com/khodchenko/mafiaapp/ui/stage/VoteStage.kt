package com.khodchenko.mafiaapp.ui.stage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.khodchenko.mafiaapp.Screen
import com.khodchenko.mafiaapp.game.MafiaGame
import com.khodchenko.mafiaapp.ui.SimpleElevatedButton
import com.khodchenko.mafiaapp.ui.theme.Background

@Composable
fun VoteStage(navController: NavController , game: MafiaGame) {

    var voters by remember { mutableStateOf(game.getAllPlayers().filter { it.isAlive }) }
    val alivePlayers by remember { mutableStateOf(game.getAllPlayers().toMutableList()) }
    //todo
    game.setCurrentPlayer(alivePlayers[0])
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(8.dp)
    ) {
        Column {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "На голосовании:\n${game.getCurrentPlayer().name}",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp),
                text = "Выберете ниже кто голосует \nпротив этого игрока:",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )

            LazyColumn {
                items(alivePlayers) { otherPlayer ->
                    val isSelected = voters.contains(otherPlayer)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 2.dp, bottom = 2.dp)
                            .clickable {
                                voters = if (isSelected) {
                                    voters
                                        .toMutableList()
                                        .apply { remove(otherPlayer) }
                                } else {
                                    voters
                                        .toMutableList()
                                        .apply { add(otherPlayer) }
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = null,
                            colors = CheckboxDefaults.colors(
                                checkmarkColor = Background,
                                checkedColor = Color.White,
                                uncheckedColor = Color.White.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier.padding(4.dp)
                        )
                        Text(
                            text = "${otherPlayer.number}:",
                            modifier = Modifier.padding(end = 4.dp),
                            color = Color.White,
                            fontSize = 28.sp
                        )
                        Text(
                            text = otherPlayer.name,
                            color = Color.White,
                            fontSize = 28.sp
                        )


                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                SimpleElevatedButton("Голосуем", onClick = {
                    navController.navigate(Screen.NightStageScreen.route)
                }) {
                }
            }

        }
    }
}