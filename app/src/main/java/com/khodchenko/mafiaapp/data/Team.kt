package com.khodchenko.mafiaapp.data

data class Team(
    val color : TeamColor,
    val players: MutableList<Player>
){
    enum class TeamColor{
        RED, BLACK
    }
}