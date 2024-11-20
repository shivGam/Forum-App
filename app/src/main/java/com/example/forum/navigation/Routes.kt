package com.example.forum.navigation

sealed class Routes(val routes: String){
    data object Home : Routes("home")
    data object Notification : Routes("notification")
    data object PostForum : Routes("post_forum")
    data object Profile : Routes("profile")
    data object Search : Routes("search")
    data object Splash : Routes("splash")
    data object BottomNav : Routes("bottom_nav")
    data object Login : Routes("login")
    data object Register : Routes("register")
    data object OthersProfile : Routes("other_profile/{data}")
}