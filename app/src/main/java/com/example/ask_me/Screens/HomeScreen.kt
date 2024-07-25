package com.example.ask_me.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ask_me.navigation.Screens

@Composable
fun HomeScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { navController.navigate(Screens.ChatScreen.name) },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Chat")
            }
            Button(
                onClick = { navController.navigate(Screens.ImageInputScreen.name) },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Input As image")
            }
        }
    }
}