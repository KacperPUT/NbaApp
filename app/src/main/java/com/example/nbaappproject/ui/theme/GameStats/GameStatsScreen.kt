package com.example.nbaappproject.ui.theme.GameStats

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun GameStatsScreen(
    modifier: Modifier = Modifier,
) {
    var selectedTab by remember { mutableStateOf("Summary") }

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextButton(
                onClick = { selectedTab = "Summary" },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (selectedTab == "Summary") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text(
                    text = "Summary",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            TextButton(
                onClick = { selectedTab = "Box Score" },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (selectedTab == "Box Score") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text(
                    text = "Box Score",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        when (selectedTab) {
            "Summary" -> GameSummaryScreen()
            "Box Score" -> GameBoxScoreScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameStatsScreenPreview() {
    GameStatsScreen()
}
