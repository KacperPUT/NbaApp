package com.example.nbaappproject.ui.theme.Home

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.nbaappproject.data.viewmodel.GameViewModel
import com.example.nbaappproject.data.viewmodel.Result
import com.example.nbaappproject.data.model.Game
import com.example.nbaappproject.ui.theme.Navigation.Screen // Import dla Screen
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class) // Dodaj opt-in dla ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = viewModel(),
    navController: NavController
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val formattedDateApi = selectedDate.format(DateTimeFormatter.ISO_DATE)
    val formattedDateUi = selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))

    val gamesResult by viewModel.games.collectAsState(initial = Result.Loading)
    val isLoading by viewModel.isLoading.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(selectedDate) {
        viewModel.fetchGamesByDate(formattedDateApi)
    }

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
            },
            selectedDate.year,
            selectedDate.monthValue - 1,
            selectedDate.dayOfMonth
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mecze NBA", style = MaterialTheme.typography.titleLarge) },
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
            // Usunięto HeaderText, ponieważ TopAppBar pełni tę funkcję

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer) // Kolor tła dla paska daty
                    .padding(horizontal = 4.dp, vertical = 8.dp)
            ) {
                IconButton(onClick = { selectedDate = selectedDate.minusDays(1) }) {
                    Icon(Icons.Filled.ArrowBack, "Previous day", tint = MaterialTheme.colorScheme.onPrimaryContainer)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        formattedDateUi,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    IconButton(onClick = { datePickerDialog.show() }) {
                        Icon(Icons.Default.DateRange, "Select date", tint = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                }

                IconButton(onClick = { selectedDate = selectedDate.plusDays(1) }) {
                    Icon(Icons.Default.ArrowForward, "Next day", tint = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) // Kolor wskaźnika ładowania
                }
            } else {
                when (gamesResult) {
                    is Result.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    }
                    is Result.Success -> {
                        val games = (gamesResult as Result.Success<List<Game>>).data
                        if (games.isEmpty()) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(
                                    "Brak meczów w wybranym dniu.",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp), // Dostosuj padding
                                verticalArrangement = Arrangement.spacedBy(8.dp) // Odstępy między kartami
                            ) {
                                items(games) { game ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                navController.navigate(Screen.GameStatsScreen.createRoute(game.id))
                                            },
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), // Kolor tła karty
                                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), // Elewacja karty
                                        shape = MaterialTheme.shapes.medium // Kształt karty
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Text(
                                                text = "${game.homeTeam.name} vs ${game.awayTeam.name}",
                                                style = MaterialTheme.typography.titleMedium, // Styl nazwy drużyn
                                                color = MaterialTheme.colorScheme.onSurface // Kolor tekstu
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "${game.homeScore} : ${game.awayScore}",
                                                style = MaterialTheme.typography.bodyLarge, // Styl wyniku
                                                color = MaterialTheme.colorScheme.onSurfaceVariant // Kolor tekstu
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    is Result.Error -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                "Wystąpił błąd podczas ładowania gier.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error // Kolor błędu
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = NavController(LocalContext.current))
}
