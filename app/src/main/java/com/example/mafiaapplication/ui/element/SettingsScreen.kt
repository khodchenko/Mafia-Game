package com.example.mafiaapplication.ui.element

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mafiaapplication.R
import com.example.mafiaapplication.helpers.SharedPreferencesHelper
import com.example.mafiaapplication.ui.theme.Background
import com.khodchenko.mafiaapp.data.Screen

//@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SettingsScreen(
navController: NavController,
sharedPreferencesHelper: SharedPreferencesHelper
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(8.dp)
    ) {

        Column() {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .fillMaxWidth()
            ) {
                IconButton(
                    onClick = {
                        //navController.previousBackStackEntry
                    },
                    modifier = Modifier.padding(end = 10.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_previous),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(120.dp)
                    )
                }

                Text(
                    text = "Настройки",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = Color.White
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 10.dp)
            ) {
                Column {
                    Text(
                        text = "Cбросить игру",
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Normal,
                        fontSize = 22.sp,
                        color = Color.White,
                        modifier = Modifier.clickable {
                            restartGame(navController = navController, sharedPreferencesHelper = sharedPreferencesHelper)
                        }
                    )

                    Text(
                        text = "Восресить игрока",
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Normal,
                        fontSize = 22.sp,
                        color = Color.White
                    )

                    Text(
                        text = "Сменить день",
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Normal,
                        fontSize = 22.sp,
                        color = Color.White
                    )
                }
                Column {

                }
            }
        }
    }
}

fun restartGame(navController : NavController, sharedPreferencesHelper: SharedPreferencesHelper) {
    sharedPreferencesHelper.clearSavedGame()
    navController.navigate(Screen.StartGameScreen.route)
}
