package com.example.mafiaapplication.ui.element

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mafiaapplication.data.Player

@Composable
fun BestMovePanel(players: List<Player>, onPlayersSelected: (List<Player>) -> Unit) {
    val selectedPlayers = remember { mutableStateListOf<Player?>(null, null, null) }

    Column {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Лучший ход:",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Выберите трех игроков, которых игрок считает мафией",
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            selectedPlayers.forEachIndexed { index, selectedPlayer ->
                PlayerDropdown(
                    players = players,
                    selectedPlayer = selectedPlayer,
                    onPlayerSelected = { player ->
                        selectedPlayers[index] = player
                        onPlayersSelected(selectedPlayers.filterNotNull())
                    }
                )
                if (index < 2) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}

