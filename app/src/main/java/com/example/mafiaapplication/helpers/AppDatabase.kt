package com.example.mafiaapplication.helpers

import androidx.room.Database
import androidx.room.RoomDatabase
import com.khodchenko.mafiaapp.data.dao.GameDao
import com.example.mafiaapplication.data.GameState
import com.example.mafiaapplication.data.Player

@Database(entities = [GameState::class, Player::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao

}