package com.khodchenko.mafiaapp.ui.stage


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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khodchenko.mafiaapp.data.Player
import com.khodchenko.mafiaapp.data.Role
import com.khodchenko.mafiaapp.ui.SimpleElevatedButton
import com.khodchenko.mafiaapp.ui.theme.Background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RolePickerStage(players: MutableList<Player>) {
    var newPlayerName by remember { mutableStateOf("") }
    var playersList by remember { mutableStateOf(players) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Background)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn {
                items(playersList) { player ->
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${player.number}: ",
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 26.sp,
                            color = Color.White
                        )
                        Text(
                            text = player.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 26.sp,
                            color = Color.White
                        )

                        Demo_DropDownMenu(selectedRole = player.role)
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))

            OutlinedTextField(
                value = newPlayerName,
                onValueChange = { newPlayerName = it },

                label = { Text("Введите имя", color = Color.White) },
                textStyle = TextStyle(color = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                ElevatedButton(onClick = {
                    if (playersList.isNotEmpty()) {
                        playersList = playersList.dropLast(1).toMutableList()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        tint = Background,
                        contentDescription = "Delete"
                    )
                }

                ElevatedButton(onClick = {
                    if (newPlayerName.isNotEmpty()) {
                        val newPlayer = Player(
                            number = playersList.size + 1,
                            name = newPlayerName,
                            role = Role.CIVIL,
                            isAlive = true,
                            score = 0.0
                        )

                        playersList = (playersList + newPlayer).toMutableList()
                        newPlayerName = ""
                    }
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
                SimpleElevatedButton(onClick = { /* Handle button click here */ }) {
                }
            }

        }

    }
}

@Preview()
@Composable
private fun RolePickerStagePreview() {
    val players = mutableListOf<Player>(
        Player(1, "Player 1", Role.MAFIA, true, 0.0),
        Player(2, "Player 2", Role.CIVIL, true, 0.0),
    )
    RolePickerStage(players)
}


@Composable
fun Demo_DropDownMenu(selectedRole: Role) {
    var expanded by remember { mutableStateOf(false) }
    var currentRole by remember(selectedRole) { mutableStateOf(selectedRole) }

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
                text = currentRole.name,
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
                        currentRole = role
                        expanded = false
                    },
                    enabled = role != currentRole
                )
            }
        }
    }
}
