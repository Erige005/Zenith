package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

/**
 * Procedural synthesizer for peaceful, crystalline chime tones and zen resonances.
 * Generates pure sine waves with gentle attack and exponential decay to prevent clicks.
 */
class ZenithAudioEngine {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    var isSoundEnabled: Boolean = true

    // Note frequencies (Hz) in C Major Pentatonic & Ambient scales
    object Notes {
        const val C4 = 261.63f
        const val D4 = 293.66f
        const val E4 = 329.63f
        const val G4 = 392.00f
        const val A4 = 440.00f
        const val B4 = 493.88f
        const val C5 = 523.25f
        const val D5 = 587.33f
        const val E5 = 659.25f
        const val G5 = 783.99f
        const val A5 = 880.00f
        const val C6 = 1046.50f
    }

    fun playTone(freq: Float, durationMs: Int = 300, volume: Float = 0.5f) {
        if (!isSoundEnabled) return
        scope.launch {
            try {
                synthesizeTone(freq, durationMs, volume)
            } catch (_: Exception) {
                // Ignore audio hardware transients
            }
        }
    }

    fun playChime(index: Int) {
        val scale = listOf(
            Notes.C4, Notes.D4, Notes.E4, Notes.G4,
            Notes.A4, Notes.C5, Notes.D5, Notes.E5, Notes.G5
        )
        val freq = scale.getOrElse(index % scale.size) { Notes.C5 }
        playTone(freq, durationMs = 380, volume = 0.6f)
    }

    fun playMerge(tier: Int) {
        val scale = listOf(
            Notes.C4, Notes.E4, Notes.G4, Notes.B4,
            Notes.C5, Notes.E5, Notes.G5, Notes.A5, Notes.C6
        )
        val freq = scale.getOrElse((tier - 1).coerceAtLeast(0) % scale.size) { Notes.G5 }
        playTone(freq, durationMs = 280, volume = 0.55f)
    }

    fun playLaserReflect() {
        playTone(Notes.A5, durationMs = 120, volume = 0.35f)
    }

    fun playVictory() {
        scope.launch {
            val chord = listOf(Notes.C5, Notes.E5, Notes.G5, Notes.C6)
            for (freq in chord) {
                synthesizeTone(freq, durationMs = 450, volume = 0.5f)
                kotlinx.coroutines.delay(100)
            }
        }
    }

    fun playEchoNote(stepIndex: Int) {
        val echoPitches = listOf(Notes.C4, Notes.E4, Notes.G4, Notes.C5, Notes.E5, Notes.G5)
        val freq = echoPitches.getOrElse(stepIndex % echoPitches.size) { Notes.C5 }
        playTone(freq, durationMs = 420, volume = 0.7f)
    }

    fun playMismatch() {
        playTone(180f, durationMs = 250, volume = 0.3f)
    }

    private fun synthesizeTone(freq: Float, durationMs: Int, volume: Float) {
        val sampleRate = 22050
        val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
        val buffer = ShortArray(numSamples)

        val attackSamples = (sampleRate * 0.015).toInt() // 15ms gentle attack

        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            // Sine wave with slight 2nd harmonic overtone for warm bell timbre
            val wave = 0.85 * sin(2.0 * PI * freq * t) + 0.15 * sin(4.0 * PI * freq * t)
            
            // Envelope: Attack ramp + Exponential decay
            val envelope = when {
                i < attackSamples -> i.toDouble() / attackSamples
                else -> exp(-4.5 * (i - attackSamples) / numSamples)
            }

            val sample = (wave * envelope * volume * Short.MAX_VALUE).toInt()
            buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }

        val minBufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        val track = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(buffer.size * 2.coerceAtLeast(minBufferSize))
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()

        track.write(buffer, 0, buffer.size)
        track.play()

        // Clean up track after tone finishes
        scope.launch {
            kotlinx.coroutines.delay(durationMs.toLong() + 50)
            try {
                track.stop()
                track.release()
            } catch (_: Exception) {}
        }
    }
}
