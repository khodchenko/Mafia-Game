package com.khodchenko.mafiaapp.data

data class Player (
    val number : Int,
    val name : String,
    val role : Role,
    var fouls : Int = 0,
    val isAlive : Boolean = true,
    val score : Double = 0.0
)