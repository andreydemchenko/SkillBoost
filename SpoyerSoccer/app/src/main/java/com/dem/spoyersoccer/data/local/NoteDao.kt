package com.dem.spoyersoccer.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dem.spoyersoccer.models.Bet
import com.dem.spoyersoccer.models.NoteEntity

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: NoteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(bet: Bet)

    @Query("SELECT * FROM noteentity")
    fun getAllNotes(): LiveData<List<NoteEntity>>

    @Query("SELECT * FROM bet")
    fun getAllBets(): LiveData<List<Bet>>

    @Delete
    fun delete(note: NoteEntity)

    @Update
    fun update(note: NoteEntity)
}
