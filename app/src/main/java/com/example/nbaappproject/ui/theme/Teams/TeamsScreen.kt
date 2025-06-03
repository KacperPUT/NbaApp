package com.example.nbaappproject.ui.theme.Teams

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.nbaappproject.viewmodel.TeamsViewModel
import com.example.nbaappproject.data.model.Team
import com.example.nbaappproject.ui.theme.Navigation.Screen // Dodaj ten import dla nawigacji

@OptIn(ExperimentalMaterial3Api::class) // Dodaj opt-in dla ExperimentalMaterial3Api
@Composable
fun TeamsScreen(
    navController: NavController,
    viewModel: TeamsViewModel = viewModel()
) {
    val teams by viewModel.teams.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Użyj Scaffold dla spójnego wyglądu ekranu
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Drużyny NBA", style = MaterialTheme.typography.titleLarge) }
            )
        },
        containerColor = MaterialTheme.colorScheme.background // Ustaw kolor tła dla całego Scaffold
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Zastosuj padding z Scaffold
                .background(MaterialTheme.colorScheme.background), // Upewnij się, że tło jest ustawione
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
                errorMessage != null -> {
                    Text(errorMessage ?: "Nieznany błąd", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyLarge)
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(teams) { team ->
                            TeamItem(team) {
                                // Nawigacja do TeamDetailsScreen
                                navController.navigate(Screen.TeamDetailsScreen.createRoute(team.id))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TeamItem(team: Team, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth() // Użyj fillMaxWidth w Column, aby Card mógł się rozciągnąć
            .clickable { onClick() }
            .padding(8.dp), // Dodaj padding wokół każdego elementu
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .size(96.dp), // Zwiększ rozmiar karty dla lepszej widoczności
            shape = MaterialTheme.shapes.medium, // Użyj kształtu z motywu
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface) // Użyj koloru powierzchni z motywu
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primaryContainer), // Kolor tła dla logo
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = team.logoUrl,
                    contentDescription = "${team.name} logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(64.dp) // Dostosuj rozmiar logo wewnątrz karty
                        .clip(CircleShape) // Zachowaj okrągły kształt logo
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = team.nickname,
            style = MaterialTheme.typography.bodyMedium, // Użyj stylu z motywu
            color = MaterialTheme.colorScheme.onSurface // Użyj koloru tekstu z motywu
        )
    }
}
