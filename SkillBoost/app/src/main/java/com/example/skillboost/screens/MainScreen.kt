package com.example.skillboost.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.material3.ButtonDefaults
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.skillboost.R
import com.example.skillboost.ui.theme.MyTheme
import com.example.skillboost.ui.theme.interFonts
import com.example.skillboost.ui.theme.juryFonts
import com.example.skillboost.viewmodels.CoursesViewModel
import com.example.skillboost.viewmodels.LoginViewModel
import com.example.skillboost.viewmodels.MainViewModel
import com.example.skillboost.viewmodels.MentorViewModel
import com.example.skillboost.viewmodels.ProfileViewModel

@Composable
fun MainScreen(mainViewModel: MainViewModel, loginViewModel: LoginViewModel) {

    val navController = rememberNavController()
    val profileViewModel = ProfileViewModel()
    val mentorViewModel = MentorViewModel()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.padding(bottom = 55.dp)) {
            NavHost(navController = navController, startDestination = NavBarScreen.Courses.route.name) {
                composable(NavBarScreen.Courses.route.name) {
                    CoursesScreen(
                        viewModel = CoursesViewModel(),
                        mainViewModel = mainViewModel
                    )
                }
                composable(NavBarScreen.Mentor.route.name) {
                    MentorScreen(
                        mainViewModel = mainViewModel,
                        viewModel = mentorViewModel,
                        profileViewModel = profileViewModel
                    )
                }
                composable(NavBarScreen.Profile.route.name) {
                    ProfileScreen(
                        mainViewModel = mainViewModel,
                        loginViewModel = loginViewModel,
                        viewModel = profileViewModel
                    )
                }
                composable(MainRoute.UPGRADE.routeName) { UpgradeScreen(viewModel = profileViewModel) }
                composable(MainRoute.HISTORY.routeName) {
                    HistoryScreen(
                        viewModel = mentorViewModel,
                        mainViewModel = mainViewModel
                    )
                }
            }
        }
        Column {
            Spacer(modifier = Modifier.weight(1f))
            AppBottomNavigation(
                navController = navController,
                currentNavBarScreen = NavBarScreen.Courses,
                mainViewModel = mainViewModel,
                mentorViewModel = mentorViewModel,
                profileViewModel = profileViewModel
            )
        }

        if (mainViewModel.hasNotSeenTutorial.value) {
            TutorialOverlay(mainViewModel)
            if (mainViewModel.currentTutorialStep.value == 2) {
                navController.navigate(MainRoute.MENTOR.name)
            }
        }

        if (mentorViewModel.showHistory.collectAsState().value) {
            navController.navigate(MainRoute.HISTORY.routeName)
        }

        if (profileViewModel.showUpgrade.collectAsState().value) {
            navController.navigate(MainRoute.UPGRADE.routeName)
        }
    }
}

@Composable
fun TutorialOverlay(viewModel: MainViewModel) {

    val currentStep = viewModel.currentTutorialStep.value
    val stepsTitle = viewModel.tutorialStepsTitle
    val stepsDesc = viewModel.tutorialStepsDescription

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (viewModel.currentTutorialStep.value != 3) Spacer(modifier = Modifier.height(200.dp))
        Text(
            text = "Step ${currentStep + 1}",
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontFamily = interFonts,
            fontSize = 18.sp
        )

        Text(
            text = stepsTitle.getOrNull(currentStep) ?: "",
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontFamily = interFonts,
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 12.dp)
        )

        Button(
            onClick = {
                if (currentStep < stepsTitle.lastIndex) {
                    viewModel.currentTutorialStep.value++
                } else {
                    viewModel.setSeenTutorial()
                    viewModel.currentTutorialStep.value = 0
                    viewModel.highlightBoxes.value = emptyList()
                }
            },
            modifier = Modifier
                .padding(top = 12.dp)
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
            Text(
                text = if (currentStep < stepsTitle.lastIndex) "Next >" else "Finish",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontFamily = interFonts,
                fontSize = 18.sp,
            )
        }
        if (viewModel.currentTutorialStep.value == 3) Spacer(modifier = Modifier.height(400.dp))
    }
}

@Composable
fun AppBottomNavigation(
    mainViewModel: MainViewModel,
    mentorViewModel: MentorViewModel,
    profileViewModel: ProfileViewModel,
    navController: NavController,
    currentNavBarScreen: NavBarScreen
) {
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
                                .tutorialBlur(
                                    mainViewModel,
                                    mainViewModel.currentTutorialStep.value != 2
                                )
                        } else {
                            Modifier
                                .width(110.dp)
                                .offset(y = 20.dp)
                                .height(110.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                                .tutorialBlur(
                                    mainViewModel,
                                    mainViewModel.currentTutorialStep.value != 2
                                )
                                .padding(2.dp)
                                .border(6.dp, MaterialTheme.colorScheme.onPrimary, CircleShape)
                                .offset(y = (-4).dp)
                        },
                        onClick = {
                            if (currentRoute != screen.route.name) {
                                navController.navigate(screen.route.name)

                                if (mentorViewModel.showHistory.value) {
                                    mentorViewModel.closeHistoryScreen()
                                }
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

                                if (profileViewModel.showUpgrade.value) {
                                    profileViewModel.closeUpgradeScreen()
                                }
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
                                .tutorialBlur(
                                    mainViewModel,
                                    screen.route.name == MainRoute.PROFILE.name ||
                                            mainViewModel.currentTutorialStep.value !in 0..1
                                )
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

val MainRoute.routeName: String
    get() = when (this) {
        MainRoute.COURSES -> "courses"
        MainRoute.MENTOR -> "mentor"
        MainRoute.PROFILE -> "profile"
        MainRoute.UPGRADE -> "upgrade"
        MainRoute.HISTORY -> "history"
    }

enum class MainRoute {
    COURSES,
    MENTOR,
    PROFILE,
    UPGRADE,
    HISTORY
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
        //MainScreen(MainViewModel(), LoginViewModel(AuthRepository()))
    }
}