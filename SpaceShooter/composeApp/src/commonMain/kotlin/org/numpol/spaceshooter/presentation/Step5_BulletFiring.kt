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


// Step 5: เพิ่มการยิงลูกกระสุนจากผู้เล่น (เคลื่อนที่ขึ้นด้านบน)
// (รวมโค้ดจาก Step 4 + เพิ่มการจัดการลูกกระสุน)

data class Bullet(
    var x: Float,
    var y: Float,
    val radius: Float = 5f
)

@Composable
fun Step5_BulletFiring() {
    var gameSize by remember { mutableStateOf(IntSize(400, 600)) }
    val gameWidth = gameSize.width.toFloat()
    val gameHeight = gameSize.height.toFloat()
    var player by remember { mutableStateOf(Player(x = gameWidth / 2, y = gameHeight - 50f)) }
    val enemies = remember { mutableStateListOf<Enemy>() }
    val bullets = remember { mutableStateListOf<Bullet>() }
    var gameTime by remember { mutableStateOf(0L) }

    LaunchedEffect(gameWidth, gameHeight) {
        var lastFrameTime = 0L
        while (true) {
            val frameTime = withFrameMillis { it }
            if (lastFrameTime == 0L) lastFrameTime = frameTime
            val deltaTime = frameTime - lastFrameTime
            lastFrameTime = frameTime
            gameTime += deltaTime

            // อัปเดตตำแหน่งศัตรู
            enemies.forEach { it.y += 3f }
            enemies.removeAll { it.y > gameHeight }
            if (gameTime % 1000L < deltaTime) {
                val enemyX = Random.nextFloat() * (gameWidth - 40f) + 20f
                enemies.add(Enemy(x = enemyX, y = 0f))
            }

            // อัปเดตตำแหน่งลูกกระสุนให้เคลื่อนที่ขึ้นด้านบน
            bullets.forEach { it.y -= 10f }
            bullets.removeAll { it.y < 0f }
            // ยิงลูกกระสุนทุก 500 มิลลิวินาที เมื่อมีศัตรูอยู่บนหน้าจอ
            if (enemies.isNotEmpty() && gameTime % 500L < deltaTime) {
                bullets.add(Bullet(x = player.x, y = player.y))
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
            // วาดศัตรู
            enemies.forEach { enemy ->
                drawRect(
                    color = Color.Red,
                    topLeft = Offset(enemy.x - enemy.size / 2, enemy.y - enemy.size / 2),
                    size = Size(enemy.size, enemy.size)
                )
            }
            // วาดลูกกระสุน
            bullets.forEach { bullet ->
                drawCircle(
                    color = Color.Yellow,
                    radius = bullet.radius,
                    center = Offset(bullet.x, bullet.y)
                )
            }
        }
    }
}