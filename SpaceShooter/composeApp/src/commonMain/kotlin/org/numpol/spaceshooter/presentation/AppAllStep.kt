package org.numpol.spaceshooter.presentation

/*
@Composable
fun App() {
    MaterialTheme {
        Surface(color = Color.Black) {
            //Step1_DrawPlayerAndEnemy()
            //Step2_DraggablePlayer()
            //Step3_ScreenSizeAndDraggablePlayer()
            //Step4_EnemyMovementAndSpawn()
            //Step5_BulletFiring()
            //Step6_ImageBasedDrawing()
            FinalSpaceShooterGame()
        }
    }
}

// Step 1: วาดผู้เล่นและศัตรูแบบ static

@Composable
fun Step1_DrawPlayerAndEnemy() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        // วาดผู้เล่นเป็นวงกลมสีฟ้า
        drawCircle(
            color = Color.Cyan,
            radius = 20f,
            center = Offset(x = size.width / 2, y = size.height - 50f)
        )
        // วาดศัตรูเป็นสี่เหลี่ยมสีแดง
        val enemySize = 40f
        drawRect(
            color = Color.Red,
            topLeft = Offset(x = size.width / 2 - enemySize / 2, y = 50f),
            size = Size(enemySize, enemySize)
        )
    }
}

// Step 2: เพิ่มการเลื่อนตำแหน่งผู้เล่นด้วยการ Drag
// (รวมโค้ดจาก Step 1 + เพิ่มการจับ gesture)

@Composable
fun Step2_DraggablePlayer() {
    var playerX by remember { mutableStateOf(200f) }
    val playerY = 550f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    playerX += dragAmount.x
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // วาดผู้เล่นที่ตำแหน่งที่อัปเดตจาก playerX
            drawCircle(
                color = Color.Cyan,
                radius = 20f,
                center = Offset(playerX, playerY)
            )
            // วาดศัตรู static เหมือนใน Step 1
            val enemySize = 40f
            drawRect(
                color = Color.Red,
                topLeft = Offset(x = size.width / 2 - enemySize / 2, y = 50f),
                size = Size(enemySize, enemySize)
            )
        }
    }
}

// Step 3: รับและตั้งค่าขนาดหน้าจอด้วย onGloballyPositioned
// (รวมโค้ดจาก Step 2 + จัดการกับขนาดหน้าจอ)
// สร้าง data class สำหรับผู้เล่น

data class Player(val x: Float, val y: Float)

@Composable
fun Step3_ScreenSizeAndDraggablePlayer() {
    var gameSize by remember { mutableStateOf(IntSize(400, 600)) }
    val gameWidth = gameSize.width.toFloat()
    val gameHeight = gameSize.height.toFloat()
    var player by remember { mutableStateOf(Player(x = gameWidth / 2, y = gameHeight - 50f)) }

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
            // วาดผู้เล่นจาก data class Player
            drawCircle(
                color = Color.Cyan,
                radius = 20f,
                center = Offset(player.x, player.y)
            )
            // วาดศัตรู static
            val enemySize = 40f
            drawRect(
                color = Color.Red,
                topLeft = Offset(x = size.width / 2 - enemySize / 2, y = 50f),
                size = Size(enemySize, enemySize)
            )
        }
    }
}

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
                val enemyX = Random.nextFloat() * (gameWidth - enemyTargetWidth) + enemyTargetWidth / 2
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

// Helper function to draw a scaled image
fun DrawScope.drawScaledImage(
    image: ImageBitmap, x: Float, y: Float, targetWidth: Float, targetHeight: Float
) {
    val imageWidth = image.width.toFloat()
    val imageHeight = image.height.toFloat()
    val scaleX = targetWidth / imageWidth
    val scaleY = targetHeight / imageHeight

    drawIntoCanvas { canvas ->
        scale(scaleX, scaleY, pivot = Offset(x, y)) {
            drawImage(
                image = image,
                topLeft = Offset(x - targetWidth / 2, y - targetHeight / 2)
            )
        }
    }
}

@Composable
fun FinalSpaceShooterGame() {
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
    val playerTargetWidth = 50f
    val playerTargetHeight = 50f
    val enemyTargetWidth = 40f
    val enemyTargetHeight = 40f

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
                val enemyX = Random.nextFloat() * (gameWidth - enemyTargetWidth) + enemyTargetWidth / 2
                enemies.add(Enemy(x = enemyX, y = 0f))
            }

            // อัปเดตตำแหน่งลูกกระสุน
            bullets.forEach { it.y -= 10f }
            bullets.removeAll { it.y < 0f }
            if (enemies.isNotEmpty() && gameTime % 500L < deltaTime) {
                bullets.add(Bullet(x = player.x, y = player.y))
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
                kotlinx.coroutines.delay(2000L)  // รอ 2 วินาที
                score = 0
                enemies.clear()
                bullets.clear()
                gameTime = 0L
                player = player.copy(x = gameWidth / 2, y = gameHeight - 50f)
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
        }
    }
}
 */