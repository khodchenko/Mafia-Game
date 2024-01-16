package com.khodchenko.mafiaapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.khodchenko.mafiaapp.data.GameStage
import com.khodchenko.mafiaapp.game.MafiaGame
import com.khodchenko.mafiaapp.game.Navigation
import com.khodchenko.mafiaapp.helpers.BluetoothHelper
import com.khodchenko.mafiaapp.ui.theme.MafiaAppTheme

class MainActivity : ComponentActivity() {

    private var isBluetoothPermissionGranted by mutableStateOf(false)

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            isBluetoothPermissionGranted = isGranted
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestBluetoothPermission()

        val mafiaGame = MafiaGame(
            gameStage = GameStage.START,
            gameInProgress = true
        )

        setContent {
            MafiaAppTheme(darkTheme = isSystemInDarkTheme()) {
                Surface(
                    modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
                ) {
                    if (isBluetoothPermissionGranted) {
                        val bluetoothHelper = BluetoothHelper(this)
                        Navigation(mafiaGame, bluetoothHelper)
                    }
                }
            }
        }
    }

    private fun requestBluetoothPermission() {
        when {
            checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED -> {
                isBluetoothPermissionGranted = true
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH)
            }
        }
    }
}