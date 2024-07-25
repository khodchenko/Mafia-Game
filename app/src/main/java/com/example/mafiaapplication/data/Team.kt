package com.khodchenko.mafiaapp.data

import com.example.mafiaapplication.data.Player

data class Team(
    val color : TeamColor,
    val players: MutableList<Player>
){
    enum class TeamColor{
        RED, BLACK
    }
}