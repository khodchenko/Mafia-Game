package com.khodchenko.mafiaapp.data

data class Day (
    var number: Int = 0
) {
    override fun toString(): String {
        return "$number"
    }
}


