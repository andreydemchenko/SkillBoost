package com.dem.spoyersoccer.models

import java.util.Date

data class PastMatch(
    val id: Int,
    val home: Team,
    val away: Team,
    val score: String,
    val timeStatus: Int,
    val time: Date
)