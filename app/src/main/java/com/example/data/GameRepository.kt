package com.example.data

import kotlinx.coroutines.flow.Flow

class GameRepository(private val dao: GameRecordDao) {
    val allRecords: Flow<List<GameRecord>> = dao.getAllRecords()
    val totalGamesOverall: Flow<Int> = dao.getTotalGamesOverall()

    fun getRecords(mode: String): Flow<List<GameRecord>> = dao.getRecordsByMode(mode)

    fun getHighScore(mode: String): Flow<Int?> = dao.getHighScore(mode)

    fun getHighestLevel(mode: String): Flow<Int?> = dao.getHighestLevel(mode)

    fun getTotalGames(mode: String): Flow<Int> = dao.getTotalGames(mode)

    suspend fun saveRecord(record: GameRecord): Long = dao.insertRecord(record)

    suspend fun resetAll(): Unit = dao.clearAllRecords()
}
