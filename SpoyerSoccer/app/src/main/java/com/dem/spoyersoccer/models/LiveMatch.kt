package com.dem.spoyersoccer.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class LiveMatch(
    @ColumnInfo(name = "match_id")
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @Embedded(prefix = "live_home_")
    val home: Team,
    @Embedded(prefix = "live_away_")
    val away: Team,
    val league: String,
    val score: String,
    val timeStatus: Int,
    val time: Date,
    val timer: String,
    val bet365Id: Int
)