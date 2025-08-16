package app.ninesevennine.twofactorauthenticator.features.qrscanner

import android.graphics.BlendMode
import android.graphics.Paint
import android.graphics.RectF
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun QRScannerOverlay(
    modifier: Modifier = Modifier,
    viewfinderWidthPercent: Float = 0.75f,
    cornerRadius: Dp = 8.dp
) {
    val density = LocalDensity.current
    val cornerRadiusPx = with(density) { cornerRadius.toPx() }
    val strokeWidthPx = with(density) { 4.dp.toPx() }

    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        // Calculate viewfinder size based on minimum dimension
        val minDimension = minOf(canvasWidth, canvasHeight)
        val viewfinderSize = minDimension * viewfinderWidthPercent

        val left = (canvasWidth - viewfinderSize) / 2
        val top = (canvasHeight - viewfinderSize) / 2
        val rect = RectF(left, top, left + viewfinderSize, top + viewfinderSize)

        // Create native paint objects
        val scrimPaint = Paint().apply {
            color = Color(0x99000000).toArgb()
        }

        val eraserPaint = Paint().apply {
            strokeWidth = strokeWidthPx
            blendMode = BlendMode.CLEAR
        }

        val borderPaint = Paint().apply {
            color = Color.White.toArgb()
            style = Paint.Style.STROKE
            strokeWidth = strokeWidthPx
        }

        // Draw using native canvas for performance
        drawContext.canvas.nativeCanvas.apply {
            // Draw scrim background
            drawRect(0f, 0f, canvasWidth, canvasHeight, scrimPaint)

            // Clear viewfinder area
            eraserPaint.style = Paint.Style.FILL
            drawRoundRect(rect, cornerRadiusPx, cornerRadiusPx, eraserPaint)

            // Draw viewfinder border
            drawRoundRect(rect, cornerRadiusPx, cornerRadiusPx, borderPaint)
        }
    }
}