package com.example.skillboost.viewmodels

import androidx.lifecycle.ViewModel
import com.example.skillboost.models.CourseCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CoursesViewModel: ViewModel() {
    private val _myCoursesCards = MutableStateFlow<List<CourseCard>>(emptyList())
    val myCoursesCards: StateFlow<List<CourseCard>> get() = _myCoursesCards

    private val _popularCoursesCards = MutableStateFlow<List<CourseCard>>(emptyList())
    val popularCoursesCards: StateFlow<List<CourseCard>> get() = _popularCoursesCards

    init {
//        _myCoursesCards.value = listOf(
//            CourseCard(1, "Как научится играть на гитаре и выступать на сцене", "Музыка"),
//            CourseCard(2, "Управляемый занос и настройка автомобиля", "Автомобили"),
//            CourseCard(3, "Card 3", "Category C")
//        )
        _popularCoursesCards.value = listOf(
            CourseCard(1, "Загрузка приложений в PlayMarket", "IT"),
            CourseCard(2, "Как стать популярным стримером", "Блог"),
            CourseCard(3, "Как научится рисовать иллюстрации в стиле Disney", "Graphic"),
            CourseCard(4, "Ux/Ui с нуля", "IT"),
            CourseCard(5, "Python - как выучить язык будущего", "IT"),
        )
    }

    private val _selectedCardId = MutableStateFlow<Int?>(null)
    val selectedCardId: StateFlow<Int?> get() = _selectedCardId

    fun onCardClicked(cardId: Int) {
        _selectedCardId.value = cardId
    }
}