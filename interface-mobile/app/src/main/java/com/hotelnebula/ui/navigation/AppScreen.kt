package com.hotelnebula.ui.navigation

data class RoomOption(
    val id: String,
    val title: String,
    val pricePerNight: String,
    val description: String,
)

sealed class AppScreen {
    data object Login : AppScreen()
    data object Profile : AppScreen()
    data object Home : AppScreen()
    data class Reservation(val room: RoomOption) : AppScreen()
    data class ReservationConfirmation(val room: RoomOption) : AppScreen()
}
