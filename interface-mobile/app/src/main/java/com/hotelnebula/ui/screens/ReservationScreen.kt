package com.hotelnebula.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hotelnebula.ui.components.SectionCard
import com.hotelnebula.ui.navigation.RoomOption

@Composable
fun ReservationScreen(
    room: RoomOption,
    onConfirmReservation: () -> Unit,
) {
    val guestName = remember { mutableStateOf("") }
    val checkInDate = remember { mutableStateOf("") }
    val checkOutDate = remember { mutableStateOf("") }
    val guests = remember { mutableStateOf("2") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        SectionCard {
            Text(
                text = "Reserva de ${room.title}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = room.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = room.pricePerNight,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        SectionCard {
            Text(
                text = "Detalhes da reserva",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            OutlinedTextField(
                value = guestName.value,
                onValueChange = { guestName.value = it },
                label = { Text("Nome do hóspede") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            OutlinedTextField(
                value = checkInDate.value,
                onValueChange = { checkInDate.value = it },
                label = { Text("Check-in") },
                placeholder = { Text("dd/mm/aaaa") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            OutlinedTextField(
                value = checkOutDate.value,
                onValueChange = { checkOutDate.value = it },
                label = { Text("Check-out") },
                placeholder = { Text("dd/mm/aaaa") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            OutlinedTextField(
                value = guests.value,
                onValueChange = { guests.value = it },
                label = { Text("Quantidade de hóspedes") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            Button(
                onClick = onConfirmReservation,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Confirmar reserva")
            }
        }
    }
}
