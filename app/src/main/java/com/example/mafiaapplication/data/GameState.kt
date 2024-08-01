package com.example.mafiaapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_state")
data class GameState(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var stage: GameStage,
    var day : Int,
    var players : List<Player>,
    var currentPlayerIndex : Int,
    var randomPlayerRoles: Boolean = false,
    var generateDumbPlayersList: Boolean = false
)

enum class GameStage {
    START,
    NIGHT,
    DAY,
    VOTE,
    VOTE_2,
    VOTE_3,
    ROLE_PICK_RANDOM,
    ROLE_PICK,
    GAME_OVER
}