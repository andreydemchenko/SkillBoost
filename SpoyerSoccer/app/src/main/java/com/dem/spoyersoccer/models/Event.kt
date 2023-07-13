package com.dem.spoyersoccer.models

import java.util.Date

data class Event(
    val id: Int,
    val home: Team,
    val away: Team,
    val score: String,
    val timeStatus: Int,
    val time: Date,
    val stats: Stats?,
    val events: List<EventItemModel>,
    val bet365Id: Int
) {
    fun getStatisticsModel(): StatisticsModel {
        return StatisticsModel(
            corners = stats?.getCornersPair(),
            goals = stats?.getGoalsPair(),
            penalties = stats?.getPenaltiesPair(),
            redCards = stats?.getRedCardsPair(),
            substitutions = stats?.getSubstitutionsPair(),
            yellowCards = stats?.getYellowCardsPair()
        )
    }
}

data class Stats(
    val corners: List<String>,
    val goals: List<String>,
    val penalties: List<String>,
    val redCards: List<String>,
    val substitutions: List<String>,
    val yellowCards: List<String>
) {
    private fun getIntPair(list: List<String>): Pair<Int, Int>? {
        return if (list.size >= 2) {
            Pair(list[0].toInt(), list[1].toInt())
        } else {
            null
        }
    }

    fun getCornersPair(): Pair<Int, Int>? {
        return getIntPair(corners)
    }

    fun getGoalsPair(): Pair<Int, Int>? {
        return getIntPair(goals)
    }

    fun getPenaltiesPair(): Pair<Int, Int>? {
        return getIntPair(penalties)
    }

    fun getRedCardsPair(): Pair<Int, Int>? {
        return getIntPair(redCards)
    }

    fun getSubstitutionsPair(): Pair<Int, Int>? {
        return getIntPair(substitutions)
    }

    fun getYellowCardsPair(): Pair<Int, Int>? {
        return getIntPair(yellowCards)
    }
}

data class StatisticsModel(
    val corners: Pair<Int, Int>?,
    val goals: Pair<Int, Int>?,
    val penalties: Pair<Int, Int>?,
    val redCards: Pair<Int, Int>?,
    val substitutions: Pair<Int, Int>?,
    val yellowCards: Pair<Int, Int>?
)

data class EventItemModel(
    val id: Int,
    val text: String
)