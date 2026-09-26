package com.example.games.lumina

enum class Direction {
    UP, RIGHT, DOWN, LEFT;

    fun opposite(): Direction = when (this) {
        UP -> DOWN
        RIGHT -> LEFT
        DOWN -> UP
        LEFT -> RIGHT
    }
}

enum class CellType {
    EMPTY,
    EMITTER,
    TARGET,
    BLOCK,
    MIRROR_FORWARD, // '/'
    MIRROR_BACKWARD, // '\'
    SPLITTER // '+'
}

data class LaserSegment(
    val startX: Float,
    val startY: Float,
    val endX: Float,
    val endY: Float,
    val colorHex: Long = 0xFF38BDF8
)

data class GridCell(
    val row: Int,
    val col: Int,
    val type: CellType = CellType.EMPTY,
    val isMovable: Boolean = false, // If true, player can tap to rotate/change
    val targetHit: Boolean = false,
    val emitterDir: Direction = Direction.RIGHT
)

data class LuminaLevel(
    val id: Int,
    val name: String,
    val description: String,
    val rows: Int = 5,
    val cols: Int = 5,
    val initialCells: List<GridCell>
)
