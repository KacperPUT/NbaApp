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
import com.example.nbaappproject.ui.theme.Navigation.Screen // Prawidłowy import

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
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) // Użycie koloru z motywu
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(team.name ?: "Nieznana drużyna", style = MaterialTheme.typography.titleLarge) }, // Użycie nazwy drużyny w tytule
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface // Kolor ikony
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
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize() // Fill max size for the column
                        .background(MaterialTheme.colorScheme.background) // Użycie koloru tła z motywu
                        .padding(16.dp)
                ) {
                    // Informacje o drużynie (logo, nazwa drużyny)
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
                                    MaterialTheme.colorScheme.surfaceVariant, // Użycie koloru z motywu
                                    shape = MaterialTheme.shapes.medium // Użycie kształtu z motywu
                                )
                        )
                        Column {
                            Text(
                                team?.name ?: "Nieznana drużyna",
                                style = MaterialTheme.typography.headlineSmall, // Większy styl dla nazwy drużyny
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                "Conference: ${team.conference ?: "Brak danych o konferencji"}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                    Spacer(Modifier.height(16.dp))

                    // Przyciski do przełączania widoku
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

                    // Wyświetlanie statystyk drużyny
                    if (showStats) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface) // Użycie koloru powierzchni
                                .padding(vertical = 8.dp)
                                .clip(MaterialTheme.shapes.large) // Zaokrąglone rogi dla LazyColumn
                        ) {
                            item {
                                Text(
                                    "Statystyki Drużyny",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
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
                    } else { // Wyświetlanie składu
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface) // Użycie koloru powierzchni
                                .padding(16.dp)
                                .clip(MaterialTheme.shapes.large) // Zaokrąglone rogi dla LazyColumn
                        ) {
                            item {
                                Text(
                                    "Skład Drużyny",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(Modifier.height(8.dp))
                                // Nagłówki tabeli
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
                                Divider(color = MaterialTheme.colorScheme.outlineVariant) // Kolor dividera
                                Spacer(Modifier.height(4.dp))
                            }
                            items(players) { player ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            // Przekazujemy tylko ID gracza
                                            player.id?.let {
                                                navController.navigate(Screen.PlayerCardScreen.createRoute(it))
                                            }
                                        }
                                        .padding(vertical = 4.dp), // Dodaj padding dla wierszy graczy
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
            .padding(horizontal = 16.dp, vertical = 4.dp), // Dostosuj padding
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) // Użycie koloru z motywu
        Text(value, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface) // Użycie koloru z motywu
    }
}
