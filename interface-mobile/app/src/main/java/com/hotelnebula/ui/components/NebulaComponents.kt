package com.hotelnebula.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hotelnebula.ui.theme.HotelNebulaTheme

@Composable
fun NebulaTopBar(
    title: String,
    subtitle: String,
    showBack: Boolean,
    onBack: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (showBack) {
                TextButton(onClick = onBack) {
                    Text("Voltar")
                }
            }
        }
    }
}

@Composable
fun SectionCard(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier.padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            content = content,
        )
    }
}

@Composable
fun NebulaHeroCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF1D3557), Color(0xFF457B9D), Color(0xFF2A9D8F)),
                ),
            )
            .padding(20.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Hotel Nebula Experience",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Escolha seu quarto ideal e finalize sua reserva em poucos toques.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.95f),
            )
            Text(
                text = "Este Card tem de ser simples",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.8f),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NebulaTopBarPreview() {
    HotelNebulaTheme {
        NebulaTopBar(
            title = "Perfil",
            subtitle = "Bem-vindo, Gabriel",
            showBack = true,
            onBack = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NebulaHeroCardPreview() {
    HotelNebulaTheme {
        NebulaHeroCard()
    }
}

@Preview(showBackground = true)
@Composable
fun SectionCardPreview() {
    HotelNebulaTheme {
        SectionCard {
            Text(
                text = "Preferencias",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text("- Vista para o mar")
            Text("- Check-in antecipado")
            Text("- Cafe premium no quarto")
        }
    }
}
