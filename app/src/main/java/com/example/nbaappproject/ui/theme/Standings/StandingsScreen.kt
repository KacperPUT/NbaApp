package com.example.nbaappproject.ui.theme.Standings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.nbaappproject.data.response.Standing
import com.example.nbaappproject.viewmodel.StandingsViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

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
                    .filter { it.conference.name.equals(selectedConference, ignoreCase = true) }
                    .sortedBy { it.conference.rank }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredStandings.size) { index ->
                        val standing = filteredStandings[index]

                        if (index == 6 || index == 10) {
                            androidx.compose.material3.Divider(
                                color = Color.Gray,
                                thickness = 2.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            )
                        }

                        StandingsItem(standing)
                    }
                }
            }
        }
    }
}

@Composable
fun StandingsItem(standing: Standing) {
    val team = standing.team
    val wins = standing.wins.total
    val losses = standing.losses.total
    val rank = standing.conference.rank
    val winPercentage = calculateWinPercentage(wins, losses)

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
                model = team.logoUrl,
                contentDescription = "${team.name} logo",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(team.name, style = MaterialTheme.typography.titleMedium)
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
    // Przykład tymczasowego pustego ekranu
    Column {
        Text("Preview placeholder – StandingsScreen()")
    }
}