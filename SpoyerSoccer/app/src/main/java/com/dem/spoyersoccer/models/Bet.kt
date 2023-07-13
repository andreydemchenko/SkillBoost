package com.dem.spoyersoccer.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Bet(
    @PrimaryKey val id: Int,
    @Embedded
    val match: LiveMatch,
    @Embedded
    val odds: LiveMatchOdds,
    val betOn: Int,
    val amount: Double
)