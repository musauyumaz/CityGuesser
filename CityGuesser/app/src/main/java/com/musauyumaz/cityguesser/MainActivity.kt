package com.musauyumaz.cityguesser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.musauyumaz.cityguesser.ui.CityGuesserApp
import com.musauyumaz.cityguesser.ui.theme.CityGuesserTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            enableEdgeToEdge()
        } catch (_: Exception) {
            // MIUI cihazlarda enableEdgeToEdge() sorun yaratabilir
        }
        setContent {
            CityGuesserTheme {
                CityGuesserApp()
            }
        }
    }
}