package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_records")
data class GameRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val gameMode: String, // "lumina", "zen_merge", "echo"
    val score: Int,
    val level: Int = 1,
    val moves: Int = 0,
    val durationSec: Int = 0,
    val completed: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)

data class ModeStatsSummary(
    val gameMode: String,
    val totalGames: Int,
    val highScore: Int,
    val totalWins: Int,
    val highestLevel: Int
)
