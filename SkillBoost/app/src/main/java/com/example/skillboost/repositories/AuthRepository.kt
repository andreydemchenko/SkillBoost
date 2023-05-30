package com.example.skillboost.repositories

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthRepository {
    private val _userLoggedIn = MutableStateFlow(true)
    val userLoggedIn: StateFlow<Boolean> = _userLoggedIn

    fun setUserLoggedIn(loggedIn: Boolean) {
        _userLoggedIn.value = loggedIn
    }
}


