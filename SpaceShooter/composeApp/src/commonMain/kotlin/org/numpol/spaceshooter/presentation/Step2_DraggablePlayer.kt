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