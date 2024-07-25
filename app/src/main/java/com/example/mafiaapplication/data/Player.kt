package com.example.mafiaapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player")
data class Player(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var gameId: Int,
    val number: Int,
    val name: String,
    var role: Role,
    var fouls: Int = 0,
    var isAlive: Boolean = true,
    var score: Double = 0.0
)

enum class Role {
    CIVIL, MAFIA, DON, SHERIFF
}