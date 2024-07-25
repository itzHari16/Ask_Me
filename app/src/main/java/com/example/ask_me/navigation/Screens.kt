package com.example.ask_me.navigation

enum class Screens {
    SplashScreen,
    HomeScreen,
    ChatScreen,
    CameraScreen,
    ImageChatScreen,
    ImageInputScreen;

    companion object {
        fun fromRoute(route: String): Screens = when (route?.substringBefore("/")) {
            SplashScreen.name -> SplashScreen
            HomeScreen.name -> HomeScreen
            ChatScreen.name -> ChatScreen
            CameraScreen.name -> CameraScreen
            ImageChatScreen.name->ImageChatScreen
            ImageInputScreen.name->ImageInputScreen
            null -> HomeScreen
            else -> throw IllegalArgumentException("Route $route is not recognized")


        }
    }

}