package com.musauyumaz.cityguesser.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.musauyumaz.cityguesser.data.Difficulty


@Composable
fun DifficultySelectionScreen(onDifficultySelected: (Difficulty) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🌆 Şehrim Nerede?",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Zorluk Seviyesi Seç",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        DifficultyButton(
            text = "🟢 Kolay",
            description = "Ünlü şehirler",
            onClick = { onDifficultySelected(Difficulty.EASY) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        DifficultyButton(
            text = "🟡 Orta",
            description = "Bilinir şehirler",
            onClick = { onDifficultySelected(Difficulty.MEDIUM) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        DifficultyButton(
            text = "🔴 Zor",
            description = "Az bilinen şehirler",
            onClick = { onDifficultySelected(Difficulty.HARD) }
        )
    }
}

@Composable
fun DifficultyButton(
    text: String,
    description: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text, style = MaterialTheme.typography.titleLarge)
            Text(description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}