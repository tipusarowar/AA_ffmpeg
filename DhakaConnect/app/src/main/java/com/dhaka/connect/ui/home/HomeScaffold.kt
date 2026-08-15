package com.dhaka.connect.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dhaka.connect.ui.feed.FeedScreen
import com.dhaka.connect.ui.friends.FriendsScreen
import com.dhaka.connect.ui.groups.GroupsScreen
import com.dhaka.connect.ui.navigation.HomeTabRoutes
import com.dhaka.connect.ui.navigation.Routes

private data class BottomTab(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

private val bottomTabs = listOf(
    BottomTab(HomeTabRoutes.FEED, "Home", Icons.Filled.Home),
    BottomTab(HomeTabRoutes.FRIENDS, "Friends", Icons.Filled.People),
    BottomTab(HomeTabRoutes.GROUPS, "Groups", Icons.Filled.Groups),
    BottomTab(HomeTabRoutes.MENU, "Menu", Icons.Filled.Menu)
)

@Composable
fun HomeScaffold(rootNavController: NavHostController) {
    val tabNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            val backStackEntry by tabNavController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination?.route
            NavigationBar {
                bottomTabs.forEach { tab ->
                    NavigationBarItem(
                        selected = currentRoute == tab.route,
                        onClick = {
                            tabNavController.navigate(tab.route) {
                                popUpTo(tabNavController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = tabNavController,
            startDestination = HomeTabRoutes.FEED,
            modifier = androidx.compose.ui.Modifier.padding(innerPadding)
        ) {
            composable(HomeTabRoutes.FEED) {
                FeedScreen(
                    onOpenComments = { postId -> rootNavController.navigate(Routes.postComments(postId)) },
                    onOpenProfile = { userId -> rootNavController.navigate(Routes.profile(userId)) },
                    onCreatePost = { rootNavController.navigate(Routes.createPost()) },
                    onOpenSearch = { rootNavController.navigate(Routes.SEARCH) },
                    onOpenNotifications = { rootNavController.navigate(Routes.NOTIFICATIONS) },
                    onOpenMessages = { rootNavController.navigate(Routes.MESSAGES) }
                )
            }
            composable(HomeTabRoutes.FRIENDS) {
                FriendsScreen(onOpenProfile = { userId -> rootNavController.navigate(Routes.profile(userId)) })
            }
            composable(HomeTabRoutes.GROUPS) {
                GroupsScreen(onOpenGroup = { groupId -> rootNavController.navigate(Routes.groupDetail(groupId)) })
            }
            composable(HomeTabRoutes.MENU) {
                MenuScreen(
                    onOpenProfile = { userId -> rootNavController.navigate(Routes.profile(userId)) },
                    onOpenNotifications = { rootNavController.navigate(Routes.NOTIFICATIONS) },
                    onOpenMessages = { rootNavController.navigate(Routes.MESSAGES) },
                    onLoggedOut = {
                        rootNavController.navigate(Routes.LOGIN) { popUpTo(0) { inclusive = true } }
                    }
                )
            }
        }
    }
}
