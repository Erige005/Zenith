package com.example.games.zenmerge

import kotlin.random.Random

data class Tile(
    val id: Long,
    val value: Int
)

data class ZenMergeState(
    val grid: List<List<Tile?>>,
    val score: Int = 0,
    val bestScore: Int = 0,
    val moves: Int = 0,
    val isGameOver: Boolean = false,
    val reachedZenith: Boolean = false,
    val lastMergedValue: Int = 0
)

class ZenMergeEngine(val size: Int = 4) {
    private var tileIdCounter = 1L
    private val history = ArrayDeque<Pair<List<List<Tile?>>, Int>>()

    fun createInitialState(bestScore: Int = 0): ZenMergeState {
        val emptyGrid = List(size) { List<Tile?>(size) { null } }
        var state = ZenMergeState(grid = emptyGrid, bestScore = bestScore)
        state = spawnTile(state)
        state = spawnTile(state)
        return state
    }

    fun canUndo(): Boolean = history.isNotEmpty()

    fun undo(currentState: ZenMergeState): ZenMergeState {
        if (history.isEmpty()) return currentState
        val (previousGrid, previousScore) = history.removeLast()
        return currentState.copy(
            grid = previousGrid,
            score = previousScore,
            isGameOver = false
        )
    }

    fun move(state: ZenMergeState, direction: Direction): Pair<ZenMergeState, Boolean> {
        val (newGrid, pointsEarned, moved, maxMerged) = when (direction) {
            Direction.LEFT -> slideAndMergeLeft(state.grid)
            Direction.RIGHT -> {
                val reversed = state.grid.map { it.reversed() }
                val (slid, pts, m, mx) = slideAndMergeLeft(reversed)
                Quad(slid.map { it.reversed() }, pts, m, mx)
            }
            Direction.UP -> {
                val transposed = transpose(state.grid)
                val (slid, pts, m, mx) = slideAndMergeLeft(transposed)
                Quad(transpose(slid), pts, m, mx)
            }
            Direction.DOWN -> {
                val transposed = transpose(state.grid).map { it.reversed() }
                val (slid, pts, m, mx) = slideAndMergeLeft(transposed)
                Quad(transpose(slid.map { it.reversed() }), pts, m, mx)
            }
        }

        if (!moved) {
            return state to false
        }

        // Save current for undo
        history.addLast(state.grid to state.score)
        if (history.size > 10) history.removeFirst()

        val newScore = state.score + pointsEarned
        val newBest = maxOf(state.bestScore, newScore)
        var nextState = state.copy(
            grid = newGrid,
            score = newScore,
            bestScore = newBest,
            moves = state.moves + 1,
            lastMergedValue = maxMerged
        )

        // Spawn new tile
        nextState = spawnTile(nextState)

        // Check 2048 milestone
        val reached = nextState.grid.flatten().any { it?.value == 2048 }
        val isOver = checkGameOver(nextState.grid)

        return nextState.copy(
            isGameOver = isOver,
            reachedZenith = reached && !state.reachedZenith
        ) to true
    }

    private data class Quad(
        val grid: List<List<Tile?>>,
        val points: Int,
        val moved: Boolean,
        val maxMerged: Int
    )

    private fun slideAndMergeLeft(grid: List<List<Tile?>>): Quad {
        var totalPoints = 0
        var anyMoved = false
        var maxMerged = 0

        val newGrid = grid.map { row ->
            val nonNull = row.filterNotNull()
            val mergedRow = mutableListOf<Tile?>()
            var i = 0

            while (i < nonNull.size) {
                if (i + 1 < nonNull.size && nonNull[i].value == nonNull[i + 1].value) {
                    val mergedValue = nonNull[i].value * 2
                    totalPoints += mergedValue
                    if (mergedValue > maxMerged) maxMerged = mergedValue
                    mergedRow.add(Tile(tileIdCounter++, mergedValue))
                    i += 2
                    anyMoved = true
                } else {
                    mergedRow.add(nonNull[i])
                    i++
                }
            }

            while (mergedRow.size < size) {
                mergedRow.add(null)
            }

            if (mergedRow.map { it?.value } != row.map { it?.value }) {
                anyMoved = true
            }

            mergedRow.toList()
        }

        return Quad(newGrid, totalPoints, anyMoved, maxMerged)
    }

    private fun transpose(grid: List<List<Tile?>>): List<List<Tile?>> {
        return List(size) { c ->
            List(size) { r -> grid[r][c] }
        }
    }

    private fun spawnTile(state: ZenMergeState): ZenMergeState {
        val emptySlots = mutableListOf<Pair<Int, Int>>()
        for (r in 0 until size) {
            for (c in 0 until size) {
                if (state.grid[r][c] == null) {
                    emptySlots.add(r to c)
                }
            }
        }
        if (emptySlots.isEmpty()) return state

        val (r, c) = emptySlots.random()
        val value = if (Random.nextFloat() < 0.9f) 2 else 4
        val newTile = Tile(tileIdCounter++, value)

        val updatedGrid = state.grid.mapIndexed { ri, row ->
            row.mapIndexed { ci, cell ->
                if (ri == r && ci == c) newTile else cell
            }
        }

        return state.copy(grid = updatedGrid)
    }

    private fun checkGameOver(grid: List<List<Tile?>>): Boolean {
        for (r in 0 until size) {
            for (c in 0 until size) {
                val tile = grid[r][c] ?: return false
                // Check right
                if (c + 1 < size && grid[r][c + 1]?.value == tile.value) return false
                // Check down
                if (r + 1 < size && grid[r + 1][c]?.value == tile.value) return false
            }
        }
        return true
    }

    enum class Direction {
        LEFT, RIGHT, UP, DOWN
    }
}
