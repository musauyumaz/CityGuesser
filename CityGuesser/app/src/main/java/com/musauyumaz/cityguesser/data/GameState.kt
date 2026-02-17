package com.musauyumaz.cityguesser.data

data class GameState(
    val currentCity: City? = null,
    val options: List<String> = emptyList(),
    val score: Int = 0,
    val round: Int = 1,
    val isGameOver: Boolean = false,
    val correctAnswer: String? = null,
    val selectedAnswer: String? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val timeLeft: Int = 30,
    val comboCount: Int = 0
)
