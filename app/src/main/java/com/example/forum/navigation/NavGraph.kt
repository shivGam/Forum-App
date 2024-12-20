package com.example.forum.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.forum.screens.BottomNav
import com.example.forum.screens.Home
import com.example.forum.screens.Login
import com.example.forum.screens.Notification
import com.example.forum.screens.OthersProfile
import com.example.forum.screens.PostForum
import com.example.forum.screens.Profile
import com.example.forum.screens.Register
import com.example.forum.screens.Search
import com.example.forum.screens.Splash

@Composable
fun NavGraph(navController: NavHostController){
    NavHost(navController = navController, startDestination = Routes.Splash.routes){
        composable(Routes.Splash.routes){
            Splash(navController)
        }
        composable(Routes.Home.routes){
            Home(navController)
        }
        composable(Routes.Notification.routes){
            Notification()
        }
        composable(Routes.PostForum.routes){
            PostForum(navController)
        }
        composable(Routes.BottomNav.routes){
            BottomNav(navController)
        }
        composable(Routes.Search.routes){
            Search(navController)
        }
        composable(Routes.Profile.routes){
            Profile(navController)
        }
        composable(Routes.OthersProfile.routes){
            val data = it.arguments!!.getString("data")
            OthersProfile(navController, data!!)
        }
        composable(Routes.Login.routes){
            Login(navController)
        }
        composable(Routes.Register.routes){
            Register(navController)
        }
    }
}