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
import com.example.nbaappproject.ui.theme.Teams.StatItem

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
        teamViewModel.loadPlayerSeasonStats(playerId, "2020")
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
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.TopCenter
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            } else {
                playerSeasonAverages?.let { averages ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(16.dp)
                            .clip(MaterialTheme.shapes.large)
                    ) {
                        item {
                            Text(
                                "${averages.firstName} ${averages.lastName}",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Drużyna: ${averages.teamName}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "Rozegrane mecze: ${averages.gamesPlayed}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                "Statystyki Sezonowe (Średnie na Mecz)",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
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
                    }
                }
            }
        }
    }
}
