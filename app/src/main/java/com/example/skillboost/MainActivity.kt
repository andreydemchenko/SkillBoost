package com.example.skillboost

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.skillboost.repositories.AuthRepository
import com.example.skillboost.screens.LoginScreen
import com.example.skillboost.screens.MainScreen
import com.example.skillboost.screens.SplashScreen
import com.example.skillboost.ui.theme.MyTheme
import com.example.skillboost.viewmodels.LoginViewModel
import com.example.skillboost.viewmodels.MainViewModel
import com.example.skillboost.viewmodels.MainViewModelFactory

class MainActivity : ComponentActivity() {

    private val authRepository = AuthRepository()
    private lateinit var sharedPreferences: SharedPreferences

    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory(authRepository, sharedPreferences)
    }

    private val loginViewModel: LoginViewModel by viewModels {
        MainViewModelFactory(authRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("MyApp", Context.MODE_PRIVATE)

        setContent {
            MyTheme {
                val navController = rememberNavController()
                val isUserLoggedIn = mainViewModel.isUserLoggedIn.collectAsState()

                NavHost(navController, startDestination = MainNavScreen.Splash.route) {
                    composable(MainNavScreen.Splash.route) {
                        SplashScreen(
                            navController = navController,
                            isUserLoggedIn = isUserLoggedIn.value
                        )
                    }
                    composable(MainNavScreen.Login.route) { LoginScreen(loginViewModel) }
                    composable(MainNavScreen.Main.route) {
                        MainScreen(
                            mainViewModel,
                            loginViewModel
                        )
                    }
                }
            }
        }
    }
}

sealed class MainNavScreen(val route: String) {
    object Splash : MainNavScreen("splash")
    object Login : MainNavScreen("login")
    object Main : MainNavScreen("main")
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    MyTheme {

    }
}