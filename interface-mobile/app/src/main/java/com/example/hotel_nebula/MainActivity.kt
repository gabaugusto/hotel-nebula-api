package com.example.hotel_nebula

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hotel_nebula.ui.HotelNebulaApp
import com.example.hotel_nebula.ui.theme.HotelnebulaTheme
import com.example.hotel_nebula.viewmodel.HotelViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Hotelnebula)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HotelnebulaTheme {
                val viewModel: HotelViewModel = viewModel()
                HotelNebulaApp(viewModel = viewModel)
            }
        }
    }
}
