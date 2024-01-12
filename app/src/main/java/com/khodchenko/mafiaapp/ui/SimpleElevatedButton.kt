package com.khodchenko.mafiaapp.ui

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.khodchenko.mafiaapp.ui.theme.Background

@Composable
fun SimpleElevatedButton(buttonText: String, onClick: () -> Unit, function: () -> Unit, ) {
    ElevatedButton(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        Text(buttonText, color = Background, fontSize = 30.sp)

    }
}