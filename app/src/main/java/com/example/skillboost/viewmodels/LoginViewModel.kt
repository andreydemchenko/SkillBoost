package com.example.skillboost.viewmodels

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModel
import com.example.skillboost.R
import com.example.skillboost.models.Language
import com.example.skillboost.models.User
import com.example.skillboost.repositories.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    private val _selectedLanguage = MutableStateFlow(Language(R.drawable.england_icon, "English", "GBP"))
    val selectedLanguage: StateFlow<Language> get() = _selectedLanguage

    fun selectLanguage(language: Language) {
        _selectedLanguage.value = language
    }

    fun signInWithGoogle() {
        // Implement Google Sign-In here

        // After signing in, update the user value
        //_user.value = User(...)
        authRepository.setUserLoggedIn(true)
    }

    fun signOut() {
        _user.value = null
        authRepository.setUserLoggedIn(false)
    }
}
