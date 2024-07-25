package com.example.ask_me.navigation

import ImageChatScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ask_me.Screens.HomeScreen
import com.example.ask_me.Screens.SplashScreen
import com.example.ask_me.Screens.chat.ChatScreen
import com.example.ask_me.Screens.chat.ChatViewModel

import com.example.ask_me.Screens.imageInput.ImageInputScreen

@Composable
fun Navigation() {

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.SplashScreen.name
    ) {
        composable(Screens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }
        composable(Screens.HomeScreen.name) {
            HomeScreen(navController = navController)
        }
        composable(Screens.ChatScreen.name) {
            ChatScreen(navController = navController, chatViewModel = ChatViewModel())
        }

        composable(Screens.ImageInputScreen.name) {
            ImageInputScreen(
                navController = navController
            )
        }
        composable(Screens.ImageChatScreen.name){
            ImageChatScreen(navController = navController)
        }


    }

}