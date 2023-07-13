package com.dem.spoyersoccer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dem.spoyersoccer.models.Bet
import com.dem.spoyersoccer.models.NoteEntity
import com.dem.spoyersoccer.utils.Converters

@Database(entities = [NoteEntity::class, Bet::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
