package org.numpol.spaceshooter.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.imageResource
import org.numpol.spaceshooter.utils.drawScaledImage
import spaceshooter.composeapp.generated.resources.Res
import spaceshooter.composeapp.generated.resources.enemy01
import spaceshooter.composeapp.generated.resources.player01
import kotlin.random.Random

@Composable
fun FinalSpaceShooterGameImproved() {
    var gameSize by remember { mutableStateOf(IntSize(400, 600)) }
    val gameWidth = gameSize.width.toFloat()
    val gameHeight = gameSize.height.toFloat()

    var player by remember { mutableStateOf(Player(x = gameWidth / 2, y = gameHeight - 50f)) }
    val enemies = remember { mutableStateListOf<Enemy>() }
    val bullets = remember { mutableStateListOf<Bullet>() }
    var gameTime by remember { mutableStateOf(0L) }
    var score by remember { mutableStateOf(0) }
    var gameOver by remember { mutableStateOf(false) }

    // กำหนดขนาดเป้าหมายสำหรับภาพ
    val playerTargetWidth = 150f
    val playerTargetHeight = 150f
    val enemyTargetWidth = 140f
    val enemyTargetHeight = 140f

    // โหลดภาพสำหรับผู้เล่นและศัตรู
    val playerImage: ImageBitmap = imageResource(Res.drawable.player01)
    val enemyImage: ImageBitmap = imageResource(Res.drawable.enemy01)

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
                val enemyX =
                    Random.nextFloat() * (gameWidth - enemyTargetWidth) + enemyTargetWidth / 2
                enemies.add(Enemy(x = enemyX, y = 0f))
            }

            // อัปเดตตำแหน่งลูกกระสุน
            bullets.forEach { it.y -= 10f }
            bullets.removeAll { it.y < 0f }
            if (enemies.isNotEmpty() && gameTime % 500L < deltaTime) {
                bullets.add(Bullet(x = player.x + playerTargetWidth / 2, y = player.y))
            }

            // --- ตรวจจับการชน: ลูกกระสุน vs. ศัตรู ---
            val collidedEnemies = mutableListOf<Enemy>()
            val collidedBullets = mutableListOf<Bullet>()
            enemies.forEach { enemy ->
                bullets.forEach { bullet ->
                    if (bullet.x in (enemy.x - enemyTargetWidth / 2)..(enemy.x + enemyTargetWidth / 2) &&
                        bullet.y in (enemy.y - enemyTargetHeight / 2)..(enemy.y + enemyTargetHeight / 2)
                    ) {
                        collidedEnemies.add(enemy)
                        collidedBullets.add(bullet)
                        score++ // เพิ่มคะแนนเมื่อยิงศัตรูได้
                    }
                }
            }
            enemies.removeAll(collidedEnemies)
            bullets.removeAll(collidedBullets)

            // --- ตรวจจับการชน: ศัตรู vs. ผู้เล่น ---
            enemies.forEach { enemy ->
                if (enemy.x in (player.x - playerTargetWidth / 2)..(player.x + playerTargetWidth / 2) &&
                    enemy.y in (player.y - playerTargetHeight / 2)..(player.y + playerTargetHeight / 2)
                ) {
                    gameOver = true
                }
            }

            // --- เมื่อเกิด Game Over: แสดงข้อความและรีเซ็ตเกม ---
            if (gameOver) {
                delay(2000L)  // รอ 2 วินาที
                score = 0
                enemies.clear()
                bullets.clear()
                gameTime = 0L
                player = player.copy(x = gameWidth / 2, y = gameHeight - 150f)
                gameOver = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .onGloballyPositioned { coordinates ->
                gameSize = coordinates.size
                player = player.copy(x = gameSize.width / 2f, y = gameSize.height - 150f)
            }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    player = player.copy(
                        x = (player.x + dragAmount.x).coerceIn(0f, gameWidth)
                    )
                }
            }, content = GameCanvas(
            gameOver,
            score,
            playerImage,
            player,
            playerTargetWidth,
            playerTargetHeight,
            enemies,
            enemyImage,
            enemyTargetWidth,
            enemyTargetHeight,
            bullets,
                gameTime
        )
    )
}

private fun GameCanvas(
    gameOver: Boolean,
    score: Int,
    playerImage: ImageBitmap,
    player: Player,
    playerTargetWidth: Float,
    playerTargetHeight: Float,
    enemies: SnapshotStateList<Enemy>,
    enemyImage: ImageBitmap,
    enemyTargetWidth: Float,
    enemyTargetHeight: Float,
    bullets: SnapshotStateList<Bullet>,
    gameTime: Long
): @Composable() (BoxScope.() -> Unit) =
    {
        // แสดงคะแนนที่มุมบนซ้าย หรือข้อความ Game Over ที่กลางหน้าจอ
        if (gameOver) {
            Text(
                text = "Game Over! กำลังรีเซ็ต...",
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            Text(
                text = "Score: $score",
                color = Color.White,
                modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
            )
        }
        // วาดทุกองค์ประกอบของเกมด้วย Canvas และ helper function
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
            // เมื่อเอา gameTime หรือ deltaTime มาเป็นตัวแปรในการ re-compose ปรากฏว่าเกมส์จะลื่นหัวแตก
            // ขอบคุณมากครับ ที่อ่านมาถึงตรงนี้ ขอให้สนุกกับการทำเกมส์นะครับ หรือนำไปประยุกต์ใช้กับ แอพอื่นได้ผลอย่างไรทักมาคุยกันได้นะครับ
            // ส่วนเหตุผลว่าทำไมมี code ชุดนี้แล้วทำให้สมรรถภาพดีขึ้นผมยังไม่่แน่ใจ ถ้าท่านทราบมาบอกกันด้วยนะครับ ข่อมครับ
            drawCircle(
                color = Color.Cyan.copy(alpha = gameTime.toFloat()),
                radius = 1f,
                center = Offset(0f, 0f)
            )
        }
    }