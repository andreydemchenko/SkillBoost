package com.example.skillboost.viewmodels

import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.IntRect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skillboost.models.User
import com.example.skillboost.repositories.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModelFactory(private val repository: AuthRepository, private val sharedPreferences: SharedPreferences? = null) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class MainViewModel(private val authRepository: AuthRepository) : ViewModel() {
    val isUserLoggedIn: StateFlow<Boolean> = authRepository.userLoggedIn
    val user = MutableStateFlow<User?>(null)
    val isUserHaveAccess = mutableStateOf(false)

    val highlightBoxes = mutableStateOf<List<IntRect>>(listOf())
    val currentTutorialStep = mutableStateOf(0)
    val tutorialStepsTitle = listOf(
        "В “Мои курсы” можно создать свой первый курс бесплатно",
        "В “Популярные курсы” собраны самые востребованные курсы со всего мира",
        "Ментор - это чат помощник который ответит на ваш вопрос в любое время суток",
        "Каждый вопрос созраняется отдельно, что позволяет не задавать вопрос дважды"
    )
    val tutorialStepsDescription = listOf(
        "Напишите чему бы вы хотели научится, и через несколько минут будет готов ваш персональный курс",
        "",
        "",
        ""
    )

    val hasNotSeenTutorial = mutableStateOf(!hasSeenTutorial())
    fun hasSeenTutorial(): Boolean {
      //  return sharedPreferences.getBoolean("HasSeenTutorial", false)
        return true
    }

    fun setSeenTutorial() {
//        val editor = sharedPreferences.edit()
//        editor.putBoolean("HasSeenTutorial", true)
//        editor.apply()
        hasNotSeenTutorial.value = false
    }
}