package com.example.mafiaapplication.helpers

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.mafiaapplication.game.GameStage
import com.example.mafiaapplication.game.GameState
import com.example.mafiaapplication.data.Player
import com.example.mafiaapplication.data.Role
import org.json.JSONArray
import org.json.JSONObject

class SharedPreferencesHelper(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MafiaAppPrefs", Context.MODE_PRIVATE)

    fun loadGameState(): GameState {
        val gameStateJson = sharedPreferences.getString("gameState", null)
        return if (gameStateJson != null) {
            val jsonObject = JSONObject(gameStateJson)
            GameState(
                id = jsonObject.getInt("id"),
                stage = GameStage.valueOf(jsonObject.getString("stage")),
                day = jsonObject.getInt("day"),
                currentPlayerIndex = jsonObject.getInt("currentPlayerIndex"),
                numbersOfPlayers = jsonObject.getInt("numbersOfPlayers"),
                players = loadPlayers()
            )
        } else {
            GameState(stage = GameStage.START, day = 1, currentPlayerIndex = 0, players = mutableListOf())
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
                        gameId = jsonObject.getInt("gameId"),
                        number = jsonObject.getInt("number"),
                        name = jsonObject.getString("name"),
                        role = Role.valueOf(jsonObject.getString("role")),
                        fouls = jsonObject.getInt("fouls"),
                        isAlive = jsonObject.getBoolean("isAlive"),
                        score = jsonObject.getDouble("score")
                    )
                )
            }
            players
        } else {
            mutableListOf()
        }
    }

    fun saveGameState(gameState: GameState) {
        val jsonObject = JSONObject()
        jsonObject.put("id", gameState.id)
        jsonObject.put("stage", gameState.stage.name)
        jsonObject.put("day", gameState.day)
        jsonObject.put("currentPlayerIndex", gameState.currentPlayerIndex)
        jsonObject.put("numbersOfPlayers", gameState.numbersOfPlayers)
        sharedPreferences.edit().putString("gameState", jsonObject.toString()).apply()
        Log.d("SharedPreferencesHelper", "Game state saved \n$jsonObject")

        val jsonArray = JSONArray()
        gameState.players.forEach { player ->
            val playerJsonObject = JSONObject()
            playerJsonObject.put("id", player.id)
            playerJsonObject.put("gameId", player.gameId)
            playerJsonObject.put("number", player.number)
            playerJsonObject.put("name", player.name)
            playerJsonObject.put("role", player.role.name)
            playerJsonObject.put("fouls", player.fouls)
            playerJsonObject.put("isAlive", player.isAlive)
            playerJsonObject.put("score", player.score)
            jsonArray.put(playerJsonObject)
        }
        sharedPreferences.edit().putString("players", jsonArray.toString()).apply()
        Log.d("SharedPreferencesHelper", "Game players saved \n$jsonArray")
    }

    fun clearSavedGame() {
        sharedPreferences.edit().remove("gameState").remove("players").apply()
        Log.d("SharedPreferencesHelper", "Saved game data cleared")
    }
}
