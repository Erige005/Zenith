# Zenith 💠

> **Minimalist Mind & Puzzle Arcade** — A serene Android puzzle experience crafted with Kotlin and Jetpack Compose.

[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Language](https://img.shields.io/badge/Language-Kotlin-purple.svg)](https://kotlinlang.org)
[![UI](https://img.shields.io/badge/UI-Jetpack%20Compose-blue.svg)](https://developer.android.com/jetpack/compose)
[![Database](https://img.shields.io/badge/Database-Room-orange.svg)](https://developer.android.com/training/data-storage/room)
[![License](https://img.shields.io/badge/License-MIT-lightgrey.svg)](LICENSE)

---

## 🔮 Overview

**Zenith** is a meditative puzzle and brain arcade designed for mindfulness, clarity, and spatial-auditory contemplation. It features three distinct minimalist puzzle disciplines, custom procedural synthesizer audio, tactile haptic feedback, and local progress tracking.

---

## ✨ Features & Disciplines

### 1. 🌈 Lumina Prism (Laser & Optics Puzzle)
- Guide vibrant, radiant laser beams across a 5x5 grid.
- Rotate movable optical mirrors (`/` and `\`) and utilize prism splitters (`+`) to illuminate target gems.
- 8 progressive handcrafted levels with real-time ray-tracing calculations and luminous bloom effects.

### 2. 🔲 Zen Merge (Number Harmony)
- Meditative 4x4 sliding tile game inspired by classic 2048.
- Smooth gesture swipes and accessible D-pad controls.
- Zen pastel palette with harmonic audio chimes on tile collision.
- Built-in **10-move Undo** capability to play without stress.

### 3. 🔔 Echo Resonance (Melodic Memory Mandala)
- 6 sacred crystal bells tuned to harmonic pentatonic frequencies (C4, E4, G4, C5, E5, G5).
- **Challenge Mode**: Memorize and repeat progressive chime sequences.
- **Zen Free Play**: Interactive chime instrument to create peaceful melodies at your own pace.

### 🎵 Procedural Audio & Tactile Haptics
- **100% Offline Audio Engine**: Generates real-time sine wave bell harmonics via Android `AudioTrack` without audio asset latency.
- **Haptic Engine**: Subtle, tactile vibration clicks and pulse waves for every move and milestone.

### 🏆 Hall of Records & Persistence
- Local persistence using **Room Database** and Kotlin Coroutines/Flow.
- Tracks high scores, longest streaks, and completion moves.
- Unlockable achievements celebrating mindful milestones.

### 🌓 Customization
- Seamless **Dark Theme** (deep space neon) and **Light Theme** (clean serenity).
- Individual toggles for sound effects and haptic vibrations.

---

## 🛠 Tech Stack & Architecture

- **Language:** Kotlin 100%
- **UI Toolkit:** Jetpack Compose (Material Design 3)
- **Architecture:** MVVM (Model-View-ViewModel) + Repository Pattern
- **Reactive Streams:** Kotlin Coroutines & `StateFlow`
- **Persistence:** Android Jetpack Room Database (with KSP)
- **Audio:** Real-time synthesis via Android `AudioTrack` API
- **Haptics:** Android `Vibrator` / `VibrationEffect` API

---

## 🚀 Building & Running

1. Clone or download this repository:
   ```bash
   git clone https://github.com/YOUR_USERNAME/Zenith.git
   ```
2. Open the project in **Android Studio Ladybug or newer**.
3. Allow Gradle to sync dependencies.
4. Run on an Android device or emulator running **Android 7.0+ (API 24+)**.

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).
