package com.example.nbaappproject.ui.theme.Standings

import androidx.compose.foundation.background
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
import coil.compose.AsyncImage
import com.example.nbaappproject.data.response.StandingResponseItem
import com.example.nbaappproject.viewmodel.StandingsViewModel

@OptIn(ExperimentalMaterial3Api::class) // Dodaj opt-in dla ExperimentalMaterial3Api
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tabele", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary, // Kolor TopAppBar
                    titleContentColor = MaterialTheme.colorScheme.onPrimary // Kolor tytułu
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background // Ustaw kolor tła dla całego Scaffold
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Zastosuj padding z Scaffold
                .background(MaterialTheme.colorScheme.background) // Upewnij się, że tło jest ustawione
        ) {
            TabRow(
                selectedTabIndex = tabs.indexOf(selectedConference),
                containerColor = MaterialTheme.colorScheme.surface, // Kolor tła dla TabRow
                contentColor = MaterialTheme.colorScheme.primary // Kolor wskaźnika i tekstu aktywnej zakładki
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = {
                            Text(
                                title,
                                style = MaterialTheme.typography.labelLarge // Styl tekstu zakładki
                            )
                        },
                        selected = selectedConference == title,
                        onClick = { selectedConference = title },
                        selectedContentColor = MaterialTheme.colorScheme.primary, // Kolor tekstu wybranej zakładki
                        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant // Kolor tekstu niewybranej zakładki
                    )
                }
            }

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) // Kolor wskaźnika ładowania
                    }
                }
                errorMessage != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = errorMessage ?: "Nieznany błąd",
                            color = MaterialTheme.colorScheme.error, // Kolor błędu
                            style = MaterialTheme.typography.bodyLarge // Styl tekstu błędu
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
                            if (index == 6 || index == 10) { // Divider dla strefy playoff/play-in
                                Divider(
                                    color = MaterialTheme.colorScheme.outlineVariant, // Kolor dividera z motywu
                                    thickness = 1.dp, // Zmniejsz grubość dla subtelności
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp) // Zwiększ padding dla lepszego odstępu
                                )
                            }
                            StandingsItem(standing = standing)
                        }
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
    val teamLogoUrl = team.logoUrl ?: "" // Możesz chcieć tu dać jakiś defaultowy obrazek

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium, // Użyj kształtu z motywu
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), // Kolor tła karty
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) // Mniejsza elewacja dla subtelności
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
                    .background(MaterialTheme.colorScheme.primaryContainer), // Kolor tła dla logo
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    teamName,
                    style = MaterialTheme.typography.titleMedium, // Styl nazwy drużyny
                    color = MaterialTheme.colorScheme.onSurface // Kolor tekstu nazwy drużyny
                )
                //Text(team.code, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) // Jeśli chcesz wyświetlić kod drużyny
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "$wins - $losses",
                    style = MaterialTheme.typography.bodyMedium, // Styl rekordu
                    color = MaterialTheme.colorScheme.onSurface // Kolor tekstu rekordu
                )
                Text(
                    "Win% $winPercentage",
                    style = MaterialTheme.typography.bodySmall, // Styl procentu wygranych
                    color = MaterialTheme.colorScheme.onSurfaceVariant // Kolor tekstu procentu wygranych
                )
                Text(
                    "Rank: $rank",
                    style = MaterialTheme.typography.bodySmall, // Styl rankingu
                    color = MaterialTheme.colorScheme.onSurfaceVariant // Kolor tekstu rankingu
                )
            }
        }
    }
}

fun calculateWinPercentage(wins: Int, losses: Int): String {
    val totalGames = wins + losses
    return if (totalGames == 0) "0.000" else String.format("%.3f", wins.toDouble() / totalGames) // Upewnij się, że zawsze są 3 miejsca po przecinku
}

@Preview
@Composable
fun StandingsScreenPreview() {
    Column {
        Text("Preview placeholder – StandingsScreen()")
    }
}
