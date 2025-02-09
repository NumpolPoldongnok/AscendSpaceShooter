package org.numpol.spaceshooter

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.numpol.spaceshooter.presentation.FinalSpaceShooterGame
import org.numpol.spaceshooter.presentation.FinalSpaceShooterGameImproved
import org.numpol.spaceshooter.presentation.Step2_DraggablePlayer
import org.numpol.spaceshooter.presentation.Step3_ScreenSizeAndDraggablePlayer
import org.numpol.spaceshooter.presentation.Step4_EnemyMovementAndSpawn
import org.numpol.spaceshooter.presentation.Step5_BulletFiring
import org.numpol.spaceshooter.presentation.Step6_ImageBasedDrawing

@Composable
fun App() {
    MaterialTheme {
        Surface(color = Color.Black) {
            // ถ้า code ส่วนใดเป็นสีแดง ให้นำเม้าไปวางจะมี popup ให้เลือก import กดเพื่อ import package อัตโนมัติ
//            Step1_DrawPlayerAndEnemy()
//            Step2_DraggablePlayer()
//            Step3_ScreenSizeAndDraggablePlayer()
//            Step4_EnemyMovementAndSpawn()
//            Step5_BulletFiring()
//            Step6_ImageBasedDrawing()
            FinalSpaceShooterGame()
//            FinalSpaceShooterGameImproved()
        }
    }
}



