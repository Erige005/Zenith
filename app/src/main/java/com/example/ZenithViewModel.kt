package com.example

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.HapticHelper
import com.example.audio.ZenithAudioEngine
import com.example.data.AppDatabase
import com.example.data.GameRecord
import com.example.data.GameRepository
import com.example.games.echo.EchoCrystals
import com.example.games.echo.EchoGameState
import com.example.games.echo.EchoPlayState
import com.example.games.echo.EchoResonanceEngine
import com.example.games.lumina.CellType
import com.example.games.lumina.GridCell
import com.example.games.lumina.LaserSegment
import com.example.games.lumina.LuminaLevel
import com.example.games.lumina.LuminaLevels
import com.example.games.lumina.LuminaSolver
import com.example.games.zenmerge.ZenMergeEngine
import com.example.games.zenmerge.ZenMergeState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class Screen {
    HOME,
    LUMINA,
    ZEN_MERGE,
    ECHO,
    STATS,
    SETTINGS
}

data class LuminaGameState(
    val levelIndex: Int = 0,
    val level: LuminaLevel,
    val cells: Map<Pair<Int, Int>, GridCell>,
    val laserSegments: List<LaserSegment> = emptyList(),
    val hitTargets: Set<Pair<Int, Int>> = emptySet(),
    val totalTargets: Int = 0,
    val moves: Int = 0,
    val isSolved: Boolean = false
)

class ZenithViewModel(application: Application) : AndroidViewModel(application) {
    val repository: GameRepository
    val audioEngine = ZenithAudioEngine()
    val hapticHelper = HapticHelper(application.applicationContext)

    // Navigation State
    private val _currentScreen = MutableStateFlow(Screen.HOME)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Settings
    private val _isDarkTheme = MutableStateFlow(true)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    private val _isSoundEnabled = MutableStateFlow(true)
    val isSoundEnabled: StateFlow<Boolean> = _isSoundEnabled.asStateFlow()

    private val _isHapticsEnabled = MutableStateFlow(true)
    val isHapticsEnabled: StateFlow<Boolean> = _isHapticsEnabled.asStateFlow()

    // 1. Lumina State
    private val luminaLevels = LuminaLevels.getLevels()
    private val luminaSolver = LuminaSolver(5, 5)
    private val _luminaState = MutableStateFlow(createLuminaState(0))
    val luminaState: StateFlow<LuminaGameState> = _luminaState.asStateFlow()

    // 2. Zen Merge State
    private val zenMergeEngine = ZenMergeEngine()
    private val _zenMergeState = MutableStateFlow(zenMergeEngine.createInitialState(0))
    val zenMergeState: StateFlow<ZenMergeState> = _zenMergeState.asStateFlow()

    // 3. Echo State
    private val echoEngine = EchoResonanceEngine()
    private val _echoState = MutableStateFlow(EchoPlayState())
    val echoState: StateFlow<EchoPlayState> = _echoState.asStateFlow()

    // Database flow records
    val allRecords: StateFlow<List<GameRecord>>
    val luminaHighScore: StateFlow<Int?>
    val zenMergeHighScore: StateFlow<Int?>
    val echoHighScore: StateFlow<Int?>
    val totalGamesPlayed: StateFlow<Int>

    init {
        val db = AppDatabase.getDatabase(application)
        repository = GameRepository(db.gameRecordDao())

        allRecords = repository.allRecords.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        luminaHighScore = repository.getHighestLevel("lumina").stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            1
        )
        zenMergeHighScore = repository.getHighScore("zen_merge").stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )
        echoHighScore = repository.getHighScore("echo").stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )
        totalGamesPlayed = repository.totalGamesOverall.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

        // Initialize high score in ZenMerge once fetched
        viewModelScope.launch {
            zenMergeHighScore.collect { high ->
                if (high != null && high > _zenMergeState.value.bestScore) {
                    _zenMergeState.value = _zenMergeState.value.copy(bestScore = high)
                }
            }
        }
        viewModelScope.launch {
            echoHighScore.collect { high ->
                if (high != null && high > _echoState.value.bestScore) {
                    _echoState.value = _echoState.value.copy(bestScore = high)
                }
            }
        }
    }

    fun navigateTo(screen: Screen) {
        hapticHelper.click()
        _currentScreen.value = screen
    }

    fun navigateBack() {
        hapticHelper.click()
        _currentScreen.value = Screen.HOME
    }

    // Toggle Settings
    fun toggleTheme() {
        hapticHelper.click()
        _isDarkTheme.value = !_isDarkTheme.value
    }

    fun toggleSound() {
        hapticHelper.click()
        val newState = !_isSoundEnabled.value
        _isSoundEnabled.value = newState
        audioEngine.isSoundEnabled = newState
    }

    fun toggleHaptics() {
        val newState = !_isHapticsEnabled.value
        _isHapticsEnabled.value = newState
        hapticHelper.isHapticsEnabled = newState
        if (newState) hapticHelper.click()
    }

    fun clearAllData() {
        viewModelScope.launch {
            repository.resetAll()
            _zenMergeState.value = zenMergeEngine.createInitialState(0)
            _echoState.value = EchoPlayState()
            _luminaState.value = createLuminaState(0)
            hapticHelper.pulse()
        }
    }

    // ==========================================
    // LUMINA PRISM ACTIONS
    // ==========================================

    private fun createLuminaState(levelIndex: Int): LuminaGameState {
        val level = luminaLevels[levelIndex % luminaLevels.size]
        val cellMap = mutableMapOf<Pair<Int, Int>, GridCell>()
        for (r in 0 until level.rows) {
            for (c in 0 until level.cols) {
                cellMap[r to c] = GridCell(row = r, col = c)
            }
        }
        level.initialCells.forEach { cell ->
            cellMap[cell.row to cell.col] = cell
        }

        val totalTargets = cellMap.values.count { it.type == CellType.TARGET }
        val (segments, hitTargets) = luminaSolver.traceBeams(cellMap)
        val isSolved = hitTargets.size >= totalTargets && totalTargets > 0

        return LuminaGameState(
            levelIndex = levelIndex,
            level = level,
            cells = cellMap,
            laserSegments = segments,
            hitTargets = hitTargets,
            totalTargets = totalTargets,
            moves = 0,
            isSolved = isSolved
        )
    }

    fun onLuminaCellClick(row: Int, col: Int) {
        val current = _luminaState.value
        if (current.isSolved) return

        val cell = current.cells[row to col] ?: return
        if (!cell.isMovable) return

        hapticHelper.click()
        audioEngine.playLaserReflect()

        // Cycle through EMPTY -> MIRROR_FORWARD -> MIRROR_BACKWARD -> EMPTY
        val nextType = when (cell.type) {
            CellType.EMPTY -> CellType.MIRROR_FORWARD
            CellType.MIRROR_FORWARD -> CellType.MIRROR_BACKWARD
            CellType.MIRROR_BACKWARD -> CellType.EMPTY
            else -> cell.type
        }

        val updatedMap = current.cells.toMutableMap()
        updatedMap[row to col] = cell.copy(type = nextType)

        val (segments, hitTargets) = luminaSolver.traceBeams(updatedMap)
        val solved = hitTargets.size >= current.totalTargets && current.totalTargets > 0

        _luminaState.value = current.copy(
            cells = updatedMap,
            laserSegments = segments,
            hitTargets = hitTargets,
            moves = current.moves + 1,
            isSolved = solved
        )

        if (solved) {
            hapticHelper.victory()
            audioEngine.playVictory()
            saveLuminaVictory(current.levelIndex + 1, current.moves + 1)
        }
    }

    fun resetLuminaLevel() {
        hapticHelper.click()
        _luminaState.value = createLuminaState(_luminaState.value.levelIndex)
    }

    fun nextLuminaLevel() {
        hapticHelper.pulse()
        val nextIdx = (_luminaState.value.levelIndex + 1) % luminaLevels.size
        _luminaState.value = createLuminaState(nextIdx)
    }

    fun selectLuminaLevel(index: Int) {
        hapticHelper.click()
        _luminaState.value = createLuminaState(index.coerceIn(0, luminaLevels.size - 1))
    }

    private fun saveLuminaVictory(level: Int, moves: Int) {
        viewModelScope.launch {
            repository.saveRecord(
                GameRecord(
                    gameMode = "lumina",
                    score = maxOf(100, 1000 - moves * 40),
                    level = level,
                    moves = moves,
                    completed = true
                )
            )
        }
    }

    // ==========================================
    // ZEN MERGE ACTIONS
    // ==========================================

    fun onZenMergeMove(direction: ZenMergeEngine.Direction) {
        val current = _zenMergeState.value
        if (current.isGameOver) return

        val (nextState, moved) = zenMergeEngine.move(current, direction)
        if (moved) {
            _zenMergeState.value = nextState
            hapticHelper.click()
            if (nextState.lastMergedValue > 0) {
                val tier = kotlin.math.round(kotlin.math.log2(nextState.lastMergedValue.toDouble())).toInt()
                audioEngine.playMerge(tier)
            }
            if (nextState.isGameOver) {
                hapticHelper.pulse()
                audioEngine.playMismatch()
                saveZenMergeRecord(nextState.score, nextState.moves)
            } else if (nextState.reachedZenith) {
                hapticHelper.victory()
                audioEngine.playVictory()
            }
        }
    }

    fun onZenMergeUndo() {
        if (!zenMergeEngine.canUndo()) return
        hapticHelper.click()
        _zenMergeState.value = zenMergeEngine.undo(_zenMergeState.value)
    }

    fun resetZenMerge() {
        hapticHelper.click()
        val best = _zenMergeState.value.bestScore
        _zenMergeState.value = zenMergeEngine.createInitialState(best)
    }

    private fun saveZenMergeRecord(score: Int, moves: Int) {
        viewModelScope.launch {
            repository.saveRecord(
                GameRecord(
                    gameMode = "zen_merge",
                    score = score,
                    moves = moves,
                    completed = false
                )
            )
        }
    }

    // ==========================================
    // ECHO RESONANCE ACTIONS
    // ==========================================

    fun startEchoGame(isFreePlay: Boolean = false) {
        hapticHelper.click()
        if (isFreePlay) {
            _echoState.value = EchoPlayState(
                isFreePlay = true,
                state = EchoGameState.PLAYER_TURN
            )
            return
        }

        val best = _echoState.value.bestScore
        val initialState = echoEngine.startNewGame(bestScore = best)
        _echoState.value = initialState
        playEchoDemoSequence(initialState.sequence)
    }

    fun onEchoNodeTap(nodeId: Int) {
        val current = _echoState.value
        val crystal = EchoCrystals.getOrNull(nodeId) ?: return

        // Audio & visual feedback for tap
        audioEngine.playTone(crystal.noteFreq, durationMs = 350, volume = 0.65f)
        hapticHelper.click()

        // Flash node briefly
        viewModelScope.launch {
            _echoState.value = _echoState.value.copy(activeNodeGlow = nodeId)
            delay(220)
            if (_echoState.value.activeNodeGlow == nodeId) {
                _echoState.value = _echoState.value.copy(activeNodeGlow = null)
            }
        }

        if (current.isFreePlay) return

        val (nextState, success) = echoEngine.handlePlayerTap(nodeId, current)
        _echoState.value = nextState

        if (!success && nextState.state == EchoGameState.GAME_OVER) {
            hapticHelper.pulse()
            audioEngine.playMismatch()
            saveEchoRecord(nextState.score)
        } else if (nextState.state == EchoGameState.ROUND_SUCCESS) {
            hapticHelper.victory()
            audioEngine.playVictory()
            viewModelScope.launch {
                delay(600)
                val advanced = echoEngine.advanceRound(_echoState.value)
                _echoState.value = advanced
                delay(400)
                playEchoDemoSequence(advanced.sequence)
            }
        }
    }

    private fun playEchoDemoSequence(sequence: List<Int>) {
        viewModelScope.launch {
            _echoState.value = _echoState.value.copy(state = EchoGameState.DEMO_PLAYING)
            delay(500)
            for (nodeId in sequence) {
                val crystal = EchoCrystals.getOrNull(nodeId)
                if (crystal != null) {
                    _echoState.value = _echoState.value.copy(activeNodeGlow = nodeId)
                    audioEngine.playTone(crystal.noteFreq, durationMs = 400, volume = 0.7f)
                    hapticHelper.click()
                    delay(450)
                    _echoState.value = _echoState.value.copy(activeNodeGlow = null)
                    delay(120)
                }
            }
            _echoState.value = _echoState.value.copy(
                state = EchoGameState.PLAYER_TURN,
                activeNodeGlow = null
            )
        }
    }

    private fun saveEchoRecord(score: Int) {
        viewModelScope.launch {
            repository.saveRecord(
                GameRecord(
                    gameMode = "echo",
                    score = score,
                    completed = true
                )
            )
        }
    }
}
