package com.hotelnebula.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hotelnebula.ui.components.SectionCard
import com.hotelnebula.ui.navigation.RoomOption
import com.hotelnebula.ui.theme.HotelNebulaTheme

@Composable
fun ReservationConfirmationScreen(
    room: RoomOption,
    onNewReservation: () -> Unit,
    onGoProfile: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        SectionCard {
            Text(
                text = "Reserva confirmada ✨",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = "Sua experiência no ${room.title} já está no radar da equipe Nebula.",
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = "Você receberá os próximos passos no check-in.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        SectionCard {
            Text(
                text = "Resumo",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text("Quarto: ${room.title}")
            Text("Valor base: ${room.pricePerNight}")
            Text("Status: aguardando pagamento no balcão")
        }

        Button(
            onClick = onNewReservation,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Fazer nova reserva")
        }
        OutlinedButton(
            onClick = onGoProfile,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Ir para perfil")
        }
    }
}


@Preview
@Composable
fun ReservationConfirmationScreenPreview() {
    HotelNebulaTheme {
        ReservationConfirmationScreen(
            room = RoomOption(
                title = "Suíte Luxo com Vista para o Mar",
                description = "Aprecie a vista deslumbrante do oceano em nossa suíte de luxo, equipada com uma cama king-size, varanda privativa e decoração elegante.",
                pricePerNight = "R$ 1.200/noite",
                id = "luxo",
            ),
            onNewReservation = {},
            onGoProfile = {},
        )
    }
}  