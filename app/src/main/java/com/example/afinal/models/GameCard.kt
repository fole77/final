package com.example.afinal.models

data class GameCard(
    val identifier: Int,
    var isFaceUp: Boolean = false,   //卡片朝下
    var isMatched: Boolean = false   //是否匹配
)