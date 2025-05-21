package com.example.nbaappproject.ui.theme.GameStats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameBoxScoreScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    val playerStats = remember {
        listOf(
            PlayerStat("T. Brown Jr.", "1:25", 11, 4, 0, 1, 0, 0, 45.0, 33.3, 80.0),
            PlayerStat("A. Davis", "3:36", 18, 21, 3, 2, 1, 2, 50.0, 0.0, 75.0),
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Game stats", color = MaterialTheme.colorScheme.onPrimary)
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            MatchHeader()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text("Summary", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("Box Score", color = MaterialTheme.colorScheme.onSurface)
                Text("Highlights", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            StatsHeaderRow()

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(playerStats) { player ->
                    PlayerStatRow(player)
                    Divider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 1.dp)
                }
            }

            TeamSwitchButton(
                team1 = "Lakers",
                team2 = "Dallas",
                selected = true,
                onSwitch = { /* toggle team */ }
            )
        }
    }
}

@Composable
fun MatchHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("LAL", color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.bodyLarge)
            Text("7–27", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("110", color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.headlineSmall)
            Text("FINAL", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelMedium)
            Text("111", color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.headlineSmall)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("DAL", color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.bodyLarge)
            Text("18–16", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun StatsHeaderRow() {
    Row(modifier = Modifier.fillMaxWidth()) {
        val headers = listOf("Players", "Min", "Pts", "Reb", "Ast", "Stl", "Blk", "Tov", "FG%", "3P%", "FT%")
        headers.forEachIndexed { i, title ->
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.weight(if (i == 0) 2f else 1f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PlayerStatRow(player: PlayerStat) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(player.name, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.weight(2f))
        listOf(
            player.min,
            player.pts.toString(),
            player.reb.toString(),
            player.ast.toString(),
            player.stl.toString(),
            player.blk.toString(),
            player.tov.toString(),
            "${player.fgPct}%",
            "${player.threePct}%",
            "${player.ftPct}%"
        ).forEach {
            Text(it, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun TeamSwitchButton(
    team1: String,
    team2: String,
    selected: Boolean,
    onSwitch: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(50))
    ) {
        Button(
            onClick = onSwitch,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = RoundedCornerShape(50),
            modifier = Modifier.weight(1f)
        ) {
            Text(team1)
        }

        Button(
            onClick = onSwitch,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (!selected) MaterialTheme.colorScheme.primary else Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = RoundedCornerShape(50),
            modifier = Modifier.weight(1f)
        ) {
            Text(team2)
        }
    }
}

@Preview
@Composable
fun GameBoxScoreScreenPreview() {
    GameBoxScoreScreen()
}
