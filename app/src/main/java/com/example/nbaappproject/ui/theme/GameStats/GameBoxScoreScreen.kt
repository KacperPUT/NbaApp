package com.example.nbaappproject.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.nbaappproject.data.response.PlayerStatsItem
import com.example.nbaappproject.data.viewmodel.GameViewModel
import com.example.nbaappproject.data.viewmodel.Result
import com.example.nbaappproject.data.response.GameDetailsItem
import androidx.compose.ui.draw.clip

@Composable
fun GameBoxScoreScreen(
    gameId: Int,
    navController: NavController,
    viewModel: GameViewModel = viewModel(),
    gameDetails: GameDetailsItem?
) {
    val playerStatsResult by viewModel.playerStats.collectAsState(initial = Result.Loading)

    LaunchedEffect(gameId) {
        viewModel.fetchPlayerStats(gameId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Statystyki Graczy",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        when (playerStatsResult) {
            is Result.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
            is Result.Success -> {
                val allPlayerStats = (playerStatsResult as Result.Success<List<PlayerStatsItem>>).data
                if (allPlayerStats.isNotEmpty() && gameDetails != null) {
                    val homeTeamPlayers = allPlayerStats.filter { it.team.id == gameDetails.teams.home.id }
                    val visitorTeamPlayers = allPlayerStats.filter { it.team.id == gameDetails.teams.visitors.id }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(MaterialTheme.shapes.medium)
                    ) {
                        item {
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = gameDetails.teams.home.name,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                            )
                            PlayerStatsHeader()
                            Divider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
                        }
                        items(homeTeamPlayers) { stat ->
                            PlayerStatsRow(stat)
                            Divider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp)
                        }

                        item {
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = gameDetails.teams.visitors.name,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                            )
                            PlayerStatsHeader()
                            Divider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
                        }
                        items(visitorTeamPlayers) { stat ->
                            PlayerStatsRow(stat)
                            Divider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp)
                        }
                    }
                } else {
                    Text(
                        "Brak dostępnych statystyk graczy dla tego meczu lub brak danych o drużynach.",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            is Result.Error -> {
                Text(
                    "Wystąpił błąd podczas ładowania statystyk graczy: ${(playerStatsResult as Result.Error).exception.message}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun PlayerStatsHeader() {
    Row(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(vertical = 8.dp, horizontal = 8.dp)
    ) {
        Text(
            "Gracz",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(2f)
        )
        Text(
            "Pkt",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.5f),
            textAlign = TextAlign.End
        )
        Text(
            "Zb",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.5f),
            textAlign = TextAlign.End
        )
        Text(
            "As",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.5f),
            textAlign = TextAlign.End
        )
    }
}

@Composable
fun PlayerStatsRow(stat: PlayerStatsItem) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 8.dp)
            .background(MaterialTheme.colorScheme.surface),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "${stat.player.firstname} ${stat.player.lastname}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(2f)
        )
        Text(
            stat.points?.toString() ?: "-",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(0.5f),
            textAlign = TextAlign.End
        )
        Text(
            stat.totalRebounds?.toString() ?: "-",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(0.5f),
            textAlign = TextAlign.End
        )
        Text(
            stat.assists?.toString() ?: "-",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(0.5f),
            textAlign = TextAlign.End
        )
    }
}
