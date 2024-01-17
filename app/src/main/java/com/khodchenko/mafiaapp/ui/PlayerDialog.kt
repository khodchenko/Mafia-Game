package com.khodchenko.mafiaapp.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.khodchenko.mafiaapp.data.Player
import com.khodchenko.mafiaapp.ui.theme.Background

@Composable
fun PlayerDialog(
    player: Player?,
    activePlayer: Player,
    onDismiss: () -> Unit,
    onVoteClick: () -> Unit,
    onFoulClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            if (player != null) {
                Text(text = player.name)
            }
        },
        text = {
            Text(text = "Активный игрок сейчас: ${activePlayer.number}:${activePlayer.name}")
        },
        confirmButton = {
            Button(
                onClick = {
                    onDismiss()
                    onVoteClick()
                },
                colors = ButtonDefaults.buttonColors(Background)
            ) {
                Text("Выставить на голосование", color = Color.White)
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismiss()
                    onFoulClick()
                },
                colors = ButtonDefaults.buttonColors(Background)
            ) {
                Text("Дать фол", color = Color.White)
            }
        }
    )
}