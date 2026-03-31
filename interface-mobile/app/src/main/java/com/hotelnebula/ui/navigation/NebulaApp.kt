package com.hotelnebula.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.hotelnebula.ui.components.NebulaTopBar
import com.hotelnebula.ui.screens.HomeScreen
import com.hotelnebula.ui.screens.LoginScreen
import com.hotelnebula.ui.screens.ProfileScreen
import com.hotelnebula.ui.screens.ReservationConfirmationScreen
import com.hotelnebula.ui.screens.ReservationScreen

@Composable
fun NebulaApp() {
    val stack = remember {
        mutableStateListOf<AppScreen>(AppScreen.Login)
    }
    val currentScreen = stack.last()

    val canGoBack = stack.size > 1
    BackHandler(enabled = canGoBack) {
        if (stack.size > 1) {
            stack.removeAt(stack.lastIndex)
        }
    }

    val title = when (currentScreen) {
        AppScreen.Login -> "Hotel Nebula"
        AppScreen.Profile -> "Perfil do Usuário"
        AppScreen.Home -> "Home"
        is AppScreen.Reservation -> "Reserva"
        is AppScreen.ReservationConfirmation -> "Confirmação"
    }

    val subtitle = when (currentScreen) {
        AppScreen.Login -> "Faça login para começar"
        AppScreen.Profile -> "Dados e preferências"
        AppScreen.Home -> "Escolha o quarto ideal"
        is AppScreen.Reservation -> "Finalize sua reserva"
        is AppScreen.ReservationConfirmation -> "Tudo pronto para sua viagem"
    }

    Scaffold(
        topBar = {
            NebulaTopBar(
                title = title,
                subtitle = subtitle,
                showBack = canGoBack,
                onBack = {
                    if (stack.size > 1) {
                        stack.removeAt(stack.lastIndex)
                    }
                },
            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                AppScreen.Login -> LoginScreen(
                    onLoginClick = {
                        stack.add(AppScreen.Profile)
                    },
                )

                AppScreen.Profile -> ProfileScreen(
                    onGoHome = {
                        stack.add(AppScreen.Home)
                    },
                )

                AppScreen.Home -> HomeScreen(
                    onSelectRoom = { room ->
                        stack.add(AppScreen.Reservation(room))
                    },
                )

                is AppScreen.Reservation -> ReservationScreen(
                    room = currentScreen.room,
                    onConfirmReservation = {
                        stack.add(AppScreen.ReservationConfirmation(currentScreen.room))
                    },
                )

                is AppScreen.ReservationConfirmation -> ReservationConfirmationScreen(
                    room = currentScreen.room,
                    onNewReservation = {
                        stack.clear()
                        stack.add(AppScreen.Home)
                    },
                    onGoProfile = {
                        stack.clear()
                        stack.add(AppScreen.Profile)
                    },
                )
            }
        }
    }
}
