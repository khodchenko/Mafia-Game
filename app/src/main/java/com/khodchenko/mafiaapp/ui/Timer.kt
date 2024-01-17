package com.khodchenko.mafiaapp.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.khodchenko.mafiaapp.R
import com.khodchenko.mafiaapp.ui.theme.Background
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ticker



@Composable
fun Timer() {
    var isRunning by remember { mutableStateOf(false) }
    var time by remember { mutableStateOf(0L) }

    val context = LocalContext.current


    Box(
        modifier = Modifier
            .background(color = Color.Transparent)
            .padding(4.dp)
    ) {
        Column {

            Row(
                modifier = Modifier
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatTime(time),
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 32.sp,
                    color = Color.White
                )

                val hasBluetoothPermission = ContextCompat.checkSelfPermission(
                    LocalContext.current,
                    Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED

                Text(
                    text = if (hasBluetoothPermission) {
                       // val connectedDevice = bluetoothHelper.getBluetoothDevices().firstOrNull()
                     //   connectedDevice?.toString() ?:
                        "Нет устройств"
                    }else {
                        "Нет прав на отображение"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(start = 12.dp),
                    fontSize = 14.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        onClick = {
                            isRunning = false
                            time = 0L
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_stop),
                            tint = Color.White,
                            contentDescription = null, modifier = Modifier.size(120.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            isRunning = !isRunning
                        }
                    ) {
                        Icon(
                            painter = if (isRunning) painterResource(id = R.drawable.ic_pause) else painterResource(
                                id = R.drawable.ic_play
                            ),
                            contentDescription = null,
                            tint = Color.White, modifier = Modifier.size(120.dp)
                        )
                    }
                }


                TimerTicker(
                    isRunning = isRunning,
                    onTick = {
                        time += TICK_INTERVAL
                    }
                )
            }

            LinearProgressIndicator(
                progress = if (isRunning) time.toFloat() / MAX_TIME.toFloat() else 0f,
                color = Background,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            )
        }

    }
}

@OptIn(ObsoleteCoroutinesApi::class)
@Composable
private fun TimerTicker(
    isRunning: Boolean,
    onTick: () -> Unit
) {
    val vibrator = LocalContext.current.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    var ticks by remember { mutableStateOf(0L) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            val ticker = ticker(delayMillis = TICK_INTERVAL, initialDelayMillis = 0)

            try {
                for (event in ticker) {
                    onTick()

                    ticks++

                    if (ticks * TICK_INTERVAL == MAX_TIME) {
                        vibratePhone(vibrator)
                    }
                }
            } finally {
                ticker.cancel()
            }
        }
    }
}

private fun vibratePhone(vibrator: Vibrator) {
    val vibrationEffect = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
    vibrator.vibrate(vibrationEffect)
}

@Composable
private fun formatTime(time: Long): String {
    val seconds = (time / 1000) % 60
    return "%02d".format(seconds)
}

private const val TICK_INTERVAL = 1000L
private const val MAX_TIME = 60000L