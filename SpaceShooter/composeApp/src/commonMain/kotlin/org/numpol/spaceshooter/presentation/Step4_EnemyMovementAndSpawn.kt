package org.numpol.spaceshooter.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import kotlin.random.Random

// Step 4: สร้างและเคลื่อนที่ของศัตรู
// (รวมโค้ดจาก Step 3 + เพิ่ม game loop สำหรับการเคลื่อนที่และ Spawn ศัตรู)
// สร้าง data class สำหรับศัตรู

data class Enemy(
    var x: Float,
    var y: Float,
    val size: Float = 40f
)

@Composable
fun Step4_EnemyMovementAndSpawn() {
    var gameSize by remember { mutableStateOf(IntSize(400, 600)) }
    val gameWidth = gameSize.width.toFloat()
    val gameHeight = gameSize.height.toFloat()
    var player by remember { mutableStateOf(Player(x = gameWidth / 2, y = gameHeight - 50f)) }
    val enemies = remember { mutableStateListOf<Enemy>() }
    var gameTime by remember { mutableStateOf(0L) }

    LaunchedEffect(gameWidth, gameHeight) {
        var lastFrameTime = 0L
        while (true) {
            val frameTime = withFrameMillis { it }
            if (lastFrameTime == 0L) lastFrameTime = frameTime
            val deltaTime = frameTime - lastFrameTime
            lastFrameTime = frameTime
            gameTime += deltaTime

            // เคลื่อนที่ศัตรูลงด้านล่าง
            enemies.forEach { it.y += 3f }
            enemies.removeAll { it.y > gameHeight }
            // Spawn ศัตรูใหม่ทุก 1 วินาที
            if (gameTime % 1000L < deltaTime) {
                val enemyX = Random.nextFloat() * (gameWidth - 40f) + 20f
                enemies.add(Enemy(x = enemyX, y = 0f))
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { coordinates ->
                gameSize = coordinates.size
                player = player.copy(x = gameSize.width / 2f, y = gameSize.height - 50f)
            }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    player = player.copy(
                        x = (player.x + dragAmount.x).coerceIn(0f, gameWidth)
                    )
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // วาดผู้เล่น
            drawCircle(
                color = Color.Cyan,
                radius = 20f,
                center = Offset(player.x, player.y)
            )
            // วาดศัตรูที่เคลื่อนที่ลงด้านล่าง
            enemies.forEach { enemy ->
                drawRect(
                    color = Color.Red,
                    topLeft = Offset(enemy.x - enemy.size / 2, enemy.y - enemy.size / 2),
                    size = Size(enemy.size, enemy.size)
                )
            }
        }
    }
}