package com.hotelnebula.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hotelnebula.R
import com.hotelnebula.ui.components.NebulaHeroCard
import com.hotelnebula.ui.components.SectionCard
import com.hotelnebula.ui.navigation.RoomOption
import com.hotelnebula.ui.theme.HotelNebulaTheme

private val rooms = listOf(
    RoomOption(
        id = "aurora",
        title = "Suíte Aurora",
        pricePerNight = "R$ 690/noite",
        description = "Vista panorâmica, banheira e iluminação ambiente.",
    ),
    RoomOption(
        id = "ocean",
        title = "Quarto Ocean",
        pricePerNight = "R$ 480/noite",
        description = "Varanda privativa e cama king com enxoval premium.",
    ),
    RoomOption(
        id = "skyline",
        title = "Skyline Studio",
        pricePerNight = "R$ 390/noite",
        description = "Estilo contemporâneo para viagens rápidas e confortáveis.",
    ),
)

@Composable
fun HomeScreen(
    onSelectRoom: (RoomOption) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.room_0003),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.35f)),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            NebulaHeroCard()

            Text(
                text = "Escolha seu quarto",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
            )

            rooms.forEach { room ->
                SectionCard {
                    Text(
                        text = room.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = room.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = room.pricePerNight,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Button(onClick = { onSelectRoom(room) }) {
                            Text("Reservar")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HotelNebulaTheme {
        HomeScreen(onSelectRoom = {})
    }
}