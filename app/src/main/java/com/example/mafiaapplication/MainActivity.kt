package com.example.mafiaapplication

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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.mafiaapplication.ui.theme.MafiaApplicationTheme
import com.example.mafiaapplication.data.GameStage
import com.example.mafiaapplication.data.GameState
import com.khodchenko.mafiaapp.game.MafiaGame
import com.example.mafiaapplication.game.Navigation
import com.example.mafiaapplication.helpers.SharedPreferencesHelper
import com.khodchenko.mafiaapp.helpers.BluetoothHelper

class MainActivity : ComponentActivity() {
    private lateinit var mafiaGame: MafiaGame
    private var isBluetoothPermissionGranted by mutableStateOf(false)
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            isBluetoothPermissionGranted = isGranted
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestBluetoothPermission()

        sharedPreferencesHelper = SharedPreferencesHelper(this)

        mafiaGame = MafiaGame(
            gameState = GameState(stage = GameStage.START, day = 1),
            players = mutableListOf()
        )

        setContent {
            MafiaApplicationTheme(darkTheme = isSystemInDarkTheme()) {
                Surface(
                    modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
                ) {
                    if (isBluetoothPermissionGranted) {
                        val bluetoothHelper = BluetoothHelper(this)
                        Navigation(
                            game = mafiaGame,
                            bluetoothHelper = bluetoothHelper,
                            sharedPreferencesHelper = sharedPreferencesHelper
                        )
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