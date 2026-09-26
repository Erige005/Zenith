package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameRecordDao {
    @Query("SELECT * FROM game_records ORDER BY timestamp DESC")
    fun getAllRecords(): Flow<List<GameRecord>>

    @Query("SELECT * FROM game_records WHERE gameMode = :mode ORDER BY timestamp DESC")
    fun getRecordsByMode(mode: String): Flow<List<GameRecord>>

    @Query("SELECT MAX(score) FROM game_records WHERE gameMode = :mode")
    fun getHighScore(mode: String): Flow<Int?>

    @Query("SELECT MAX(level) FROM game_records WHERE gameMode = :mode AND completed = 1")
    fun getHighestLevel(mode: String): Flow<Int?>

    @Query("SELECT COUNT(*) FROM game_records WHERE gameMode = :mode")
    fun getTotalGames(mode: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM game_records")
    fun getTotalGamesOverall(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: GameRecord): Long

    @Query("DELETE FROM game_records")
    suspend fun clearAllRecords()
}
