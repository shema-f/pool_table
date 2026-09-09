# 🎱 Fiesta Pool Table

An original, polished multiplayer billiards platform built for Android in **Kotlin** and **Jetpack Compose**.

## 🚀 Features

- **Realistic Physics & Ball Motion**: Custom 2D fixed-timestep physics engine supporting elastic collisions, cushion rail bouncing, surface rolling friction, ball spin, and pocket detection.
- **UK 8-Ball & Standard Rules**: Complete rules engine governing turns, yellow/red ball group assignments, fouls, scratches, ball-in-hand, and 8-ball victory conditions.
- **Immersive Touch Controls**: 
  - Smooth table touch drag aiming with continuous cue stick rotation.
  - Visible wooden cue stick and dashed aiming trajectory guide.
  - Interactive side power/energy column meter.
  - Dedicated **[ ⚡ SHOOT ]** action button.
- **AI Opponents**: Play against intelligent AI opponents that evaluate table positions and execute shots using the same physical simulation.
- **Player Profiles & Statistics**: Track level, trophies, wins, win rates, and play-style profiles (Aggression, Defense, Trick Shots, Risk).
- **Clean Modular Architecture**: Clean separation between UI, ViewModels, Game State, Physics simulation, Rules engine, and Network transport abstractions.

---

## 🛠️ Technology Stack

- **Language**: Kotlin
- **UI Toolkit**: Jetpack Compose & Material 3
- **Rendering**: Custom Canvas 2D Game Rendering Layer
- **Architecture**: Clean Architecture / Unidirectional Data Flow

---

## 📱 Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/shema-f/pool_table.git
   ```
2. Open the project in **Android Studio**.
3. Sync Gradle and run the app on an Android emulator or physical device.

---

## 📦 Repository

[GitHub Repository](https://github.com/shema-f/pool_table.git)
