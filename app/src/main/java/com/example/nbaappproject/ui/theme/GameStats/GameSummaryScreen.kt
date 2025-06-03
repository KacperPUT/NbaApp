package com.example.nbaappproject.ui.theme.GameStats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nbaappproject.data.response.GameDetailsItem
import com.example.nbaappproject.data.viewmodel.TeamViewModel
import com.example.nbaappproject.ui.theme.Teams.StatItem // Reużywamy StatItem
import androidx.navigation.NavController // Dodaj import NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameSummaryScreen(
    modifier: Modifier = Modifier,
    gameDetails: GameDetailsItem?,
    teamViewModel: TeamViewModel = viewModel(), // Dodajemy ViewModel
    navController: NavController // Dodaj NavController
) {
    val gameTeamStatistics by teamViewModel.gameTeamStatistics.collectAsState()
    val isLoading by teamViewModel.isLoading.collectAsState()

    LaunchedEffect(key1 = gameDetails?.id) {
        gameDetails?.id?.let {
            teamViewModel.loadGameTeamStatistics(it)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Podsumowanie Meczu", style = MaterialTheme.typography.titleLarge) },
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
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(paddingValues) // Zastosuj padding z Scaffold
                .background(MaterialTheme.colorScheme.background), // Upewnij się, że tło jest ustawione
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (gameDetails != null) {
                Text(
                    text = "Wynik Końcowy: ${gameDetails.scores.home.points} - ${gameDetails.scores.visitors.points}",
                    style = MaterialTheme.typography.headlineSmall, // Większy styl dla wyniku
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = gameDetails.teams.home.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "vs",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = gameDetails.teams.visitors.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Q1   Q2   Q3   Q4   T",
                    style = MaterialTheme.typography.labelLarge, // Styl dla nagłówków kwartalnych
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
                    val homeScores = gameDetails.scores.home.linescore ?: emptyList()
                    val visitorScores = gameDetails.scores.visitors.linescore ?: emptyList()
                    Text(
                        text = homeScores.joinToString("   "),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = visitorScores.joinToString("   "),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Porównanie Drużyn", // Usunięto "Placeholder"
                    style = MaterialTheme.typography.titleLarge, // Większy styl dla nagłówka porównania
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Dodajemy dostępne już informacje
                gameDetails.timesTied?.let {
                    StatItem(label = "Remisy", value = it.toString()) // Użycie StatItem
                }
                gameDetails.leadChanges?.let {
                    StatItem(label = "Zmiany Prowadzenia", value = it.toString()) // Użycie StatItem
                }
                gameDetails.nugget?.let {
                    if (it.isNotBlank()) {
                        StatItem(label = "Ciekawostka", value = it) // Użycie StatItem
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Wyświetlanie szczegółowych statystyk drużynowych z meczu
                if (isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                } else {
                    gameTeamStatistics?.response?.let { statsList ->
                        if (statsList.size == 2) { // Oczekujemy statystyk dla dwóch drużyn
                            val homeStats = statsList.find { it.team.id == gameDetails.teams.home.id }?.statistics?.firstOrNull()
                            val visitorStats = statsList.find { it.team.id == gameDetails.teams.visitors.id }?.statistics?.firstOrNull()

                            if (homeStats != null && visitorStats != null) {
                                Text(
                                    "Szczegółowe Statystyki Drużynowe",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                // Nagłówki kolumn
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.surfaceVariant) // Tło dla nagłówków tabeli
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceAround,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Spacer(modifier = Modifier.weight(0.3f)) // Pusta przestrzeń dla etykiety statystyki
                                    Text(
                                        gameDetails.teams.home.name,
                                        style = MaterialTheme.typography.labelLarge, // Styl nagłówka drużyny
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.weight(0.35f),
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        gameDetails.teams.visitors.name,
                                        style = MaterialTheme.typography.labelLarge, // Styl nagłówka drużyny
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.weight(0.35f),
                                        textAlign = TextAlign.Center
                                    )
                                }
                                Divider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp) // Kolor dividera

                                // Funkcja pomocnicza do wyświetlania wierszy statystyk
                                @Composable
                                fun StatComparisonRow(label: String, homeValue: String, visitorValue: String) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceAround,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            label,
                                            style = MaterialTheme.typography.bodyMedium, // Styl etykiety statystyki
                                            color = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.weight(0.3f)
                                        )
                                        Text(
                                            homeValue,
                                            style = MaterialTheme.typography.bodyLarge, // Styl wartości statystyki
                                            color = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.weight(0.35f),
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            visitorValue,
                                            style = MaterialTheme.typography.bodyLarge, // Styl wartości statystyki
                                            color = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.weight(0.35f),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }

                                StatComparisonRow("Punkty", homeStats.points?.toString() ?: "N/A", visitorStats.points?.toString() ?: "N/A")
                                StatComparisonRow("FGM", homeStats.fgm?.toString() ?: "N/A", visitorStats.fgm?.toString() ?: "N/A")
                                StatComparisonRow("FGA", homeStats.fga?.toString() ?: "N/A", visitorStats.fga?.toString() ?: "N/A")
                                StatComparisonRow("FG%", homeStats.fgp ?: "N/A", visitorStats.fgp ?: "N/A")
                                StatComparisonRow("3PM", homeStats.tpm?.toString() ?: "N/A", visitorStats.tpm?.toString() ?: "N/A")
                                StatComparisonRow("3PA", homeStats.tpa?.toString() ?: "N/A", visitorStats.tpa?.toString() ?: "N/A")
                                StatComparisonRow("3P%", homeStats.tpp ?: "N/A", visitorStats.tpp ?: "N/A")
                                StatComparisonRow("FTM", homeStats.ftm?.toString() ?: "N/A", visitorStats.ftm?.toString() ?: "N/A")
                                StatComparisonRow("FTA", homeStats.fta?.toString() ?: "N/A", visitorStats.fta?.toString() ?: "N/A")
                                StatComparisonRow("FT%", homeStats.ftp ?: "N/A", visitorStats.ftp ?: "N/A")
                                StatComparisonRow("Zbiórki Off.", homeStats.offReb?.toString() ?: "N/A", visitorStats.offReb?.toString() ?: "N/A")
                                StatComparisonRow("Zbiórki Def.", homeStats.defReb?.toString() ?: "N/A", visitorStats.defReb?.toString() ?: "N/A")
                                StatComparisonRow("Zbiórki Całk.", homeStats.totReb?.toString() ?: "N/A", visitorStats.totReb?.toString() ?: "N/A")
                                StatComparisonRow("Asysty", homeStats.assists?.toString() ?: "N/A", visitorStats.assists?.toString() ?: "N/A")
                                StatComparisonRow("Faule", homeStats.pFouls?.toString() ?: "N/A", visitorStats.pFouls?.toString() ?: "N/A")
                                StatComparisonRow("Steals", homeStats.steals?.toString() ?: "N/A", visitorStats.steals?.toString() ?: "N/A")
                                StatComparisonRow("Straty", homeStats.turnovers?.toString() ?: "N/A", visitorStats.turnovers?.toString() ?: "N/A")
                                StatComparisonRow("Bloki", homeStats.blocks?.toString() ?: "N/A", visitorStats.blocks?.toString() ?: "N/A")
                                StatComparisonRow("Plus/Minus", homeStats.plusMinus ?: "N/A", visitorStats.plusMinus ?: "N/A")
                                StatComparisonRow("Fast Break Pts", homeStats.fastBreakPoints?.toString() ?: "N/A", visitorStats.fastBreakPoints?.toString() ?: "N/A")
                                StatComparisonRow("Points In Paint", homeStats.pointsInPaint?.toString() ?: "N/A", visitorStats.pointsInPaint?.toString() ?: "N/A")
                                StatComparisonRow("Second Chance Pts", homeStats.secondChancePoints?.toString() ?: "N/A", visitorStats.secondChancePoints?.toString() ?: "N/A")
                                StatComparisonRow("Points Off Turnovers", homeStats.pointsOffTurnovers?.toString() ?: "N/A", visitorStats.pointsOffTurnovers?.toString() ?: "N/A")
                                StatComparisonRow("Największe Prowadzenie", homeStats.biggestLead?.toString() ?: "N/A", visitorStats.biggestLead?.toString() ?: "N/A")
                                StatComparisonRow("Najdłuższy Run", homeStats.longestRun?.toString() ?: "N/A", visitorStats.longestRun?.toString() ?: "N/A")

                            } else {
                                Text(
                                    "Brak szczegółowych statystyk drużynowych dla tego meczu.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        } else {
                            Text(
                                "Brak szczegółowych statystyk drużynowych dla tego meczu.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            } else {
                Text(
                    "Ładowanie podsumowania meczu...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}
