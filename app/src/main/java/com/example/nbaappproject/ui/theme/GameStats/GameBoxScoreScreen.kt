package com.example.nbaappproject.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.nbaappproject.data.response.PlayerStatsItem
import com.example.nbaappproject.data.viewmodel.GameViewModel
import com.example.nbaappproject.data.viewmodel.Result
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.ui.draw.clip
import com.example.nbaappproject.data.response.GameDetailsItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameBoxScoreScreen(
    gameId: Int, // Otrzymujemy gameId jako argument
    navController: NavController,
    viewModel: GameViewModel = viewModel(),
    gameDetails: GameDetailsItem? // gameDetails może być null, jeśli przechodzimy bezpośrednio
) {
    val playerStatsResult by viewModel.playerStats.collectAsState(initial = Result.Loading)

    // Pobierz statystyki graczy po wejściu na ekran
    androidx.compose.runtime.LaunchedEffect(gameId) {
        viewModel.fetchPlayerStats(gameId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Box Score", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Filled.ArrowBack,
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
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background) // Upewnij się, że tło jest ustawione
        ) {
            Text(
                text = "Statystyki Graczy dla meczu ID: $gameId",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground, // Kolor tekstu
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            when (playerStatsResult) {
                is Result.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) // Kolor wskaźnika ładowania
                    }
                }
                is Result.Success -> {
                    val playerStats = (playerStatsResult as Result.Success<List<PlayerStatsItem>>).data
                    if (playerStats.isNotEmpty()) {
                        PlayerStatsList(playerStats = playerStats)
                    } else {
                        Text(
                            "Brak dostępnych statystyk graczy dla tego meczu.",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge, // Styl tekstu
                            color = MaterialTheme.colorScheme.onBackground // Kolor tekstu
                        )
                    }
                }
                is Result.Error -> {
                    Text(
                        "Wystąpił błąd podczas ładowania statystyk graczy: ${(playerStatsResult as Result.Error).exception.message}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge, // Styl tekstu
                        color = MaterialTheme.colorScheme.error // Kolor błędu
                    )
                }
            }
        }
    }
}

@Composable
fun PlayerStatsList(playerStats: List<PlayerStatsItem>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface) // Tło dla listy statystyk
            .padding(horizontal = 8.dp, vertical = 4.dp) // Padding dla LazyColumn
            .clip(MaterialTheme.shapes.medium) // Zaokrąglone rogi dla listy
    ) {
        item {
            // Nagłówki tabeli
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant) // Tło dla nagłówków
                    .padding(vertical = 8.dp, horizontal = 8.dp) // Padding dla nagłówków
            ) {
                Text(
                    "Gracz",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant, // Kolor tekstu
                    modifier = Modifier.weight(2f)
                )
                Text(
                    "Pkt",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant, // Kolor tekstu
                    modifier = Modifier.weight(0.5f),
                    textAlign = TextAlign.End
                )
                Text(
                    "Zb",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant, // Kolor tekstu
                    modifier = Modifier.weight(0.5f),
                    textAlign = TextAlign.End
                )
                Text(
                    "As",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant, // Kolor tekstu
                    modifier = Modifier.weight(0.5f),
                    textAlign = TextAlign.End
                )
                // Dodaj inne nagłówki statystyk, które chcesz wyświetlić
            }
            Divider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp) // Kolor dividera
        }
        items(playerStats) { stat ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp, horizontal = 8.dp), // Padding dla wierszy
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "${stat.player.firstname} ${stat.player.lastname}",
                    style = MaterialTheme.typography.bodyMedium, // Styl tekstu
                    color = MaterialTheme.colorScheme.onSurface, // Kolor tekstu
                    modifier = Modifier.weight(2f)
                )
                Text(
                    stat.points?.toString() ?: "-",
                    style = MaterialTheme.typography.bodyMedium, // Styl tekstu
                    color = MaterialTheme.colorScheme.onSurface, // Kolor tekstu
                    modifier = Modifier.weight(0.5f),
                    textAlign = TextAlign.End
                )
                Text(
                    stat.totalRebounds?.toString() ?: "-",
                    style = MaterialTheme.typography.bodyMedium, // Styl tekstu
                    color = MaterialTheme.colorScheme.onSurface, // Kolor tekstu
                    modifier = Modifier.weight(0.5f),
                    textAlign = TextAlign.End
                )
                Text(
                    stat.assists?.toString() ?: "-",
                    style = MaterialTheme.typography.bodyMedium, // Styl tekstu
                    color = MaterialTheme.colorScheme.onSurface, // Kolor tekstu
                    modifier = Modifier.weight(0.5f),
                    textAlign = TextAlign.End
                )
                // Dodaj inne statystyki
            }
            Divider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp) // Kolor dividera
        }
    }
}
