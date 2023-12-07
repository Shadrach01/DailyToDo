package com.example.dailytodo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.dailytodo.ui.screens.EditToDoDestination
import com.example.dailytodo.ui.screens.EditToDoScreen
import com.example.dailytodo.ui.screens.NewToDoScreen
import com.example.dailytodo.ui.screens.NewTodoScreenDestination
import com.example.dailytodo.ui.screens.ToDoDetailsScreen
import com.example.dailytodo.ui.screens.ToDoDetailsScreenDestination
import com.example.dailytodo.ui.screens.ToDoScreen
import com.example.dailytodo.ui.screens.ToDoScreensDestination
import com.example.dailytodo.ui.screens.WelcomeScreen
import com.example.dailytodo.ui.screens.WelcomeScreenDestination

/**
 * Navigation graph for the whole app
 * */

@Composable
fun DailyTodoNavHost(
    modifier: Modifier = Modifier, navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = WelcomeScreenDestination.route,
        modifier = modifier
    ) {
        composable(route = WelcomeScreenDestination.route) {
            WelcomeScreen(navigateToToDoScreens = {
                navController.navigate(ToDoScreensDestination.route)
            }
            )
        }
        composable(ToDoScreensDestination.route) {
            ToDoScreen(
                navigateToAddNewToDo = { navController.navigate(NewTodoScreenDestination.route) },
                navigateToDoDoDetailsScreen = {
                    navController.navigate(
                        "${ToDoDetailsScreenDestination.route}/${it}"
                    )
                },
                navigateUp = { navController.navigateUp()}
            )
        }
        composable(NewTodoScreenDestination.route) {
            NewToDoScreen(
                navigateBack = {
                    navController.popBackStack(
                        ToDoScreensDestination.route, inclusive = false
                    )
                },
                navigateUp = { navController.navigateUp() },
            )
        }
        composable(
            route = ToDoDetailsScreenDestination.routeWithArgs,
            arguments = listOf(navArgument(ToDoDetailsScreenDestination.toDoIdArg) {
                    type = NavType.IntType
                })
        ) {
            ToDoDetailsScreen(
                navigateBack = { navController.navigateUp() },
                navigateToEditTodo = {
                    navController.navigate(
                        "${EditToDoDestination.route}/${it}"
                    )
                }
            )
        }
        composable(
            route = EditToDoDestination.routeWithArgs,
            arguments = listOf(navArgument(EditToDoDestination.toDoIdArg){
                type = NavType.IntType
            })
        ) {
            EditToDoScreen(
                navigateUp = { navController.navigateUp() },
                navigateBack = {navController.popBackStack(
                    ToDoScreensDestination.route, inclusive = false
                )}
            )

        }
    }
}