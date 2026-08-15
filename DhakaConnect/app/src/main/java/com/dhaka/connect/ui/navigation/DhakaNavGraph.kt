package com.dhaka.connect.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.dhaka.connect.ui.auth.LoginScreen
import com.dhaka.connect.ui.auth.RegisterScreen
import com.dhaka.connect.ui.auth.SplashScreen
import com.dhaka.connect.ui.feed.CreatePostScreen
import com.dhaka.connect.ui.feed.PostCommentsScreen
import com.dhaka.connect.ui.groups.GroupDetailScreen
import com.dhaka.connect.ui.home.HomeScaffold
import com.dhaka.connect.ui.messages.ChatScreen
import com.dhaka.connect.ui.messages.MessagesScreen
import com.dhaka.connect.ui.notifications.NotificationsScreen
import com.dhaka.connect.ui.profile.EditProfileScreen
import com.dhaka.connect.ui.profile.ProfileScreen
import com.dhaka.connect.ui.search.SearchScreen

@Composable
fun DhakaNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.SPLASH) {

        composable(Routes.SPLASH) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN) { popUpTo(Routes.SPLASH) { inclusive = true } }
                },
                onNavigateToHome = {
                    navController.navigate(Routes.HOME) { popUpTo(Routes.SPLASH) { inclusive = true } }
                }
            )
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) { popUpTo(Routes.LOGIN) { inclusive = true } }
                },
                onNavigateToRegister = { navController.navigate(Routes.REGISTER) }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.HOME) { popUpTo(Routes.LOGIN) { inclusive = true } }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Routes.HOME) {
            HomeScaffold(rootNavController = navController)
        }

        composable(
            route = Routes.CREATE_POST,
            arguments = listOf(navArgument("groupId") { type = NavType.StringType; nullable = true; defaultValue = null })
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId")
            CreatePostScreen(
                groupId = groupId,
                onPostCreated = { navController.popBackStack() },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.POST_COMMENTS,
            arguments = listOf(navArgument("postId") {})
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId").orEmpty()
            PostCommentsScreen(
                postId = postId,
                onNavigateBack = { navController.popBackStack() },
                onOpenProfile = { userId -> navController.navigate(Routes.profile(userId)) }
            )
        }

        composable(
            route = Routes.PROFILE,
            arguments = listOf(navArgument("userId") {})
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId").orEmpty()
            ProfileScreen(
                userId = userId,
                onNavigateBack = { navController.popBackStack() },
                onOpenComments = { postId -> navController.navigate(Routes.postComments(postId)) },
                onOpenChat = { otherUserId -> navController.navigate(Routes.chat(otherUserId)) },
                onEditProfile = { navController.navigate(Routes.EDIT_PROFILE) },
                onLogout = {
                    navController.navigate(Routes.LOGIN) { popUpTo(0) { inclusive = true } }
                }
            )
        }

        composable(Routes.EDIT_PROFILE) {
            EditProfileScreen(
                onSaved = { navController.popBackStack() },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Routes.NOTIFICATIONS) {
            NotificationsScreen(
                onNavigateBack = { navController.popBackStack() },
                onOpenPost = { postId -> navController.navigate(Routes.postComments(postId)) },
                onOpenProfile = { userId -> navController.navigate(Routes.profile(userId)) }
            )
        }

        composable(Routes.SEARCH) {
            SearchScreen(
                onNavigateBack = { navController.popBackStack() },
                onOpenProfile = { userId -> navController.navigate(Routes.profile(userId)) }
            )
        }

        composable(Routes.MESSAGES) {
            MessagesScreen(
                onNavigateBack = { navController.popBackStack() },
                onOpenChat = { userId -> navController.navigate(Routes.chat(userId)) }
            )
        }

        composable(
            route = Routes.CHAT,
            arguments = listOf(navArgument("userId") {})
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId").orEmpty()
            ChatScreen(
                otherUserId = userId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.GROUP_DETAIL,
            arguments = listOf(navArgument("groupId") {})
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId").orEmpty()
            GroupDetailScreen(
                groupId = groupId,
                onNavigateBack = { navController.popBackStack() },
                onOpenComments = { postId -> navController.navigate(Routes.postComments(postId)) },
                onOpenProfile = { userId -> navController.navigate(Routes.profile(userId)) },
                onCreatePost = { navController.navigate(Routes.createPost(groupId)) }
            )
        }
    }
}
