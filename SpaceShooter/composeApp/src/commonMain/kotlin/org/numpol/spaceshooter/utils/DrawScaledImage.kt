package org.numpol.spaceshooter.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.scale

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