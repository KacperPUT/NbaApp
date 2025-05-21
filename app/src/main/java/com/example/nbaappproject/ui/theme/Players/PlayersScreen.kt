package com.example.nbaappproject.ui.theme.Players

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nbaappproject.data.model.PlayerUi
import com.example.nbaappproject.data.viewmodel.PlayersViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize

@Composable
fun PlayersScreen(navController: NavController, viewModel: PlayersViewModel = viewModel()) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTeam by remember { mutableStateOf("All") }
    var selectedPosition by remember { mutableStateOf("All") }
    var selectedPlayer by remember { mutableStateOf<PlayerUi?>(null) }

    val players by viewModel.players.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPlayers()
    }

    val teamOptions = remember(players) {
        listOf("All") + players.map { it.teamName }.distinct().sorted()
    }

    val positionOptions = remember(players) {
        listOf("All") + players.mapNotNull { it.position }.distinct().sorted()
    }

    val filteredPlayers = remember(searchQuery, selectedTeam, selectedPosition, players) {
        players.filter { player ->
            val fullName = "${player.firstName} ${player.lastName}".lowercase()
            val queryMatch = searchQuery.lowercase() in fullName
            val teamMatch = selectedTeam == "All" || player.teamName == selectedTeam
            val posMatch = selectedPosition == "All" || player.position == selectedPosition
            queryMatch && teamMatch && posMatch
        }
    }

    if (selectedPlayer != null) {
        PlayerDetailCard(player = selectedPlayer!!) {
            selectedPlayer = null
        }
    } else {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search player") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DropdownFilter(
                        label = "Team",
                        options = teamOptions,
                        selectedOption = selectedTeam,
                        onOptionSelected = { selectedTeam = it },
                        modifier = Modifier.weight(1f)
                    )
                    DropdownFilter(
                        label = "Position",
                        options = positionOptions,
                        selectedOption = selectedPosition,
                        onOptionSelected = { selectedPosition = it },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn {
                    items(filteredPlayers) { player ->
                        Text(
                            text = "${player.firstName} ${player.lastName} — ${player.teamName} (${player.position})",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedPlayer = player }
                                .padding(vertical = 6.dp)
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun PlayerDetailCard(player: PlayerUi, onClose: () -> Unit) {
    AlertDialog(
        onDismissRequest = onClose,
        confirmButton = {
            TextButton(onClick = onClose) {
                Text("Close")
            }
        },
        title = {
            Text("${player.firstName} ${player.lastName}")
        },
        text = {
            Column {
                Text("Team: ${player.teamName}")
                Text("Position: ${player.position ?: "N/A"}")
                Text("Height: ${player.height} m")
                Text("Weight: ${player.weight} kg")
                Text("Birth Date: ${player.birthDate}")
                Text("College: ${player.college ?: "N/A"}")
                Text("Nationality: ${player.country}")
                Text("NBA Since: ${player.startNba}")
            }
        }
    )
}

@Composable
fun DropdownFilter(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {}, // readOnly, zmiana tylko przez dropdown
            label = { Text(label) },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
