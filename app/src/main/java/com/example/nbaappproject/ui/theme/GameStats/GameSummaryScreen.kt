package com.example.nbaappproject.ui.theme.GameStats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.example.nbaappproject.ui.theme.Teams.StatItem
import androidx.navigation.NavController

@Composable
fun GameSummaryScreen(
    modifier: Modifier = Modifier,
    gameDetails: GameDetailsItem?,
    teamViewModel: TeamViewModel = viewModel(),
    navController: NavController
) {
    val gameTeamStatistics by teamViewModel.gameTeamStatistics.collectAsState()
    val isLoading by teamViewModel.isLoading.collectAsState()

    LaunchedEffect(key1 = gameDetails?.id) {
        gameDetails?.id?.let {
            teamViewModel.loadGameTeamStatistics(it)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (gameDetails != null) {
            val homeScores = gameDetails.scores.home.linescore ?: emptyList()
            val visitorScores = gameDetails.scores.visitors.linescore ?: emptyList()

            Text(
                text = "Wynik Końcowy: ${gameDetails.scores.home.points} - ${gameDetails.scores.visitors.points}",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 0.dp)
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Q1", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                Text("Q2", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                Text("Q3", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                Text("Q4", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                Text("T", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0 until 4) {
                    Text(
                        text = homeScores.getOrNull(i) ?: "-",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
                Text(
                    text = gameDetails.scores.home.points?.toString() ?: "-",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0 until 4) {
                    Text(
                        text = visitorScores.getOrNull(i) ?: "-",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
                Text(
                    text = gameDetails.scores.visitors.points?.toString() ?: "-",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }


            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Porównanie Drużyn",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))

            gameDetails.timesTied?.let {
                StatItem(label = "Remisy", value = it.toString())
            }
            gameDetails.leadChanges?.let {
                StatItem(label = "Zmiany Prowadzenia", value = it.toString())
            }
            gameDetails.nugget?.let {
                if (it.isNotBlank()) {
                    StatItem(label = "Ciekawostka", value = it)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            } else {
                gameTeamStatistics?.response?.let { statsList ->
                    if (statsList.size == 2) {
                        val homeStats = statsList.find { it.team.id == gameDetails.teams.home.id }?.statistics?.firstOrNull()
                        val visitorStats = statsList.find { it.team.id == gameDetails.teams.visitors.id }?.statistics?.firstOrNull()

                        if (homeStats != null && visitorStats != null) {
                            Text(
                                "Szczegółowe Statystyki Drużynowe",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(modifier = Modifier.weight(0.3f))
                                Text(
                                    gameDetails.teams.home.name,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.weight(0.35f),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    gameDetails.teams.visitors.name,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.weight(0.35f),
                                    textAlign = TextAlign.Center
                                )
                            }
                            Divider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)

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
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.weight(0.3f)
                                    )
                                    Text(
                                        homeValue,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.weight(0.35f),
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        visitorValue,
                                        style = MaterialTheme.typography.bodyLarge,
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
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Ładowanie podsumowania meczu...",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}
