package com.example.nbaappproject

import androidx.compose.foundation.layout.padding
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.example.nbaappproject.ui.theme.GameStats.GameStatsScreen
import com.example.nbaappproject.ui.theme.Home.HomeScreen
import com.example.nbaappproject.ui.theme.Teams.TeamsScreen
import com.example.nbaappproject.ui.theme.Standings.StandingsScreen
import com.example.nbaappproject.ui.theme.Navigation.BottomNavBar
import com.example.nbaappproject.ui.theme.Teams.TeamDetailsScreen
import com.example.nbaappproject.ui.theme.GameBoxScoreScreen
import com.example.nbaappproject.ui.theme.Players.PlayerCardScreen // Import PlayerCardScreen
import com.example.nbaappproject.ui.theme.NbaAppProjectTheme // WAŻNE: Importuj swój motyw

@Composable
fun MainApp() {
    NbaAppProjectTheme { // Otocz całą aplikację swoim motywem
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
                composable("teams") { TeamsScreen(navController = navController) }
                composable("standings") { StandingsScreen() }

                composable("gameStats/{gameId}") { backStackEntry ->
                    val gameId = backStackEntry.arguments?.getString("gameId")?.toIntOrNull()
                    GameStatsScreen(gameId = gameId, navController = navController)
                }

                composable("teamDetails/{teamId}") { backStackEntry ->
                    val teamId = backStackEntry.arguments?.getString("teamId")?.toIntOrNull()
                    teamId?.let {
                        TeamDetailsScreen(teamId = it, navController = navController)
                    }
                }

                composable(
                    "gameBoxScore/{gameId}",
                    arguments = listOf(navArgument("gameId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val gameId = backStackEntry.arguments?.getInt("gameId")
                    if (gameId != null) {
                        GameBoxScoreScreen(gameId = gameId, navController = navController, gameDetails = null)
                    } else {
                        Text("Błąd: Nie znaleziono ID meczu")
                    }
                }

                composable(
                    "playerCard/{playerId}",
                    arguments = listOf(navArgument("playerId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val playerId = backStackEntry.arguments?.getInt("playerId")
                    if (playerId != null) {
                        PlayerCardScreen(playerId = playerId, navController = navController)
                    } else {
                        Text("Błąd: Nie znaleziono ID zawodnika")
                    }
                }
            }
        }
    }
}
