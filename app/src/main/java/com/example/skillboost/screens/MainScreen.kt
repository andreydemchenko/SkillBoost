package com.example.skillboost.screens

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.skillboost.R
import com.example.skillboost.repositories.AuthRepository
import com.example.skillboost.ui.theme.MyTheme
import com.example.skillboost.ui.theme.interFonts
import com.example.skillboost.ui.theme.juryFonts
import com.example.skillboost.viewmodels.CoursesViewModel
import com.example.skillboost.viewmodels.LoginViewModel
import com.example.skillboost.viewmodels.MentorViewModel
import com.example.skillboost.viewmodels.ProfileViewModel

@Composable
fun MainScreen(loginViewModel: LoginViewModel) {

    val navController = rememberNavController()
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.padding(bottom = 55.dp)) {
            NavHost(navController = navController, startDestination = NavBarScreen.Courses.route.name) {
                composable(NavBarScreen.Courses.route.name) { CoursesScreen(viewModel = CoursesViewModel()) }
                composable(NavBarScreen.Mentor.route.name) { MentorScreen(viewModel = MentorViewModel()) }
                composable(NavBarScreen.Profile.route.name) {
                    ProfileScreen(
                        loginViewModel,
                        ProfileViewModel()
                    )
                }
            }
        }
        Column {
            Spacer(modifier = Modifier.weight(1f))
            AppBottomNavigation(
                navController = navController,
                currentNavBarScreen = NavBarScreen.Courses
            )
        }
    }
}

@Composable
fun AppBottomNavigation(navController: NavController, currentNavBarScreen: NavBarScreen) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: currentNavBarScreen.route

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .background(Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(60.dp)
                .background(MaterialTheme.colorScheme.onPrimary)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { screen ->
                if (screen == items[1]) {
                    Button(
                        modifier =
                        if (currentRoute != screen.route.name) {
                            Modifier
                                .offset(y = 10.dp)
                                .height(90.dp)
                                .padding(horizontal = 7.dp)
                                .background(MaterialTheme.colorScheme.primary, CircleShape)
                                .padding(4.dp)
                                .offset(y = (-4).dp)
                        } else {
                            Modifier
                                .width(110.dp)
                                .offset(y = 20.dp)
                                .height(110.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(2.dp)
                                .border(6.dp, MaterialTheme.colorScheme.onPrimary, CircleShape)
                                .offset(y = (-4).dp)
                        },
                        onClick = {
                            if (currentRoute != screen.route.name) {
                                navController.navigate(screen.route.name)
                            }
                        }
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val (width, height) = screen.iconSize
                            Image(
                                painterResource(id = screen.iconId),
                                contentDescription = screen.label,
                                modifier = Modifier
                                    .width(width.dp)
                                    .height(height.dp),
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                            )
                            Text(
                                text = screen.label,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontSize = 12.sp,
                                fontFamily = juryFonts,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } else {
                    IconButton(
                        onClick = {
                            if (currentRoute != screen.route.name) {
                                navController.navigate(screen.route.name)
                            }
                        },
                        modifier = Modifier
                            .width(70.dp)
                            .offset(y = if (currentRoute == MainRoute.MENTOR.name) 20.dp else 10.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .height(70.dp)
                                .align(Alignment.Bottom)
                        ) {
                            val (width, height) = screen.iconSize
                            Image(
                                painterResource(id = screen.iconId),
                                contentDescription = screen.label,
                                modifier = Modifier
                                    .width(width.dp)
                                    .height(height.dp),
                                colorFilter = ColorFilter.tint(if (currentRoute == screen.route.name) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface)
                            )
                            Text(
                                text = screen.label,
                                color = if (currentRoute == screen.route.name) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                fontSize = 12.sp,
                                fontFamily = juryFonts,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

sealed class NavBarScreen(val route: MainRoute, val label: String, val iconId: Int, val iconSize: Pair<Double, Double>) {
    object Courses : NavBarScreen(MainRoute.COURSES, MainRoute.COURSES.routeName, R.drawable.coursers_tab_icon, Pair(30.0, 26.3))
    object Mentor : NavBarScreen(MainRoute.MENTOR, MainRoute.MENTOR.routeName, R.drawable.mentor_tab_icon, Pair(39.0, 39.0))
    object Profile : NavBarScreen(MainRoute.PROFILE, MainRoute.PROFILE.routeName, R.drawable.mentor_tab_icon, Pair(20.0, 25.0))
}

private val MainRoute.routeName: String
    get() = when (this) {
        MainRoute.COURSES -> "courses"
        MainRoute.MENTOR -> "mentor"
        MainRoute.PROFILE -> "profile"
    }

enum class MainRoute {
    COURSES,
    MENTOR,
    PROFILE
}

private val items = listOf(
    NavBarScreen.Courses,
    NavBarScreen.Mentor,
    NavBarScreen.Profile
)

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    MyTheme {
        MainScreen(LoginViewModel(AuthRepository()))
    }
}