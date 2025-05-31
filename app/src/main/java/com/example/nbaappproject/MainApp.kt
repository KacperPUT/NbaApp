package com.example.nbaappproject

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nbaappproject.ui.theme.GameStats.GameStatsScreen
import com.example.nbaappproject.ui.theme.Home.HomeScreen
import com.example.nbaappproject.ui.theme.Players.PlayersScreen
import com.example.nbaappproject.ui.theme.Teams.TeamsScreen
import com.example.nbaappproject.ui.theme.Standings.StandingsScreen

import com.example.nbaappproject.ui.theme.Navigation.BottomNavBar
import com.example.nbaappproject.ui.theme.Teams.TeamDetailsScreen
import com.example.nbaappproject.ui.theme.Teams.TeamStats

@Composable
fun MainApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeScreen(navController = navController) }
            composable("players") { PlayersScreen(navController = navController) }
            composable("teams") { TeamsScreen(navController = navController) }
            composable("standings") { StandingsScreen() }

            composable("gameStats/{gameId}") { backStackEntry ->
                val gameId = backStackEntry.arguments?.getString("gameId")?.toIntOrNull()
                GameStatsScreen(gameId = gameId)
            }

            composable("teamDetails/{teamId}") { backStackEntry ->
                val teamId = backStackEntry.arguments?.getString("teamId")?.toIntOrNull()
                teamId?.let {
                    TeamDetailsScreen(teamId = it, navController = navController)
                }
            }
        }
    }
}

