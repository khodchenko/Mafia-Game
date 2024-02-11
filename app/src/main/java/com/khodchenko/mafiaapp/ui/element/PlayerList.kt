package com.khodchenko.mafiaapp.ui.element

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khodchenko.mafiaapp.data.Player
import com.khodchenko.mafiaapp.game.MafiaGame


@Composable
fun PlayerList(
    playersList: List<Player>,
    activePlayerIndex: Int,
    onPlayerClick: (Int) -> Unit,
    activePlayerColor: Color = Color.Gray,
    showRoles: Boolean = false,
    showVotes: Boolean = false,
    showScores: Boolean = false,
    game: MafiaGame
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Transparent)
            .padding(2.dp)
    ) {
        Column() {

            LazyColumn {
                itemsIndexed(playersList) { index, player ->
                    PlayerItem(
                        player,
                        index == activePlayerIndex,
                        { onPlayerClick(index) },
                        activePlayerColor,
                        showRoles,
                        showVotes,
                        showScores,
                        game
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayerItem(
    player: Player,
    isActive: Boolean,
    onPlayerClick: () -> Unit,
    activePlayerColor: Color,
    showRoles: Boolean,
    showVotes: Boolean,
    showScores: Boolean,
    game: MafiaGame
) {
    Row(
        modifier = Modifier
            .clickable { onPlayerClick() }
            .fillMaxWidth()
            .padding(6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (isActive) activePlayerColor else Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.width(200.dp),
            text = "${player.number}.  ${player.name}",
            style = MaterialTheme.typography.titleMedium,
            fontSize = if (isActive) 28.sp else 22.sp,
            color = Color.White
        )

        Text(
            text = if (!showVotes) {
                if (player.fouls == 0) {
                    ""
                } else player.fouls.toString()
            } else game.getVotersByCandidate(player)?.size.toString(),
            style = MaterialTheme.typography.bodySmall,
            fontSize = 22.sp,
            color = Color.White,
            modifier = Modifier.padding(start = 12.dp)
        )

        Text(
            text = if (!showScores) ""
            else player.score.toString(),
            style = MaterialTheme.typography.bodySmall,
            fontSize = 22.sp,
            color = Color.White,
            modifier = Modifier.padding(start = 12.dp)
        )

        Spacer(modifier = Modifier.weight(1f))
        if (showRoles) {
            Text(
                text = player.role.toString(),
                style = MaterialTheme.typography.bodySmall,
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}
