package com.musauyumaz.cityguesser.data

data class City(
    val name: String,
    val country: String,
    val imageUrl: String,
    val difficulty: Difficulty
)

enum class Difficulty{
    EASY,
    MEDIUM,
    HARD
}