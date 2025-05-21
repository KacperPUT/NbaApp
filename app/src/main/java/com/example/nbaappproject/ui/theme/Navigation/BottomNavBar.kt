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

data class BottomNavItem(
    val route: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem("home", Icons.Filled.Home),
    BottomNavItem("players", Icons.Filled.Person),
    BottomNavItem("teams", Icons.Filled.List),
    BottomNavItem("standings", Icons.Filled.Star)
)

@Composable
fun BottomNavBar(navController: NavController, modifier: Modifier = Modifier) {
    NavigationBar(modifier = modifier) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = { Icon(imageVector = item.icon, contentDescription = item.route) },
                label = { Text(item.route) }
            )
        }
    }
}

@Preview
@Composable
fun BottomNavBarPreview() {
    BottomNavBar(navController = NavController(LocalContext.current))
}
