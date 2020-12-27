package com.example.afinal.models


enum class BoardSize(val numCards: Int) {
    EASY(8),
    NORMAL(18),
    HARD(24);


    fun getWidth(): Int {
        return when (this) {
            EASY -> 2
            NORMAL -> 3
            HARD -> 4
        }
    }

    fun getHeight(): Int {
        return numCards /getWidth()
    }
    fun getNumPairs(): Int {
        return numCards /2
    }
}