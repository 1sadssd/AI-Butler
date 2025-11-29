package com.smartwardrobe.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import com.smartwardrobe.app.data.WardrobeRepository
import com.smartwardrobe.app.ui.screens.WardrobeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WardrobeRepository.loadAll(this)
        setContent {
            SmartWardrobeApp()
        }
    }
}

@Composable
fun SmartWardrobeApp() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentRoute == "home",
                    onClick = { navController.navigate("home") },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Главная") }
                )
                NavigationBarItem(
                    selected = currentRoute == "wardrobe",
                    onClick = { navController.navigate("wardrobe") },
                    icon = { Icon(Icons.Default.List, contentDescription = null) },
                    label = { Text("Гардероб") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "wardrobe",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("wardrobe") { WardrobeScreen() }
            composable("home") { HomeScreen() }
        }
    }
}

@Composable
fun HomeScreen() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Главный экран", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Здесь будет отображаться образ от ИИ и погода")
    }
}
