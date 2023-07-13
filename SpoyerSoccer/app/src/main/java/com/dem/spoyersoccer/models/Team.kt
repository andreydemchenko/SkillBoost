package com.dem.spoyersoccer.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Team(
    @ColumnInfo(name = "team_id")
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val imageUrl: String
)