package com.example.skillboost.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.skillboost.models.ChatMessage
import com.example.skillboost.models.CourseCard
import com.example.skillboost.models.Plan
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

    val plans = listOf(
        Plan(
            title = "Start",
            subtitle = "3 courses + 24/7 mentor",
            price = "$5/mo",
            description = """
✦3 Personalized courses per month

✦Videos in tutorials.

✦Test interests and skills, by analyzing educational videos you liked on YouTube. It helps you see your hidden passion.

✦List of best careers that matches your interests with a personal learning plan.
""".trimIndent()
        ),
        Plan(
            title = "Premium",
            subtitle = "6 courses + 24/7 mentor",
            price = "$20/mo",
            description = """
✦20 Personalized courses per month to close blind spots in knowledge.

✦Videos in tutorials.

✦Test interests and skills, by analyzing educational videos you liked on YouTube. It helps you see your hidden passion.

✦List of best careers that matches your interests with a personal learning plan.
""".trimIndent()
        ),
        Plan(
            title = "Pro",
            subtitle = "unlimited courses + 24/7 mentor",
            price = "$35/mo",
            description = """
✦Unlimited AI-courses generator that save 80% of your time on course creating.

✦Tutorial converter from Youtube video or article that generates step-by-step guide, quiz and practical task in a minutes.

✦Students progress. Personalized tutorials to close blind spots.

✦Course hosting and embedded Stripe payments.

✦Embedded live room on the course page.

✦Human support.
""".trimIndent()
        )
    )

    private val _showUpgrade = MutableStateFlow(false)
    val showUpgrade: StateFlow<Boolean> get() = _showUpgrade

    fun openUpgradeScreen() {
        _showUpgrade.value = true
    }

    fun closeUpgradeScreen() {
        _showUpgrade.value = false
    }

    fun onCardClicked(cardId: Int) {
        _selectedCardId.value = cardId
    }

}