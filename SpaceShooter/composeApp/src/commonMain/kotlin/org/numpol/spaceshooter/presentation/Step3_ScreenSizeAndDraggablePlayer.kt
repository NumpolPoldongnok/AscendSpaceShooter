package org.numpol.spaceshooter.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize

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