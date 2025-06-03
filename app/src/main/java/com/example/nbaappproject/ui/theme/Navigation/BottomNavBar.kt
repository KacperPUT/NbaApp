package com.example.nbaappproject.ui.theme.Navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
// Usunięto import com.example.nbaappproject.data.model.PlayerUi
// Usunięto import com.google.gson.Gson

sealed class Screen(val route: String) {
    object HomeScreen : Screen("home")
    // object PlayersScreen : Screen("players") // Zakomentowane w Twoim MainApp
    object TeamsScreen : Screen("teams")
    object StandingsScreen : Screen("standings")
    object GameStatsScreen : Screen("gameStats/{gameId}") {
        fun createRoute(gameId: Int): String {
            return "gameStats/$gameId"
        }
    }
    object TeamDetailsScreen : Screen("teamDetails/{teamId}") {
        fun createRoute(teamId: Int): String {
            return "teamDetails/$teamId"
        }
    }
    object GameBoxScoreScreen : Screen("gameBoxScore/{gameId}") {
        fun createRoute(gameId: Int): String {
            return "gameBoxScore/$gameId"
        }
    }
    // Zmieniamy PlayerCardScreen, aby przyjmował tylko playerId
    object PlayerCardScreen : Screen("playerCard/{playerId}") {
        fun createRoute(playerId: Int): String {
            return "playerCard/$playerId"
        }
    }
}

data class BottomNavItem(
    val route: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.HomeScreen.route, Icons.Filled.Home),
    BottomNavItem(Screen.TeamsScreen.route, Icons.Filled.List),
    BottomNavItem(Screen.StandingsScreen.route, Icons.Filled.Star)
)

@Composable
fun BottomNavBar(navController: NavController, modifier: Modifier = Modifier) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface, // Kolor tła paska nawigacji
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant // Domyślny kolor ikon/tekstu
    ) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        bottomNavItems.forEach { item ->
            val selected = currentRoute?.startsWith(item.route) == true && item.route == Screen.HomeScreen.route ||
                    currentRoute?.startsWith(item.route) == true && item.route != Screen.HomeScreen.route // Poprawiona logika wyboru

            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (item.route == Screen.HomeScreen.route) {
                        navController.navigate(Screen.HomeScreen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    } else if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.route,
                        tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant // Kolor ikony w zależności od wyboru
                    )
                },
                label = {
                    Text(
                        item.route,
                        style = MaterialTheme.typography.labelSmall, // Styl tekstu etykiety
                        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant // Kolor tekstu etykiety
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer // Kolor wskaźnika wybranej zakładki
                )
            )
        }
    }
}

@Preview
@Composable
fun BottomNavBarPreview() {
    BottomNavBar(navController = NavController(LocalContext.current))
}
