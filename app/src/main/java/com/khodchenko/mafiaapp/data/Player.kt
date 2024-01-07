package com.khodchenko.mafiaapp.data

data class Player (
    val number : Int,
    val name : String,
    val role : Role,
    val isAlive : Boolean,
    val score : Double
)