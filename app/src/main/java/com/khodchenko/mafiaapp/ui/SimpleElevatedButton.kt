package com.khodchenko.mafiaapp.ui

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khodchenko.mafiaapp.ui.theme.Background

@Composable
fun SimpleElevatedButton(onClick: () -> Unit, function: () -> Unit, ) {
    val buttonText = "Погнали"
    ElevatedButton(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        modifier = Modifier
            .height(56.dp)
            .width(168.dp)
    ) {
        Text(buttonText, color = Background, fontSize = 30.sp)

    }
}