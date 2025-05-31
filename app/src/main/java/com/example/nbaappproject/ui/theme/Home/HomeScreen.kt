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
import com.example.nbaappproject.data.model.Game // Upewnij się, że ten import jest
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HeaderText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.headlineMedium,
        modifier = modifier.padding(10.dp)
    )
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = viewModel(),
    navController: NavController
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val formattedDateApi = selectedDate.format(DateTimeFormatter.ISO_DATE)
    val formattedDateUi = selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))

    val gamesResult by viewModel.games.collectAsState(initial = Result.Loading) // Inicjalizujemy stan ładowania
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            HeaderText(text = "Games")
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(horizontal = 4.dp, vertical = 8.dp)
        ) {
            IconButton(onClick = { selectedDate = selectedDate.minusDays(1) }) {
                Icon(Icons.Filled.ArrowBack, "Previous day", tint = MaterialTheme.colorScheme.onPrimaryContainer)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(formattedDateUi, color = MaterialTheme.colorScheme.onPrimaryContainer, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(end = 8.dp))
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
                CircularProgressIndicator()
            }
        } else {
            when (gamesResult) {
                is Result.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is Result.Success -> {
                    val games = (gamesResult as Result.Success<List<Game>>).data
                    LazyColumn {
                        items(games) { game ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp, horizontal = 8.dp)
                                    .clickable {
                                        navController.navigate("gameStats/${game.id}")
                                    },
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "${game.homeTeam.name} vs ${game.awayTeam.name}",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "${game.homeScore} : ${game.awayScore}",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                        }
                    }
                }
                is Result.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Wystąpił błąd podczas ładowania gier.")
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