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
import com.example.nbaappproject.data.RetrofitInstance
import com.example.nbaappproject.data.viewmodel.TeamViewModel
import com.example.nbaappproject.data.model.Team
import com.example.nbaappproject.viewmodel.TeamsViewModel
import com.example.nbaappproject.data.model.PlayerUi
import com.example.nbaappproject.ui.theme.Navigation.Screen

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
    println("Liczba graczy pobrana dla teamId $teamId: ${players.size}")

    val isLoading by teamViewModel.isLoading.collectAsState()
    var showStats by remember { mutableStateOf(true) } // Stan do przełączania widoku

    LaunchedEffect(key1 = teamId) {
        println("LaunchedEffect dla teamId: $teamId")
        teamViewModel.loadTeamStats(teamId, "2020") // Ustawiamy sezon na "2020"
        println("Po loadTeamStats")
        println("Przed loadPlayers z teamId: $teamId")
        teamViewModel.loadPlayers(teamId, "2021") // Ustawiamy sezon dla graczy
        println("Po loadPlayers")
    }

    LaunchedEffect(key1 = teamId) {
        println("Drugi LaunchedEffect dla teamId: $teamId (getTeamStatistics)")
        val statsResponse = RetrofitInstance.api.getTeamStatistics(teamId = teamId, season = "2020") // Ustawiamy sezon na "2020"
        Log.d("TeamDetailsScreen", "Odpowiedź getTeamStatistics w LaunchedEffect: $statsResponse")
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
                title = { Text("Statystyki Drużynowe", style = MaterialTheme.typography.titleLarge) },
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
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Informacje o drużynie (logo, konferencja)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        AsyncImage(
                            model = team.logoUrl,
                            contentDescription = "Logo",
                            modifier = Modifier
                                .size(64.dp)
                                .background(
                                    Color.LightGray,
                                    shape = MaterialTheme.shapes.medium
                                )
                        )
                        Column {
                            Text(
                                team?.name ?: "Nieznana drużyna",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    Spacer(Modifier.height(16.dp))

                    // Przyciski do przełączania widoku
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { showStats = true }) {
                            Text("Statystyki")
                        }
                        Button(onClick = { showStats = false }) {
                            Text("Skład")
                        }
                    }
                    Spacer(Modifier.height(16.dp))

                    // Wyświetlanie statystyk drużyny
                    if (showStats) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background)
                                .padding(vertical = 8.dp)
                        ) {
                            item {
                                Text("Statystyki Drużyny", style = MaterialTheme.typography.titleMedium)
                                Spacer(Modifier.height(8.dp))
                            }
                            stats?.stats?.let { listOfStats ->
                                if (listOfStats.isNotEmpty()) {
                                    val s = listOfStats.first()
                                    item {
                                        StatItem("Games Played", "${s.games}")
                                        StatItem("Points", "${s.points}")
                                        StatItem("FG%", s.fieldGoalPercentage ?: "N/A")
                                        StatItem("FT%", s.freeThrowPercentage ?: "N/A")
                                        StatItem("3P%", s.threePointPercentage ?: "N/A")
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
                            }
                        }
                    } else { // Wyświetlanie składu
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background)
                                .padding(16.dp)
                        ) {
                            item {
                                Text("Skład Drużyny", style = MaterialTheme.typography.titleMedium)
                                Spacer(Modifier.height(8.dp))
                                // Nagłówki tabeli
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Nr", style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(0.1f))
                                    Text("Nazwisko", style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(0.3f))
                                    Text("Imię", style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(0.2f))
                                    Text("Poz.", style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(0.1f))
                                    Text("Wzrost", style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(0.2f))
                                    Text("Waga", style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(0.1f))
                                }
                                Spacer(Modifier.height(4.dp))
                                Divider()
                                Spacer(Modifier.height(4.dp))
                            }
                            items(players) { player ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                        player.id?.let {
                                            navController.navigate(Screen.PlayerCardScreen.createRoute(it))
                                        }
                                    },
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(player.jerseyNumber?.toString() ?: "N/A", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(0.1f))
                                    Text(player.lastName ?: "N/A", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(0.3f))
                                    Text(player.firstName ?: "N/A", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(0.2f))
                                    Text(player.position ?: "N/A", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(0.1f))
                                    Text(player.height, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(0.2f))
                                    Text(player.weight, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(0.1f))
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
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}