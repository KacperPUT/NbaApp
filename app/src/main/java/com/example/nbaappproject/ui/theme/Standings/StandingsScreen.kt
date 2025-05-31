package com.example.nbaappproject.ui.theme.Standings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import coil.compose.AsyncImage
import com.example.nbaappproject.data.response.StandingResponseItem
import com.example.nbaappproject.viewmodel.StandingsViewModel

@Composable
fun StandingsScreen(
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TabRow(selectedTabIndex = tabs.indexOf(selectedConference)) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = selectedConference == title,
                    onClick = { selectedConference = title }
                )
            }
        }

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            errorMessage != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = errorMessage ?: "Nieznany błąd",
                        color = Color.Red
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
                        println("Processing standing for: ${standing.team?.name}") // Zabezpieczony log
                        if (index == 6 || index == 10) {
                            androidx.compose.material3.Divider(
                                color = Color.Gray,
                                thickness = 2.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            )
                        }
                        StandingsItem(standing = standing)
                    }
                }
            }
        }
    }
}

@Composable
fun StandingsItem(standing: StandingResponseItem) {
    println("Processing StandingsItem for team: ${standing.team}") // Dodany log obiektu team
    val team = standing.team
    if (team == null) {
        Text("Brak informacji o drużynie")
        return
    }
    val wins = standing.win?.total ?: 0
    val losses = standing.loss?.total ?: 0
    val rank = standing.conference?.rank ?: "N/A"
    val winPercentage = calculateWinPercentage(wins, losses)
    val teamName = team.name ?: "Nieznana drużyna"
    val teamLogoUrl = team.logoUrl ?: "" // Możesz chcieć tu dać jakiś defaultowy obrazek

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                    .background(Color.LightGray),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(teamName, style = MaterialTheme.typography.titleMedium)
                //Text(team.code, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text("$wins - $losses", style = MaterialTheme.typography.bodyMedium)
                Text("Win% $winPercentage", style = MaterialTheme.typography.bodySmall)
                Text("Rank: $rank", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

fun calculateWinPercentage(wins: Int, losses: Int): String {
    val totalGames = wins + losses
    return if (totalGames == 0) "0.0" else String.format("%.3f", wins.toDouble() / totalGames)
}

@Preview
@Composable
fun StandingsScreenPreview() {
    Column {
        Text("Preview placeholder – StandingsScreen()")
    }
}