package org.numpol.spaceshooter.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

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