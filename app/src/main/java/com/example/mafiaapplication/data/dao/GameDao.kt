package com.khodchenko.mafiaapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mafiaapplication.data.GameState
import com.example.mafiaapplication.data.Player

@Dao
interface GameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameState(gameState: GameState): Long

    @Query("SELECT * FROM game_state ORDER BY id DESC LIMIT 1")
    suspend fun getLastGameState(): GameState?

    @Query("DELETE FROM game_state")
    suspend fun clearGameState()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayers(players: List<Player>)

    @Query("SELECT * FROM player WHERE id = :gameId")
    suspend fun getPlayersByGameId(gameId: Int): List<Player>

    @Query("DELETE FROM player WHERE id = :gameId")
    suspend fun clearPlayersByGameId(gameId: Int)

    @Query("SELECT * FROM game_state")
    fun getAllGames(): List<GameState>
}