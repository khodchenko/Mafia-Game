package com.khodchenko.mafiaapp.data

data class Team(
    val color : TeamColor,
    val players: List<Player>
){
    enum class TeamColor{
        RED, BLACK
    }
}