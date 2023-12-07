package com.example.dailytodo

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.dailytodo.navigation.DailyTodoNavHost

/**
 * Top level application that represents the screen for the app
 **/
@Composable
fun DailyToDoApp(navController: NavHostController = rememberNavController()) {
    DailyTodoNavHost(navController = navController)
}