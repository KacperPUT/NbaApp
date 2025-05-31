package com.example.nbaappproject.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.Icons
import com.example.nbaappproject.data.response.GameDetailsItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameBoxScoreScreen(
    gameId: Int, // Otrzymujemy gameId jako argument
    navController: NavController,
    viewModel: GameViewModel = viewModel(),
    gameDetails: GameDetailsItem?
) {
    val playerStatsResult by viewModel.playerStats.collectAsState(initial = Result.Loading)

    // Pobierz statystyki graczy po wejściu na ekran
    androidx.compose.runtime.LaunchedEffect(gameId) {
        viewModel.fetchPlayerStats(gameId)
    }

    Scaffold(
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Text(
                text = "Statystyki Graczy dla meczu ID: $gameId",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            when (playerStatsResult) {
                is Result.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is Result.Success -> {
                    val playerStats = (playerStatsResult as Result.Success<List<PlayerStatsItem>>).data
                    if (playerStats.isNotEmpty()) {
                        PlayerStatsList(playerStats = playerStats)
                    } else {
                        Text(
                            "Brak dostępnych statystyk graczy dla tego meczu.",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                is Result.Error -> {
                    Text(
                        "Wystąpił błąd podczas ładowania statystyk graczy: ${(playerStatsResult as Result.Error).exception.message}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun PlayerStatsList(playerStats: List<PlayerStatsItem>) {
    LazyColumn {
        item {
            Row(Modifier.fillMaxWidth().padding(8.dp)) {
                Text("Gracz", style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(2f))
                Text("Pkt", style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(0.5f), textAlign = TextAlign.End)
                Text("Zb", style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(0.5f), textAlign = TextAlign.End)
                Text("As", style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(0.5f), textAlign = TextAlign.End)
                // Dodaj inne nagłówki statystyk, które chcesz wyświetlić
            }
            Divider()
        }
        items(playerStats) { stat ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("${stat.player.firstname} ${stat.player.lastname}", modifier = Modifier.weight(2f))
                Text(stat.points?.toString() ?: "-", modifier = Modifier.weight(0.5f), textAlign = TextAlign.End)
                Text(stat.totalRebounds?.toString() ?: "-", modifier = Modifier.weight(0.5f), textAlign = TextAlign.End)
                Text(stat.assists?.toString() ?: "-", modifier = Modifier.weight(0.5f), textAlign = TextAlign.End)
                // Dodaj inne statystyki
            }
            Divider()
        }
    }
}