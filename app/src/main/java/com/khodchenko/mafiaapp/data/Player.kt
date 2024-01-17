package com.khodchenko.mafiaapp.data

data class Player (
    val number : Int,
    val name : String,
    var role : Role,
    var fouls : Int = 0,
    var isAlive : Boolean = true,
    val score : Double = 0.0
)