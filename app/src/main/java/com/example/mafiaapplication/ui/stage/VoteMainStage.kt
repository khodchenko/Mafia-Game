package com.example.mafiaapplication.ui.stage

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mafiaapplication.R
import com.example.mafiaapplication.game.GameStage
import com.example.mafiaapplication.game.GameState
import com.khodchenko.mafiaapp.data.Screen
import com.example.mafiaapplication.ui.element.CustomElevatedButton
import com.example.mafiaapplication.ui.element.HeaderBanner
import com.example.mafiaapplication.ui.theme.Background
import com.khodchenko.mafiaapp.ui.element.PlayerList
import com.khodchenko.mafiaapp.ui.element.Timer


@Composable
fun VoteMainStage(
    navController: NavController,
    gameState: GameState
) {
    val showRoles = remember { mutableStateOf(false) }
    val context = LocalContext.current
    var raiseAllDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Background)
            .padding(4.dp)
    ) {
        Column(
        modifier = Modifier
            .fillMaxHeight()
            .align(Alignment.Center)
    ) {
        HeaderBanner(
            leftIconId = R.drawable.ic_settings,
            leftIconClick = {
                navController.navigate(Screen.SettingsScreen.route)
            },
            text = "Голосование: ${gameState.day}",
            rightIconId = if (showRoles.value) R.drawable.ic_roles_show_hide
            else R.drawable.ic_roles_show_hide,
            rightIconClick = {
                showRoles.value = !showRoles.value
            }
        )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(Color.White)
    )

    Text(
        modifier = Modifier
            .fillMaxWidth(),
        text = when (gameState.stage) {
            GameStage.VOTE -> "Фаза 1"
            GameStage.VOTE_2 -> "Фаза 2"
            GameStage.VOTE_3 -> "Фаза 3"
            else -> ""
        },
        color = Color.White,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )

    Text(
        modifier = Modifier
            .fillMaxWidth(),
        text = "В живых: ${gameState.getAllAlivePlayers().size}",
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
        playersList = gameState.getCandidates().toMutableList(),
        activePlayerIndex = gameState.currentPlayerIndex,
        showRoles = showRoles.value,
        onPlayerClick = {

        },
        showVotes = true,
        gameState = gameState
    )

    Spacer(modifier = Modifier.weight(1f))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        CustomElevatedButton("К голосованию", true) {
            if (!gameState.checkFaults()) {
                if (gameState.getCandidates().isEmpty()) {
                    Log.d("VoteMainStage", "End of stage.")
                    Log.d(
                        "VoteMainStage",
                        "Most votes: ${gameState.findCandidatesWithLongestVotes()}"
                    )
                    gameState.newDay()
                    gameState.stage = GameStage.NIGHT
                    navController.navigate(Screen.NightStageScreen.route)
                    Toast.makeText(context, "End of voting", Toast.LENGTH_SHORT).show()
                } else if (gameState.stage != GameStage.VOTE_3) {
                    Log.d("VoteMainStage", "Current player: ${gameState.currentPlayerIndex}")
                    navController.navigate(Screen.VoteStageScreen.route)
                } else {
                    raiseAllDialog = true
                }
            } else {
                gameState.clearVote()
                navController.navigate(Screen.LastWordsScreen.route)
            }

        }
    }

    Timer(30000)

    if (raiseAllDialog) {
        ShowRaiseAllDialog(
            onRaiseAll = {
                raiseAllDialog = false
                for (candidate in gameState.getCandidates()) {
                    gameState.killPlayer(candidate)
                    gameState.setCurrentPlayer(player = candidate)
                    navController.navigate(Screen.LastWordsScreen.route)
                }
            },
            onLeaveAll = {
                raiseAllDialog = false
                navController.navigate(Screen.NightStageScreen.route)
            }
        )
    }

}
}
}

@Composable
fun ShowRaiseAllDialog(onRaiseAll: () -> Unit, onLeaveAll: () -> Unit) {
    AlertDialog(
        containerColor = Color.Black,
        onDismissRequest = {

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
                    onRaiseAll()
                }
            ) {
                Text("Поднимаем")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onLeaveAll()
                }
            ) {
                Text("Оставляем в игре")
            }
        }
    )
}