package com.example.forum.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.forum.data.models.BottomNavParams
import com.example.forum.navigation.Routes

@Composable
fun BottomNav(navController: NavHostController){
    val navHostController = rememberNavController()

    Scaffold(bottomBar = { FloatingBottomBar(navHostController) }) { innerPadding ->
        NavHost(navController = navHostController, startDestination = Routes.Home.routes, modifier = Modifier.padding(innerPadding)){
            composable(Routes.Splash.routes){
                Splash(navHostController)
            }
            composable(Routes.Home.routes){
                Home(navController)
            }
            composable(Routes.Notification.routes){
                Notification()
            }
            composable(Routes.PostForum.routes){
                PostForum(navHostController)
            }
            composable(Routes.Search.routes){
                Search(navController)
            }
            composable(Routes.Profile.routes){
                Profile(navController)
            }
        }
    }
}

@Composable
fun FloatingBottomBar(navHostController: NavHostController) {

    val backStackEntry = navHostController.currentBackStackEntryAsState()

    val listNavItem = listOf(
        BottomNavParams(
            "Home",
            Routes.Home.routes,
            Icons.Rounded.Home
        ),
        BottomNavParams(
            "Search",
            Routes.Search.routes,
            Icons.Rounded.Search
        ),
        BottomNavParams(
            "Post Forum",
            Routes.PostForum.routes,
            Icons.Rounded.Add
        ),
        BottomNavParams(
            "Notifications",
            Routes.Notification.routes,
            Icons.Rounded.Notifications
        ),BottomNavParams(
            "Profile",
            Routes.Profile.routes,
            Icons.Rounded.Person
        )
    )

    BottomAppBar(
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
    ) {
        listNavItem.forEach {
            val selected = it.route == backStackEntry.value?.destination?.route
            NavigationBarItem(selected = selected, onClick = {
                navHostController.navigate(it.route){
                    popUpTo(navHostController.graph.findStartDestination().id){
                        saveState = true
                    }
                    launchSingleTop = true
                }
            }, icon = {
                Icon(imageVector = it.icon , contentDescription = it.title)
            })
        }
    }

}
