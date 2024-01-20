package com.khodchenko.mafiaapp.ui.stage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.khodchenko.mafiaapp.R
import com.khodchenko.mafiaapp.data.Screen
import com.khodchenko.mafiaapp.ui.CustomElevatedButton
import com.khodchenko.mafiaapp.ui.theme.Background

@Composable
fun StartGameStage(navController: NavController) {

    val englishText = "English"
    val mafiaText = "MAFIA"
    val chooseRolesText = "Выбор ролей"
    var rolesInfoText by remember { mutableStateOf("Роли будут выданы случайным образом.") }
    val importToServerText = "Импорт на сервер"
    var exportInfoText by remember { mutableStateOf("Не экспортирует данные в телеграм.") }
    var playersCountText by remember { mutableStateOf("Количество игроков:")}
    var playersCount by remember { mutableStateOf(7f) }
    val playersCountInfoText = "От количества игроков зависит количество “черных” ролей."
    val hutirText = "HUTIR"
    val productInfoText = "Продукт для внутреннего использования сообществом украинцев в Бельгии."


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Background)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = englishText,
                color = Color(0xffffffff),
                fontSize = 26.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.End
            )

            Text(
                text = mafiaText,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.koulen_regular)),
                style = TextStyle(
                    fontSize = 96.sp,
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.25f),
                        blurRadius = 10f,
                        offset = Offset(18f, 18f),
                    )
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = chooseRolesText,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.weight(1f))
                SimpleSwitch(onSwitchChanged = { isChecked ->
                    rolesInfoText = if (isChecked) {
                        "Нужно будет назначить роли каждому игроку."
                    } else {
                        "Роли будут выданы случайным образом."
                    }
                })

            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.ic_info),
                    contentDescription = "info icon",
                    tint = Color.White
                )

                Text(
                    text = rolesInfoText,
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 12.sp
                    )
                )
            }


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = importToServerText,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.weight(1f))
                SimpleSwitch(onSwitchChanged = { isChecked ->
                    exportInfoText = if (isChecked) {
                        "Экспортирует хронологию игры в телеграм."
                    } else {
                        "Не экспортирует данные в телеграм."
                    }
                })
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_info),
                    contentDescription = "info icon",
                    tint = Color.White
                )
                Text(
                    text = exportInfoText,
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 12.sp
                    )
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = playersCountText,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_info),
                    contentDescription = "info icon",
                    tint = Color.White
                )
                Text(
                    text = playersCountInfoText,
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 12.sp
                    )
                )

            }
            SliderMinimalExample(
                onSliderValueChanged = { value ->
                    playersCount = value
                    playersCountText =
                        "Количество игроков: ${value.toInt()}"
                },
                currentValue = playersCount
            )
            //todo
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center,
            ) {

                CustomElevatedButton("Погнали", enabled = true, onClick = {
                  navController.navigate(Screen.RolePickerScreen.route)
                })

            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(),

                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    text = hutirText,
                    textAlign = TextAlign.Center,
                    color = Color(0xffffffff),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = productInfoText,
                color = Color.White,
                textAlign = TextAlign.Center,
                style = TextStyle(fontSize = 10.sp),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}



@Composable
fun SimpleSwitch(onSwitchChanged: (Boolean) -> Unit) {
    var switchState by remember { mutableStateOf(false) }

    Row {
        Switch(
            checked = switchState,
            onCheckedChange = { isChecked ->
                switchState = isChecked
                onSwitchChanged(isChecked)
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Background,
                checkedTrackColor = Color.White,
                uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                uncheckedTrackColor = Color.White,
            )
        )
    }
}


@Composable
fun SliderMinimalExample(onSliderValueChanged: (Float) -> Unit, currentValue: Float) {
    Column {
        Slider(
            value = currentValue,
            onValueChange = {
                onSliderValueChanged(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(8.dp),
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.White,
                inactiveTrackColor = Color.Gray
            ),
            steps = 2,
            valueRange = 7f..10F
        )
    }
}