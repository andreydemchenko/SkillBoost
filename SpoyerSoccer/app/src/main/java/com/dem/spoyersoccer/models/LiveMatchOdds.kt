package com.dem.spoyersoccer.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LiveMatchOdds(
    @ColumnInfo(name = "live_match_id")
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @Embedded(prefix = "home_")
    val home: Odds,
    @Embedded(prefix = "draw_")
    val draw: Odds,
    @Embedded(prefix = "away_")
    val away: Odds,
)

@Entity
data class Odds(
    @ColumnInfo(name = "odds_id")
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val value: Double,
    val name: String
)