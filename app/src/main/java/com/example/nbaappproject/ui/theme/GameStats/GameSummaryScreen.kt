package com.example.nbaappproject.ui.theme.GameStats

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nbaappproject.data.response.GameDetailsItem

@Composable
fun GameSummaryScreen(
    modifier: Modifier = Modifier,
    gameDetails: GameDetailsItem?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (gameDetails != null) {
            Text(
                text = "Final Score: ${gameDetails.scores.home.points} - ${gameDetails.scores.visitors.points}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
                Text(text = gameDetails.teams.home.name, style = MaterialTheme.typography.bodyMedium)
                Text(text = "vs", style = MaterialTheme.typography.bodyMedium)
                Text(text = gameDetails.teams.visitors.name, style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Q1   Q2   Q3   Q4   T",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
                val homeScores = gameDetails?.scores?.home?.linescore ?: emptyList()
                val visitorScores = gameDetails?.scores?.visitors?.linescore ?: emptyList()
                Text(text = homeScores.joinToString("   "), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground)
                Text(text = visitorScores.joinToString("   "), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground)
            }
            Text(
                text = "${gameDetails.scores.home.points}   ${gameDetails.scores.visitors.points}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Team Comparison (Placeholder)",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            // Tutaj możesz dodać więcej szczegółów porównania drużyn, jeśli są dostępne w gameDetails
        } else {
            Text("Ładowanie podsumowania meczu...")
        }
    }
}