package com.example.dailytodo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dailytodo.R
import com.example.dailytodo.navigation.NavigationDestination
import com.example.dailytodo.ui.theme.DailyToDoTheme

/***
 *  Composable for the Welcome screen
 */

// Welcome screen destination route
object WelcomeScreenDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    navigateToToDoScreens: () -> Unit
) {
    Scaffold(
        topBar = {
            ToDoAppBar(
                canNavigateBack = false,
                title = stringResource(WelcomeScreenDestination.titleRes),
            )
        },
        modifier = modifier
    )
    { innerPadding ->
        WelcomeScreenBody(
            onStartToDoClicked = navigateToToDoScreens,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun WelcomeScreenBody(
    modifier: Modifier = Modifier,
    onStartToDoClicked: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.medium_padding))
    ) {
        Text(
            text = stringResource(R.string.welcome_text),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(70.dp))

        Button(
            onClick = { onStartToDoClicked() },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = stringResource(R.string.welcome_button_text),
                style = MaterialTheme.typography.bodyMedium
            )
        }

    }
}

@Preview
@Composable
fun WelcomeScreenPreview() {
    DailyToDoTheme {
        WelcomeScreen(navigateToToDoScreens = {})
    }
}