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
import com.example.nbaappproject.ui.theme.Navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamsScreen(
    navController: NavController,
    viewModel: TeamsViewModel = viewModel()
) {
    val teams by viewModel.teams.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Drużyny NBA", style = MaterialTheme.typography.titleLarge) }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
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
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .size(96.dp),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = team.logoUrl,
                    contentDescription = "${team.name} logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = team.nickname,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
