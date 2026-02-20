package com.musauyumaz.cityguesser.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.collections.filter
import kotlin.collections.firstOrNull
import kotlin.collections.sortedByDescending
import kotlin.collections.toMutableList

class ScoreRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("scores", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveScore(score: Int, difficulty: Difficulty) {
        val scores = getScores().toMutableList()
        scores.add(ScoreEntry(score, difficulty))

        val scoresJson = gson.toJson(scores)
        prefs.edit().putString("score_list", scoresJson).apply()
    }

    fun getScores(): List<ScoreEntry> {
        val scoresJson = prefs.getString("score_list", null) ?: return emptyList()
        val type = object : TypeToken<List<ScoreEntry>>() {}.type
        return gson.fromJson(scoresJson, type)
    }

    fun getTopScores(difficulty: Difficulty? = null, limit: Int = 10): List<ScoreEntry> {
        val scores = getScores()
        val filtered = if (difficulty != null) {
            scores.filter { it.difficulty == difficulty }
        } else {
            scores
        }
        return filtered.sortedByDescending { it.score }.take(limit)
    }

    fun getHighScore(difficulty: Difficulty? = null): Int {
        return getTopScores(difficulty, 1).firstOrNull()?.score ?: 0
    }

    fun clearScores() {
        prefs.edit().clear().apply()
    }
}