package com.example.nbaappproject.ui.theme.Players

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.nbaappproject.data.viewmodel.TeamViewModel
import com.example.nbaappproject.ui.theme.Teams.StatItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerCardScreen(
    playerId: Int,
    navController: NavController,
    teamViewModel: TeamViewModel = viewModel()
) {
    val playerSeasonStats by teamViewModel.playerSeasonStats.collectAsState()
    val isLoading by teamViewModel.isLoading.collectAsState()

    LaunchedEffect(key1 = playerId) {
        teamViewModel.loadPlayerSeasonStats(playerId, "2021") // Używamy tego samego sezonu co dla listy graczy
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Karta Zawodnika", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                playerSeasonStats?.playerStats?.firstOrNull()?.let { firstStat ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        item {
                            Text(
                                "${firstStat.player.firstname} ${firstStat.player.lastname}",
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Drużyna: ${firstStat.team.name}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                "Pozycja: ${firstStat.position ?: "N/A"}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Statystyki (Średnie na Mecz - do implementacji)",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            // Na razie wyświetlamy dane z pierwszego meczu w sezonie
                            StatItem("Punkty", firstStat.points?.toString() ?: "N/A")
                            StatItem("Asysty", firstStat.assists?.toString() ?: "N/A")
                            StatItem("Zbiórki", firstStat.totalRebounds?.toString() ?: "N/A")
                            StatItem("Steals", firstStat.steals?.toString() ?: "N/A")
                            StatItem("Bloki", firstStat.blocks?.toString() ?: "N/A")
                            StatItem("Minuty", firstStat.minutes ?: "N/A")
                            // Dodaj więcej statystyk, które chcesz wyświetlić
                        }
                    }
                } ?: run {
                    Text("Brak danych o statystykach zawodnika.")
                }
            }
        }
    }
}