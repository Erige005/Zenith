package com.example.games.lumina

object LuminaLevels {
    fun getLevels(): List<LuminaLevel> = listOf(
        // Level 1: First Reflection (Warm up)
        LuminaLevel(
            id = 1,
            name = "First Refraction",
            description = "Rotate the mirror to guide the beam into the crystal gem.",
            rows = 5,
            cols = 5,
            initialCells = listOf(
                GridCell(row = 0, col = 0, type = CellType.EMITTER, emitterDir = Direction.RIGHT),
                GridCell(row = 0, col = 3, type = CellType.EMPTY, isMovable = true),
                GridCell(row = 3, col = 3, type = CellType.TARGET),
                GridCell(row = 1, col = 1, type = CellType.BLOCK)
            )
        ),
        // Level 2: The Corner
        LuminaLevel(
            id = 2,
            name = "Dual Corner",
            description = "Navigate around barriers using two mirrors.",
            rows = 5,
            cols = 5,
            initialCells = listOf(
                GridCell(row = 0, col = 1, type = CellType.EMITTER, emitterDir = Direction.DOWN),
                GridCell(row = 4, col = 1, type = CellType.EMPTY, isMovable = true),
                GridCell(row = 4, col = 4, type = CellType.EMPTY, isMovable = true),
                GridCell(row = 1, col = 4, type = CellType.TARGET),
                GridCell(row = 2, col = 2, type = CellType.BLOCK),
                GridCell(row = 3, col = 2, type = CellType.BLOCK)
            )
        ),
        // Level 3: Dual Prism
        LuminaLevel(
            id = 3,
            name = "Prism Split",
            description = "Illuminate both crystal cores simultaneously with a beam splitter.",
            rows = 5,
            cols = 5,
            initialCells = listOf(
                GridCell(row = 2, col = 0, type = CellType.EMITTER, emitterDir = Direction.RIGHT),
                GridCell(row = 2, col = 2, type = CellType.SPLITTER, isMovable = false),
                GridCell(row = 0, col = 2, type = CellType.TARGET),
                GridCell(row = 4, col = 2, type = CellType.EMPTY, isMovable = true),
                GridCell(row = 4, col = 4, type = CellType.TARGET)
            )
        ),
        // Level 4: The Serpentine Path
        LuminaLevel(
            id = 4,
            name = "Harmonic Wave",
            description = "Weave the radiant laser through 3 mirrors to reach the sanctuary.",
            rows = 5,
            cols = 5,
            initialCells = listOf(
                GridCell(row = 1, col = 0, type = CellType.EMITTER, emitterDir = Direction.RIGHT),
                GridCell(row = 1, col = 3, type = CellType.EMPTY, isMovable = true),
                GridCell(row = 3, col = 3, type = CellType.EMPTY, isMovable = true),
                GridCell(row = 3, col = 1, type = CellType.EMPTY, isMovable = true),
                GridCell(row = 4, col = 1, type = CellType.TARGET),
                GridCell(row = 2, col = 2, type = CellType.BLOCK),
                GridCell(row = 0, col = 3, type = CellType.BLOCK)
            )
        ),
        // Level 5: Quad Resonance
        LuminaLevel(
            id = 5,
            name = "Quad Resonance",
            description = "Split the light and redirect both streams to twin targets.",
            rows = 5,
            cols = 5,
            initialCells = listOf(
                GridCell(row = 4, col = 2, type = CellType.EMITTER, emitterDir = Direction.UP),
                GridCell(row = 2, col = 2, type = CellType.SPLITTER, isMovable = false),
                GridCell(row = 2, col = 0, type = CellType.EMPTY, isMovable = true),
                GridCell(row = 0, col = 0, type = CellType.TARGET),
                GridCell(row = 2, col = 4, type = CellType.EMPTY, isMovable = true),
                GridCell(row = 0, col = 4, type = CellType.TARGET),
                GridCell(row = 1, col = 2, type = CellType.BLOCK)
            )
        ),
        // Level 6: Mirror Labyrinth
        LuminaLevel(
            id = 6,
            name = "Starlight Labyrinth",
            description = "Master precision reflections across multiple movable optical facets.",
            rows = 5,
            cols = 5,
            initialCells = listOf(
                GridCell(row = 0, col = 2, type = CellType.EMITTER, emitterDir = Direction.DOWN),
                GridCell(row = 1, col = 2, type = CellType.EMPTY, isMovable = true),
                GridCell(row = 1, col = 4, type = CellType.EMPTY, isMovable = true),
                GridCell(row = 4, col = 4, type = CellType.EMPTY, isMovable = true),
                GridCell(row = 4, col = 1, type = CellType.EMPTY, isMovable = true),
                GridCell(row = 2, col = 1, type = CellType.TARGET),
                GridCell(row = 3, col = 2, type = CellType.BLOCK),
                GridCell(row = 2, col = 3, type = CellType.BLOCK)
            )
        ),
        // Level 7: Triple Alignment
        LuminaLevel(
            id = 7,
            name = "Celestial Alignment",
            description = "Align reflections through 3 target nodes in a loop.",
            rows = 5,
            cols = 5,
            initialCells = listOf(
                GridCell(row = 0, col = 0, type = CellType.EMITTER, emitterDir = Direction.RIGHT),
                GridCell(row = 0, col = 4, type = CellType.EMPTY, isMovable = true),
                GridCell(row = 2, col = 4, type = CellType.TARGET),
                GridCell(row = 4, col = 4, type = CellType.EMPTY, isMovable = true),
                GridCell(row = 4, col = 0, type = CellType.EMPTY, isMovable = true),
                GridCell(row = 2, col = 0, type = CellType.TARGET),
                GridCell(row = 2, col = 2, type = CellType.BLOCK)
            )
        ),
        // Level 8: Zenith Master
        LuminaLevel(
            id = 8,
            name = "Zenith Master Core",
            description = "The ultimate synthesis of prism refraction, reflection and patience.",
            rows = 5,
            cols = 5,
            initialCells = listOf(
                GridCell(row = 2, col = 4, type = CellType.EMITTER, emitterDir = Direction.LEFT),
                GridCell(row = 2, col = 2, type = CellType.SPLITTER, isMovable = false),
                GridCell(row = 0, col = 2, type = CellType.EMPTY, isMovable = true),
                GridCell(row = 0, col = 0, type = CellType.TARGET),
                GridCell(row = 4, col = 2, type = CellType.EMPTY, isMovable = true),
                GridCell(row = 4, col = 0, type = CellType.TARGET),
                GridCell(row = 1, col = 1, type = CellType.BLOCK),
                GridCell(row = 3, col = 1, type = CellType.BLOCK)
            )
        )
    )
}

class LuminaSolver(private val rows: Int, private val cols: Int) {
    data class RayState(val row: Int, val col: Int, val dir: Direction)

    fun traceBeams(
        cells: Map<Pair<Int, Int>, GridCell>
    ): Pair<List<LaserSegment>, Set<Pair<Int, Int>>> {
        val segments = mutableListOf<LaserSegment>()
        val hitTargets = mutableSetOf<Pair<Int, Int>>()

        // Find all emitters
        val emitters = cells.values.filter { it.type == CellType.EMITTER }
        val visited = mutableSetOf<RayState>()

        for (emitter in emitters) {
            val queue = ArrayDeque<RayState>()
            queue.add(RayState(emitter.row, emitter.col, emitter.emitterDir))

            while (queue.isNotEmpty()) {
                val current = queue.removeFirst()
                if (current in visited) continue
                visited.add(current)

                // Compute next coordinate
                val (nextRow, nextCol) = when (current.dir) {
                    Direction.UP -> current.row - 1 to current.col
                    Direction.RIGHT -> current.row to current.col + 1
                    Direction.DOWN -> current.row + 1 to current.col
                    Direction.LEFT -> current.row to current.col - 1
                }

                // Add laser segment from center of current to center of next (or bounds)
                val startX = current.col + 0.5f
                val startY = current.row + 0.5f

                if (nextRow !in 0 until rows || nextCol !in 0 until cols) {
                    // Reached boundary edge
                    val endX = when (current.dir) {
                        Direction.RIGHT -> cols.toFloat()
                        Direction.LEFT -> 0f
                        else -> startX
                    }
                    val endY = when (current.dir) {
                        Direction.DOWN -> rows.toFloat()
                        Direction.UP -> 0f
                        else -> startY
                    }
                    segments.add(LaserSegment(startX, startY, endX, endY))
                    continue
                }

                val endX = nextCol + 0.5f
                val endY = nextRow + 0.5f
                segments.add(LaserSegment(startX, startY, endX, endY))

                val targetCell = cells[nextRow to nextCol]
                if (targetCell != null) {
                    when (targetCell.type) {
                        CellType.BLOCK -> {
                            // Beam absorbed
                        }
                        CellType.TARGET -> {
                            hitTargets.add(nextRow to nextCol)
                            // Beam passes through crystal to allow daisy chaining!
                            queue.add(RayState(nextRow, nextCol, current.dir))
                        }
                        CellType.MIRROR_FORWARD -> {
                            // '/' mirror
                            val newDir = when (current.dir) {
                                Direction.UP -> Direction.RIGHT
                                Direction.RIGHT -> Direction.UP
                                Direction.DOWN -> Direction.LEFT
                                Direction.LEFT -> Direction.DOWN
                            }
                            queue.add(RayState(nextRow, nextCol, newDir))
                        }
                        CellType.MIRROR_BACKWARD -> {
                            // '\' mirror
                            val newDir = when (current.dir) {
                                Direction.UP -> Direction.LEFT
                                Direction.RIGHT -> Direction.DOWN
                                Direction.DOWN -> Direction.RIGHT
                                Direction.LEFT -> Direction.UP
                            }
                            queue.add(RayState(nextRow, nextCol, newDir))
                        }
                        CellType.SPLITTER -> {
                            // Split into two 90-degree perpendicular paths
                            val (dir1, dir2) = when (current.dir) {
                                Direction.UP, Direction.DOWN -> Direction.LEFT to Direction.RIGHT
                                Direction.LEFT, Direction.RIGHT -> Direction.UP to Direction.DOWN
                            }
                            queue.add(RayState(nextRow, nextCol, dir1))
                            queue.add(RayState(nextRow, nextCol, dir2))
                        }
                        CellType.EMITTER -> {
                            // Passes emitter
                            queue.add(RayState(nextRow, nextCol, current.dir))
                        }
                        CellType.EMPTY -> {
                            queue.add(RayState(nextRow, nextCol, current.dir))
                        }
                    }
                } else {
                    queue.add(RayState(nextRow, nextCol, current.dir))
                }
            }
        }

        return segments to hitTargets
    }
}
