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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.nbaappproject.data.viewmodel.TeamViewModel
import com.example.nbaappproject.data.model.Team
import com.example.nbaappproject.viewmodel.TeamsViewModel

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

    val stats by teamViewModel.teamStats.collectAsState()
    val players by teamViewModel.players.collectAsState()
    println("Liczba graczy pobrana dla teamId $teamId: ${players.size}") // Dodany log

    val isLoading by teamViewModel.isLoading.collectAsState()


    LaunchedEffect(teamId) {
        println("Ładuję statystyki i graczy dla teamId: $teamId") // Dodany log
        teamViewModel.loadTeamStats(teamId)
        teamViewModel.loadPlayers(teamId)
    }

    if (team == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(team.name ?: "Nieznana drużyna", style = MaterialTheme.typography.titleLarge) },                navigationIcon = {
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
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(16.dp)
                ) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            AsyncImage(
                                model = team.logoUrl,
                                contentDescription = "Logo",
                                //placeholder = painterResource(R.drawable.placeholder),
                                //error = painterResource(R.drawable.placeholder),
                                modifier = Modifier
                                    .size(64.dp)
                                    .background(
                                        Color.LightGray,
                                        shape = MaterialTheme.shapes.medium
                                    )
                            )
                            Column {
                                Text(
                                    "Conference: ${team.conference ?: "Brak danych o konferencji"}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                //Text("Rank: ${team.rank}", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                    }

                    stats?.let { s ->
                        item {
                            Text("Team Stats", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(8.dp))

                            StatItem("Games Played", "${s.games}")
                            StatItem("Points", "${s.points}")
                            StatItem("FG%", s.fieldGoalPercentage)
                            StatItem("FT%", s.freeThrowPercentage)
                            StatItem("3P%", s.threePointPercentage)
                            StatItem("Fast Break Pts", "${s.fastBreakPoints}")
                            StatItem("Points in Paint", "${s.pointsInPaint}")
                            StatItem("Biggest Lead", "${s.biggestLead}")
                            StatItem("Second Chance Pts", "${s.secondChancePoints}")
                            StatItem("Points Off Turnovers", "${s.pointsOffTurnovers}")
                            StatItem("Longest Run", "${s.longestRun}")
                            StatItem("FGM", "${s.fgm}")
                            StatItem("FGA", "${s.fga}")
                            StatItem("FTM", "${s.ftm}")
                            StatItem("FTA", "${s.fta}")
                            StatItem("TPM", "${s.tpm}")
                            StatItem("TPA", "${s.tpa}")
                            StatItem("Off. Rebounds", "${s.offensiveRebounds}")
                            StatItem("Def. Rebounds", "${s.defensiveRebounds}")
                            StatItem("Total Rebounds", "${s.totalRebounds}")
                            StatItem("Assists", "${s.assists}")
                            StatItem("Personal Fouls", "${s.personalFouls}")
                            StatItem("Steals", "${s.steals}")
                            StatItem("Turnovers", "${s.turnovers}")
                            StatItem("Blocks", "${s.blocks}")
                            StatItem("Plus/Minus", "${s.plusMinus}")
                            Spacer(Modifier.height(16.dp))
                        }
                    }

                    item {
                        Text("Roster", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))
                    }

                    items(players) { player ->
                        val fullName = "${player.firstName} ${player.lastName}"
                        println("Wyświetlam zawodnika: $fullName")
                        Text(fullName, style = MaterialTheme.typography.bodyMedium)
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
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}
