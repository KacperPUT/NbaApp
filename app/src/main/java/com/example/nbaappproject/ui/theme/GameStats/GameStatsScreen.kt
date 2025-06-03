package com.example.nbaappproject.ui.theme.GameStats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.nbaappproject.data.response.GameDetailsItem
import com.example.nbaappproject.data.viewmodel.GameViewModel
import com.example.nbaappproject.data.viewmodel.Result
import com.example.nbaappproject.ui.theme.GameBoxScoreScreen
import com.example.nbaappproject.ui.theme.GameStats.GameSummaryScreen // Upewnij się, że ten import jest poprawny

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameStatsScreen(
    gameId: Int?,
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = viewModel(),
    navController: NavController
) {
    var selectedTab by remember { mutableStateOf("Summary") }

    LaunchedEffect(gameId) {
        gameId?.let { viewModel.fetchGameDetails(it) }
    }

    val gameDetailsResult by viewModel.gameDetails.collectAsState(initial = Result.Loading)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Szczegóły Meczu", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back to Home",
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
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background) // Upewnij się, że tło jest ustawione
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface) // Kolor tła dla paska zakładek
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TextButton(
                    onClick = { selectedTab = "Summary" },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = if (selectedTab == "Summary") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Text(
                        text = "Podsumowanie", // Zmieniono na polski
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                TextButton(
                    onClick = { selectedTab = "Box Score" },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = if (selectedTab == "Box Score") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Text(
                        text = "Box Score",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            when (gameDetailsResult) {
                is Result.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) // Kolor wskaźnika ładowania
                    }
                }
                is Result.Success -> {
                    val gameDetails = (gameDetailsResult as Result.Success<GameDetailsItem?>).data
                    when (selectedTab) {
                        "Summary" -> GameSummaryScreen(gameDetails = gameDetails, navController = navController) // Przekazujemy navController
                        "Box Score" -> gameId?.let { id ->
                            GameBoxScoreScreen(
                                gameId = id,
                                navController = navController,
                                gameDetails = gameDetails
                            )
                        }
                    }
                }
                is Result.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Wystąpił błąd podczas ładowania szczegółów meczu.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error // Kolor błędu
                        )
                    }
                }
            }
        }
    }
}
