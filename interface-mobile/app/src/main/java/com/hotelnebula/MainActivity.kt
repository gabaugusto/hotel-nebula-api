package com.hotelnebula

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.hotelnebula.ui.navigation.NebulaApp
import com.hotelnebula.ui.theme.HotelNebulaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HotelNebulaTheme {
                NebulaApp()
            }
        }
    }
}