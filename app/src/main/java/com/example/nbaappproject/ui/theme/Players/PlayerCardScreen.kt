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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.nbaappproject.data.viewmodel.TeamViewModel
import com.example.nbaappproject.ui.theme.Teams.StatItem // Upewnij się, że ten import jest poprawny

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerCardScreen(
    playerId: Int,
    navController: NavController,
    teamViewModel: TeamViewModel = viewModel()
) {
    val playerSeasonAverages by teamViewModel.playerSeasonAverages.collectAsState()
    val isLoading by teamViewModel.isLoading.collectAsState()

    LaunchedEffect(key1 = playerId) {
        teamViewModel.loadPlayerSeasonStats(playerId, "2020") // Sezon "2020" dla statystyk zawodnika
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Karta Zawodnika", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary // Kolor ikony
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary, // Kolor TopAppBar
                    titleContentColor = MaterialTheme.colorScheme.onPrimary // Kolor tytułu
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background // Ustaw kolor tła dla całego Scaffold
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background), // Upewnij się, że tło jest ustawione
            contentAlignment = Alignment.TopCenter
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) // Kolor wskaźnika ładowania
            } else {
                playerSeasonAverages?.let { averages ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface) // Kolor tła LazyColumn
                            .padding(16.dp)
                            .clip(MaterialTheme.shapes.large) // Zaokrąglone rogi dla LazyColumn
                    ) {
                        item {
                            Text(
                                "${averages.firstName} ${averages.lastName}",
                                style = MaterialTheme.typography.headlineSmall, // Styl nazwiska
                                color = MaterialTheme.colorScheme.onSurface // Kolor tekstu
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Drużyna: ${averages.teamName}",
                                style = MaterialTheme.typography.bodyMedium, // Styl drużyny
                                color = MaterialTheme.colorScheme.onSurfaceVariant // Kolor tekstu
                            )
                            Text(
                                "Rozegrane mecze: ${averages.gamesPlayed}",
                                style = MaterialTheme.typography.bodyMedium, // Styl rozegranych meczów
                                color = MaterialTheme.colorScheme.onSurfaceVariant // Kolor tekstu
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            // Statystyki Sezonowe
                            Text(
                                "Statystyki Sezonowe (Średnie na Mecz)",
                                style = MaterialTheme.typography.titleMedium, // Styl nagłówka statystyk
                                color = MaterialTheme.colorScheme.onSurface // Kolor tekstu
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            StatItem("Minuty", averages.avgMinutes)
                            StatItem("Punkty", averages.avgPoints.toString())
                            StatItem("Asysty", averages.avgAssists.toString())
                            StatItem("Zbiórki (Całkowite)", averages.avgTotalRebounds.toString())
                            StatItem("Zbiórki (Offensywne)", averages.avgOffensiveRebounds.toString())
                            StatItem("Zbiórki (Defensywne)", averages.avgDefensiveRebounds.toString())
                            StatItem("Steals", averages.avgSteals.toString())
                            StatItem("Bloki", averages.avgBlocks.toString())
                            StatItem("Faule Osobiste", averages.avgPersonalFouls.toString())
                            StatItem("Straty", averages.avgTurnovers.toString())
                            StatItem("Plus/Minus", averages.avgPlusMinus.toString())
                            StatItem("FG%", "${String.format("%.1f", averages.avgFieldGoalPercentage)}%")
                            StatItem("FT%", "${String.format("%.1f", averages.avgFreeThrowPercentage)}%")
                            StatItem("3P%", "${String.format("%.1f", averages.avgThreePointPercentage)}%")
                        }
                    } ?: run {
                        Text(
                            "Brak danych o statystykach zawodnika dla tego sezonu.",
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
