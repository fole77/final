package com.example.afinal.models

import com.example.afinal.utils.DEFAULT_ICONS

class DrawGame(private val boardSize: BoardSize) {

    val cards: List<GameCard>
    var numPairsFound =0
    private var numCardFlips = 0
    private var indexOfSingleSelectedCard: Int? =null

    init {   //随机匹配图片
            val chosenImages =DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
            val randomizedImages =(chosenImages + chosenImages).shuffled()
            cards = randomizedImages.map { GameCard(it) }
    }

    fun flipCard(position: Int): Boolean {     //点击翻转，翻转逻辑
        numCardFlips++
        val card = cards[position]
        var foundMatch = false

        if (indexOfSingleSelectedCard ==null) {
            restoreCards()
            indexOfSingleSelectedCard = position
        } else {
            foundMatch = checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
        card.isFaceUp = !card.isFaceUp
        return foundMatch
    }

    private fun checkForMatch(position1: Int, position2: Int): Boolean {
        if (cards[position1].identifier != cards[position2].identifier) {
            return false
        }
        cards[position1].isMatched = true
        cards[position2].isMatched = true
        numPairsFound++
        return true
    }

    private fun restoreCards() {  //遍历持有卡片，将卡片翻转朝下
        for (card in cards) {
            if (!card.isMatched) {
                card.isFaceUp= false
            }
        }
    }

    fun haveWonGame(): Boolean {
        return numPairsFound == boardSize.getNumPairs()
    }

    fun isCardFaceUp(position: Int): Boolean {   //检查面朝向
        return cards[position].isFaceUp
    }

    fun getNumMoves(): Int {
        return numCardFlips/2
    }
}