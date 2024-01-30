package com.khodchenko.mafiaapp.ui.stage

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.khodchenko.mafiaapp.R
import com.khodchenko.mafiaapp.data.GameStage
import com.khodchenko.mafiaapp.data.Screen
import com.khodchenko.mafiaapp.game.MafiaGame
import com.khodchenko.mafiaapp.ui.CustomElevatedButton
import com.khodchenko.mafiaapp.ui.PlayerList
import com.khodchenko.mafiaapp.ui.theme.Background

@Composable
fun VoteMainStage(navController: NavController, game: MafiaGame) {
    var showRoles by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var raiseAllDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Background)
            .padding(16.dp)
    ) {

        Column(modifier = Modifier.fillMaxHeight()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier.padding(start = 60.dp),
                    text = "Голосование: ${game.getCurrentDay()}",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
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

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "В живых: ${game.getAllAlivePlayers().size}",
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
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )

            PlayerList(
                playersList = game.getCandidates().toMutableList(),
                activePlayerIndex = game.getCurrentPlayer().number,
                showRoles = showRoles,
                onPlayerClick = {

                },
                showVotes = true,
                game = game
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 100.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                CustomElevatedButton("К голосованию", true) {
                    if (game.getCandidates().isEmpty()) {
                        Log.d("VoteMainStage", "End of stage.")
                        Log.d(
                            "VoteMainStage",
                            "Most votes: ${game.findCandidatesWithLongestVotes()}"
                        )
                        game.newDay()
                        game.setStage(GameStage.NIGHT)
                        navController.navigate(Screen.NightStageScreen.route)
                        Toast.makeText(context, "End of voting", Toast.LENGTH_SHORT).show()
                    } else if (game.getCurrentStage() != GameStage.VOTE_3) {
                        Log.d("VoteMainStage", "Current player: ${game.getCurrentPlayer()}")
                        navController.navigate(Screen.VoteStageScreen.route)
                    } else {
                        raiseAllDialog = true
                    }
                }
            }
            if (raiseAllDialog) {
                ShowRaiseAllDialog(
                    onRaiseAll = {
                        // Вызываем функцию для обработки поднятия всех
                        raiseAllDialog = false
                    },
                    onLeaveAll = {
                        // Вызываем функцию для обработки оставления в игре
                        raiseAllDialog = false
                    }
                )
            }

        }
    }
}

@Composable
fun ShowRaiseAllDialog(onRaiseAll: () -> Unit, onLeaveAll: () -> Unit) {
    AlertDialog(
        onDismissRequest = {
            // Обработка закрытия диалога
        },
        title = {
            Text("Поднимаем всех?")
        },
        text = {
            Text("Выберите действие:")
        },
        confirmButton = {
            Button(
                onClick = {
                    // Вызываем функцию для обработки поднятия всех
                    onRaiseAll()
                }
            ) {
                Text("Поднимаем")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    // Вызываем функцию для обработки оставления в игре
                    onLeaveAll()
                }
            ) {
                Text("Оставляем в игре")
            }
        }
    )
}