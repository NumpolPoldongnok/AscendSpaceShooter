# Ascend Red Space Shooter

Ascend Red Space Shooter is a cross-platform retro arcade game built with Compose Multiplatform. Test the performance of Jetpack Compose's native rendering engine as it handles animation, action, and multiple on-screen objects in a fast-paced space shooter game.

## Features

- **Cross-Platform:** Runs on iOS Android, Desktop, and more using Compose Multiplatform.
- **Player Movement:** Drag to move your spaceship horizontally along the bottom of the screen.
- **Enemy Spawning:** Enemies spawn from the top and move downward.
- **Shooting Mechanics:** Bullets are fired from the player upward automatically.
- **Collision Detection:** Score increases when bullets hit enemies; game over when an enemy collides with the player.
- **Game Over & Reset:** When a collision occurs, the game displays a game over message and resets automatically.
- **Retro Graphics:** Uses custom image drawing with a helper function to scale images for a retro look.

## Technologies

- **Kotlin Multiplatform:** Shared game logic across platforms.
- **Jetpack Compose:** For building declarative UIs and animations.
- **Compose Multiplatform Desktop & Android:** Native rendering on multiple targets.

## Getting Started

### Prerequisites

- JDK 11 or later
- IntelliJ IDEA or Android Studio with Kotlin Multiplatform support
- Gradle

### Running the Game

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/ascend-red-space-shooter.git
   cd ascend-red-space-shooter
