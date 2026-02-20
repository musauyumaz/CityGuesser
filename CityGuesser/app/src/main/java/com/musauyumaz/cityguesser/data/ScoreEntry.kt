package com.musauyumaz.cityguesser.data

data class ScoreEntry(
    val score: Int,
    val difficulty: Difficulty,
    val timestamp: Long = System.currentTimeMillis()
)
