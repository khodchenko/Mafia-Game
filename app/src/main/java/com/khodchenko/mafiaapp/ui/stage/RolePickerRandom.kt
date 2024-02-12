package com.khodchenko.mafiaapp.ui.stage

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khodchenko.mafiaapp.R
import com.khodchenko.mafiaapp.data.Player
import com.khodchenko.mafiaapp.data.Role
import com.khodchenko.mafiaapp.ui.theme.Background

@Composable
fun RolePickerRandom(player: Player) {
    var showImage by remember { mutableStateOf(false) }

    val rotationState by animateFloatAsState(if (showImage) 180f else 0f, label = "")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Background)
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row() {
                Icon(
                    painter = painterResource(id = R.drawable.ic_refresh),
                    contentDescription = "info icon",
                    tint = Color.White,
                    modifier = Modifier.size(44.dp)
                )
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Игрок №${player.number}",
                color = Color(0xffffffff),
                fontSize = 26.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = player.name,
                color = Color(0xffffffff),
                fontSize = 30.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .clickable {
                        showImage = !showImage
                    },
                contentAlignment = Alignment.Center
            ) {
                val imageModifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(rotationY = rotationState)

                Image(
                    painter = if (showImage) painterResource(id = R.drawable.ic_next)
                    else painterResource(id = R.drawable.back),
                    contentDescription = "Your Image",
                    modifier = imageModifier,
                    contentScale = ContentScale.FillHeight
                )
            }
        }
    }
}

@Preview
@Composable
private fun RolePickerRandomPreview() {
    RolePickerRandom(Player(1, "Player", Role.MAFIA))
}