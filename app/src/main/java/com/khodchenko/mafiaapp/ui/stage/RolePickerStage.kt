package com.khodchenko.mafiaapp.ui.stage

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.khodchenko.mafiaapp.data.GameStage
import com.khodchenko.mafiaapp.data.Player
import com.khodchenko.mafiaapp.data.Role
import com.khodchenko.mafiaapp.data.Screen
import com.khodchenko.mafiaapp.game.MafiaGame
import com.khodchenko.mafiaapp.ui.CustomElevatedButton
import com.khodchenko.mafiaapp.ui.theme.Background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RolePickerStage(navController: NavController, game: MafiaGame) {
    var newPlayerName by remember { mutableStateOf("") }
    var playersList by remember { mutableStateOf(listOf<Player>()) }
    var rulesCheck by remember { mutableStateOf(false) }


//todo TESTING
   playersList = generateTestPlayers().toMutableList()
    Log.d("RolePickerStage", "PlayerList = $playersList")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Background)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Создание игроков",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 22.sp,
                color = Color.White
            )
            LazyColumn {
                items(playersList) { player ->
                    Row(
                        modifier = Modifier.padding(6.dp),
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

                        CustomDropDownMenu(player = player) { selectedRole ->
                            player.role = selectedRole
                            rulesCheck = checkRules(playersList = playersList)
                            Log.d(
                                "RolePickerStage",
                                "Player ${player.number} role changed to $selectedRole"
                            )
                            Log.d("RolePickerStage", "RulesCheck = ${rulesCheck}")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            OutlinedTextField(
                value = newPlayerName,
                onValueChange = {
                    if (it.length <= 20) {
                        newPlayerName = it
                    }
                },
                label = { Text("Введите имя", color = Color.White) },
                textStyle = TextStyle(color = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                singleLine = true,
                maxLines = 1
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ElevatedButton(enabled = playersList.isNotEmpty(), onClick = {

                    playersList = playersList.dropLast(1).toMutableList()
                    Log.d("RolePickerStage", "PlayerList = ${playersList}")

                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        tint = Background,
                        contentDescription = "Delete"
                    )
                }

                ElevatedButton(
                    enabled = (newPlayerName.isNotEmpty() && playersList.size < 10),
                    onClick = {

                        val newPlayer = Player(
                            number = playersList.size + 1,
                            name = newPlayerName,
                            role = Role.CIVIL,
                            isAlive = true,
                            score = 0.0
                        )

                        playersList = (playersList + newPlayer).toMutableList()
                        newPlayerName = ""

                    }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        tint = Background,
                        contentDescription = "Add"
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                CustomElevatedButton(
                    "Погнали", rulesCheck,
                    onClick = {
                        game.initialPlayers(playersList)
                        game.setStage(GameStage.NIGHT)
                        navController.navigate(Screen.NightStageScreen.route)
                    },
                )
            }
        }
    }
}


fun checkRules(playersList: List<Player>, numbersOfPlayers: Int = 10): Boolean {
    val mafiaCount = playersList.count { it.role == Role.MAFIA }
    val donCount = playersList.count { it.role == Role.DON }
    val sheriffCount = playersList.count { it.role == Role.SHERIFF }

    return numbersOfPlayers == 10 && donCount == 1 && sheriffCount == 1 && mafiaCount <= 2 && (donCount + sheriffCount) > 0
}

@Composable
fun CustomDropDownMenu(player: Player, onRoleSelected: (Role) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
    ) {
        Row(
            modifier = Modifier
                .clickable { expanded = !expanded },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = player.role.name,
                color = Color.White
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More",
                tint = Color.White
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Role.values().forEach { role ->
                DropdownMenuItem(
                    text = { Text(role.name) },
                    onClick = {
                        expanded = false
                        onRoleSelected(role)
                    },
                    enabled = role != player.role
                )
            }
        }
    }
}

fun generateTestPlayers(
    mafiaCount: Int = 2,
    donCount: Int = 1,
    sheriffCount: Int = 1,
    civilCount: Int = 6
): List<Player> {
    val playerNames = listOf(
        "Player1", "Player2", "Player3", "Player4", "Player5",
        "Player6", "Player7", "Player8", "Player9", "Player10"
    )

    val players = mutableListOf<Player>()

    repeat(mafiaCount) {
        players.add(
            Player(
                number = players.size + 1,
                name = playerNames[it],
                role = Role.MAFIA,
                isAlive = true,
                score = 0.0
            )
        )
    }

    repeat(donCount) {
        players.add(
            Player(
                number = players.size + 1,
                name = playerNames[it + mafiaCount],
                role = Role.DON,
                isAlive = true,
                score = 0.0
            )
        )
    }

    repeat(sheriffCount) {
        players.add(
            Player(
                number = players.size + 1,
                name = playerNames[it + mafiaCount + donCount],
                role = Role.SHERIFF,
                isAlive = true,
                score = 0.0
            )
        )
    }

    repeat(civilCount) {
        players.add(
            Player(
                number = players.size + 1,
                name = playerNames[it + mafiaCount + donCount + sheriffCount],
                role = Role.CIVIL,
                isAlive = true,
                score = 0.0
            )
        )
    }

    return players
}
