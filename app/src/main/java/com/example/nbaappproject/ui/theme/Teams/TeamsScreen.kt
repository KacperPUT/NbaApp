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

@Composable
fun TeamsScreen(
    navController: NavController,
    viewModel: TeamsViewModel = viewModel()
) {
    val teams by viewModel.teams.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    when {
        isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        errorMessage != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(errorMessage ?: "Nieznany błąd", color = Color.Red)
            }
        }
        else -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(teams) { team ->
                    TeamItem(team) {
                        navController.navigate("teamDetails/${team.id}")
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
            .width(96.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape),
            shape = CircleShape
        ) {
            AsyncImage(
                model = team.logoUrl,
                contentDescription = "${team.name} logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = team.nickname,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}
