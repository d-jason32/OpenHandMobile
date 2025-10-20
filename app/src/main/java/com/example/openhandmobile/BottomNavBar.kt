package com.example.openhandmobile

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun BottomNavBar(navController: NavHostController) {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Home", "Classes", "Roadmap", "Test", "Settings", "Profile")
    val icons = listOf(Icons.Outlined.Home, Icons.Outlined.Book, Icons.Outlined.Route, Icons.Outlined.Science, Icons.Outlined.Settings, Icons.Outlined.Person)

    val routes = listOf("Home", "Classes", "Roadmap", "CameraTest", "Settings", "Profile")

    NavigationBar(
        containerColor = Color.Black,
        contentColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = {

                        selectedItem = index
                        val route = routes[index]

                        if (navController.currentBackStackEntry?.destination?.route != route) {
                            navController.navigate(route) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                            }
                        }

                          },
                icon = {
                    Icon(
                        imageVector = icons[index],
                        contentDescription = item,
                        modifier = Modifier.size(30.dp)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF00A6FF),
                    unselectedIconColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}