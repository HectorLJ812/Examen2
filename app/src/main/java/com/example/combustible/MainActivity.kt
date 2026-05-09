package com.example.combustible

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize

val Morado      = androidx.compose.ui.graphics.Color(0xFF6B3FA0)
val FondoGris   = androidx.compose.ui.graphics.Color(0xFFECE9F1)
val TarjetaGris = androidx.compose.ui.graphics.Color(0xFFF5F3F8)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    color = FondoGris,
                    modifier = Modifier.fillMaxSize()
                ) {
                    PrecioScreen()
                }
            }
        }
    }
}