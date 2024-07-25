package com.example.mafiaapplication


import android.Manifest
import android.content.Context
import android.content.SharedPreferences
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
import androidx.room.Room
import com.example.mafiaapplication.ui.theme.MafiaApplicationTheme
import com.khodchenko.mafiaapp.data.dao.GameDao
import com.example.mafiaapplication.data.GameStage
import com.example.mafiaapplication.data.GameState
import com.example.mafiaapplication.data.Player
import com.example.mafiaapplication.data.Role
import com.khodchenko.mafiaapp.game.MafiaGame
import com.khodchenko.mafiaapp.game.Navigation
import com.example.mafiaapplication.helpers.AppDatabase
import com.khodchenko.mafiaapp.helpers.BluetoothHelper
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : ComponentActivity() {
    private lateinit var db: AppDatabase
    private lateinit var gameDao: GameDao
    private lateinit var mafiaGame: MafiaGame
    private var isBluetoothPermissionGranted by mutableStateOf(false)
    private lateinit var sharedPreferences: SharedPreferences

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            isBluetoothPermissionGranted = isGranted
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestBluetoothPermission()

        db = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "game-database"
        ).build()

        sharedPreferences = getSharedPreferences("MafiaAppPrefs", Context.MODE_PRIVATE)

        mafiaGame = MafiaGame( gameState = GameState(stage = GameStage.START, day = 1), players = mutableListOf())

        setContent {
            MafiaApplicationTheme(darkTheme = isSystemInDarkTheme()) {
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

    private fun saveGameState(gameState: GameState) {
        val jsonObject = JSONObject()
        jsonObject.put("id", gameState.id)
        jsonObject.put("stage", gameState.stage.name)
        jsonObject.put("day", gameState.day)
        sharedPreferences.edit().putString("gameState", jsonObject.toString()).apply()
    }

    private fun savePlayers(players: List<Player>) {
        val jsonArray = JSONArray()
        players.forEach { player ->
            val jsonObject = JSONObject()
            jsonObject.put("id", player.id)
            jsonObject.put("gameId", player.gameId)
            jsonObject.put("name", player.name)
            jsonObject.put("role", player.role)
            jsonArray.put(jsonObject)
        }
        sharedPreferences.edit().putString("players", jsonArray.toString()).apply()
    }

    private fun loadGameState(): GameState {
        val gameStateJson = sharedPreferences.getString("gameState", null)
        return if (gameStateJson != null) {
            val jsonObject = JSONObject(gameStateJson)
            GameState(
                id = jsonObject.getInt("id"),
                stage = GameStage.valueOf(jsonObject.getString("stage")),
                day = jsonObject.getInt("day")
            )
        } else {
            GameState(stage = GameStage.START, day = 1)
        }
    }

    private fun loadPlayers(): MutableList<Player> {
        val playersJson = sharedPreferences.getString("players", null)
        return if (playersJson != null) {
            val jsonArray = JSONArray(playersJson)
            val players = mutableListOf<Player>()
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                players.add(
                    Player(
                        id = jsonObject.getInt("id"),
                        number = jsonObject.getInt("number"),
                        gameId = jsonObject.getInt("gameId"),
                        name = jsonObject.getString("name"),
                        role = Role.valueOf(jsonObject.getString("role"))
                    )
                )
            }
            players
        } else {
            mutableListOf()
        }
    }
}