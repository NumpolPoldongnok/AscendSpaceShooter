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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import org.jetbrains.compose.resources.imageResource
import org.numpol.spaceshooter.utils.drawScaledImage
import spaceshooter.composeapp.generated.resources.Res
import spaceshooter.composeapp.generated.resources.enemy01
import spaceshooter.composeapp.generated.resources.player01
import kotlin.random.Random

// Step 6: วาดผู้เล่นและศัตรูด้วยภาพโดยใช้ drawScaledImage
// (รวมโค้ดจาก Step 5 + เปลี่ยนการวาดผู้เล่นและศัตรูจากรูปทรงเป็นการแสดงผลด้วยภาพ)

@Composable
fun Step6_ImageBasedDrawing() {
    var gameSize by remember { mutableStateOf(IntSize(400, 600)) }
    val gameWidth = gameSize.width.toFloat()
    val gameHeight = gameSize.height.toFloat()
    var player by remember { mutableStateOf(Player(x = gameWidth / 2, y = gameHeight - 50f)) }
    val enemies = remember { mutableStateListOf<Enemy>() }
    val bullets = remember { mutableStateListOf<Bullet>() }
    var gameTime by remember { mutableStateOf(0L) }

    // โหลดภาพสำหรับผู้เล่นและศัตรู
    val playerImage: ImageBitmap = imageResource(Res.drawable.player01)
    val enemyImage: ImageBitmap = imageResource(Res.drawable.enemy01)
    // กำหนดขนาดเป้าหมายสำหรับภาพ
    val playerTargetWidth = 50f
    val playerTargetHeight = 50f
    val enemyTargetWidth = 40f
    val enemyTargetHeight = 40f

    LaunchedEffect(gameWidth, gameHeight) {
        var lastFrameTime = 0L
        while (true) {
            val frameTime = withFrameMillis { it }
            if (lastFrameTime == 0L) lastFrameTime = frameTime
            val deltaTime = frameTime - lastFrameTime
            lastFrameTime = frameTime
            gameTime += deltaTime

            enemies.forEach { it.y += 3f }
            enemies.removeAll { it.y > gameHeight }
            if (gameTime % 1000L < deltaTime) {
                val enemyX =
                    Random.nextFloat() * (gameWidth - enemyTargetWidth) + enemyTargetWidth / 2
                enemies.add(Enemy(x = enemyX, y = 0f))
            }

            bullets.forEach { it.y -= 10f }
            bullets.removeAll { it.y < 0f }
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
            // วาดผู้เล่นด้วยภาพ
            drawScaledImage(
                image = playerImage,
                x = player.x,
                y = player.y,
                targetWidth = playerTargetWidth,
                targetHeight = playerTargetHeight
            )
            // วาดศัตรูด้วยภาพ
            enemies.forEach { enemy ->
                drawScaledImage(
                    image = enemyImage,
                    x = enemy.x,
                    y = enemy.y,
                    targetWidth = enemyTargetWidth,
                    targetHeight = enemyTargetHeight
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