package com.example.games.echo

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

data class CrystalNode(
    val id: Int,
    val name: String,
    val color: Color,
    val noteFreq: Float,
    val noteName: String
)

val EchoCrystals = listOf(
    CrystalNode(0, "Celeste", Color(0xFF38BDF8), 261.63f, "C"),
    CrystalNode(1, "Jade", Color(0xFF34D399), 329.63f, "E"),
    CrystalNode(2, "Amethyst", Color(0xFFA855F7), 392.00f, "G"),
    CrystalNode(3, "Amber", Color(0xFFFBBF24), 523.25f, "C+"),
    CrystalNode(4, "Rose", Color(0xFFFB7185), 659.25f, "E+"),
    CrystalNode(5, "Cobalt", Color(0xFF818CF8), 783.99f, "G+")
)

enum class EchoGameState {
    IDLE,
    DEMO_PLAYING,
    PLAYER_TURN,
    ROUND_SUCCESS,
    GAME_OVER
}

data class EchoPlayState(
    val sequence: List<Int> = emptyList(),
    val playerStep: Int = 0,
    val score: Int = 0,
    val bestScore: Int = 0,
    val state: EchoGameState = EchoGameState.IDLE,
    val activeNodeGlow: Int? = null,
    val isFreePlay: Boolean = false
)

class EchoResonanceEngine {
    fun startNewGame(nodeCount: Int = 4, bestScore: Int = 0): EchoPlayState {
        val firstNode = Random.nextInt(nodeCount)
        return EchoPlayState(
            sequence = listOf(firstNode),
            playerStep = 0,
            score = 0,
            bestScore = bestScore,
            state = EchoGameState.DEMO_PLAYING,
            activeNodeGlow = null,
            isFreePlay = false
        )
    }

    fun advanceRound(currentState: EchoPlayState, nodeCount: Int = 4): EchoPlayState {
        val nextNode = Random.nextInt(nodeCount)
        val newSequence = currentState.sequence + nextNode
        val newScore = currentState.score + 1
        return currentState.copy(
            sequence = newSequence,
            playerStep = 0,
            score = newScore,
            bestScore = maxOf(currentState.bestScore, newScore),
            state = EchoGameState.DEMO_PLAYING,
            activeNodeGlow = null
        )
    }

    fun handlePlayerTap(nodeId: Int, currentState: EchoPlayState): Pair<EchoPlayState, Boolean> {
        if (currentState.isFreePlay) {
            return currentState to true
        }

        if (currentState.state != EchoGameState.PLAYER_TURN) {
            return currentState to false
        }

        val expected = currentState.sequence.getOrNull(currentState.playerStep)
        if (expected == null || expected != nodeId) {
            // Mismatch
            return currentState.copy(state = EchoGameState.GAME_OVER) to false
        }

        val nextStep = currentState.playerStep + 1
        if (nextStep >= currentState.sequence.size) {
            // Completed current round sequence!
            return currentState.copy(
                playerStep = nextStep,
                state = EchoGameState.ROUND_SUCCESS
            ) to true
        }

        return currentState.copy(playerStep = nextStep) to true
    }
}
