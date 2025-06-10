package com.example.nbaappproject.ui.theme.Standings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.nbaappproject.data.response.StandingResponseItem
import com.example.nbaappproject.viewmodel.StandingsViewModel
import com.example.nbaappproject.ui.theme.Navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandingsScreen(
    navController: NavController,
    viewModel: StandingsViewModel = viewModel()
) {
    var selectedConference by remember { mutableStateOf("East") }
    val tabs = listOf("East", "West")

    val standings by viewModel.standings.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadStandings()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tabele", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            TabRow(
                selectedTabIndex = tabs.indexOf(selectedConference),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = {
                            Text(
                                title,
                                style = MaterialTheme.typography.labelLarge
                            )
                        },
                        selected = selectedConference == title,
                        onClick = { selectedConference = title },
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
                errorMessage != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = errorMessage ?: "Nieznany błąd",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                else -> {
                    val filteredStandings = standings
                        .filter { it.conference.name.equals(selectedConference, ignoreCase = true) && it.team != null }
                        .sortedBy { it.conference.rank }

                    println("Standings data received: ${standings.size} items")

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(filteredStandings) { index, standing ->
                            println("Processing standing for: ${standing.team?.name}")
                            if (index == 6 || index == 10) {
                                Divider(
                                    color = MaterialTheme.colorScheme.outlineVariant,
                                    thickness = 1.dp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                )
                            }
                            StandingsItem(standing = standing, navController = navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StandingsItem(standing: StandingResponseItem, navController: NavController) {
    println("Processing StandingsItem for team: ${standing.team}")
    val team = standing.team
    if (team == null) {
        Text(
            "Brak informacji o drużynie",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        return
    }
    val wins = standing.win?.total ?: 0
    val losses = standing.loss?.total ?: 0
    val rank = standing.conference?.rank ?: "N/A"
    val winPercentage = calculateWinPercentage(wins, losses)
    val teamName = team.name ?: "Nieznana drużyna"
    val teamLogoUrl = team.logoUrl ?: ""

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                team.id?.let { teamId ->
                    navController.navigate(Screen.TeamDetailsScreen.createRoute(teamId))
                }
            },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = teamLogoUrl,
                contentDescription = "${teamName} logo",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    teamName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "$wins - $losses",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "Win% $winPercentage",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    "Rank: $rank",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

fun calculateWinPercentage(wins: Int, losses: Int): String {
    val totalGames = wins + losses
    return if (totalGames == 0) "0.000" else String.format("%.3f", wins.toDouble() / totalGames)
}

@Preview
@Composable
fun StandingsScreenPreview() {
    Text("Preview placeholder – StandingsScreen()")
}
