package com.dem.spoyersoccer.models

import java.util.Date

data class UpcomingMatch(
        val id: Int,
        val home: Team,
        val away: Team,
        val time: Date,
        val bet365Id: Int
)