package com.musauyumaz.cityguesser.viewmodel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.musauyumaz.cityguesser.data.CityRepository
import com.musauyumaz.cityguesser.data.Difficulty
import com.musauyumaz.cityguesser.data.GameState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CityRepository(application.applicationContext)

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private var currentDifficulty: Difficulty = Difficulty.EASY

    fun startGame(difficulty: Difficulty) {
        currentDifficulty = difficulty
        viewModelScope.launch {
            try {
                repository.initialize()
                loadNextQuestion()
            } catch (e: Exception) {
                _gameState.value = _gameState.value.copy(
                    isLoading = false,
                    errorMessage = "Şehirler yüklenemedi: ${e.message}"
                )
            }
        }
    }

    private fun loadNextQuestion() {
        if (_gameState.value.round > 10) {
            endGame()
            return
        }

        val city = repository.getRandomCity(currentDifficulty)

        if (city == null) {
            _gameState.value = _gameState.value.copy(
                isLoading = false,
                errorMessage = "Hiç şehir bulunamadı!"
            )
            return
        }

        val options = repository.generateOptions(city, currentDifficulty)

        _gameState.value = _gameState.value.copy(
            currentCity = city,
            options = options,
            correctAnswer = city.name,
            selectedAnswer = null,
            isLoading = false,
            errorMessage = null
        )
    }

    fun selectAnswer(selectedOption: String) {
        val currentState = _gameState.value

        if (currentState.selectedAnswer != null) return

        val isCorrect = selectedOption == currentState.correctAnswer

        val newScore = if (isCorrect) {
            currentState.score + 10
        } else {
            currentState.score
        }

        val newCombo = if (isCorrect) {
            currentState.comboCount + 1
        } else {
            0
        }

        _gameState.value = currentState.copy(
            selectedAnswer = selectedOption,
            score = newScore,
            comboCount = newCombo
        )
    }

    fun nextQuestion() {
        _gameState.value = _gameState.value.copy(
            round = _gameState.value.round + 1
        )
        loadNextQuestion()
    }

    private fun endGame() {
        _gameState.value = _gameState.value.copy(
            isGameOver = true
        )
    }

    fun restartGame() {
        _gameState.value = GameState()
        startGame(currentDifficulty)
    }
}