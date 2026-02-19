package com.musauyumaz.cityguesser.ui

import androidx.compose.runtime.*
import com.musauyumaz.cityguesser.viewmodel.GameViewModel


sealed class Screen {
    object DifficultySelection : Screen()
    object Game : Screen()
}

@Composable
fun CityGuesserApp(viewModel: GameViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.DifficultySelection) }

    when (currentScreen) {
        is Screen.DifficultySelection -> {
            DifficultySelectionScreen { difficulty ->
                viewModel.startGame(difficulty)
                currentScreen = Screen.Game
            }
        }
        is Screen.Game -> {
            GameScreen(viewModel = viewModel)
        }
    }
}
