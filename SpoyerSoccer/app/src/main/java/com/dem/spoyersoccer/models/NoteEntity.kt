package com.dem.spoyersoccer.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
data class NoteEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val text: String
)

@Parcelize
data class Note(
    val id: Int,
    val title: String,
    val text: String
) : Parcelable