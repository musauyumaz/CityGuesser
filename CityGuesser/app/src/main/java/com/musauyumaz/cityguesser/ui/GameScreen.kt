package com.musauyumaz.cityguesser.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.musauyumaz.cityguesser.data.Difficulty
import com.musauyumaz.cityguesser.data.GameState
import com.musauyumaz.cityguesser.viewmodel.GameViewModel

@Composable
fun GameScreen(viewModel: GameViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val gameState by viewModel.gameState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            gameState.isLoading -> LoadingScreen()
            gameState.isGameOver -> GameOverScreen(
                score = gameState.score,
                onRestart = { viewModel.restartGame() }
            )
            gameState.errorMessage != null -> ErrorScreen(
                message = gameState.errorMessage ?: "Bilinmeyen hata",
                onRetry = { viewModel.startGame(Difficulty.EASY) }
            )
            else -> GameContent(
                gameState = gameState,
                onAnswerSelected = { viewModel.selectAnswer(it) },
                onNextQuestion = { viewModel.nextQuestion() }
            )
        }
    }
}

@Composable
fun LoadingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Yükleniyor...", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground)
    }
}

@Composable
fun ErrorScreen(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("❌ Hata", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(16.dp))
        Text(message, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onRetry, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) {
            Text("Tekrar Dene", color = Color.White)
        }
    }
}

@Composable
fun GameContent(
    gameState: GameState,
    onAnswerSelected: (String) -> Unit,
    onNextQuestion: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        ScoreHeader(
            score = gameState.score,
            round = gameState.round,
            combo = gameState.comboCount
        )

        Spacer(modifier = Modifier.height(16.dp))

        TimerBar(timeLeft = gameState.timeLeft)

        Spacer(modifier = Modifier.height(16.dp))

        CityImage(
            imageUrl = gameState.currentCity?.imageUrl ?: "",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Bu şehir hangisi?",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        OptionButtons(
            options = gameState.options,
            selectedAnswer = gameState.selectedAnswer,
            correctAnswer = gameState.correctAnswer,
            onOptionClick = onAnswerSelected
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (gameState.selectedAnswer != null) {
            Button(
                onClick = onNextQuestion,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text("Sonraki Soru")
            }
        }
    }
}

@Composable
fun TimerBar(timeLeft: Int) {
    val progress = timeLeft / 30f
    val color = when {
        timeLeft > 20 -> Color(0xFF4CAF50)
        timeLeft > 10 -> Color(0xFFFFC107)
        else -> Color(0xFFF44336)
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("⏱️ Süre", style = MaterialTheme.typography.bodyMedium)
            Text("${timeLeft}s", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color
        )
    }
}

@Composable
fun ScoreHeader(score: Int, round: Int, combo: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Skor: $score", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text("Soru: $round/10", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        if (combo > 1) {
            Text("🔥 $combo", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CityImage(imageUrl: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = imageUrl,
        contentDescription = "Şehir fotoğrafı",
        modifier = modifier.clip(RoundedCornerShape(16.dp)),
        contentScale = ContentScale.Crop,
        placeholder = painterResource(android.R.drawable.ic_menu_gallery),
        error = painterResource(android.R.drawable.ic_menu_report_image)
    )
}

@Composable
fun OptionButtons(
    options: List<String>,
    selectedAnswer: String?,
    correctAnswer: String?,
    onOptionClick: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        options.forEach { option ->
            OptionButton(
                text = option,
                isSelected = option == selectedAnswer,
                isCorrect = option == correctAnswer && selectedAnswer != null,
                isWrong = option == selectedAnswer && option != correctAnswer,
                onClick = { if (selectedAnswer == null) onOptionClick(option) }
            )
        }
    }
}

@Composable
fun OptionButton(
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    isWrong: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isCorrect -> Color(0xFF4CAF50)
        isWrong -> Color(0xFFF44336)
        isSelected -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.surface
    }

    val textColor = when {
        isCorrect -> Color.White
        isWrong -> Color.White
        else -> MaterialTheme.colorScheme.onSurface
    }

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        )
    ) {
        Text(text, modifier = Modifier.padding(8.dp), fontWeight = FontWeight.Medium)
    }
}

@Composable
fun GameOverScreen(score: Int, onRestart: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🎉 Oyun Bitti!", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(24.dp))
        Text("Skorun: $score", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onRestart) {
            Text("Yeniden Oyna")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    MaterialTheme {
        GameScreen()
    }
}
