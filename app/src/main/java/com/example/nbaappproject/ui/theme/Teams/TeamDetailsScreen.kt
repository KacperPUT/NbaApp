package com.example.nbaappproject.ui.theme.Teams

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.nbaappproject.data.RetrofitInstance
import com.example.nbaappproject.data.viewmodel.TeamViewModel
import com.example.nbaappproject.data.model.Team
import com.example.nbaappproject.viewmodel.TeamsViewModel
import com.example.nbaappproject.data.model.PlayerUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamDetailsScreen(
    teamId: Int,
    navController: NavController,
    teamsViewModel: TeamsViewModel = viewModel(),
    teamViewModel: TeamViewModel = viewModel()
) {
    val teams by teamsViewModel.teams.collectAsState()
    val team = teams.find { it.id == teamId }

    val statsResponse by teamViewModel.teamStats.collectAsState()
    val players by teamViewModel.players.collectAsState()
    println("Liczba graczy pobrana dla teamId $teamId: ${players.size}")

    val isLoading by teamViewModel.isLoading.collectAsState()
    var showStats by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = teamId) {
        println("LaunchedEffect dla teamId: $teamId")
        teamViewModel.loadTeamStats(teamId, "2024")
        println("Po loadTeamStats")
        println("Przed loadPlayers z teamId: $teamId")
        teamViewModel.loadPlayers(teamId, "2024")
        println("Po loadPlayers")
    }

    LaunchedEffect(key1 = teamId) {
        println("Drugi LaunchedEffect dla teamId: $teamId (getTeamStatistics)")
        val statsApiCallResponse = RetrofitInstance.api.getTeamStatistics(teamId = teamId, season = "2024")
        Log.d("TeamDetailsScreen", "Odpowiedź getTeamStatistics w LaunchedEffect: ${statsApiCallResponse.body()?.stats?.firstOrNull()?.games ?: "Brak danych o meczach"}")
    }

    if (team == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(team.name ?: "Nieznana drużyna", style = MaterialTheme.typography.titleLarge) },
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
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AsyncImage(
                            model = team.logoUrl,
                            contentDescription = "Logo",
                            modifier = Modifier
                                .size(64.dp)
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    shape = MaterialTheme.shapes.medium
                                )
                        )
                        Column {
                            Text(
                                team?.name ?: "Nieznana drużyna",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            val conferenceName = team.conference
                            if (conferenceName != null && conferenceName.isNotBlank()) {
                                Text(
                                    "Konferencja: $conferenceName",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = { showStats = true },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (showStats) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = if (showStats) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer
                            ),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text("Statystyki", style = MaterialTheme.typography.labelLarge)
                        }
                        Button(
                            onClick = { showStats = false },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (!showStats) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = if (!showStats) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer
                            ),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text("Skład", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                    Spacer(Modifier.height(16.dp))

                    if (showStats) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(vertical = 8.dp)
                                .clip(MaterialTheme.shapes.large)
                        ) {
                            item {
                                Text(
                                    "Statystyki Drużyny (Średnie na Mecz)",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                                Spacer(Modifier.height(8.dp))
                            }
                            statsResponse?.stats?.let { listOfStats ->
                                if (listOfStats.isNotEmpty()) {
                                    val s = listOfStats.first()
                                    val gamesPlayed = s.games.toDouble()
                                    val safeGamesPlayed = if (gamesPlayed > 0) gamesPlayed else 1.0

                                    item {
                                        StatItem("Rozegrane mecze", "${s.games}")
                                        StatItem("Punkty", String.format("%.1f", s.points.toDouble() / safeGamesPlayed))
                                        StatItem("FG%", s.fieldGoalPercentage ?: "N/A")
                                        StatItem("FT%", s.freeThrowPercentage ?: "N/A")
                                        StatItem("3P%", s.threePointPercentage ?: "N/A")
                                        StatItem("FGM", String.format("%.1f", s.fgm.toDouble() / safeGamesPlayed))
                                        StatItem("FGA", String.format("%.1f", s.fga.toDouble() / safeGamesPlayed))
                                        StatItem("FTM", String.format("%.1f", s.ftm.toDouble() / safeGamesPlayed))
                                        StatItem("FTA", String.format("%.1f", s.fta.toDouble() / safeGamesPlayed))
                                        StatItem("3PM", String.format("%.1f", s.tpm.toDouble() / safeGamesPlayed))
                                        StatItem("3PA", String.format("%.1f", s.tpa.toDouble() / safeGamesPlayed))
                                        StatItem("Zbiórki Off.", String.format("%.1f", s.offensiveRebounds.toDouble() / safeGamesPlayed))
                                        StatItem("Zbiórki Def.", String.format("%.1f", s.defensiveRebounds.toDouble() / safeGamesPlayed))
                                        StatItem("Zbiórki Całk.", String.format("%.1f", s.totalRebounds.toDouble() / safeGamesPlayed))
                                        StatItem("Asysty", String.format("%.1f", s.assists.toDouble() / safeGamesPlayed))
                                        StatItem("Faule Osobiste", String.format("%.1f", s.personalFouls.toDouble() / safeGamesPlayed))
                                        StatItem("Steals", String.format("%.1f", s.steals.toDouble() / safeGamesPlayed))
                                        StatItem("Straty", String.format("%.1f", s.turnovers.toDouble() / safeGamesPlayed))
                                        StatItem("Bloki", String.format("%.1f", s.blocks.toDouble() / safeGamesPlayed))
                                        StatItem("Plus/Minus", String.format("%.1f", s.plusMinus.toDouble() / safeGamesPlayed))
                                        Spacer(Modifier.height(16.dp))
                                    }
                                } else {
                                    item {
                                        Text(
                                            "Brak dostępnych statystyk drużynowych dla tego sezonu.",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(16.dp)
                                        )
                                    }
                                }
                            } ?: run {
                                item {
                                    Text(
                                        "Brak dostępnych statystyk drużynowych dla tego sezonu.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(16.dp)
                                .clip(MaterialTheme.shapes.large)
                        ) {
                            item {
                                Text(
                                    "Skład Drużyny",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Nr", style = MaterialTheme.typography.labelSmall, modifier = Modifier.weight(0.1f), color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text("Nazwisko", style = MaterialTheme.typography.labelSmall, modifier = Modifier.weight(0.3f), color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text("Imię", style = MaterialTheme.typography.labelSmall, modifier = Modifier.weight(0.2f), color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text("Poz.", style = MaterialTheme.typography.labelSmall, modifier = Modifier.weight(0.1f), color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text("Wzrost", style = MaterialTheme.typography.labelSmall, modifier = Modifier.weight(0.2f), color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text("Waga", style = MaterialTheme.typography.labelSmall, modifier = Modifier.weight(0.1f), color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Spacer(Modifier.height(4.dp))
                                Divider(color = MaterialTheme.colorScheme.outlineVariant)
                                Spacer(Modifier.height(4.dp))
                            }
                            items(players) { player ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            player.id?.let {
                                                navController.navigate(com.example.nbaappproject.ui.theme.Navigation.Screen.PlayerCardScreen.createRoute(it))
                                            }
                                        }
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(player.jerseyNumber?.toString() ?: "N/A", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(0.1f), color = MaterialTheme.colorScheme.onSurface)
                                    Text(player.lastName ?: "N/A", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(0.3f), color = MaterialTheme.colorScheme.onSurface)
                                    Text(player.firstName ?: "N/A", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(0.2f), color = MaterialTheme.colorScheme.onSurface)
                                    Text(player.position ?: "N/A", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(0.1f), color = MaterialTheme.colorScheme.onSurface)
                                    Text(player.height, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(0.2f), color = MaterialTheme.colorScheme.onSurface)
                                    Text(player.weight, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(0.1f), color = MaterialTheme.colorScheme.onSurface)
                                }
                            }
                            if (players.isEmpty()) {
                                item {
                                    Text(
                                        "Brak dostępnych graczy dla tej drużyny w tym sezonie.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
@Composable
fun StatItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
    }
}
