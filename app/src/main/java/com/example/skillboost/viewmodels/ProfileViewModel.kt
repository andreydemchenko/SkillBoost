package com.example.skillboost.viewmodels

import androidx.lifecycle.ViewModel
import com.example.skillboost.models.CourseCard
import com.example.skillboost.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel: ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    private val _completedCoursesCards = MutableStateFlow<List<CourseCard>>(emptyList())
    val completedCoursesCards: StateFlow<List<CourseCard>> get() = _completedCoursesCards

    init {
        _user.value = User(
            id = "id123",
            email = "ivan33school@gmail.ru",
            name = "Valera Pen",
            photoUrl = ""
        )

        _completedCoursesCards.value = listOf(
            CourseCard(1, "Как уменьшить расход автомобиля в городе", "Автомобили"),
            CourseCard(2, "Как научится играть на гитаре и выступатиь на сцене", "Музыка")
        )
    }

    private val _selectedCardId = MutableStateFlow<Int?>(null)
    val selectedCardId: StateFlow<Int?> get() = _selectedCardId

    fun onCardClicked(cardId: Int) {
        _selectedCardId.value = cardId
    }
}