package com.vant.tracker.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vant.tracker.ui.ViewModelFactory
import com.vant.tracker.ui.addedit.AddEditScreen
import com.vant.tracker.ui.addedit.AddEditViewModel
import com.vant.tracker.ui.detail.DetailScreen
import com.vant.tracker.ui.detail.DetailViewModel
import com.vant.tracker.ui.home.HomeScreen
import com.vant.tracker.ui.home.HomeViewModel
import com.vant.tracker.ui.library.LibraryScreen
import com.vant.tracker.ui.library.LibraryViewModel
import com.vant.tracker.ui.settings.SettingsScreen
import com.vant.tracker.ui.settings.SettingsViewModel
import com.vant.tracker.ui.theme.AppTheme

private data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
)

private val bottomNavItems = listOf(
    BottomNavItem(Screen.Home.route, "Home", Icons.Default.Home),
    BottomNavItem(Screen.Library.route, "Library", Icons.Default.VideoLibrary),
    BottomNavItem(Screen.Add.route, "Add", Icons.Default.Add),
    BottomNavItem(Screen.Settings.route, "Settings", Icons.Default.Settings),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VantNavGraph(currentTheme: AppTheme, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val factory = remember(context) { ViewModelFactory(context) }
    val navBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStack?.destination?.route

    val showBottomBar = currentRoute in bottomNavItems.map { it.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                        )
                    }
                }
            }
        },
        modifier = modifier,
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Screen.Home.route) {
                val vm: HomeViewModel = viewModel(factory = factory)
                HomeScreen(
                    viewModel = vm,
                    onItemClick = { id -> navController.navigate(Screen.Detail.createRoute(id)) },
                    modifier = Modifier.fillMaxSize(),
                )
            }

            composable(Screen.Library.route) {
                val vm: LibraryViewModel = viewModel(factory = factory)
                LibraryScreen(
                    viewModel = vm,
                    onItemClick = { id -> navController.navigate(Screen.Detail.createRoute(id)) },
                    modifier = Modifier.fillMaxSize(),
                )
            }

            composable(Screen.Add.route) {
                val vm: AddEditViewModel = viewModel(factory = factory)
                AddEditScreen(
                    viewModel = vm,
                    itemId = null,
                    onNavigateBack = { navController.popBackStack() },
                    modifier = Modifier.fillMaxSize(),
                )
            }

            composable(
                Screen.Detail.route,
                arguments = listOf(navArgument("itemId") { type = NavType.LongType }),
            ) { backStack ->
                val id = backStack.arguments!!.getLong("itemId")
                val vm: DetailViewModel = viewModel(factory = factory)
                DetailScreen(
                    viewModel = vm,
                    itemId = id,
                    onNavigateBack = { navController.popBackStack() },
                    onEditClick = { itemId -> navController.navigate(Screen.Edit.createRoute(itemId)) },
                    modifier = Modifier.fillMaxSize(),
                )
            }

            composable(
                Screen.Edit.route,
                arguments = listOf(navArgument("itemId") { type = NavType.LongType }),
            ) { backStack ->
                val id = backStack.arguments!!.getLong("itemId")
                val vm: AddEditViewModel = viewModel(factory = factory)
                AddEditScreen(
                    viewModel = vm,
                    itemId = id,
                    onNavigateBack = { navController.popBackStack() },
                    modifier = Modifier.fillMaxSize(),
                )
            }

            composable(Screen.Settings.route) {
                val vm: SettingsViewModel = viewModel(factory = factory)
                SettingsScreen(
                    viewModel = vm,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}
