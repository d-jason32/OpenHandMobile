package com.example.squares

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.floor
import kotlin.math.max

@Composable
fun Squares(
    // Default parameters
    modifier: Modifier = Modifier.fillMaxSize(),
    speed: Float = 60f,
    // color is set to allow the top elements to show through
    borderColor: Color = Color(0xFF262525),
    // * the square size of each square will be set to equal 40 dp units
    squareSize: Dp = 40.dp
) {
    // Size of the pixel is determined to be set to the density of the currant screen
    val squareSizePx = with(LocalDensity.current) { squareSize.toPx() }

    var gridOffsetX by remember { mutableStateOf(0f) }
    // It will be moving towards the right so it y value is not needed.
    val gridOffsetY = 0f

    // we need to use this launched effect as a way for it to run in the background controlled
        // by compose
    LaunchedEffect(speed, squareSizePx) {
        // stores the last timestamp of the frame and we use this to determine how much
        // time has passed in between frames
        var last = 0L
        while (true) {
            withFrameNanos { now ->
                // first frame
                if (last == 0L) { last = now; return@withFrameNanos }


                // determine the last time between frames
                val dt = (now - last) / 1_000_000_000f
                last = now

                val s = max(speed, 0.1f)
                fun wrap(v: Float) = (v % squareSizePx + squareSizePx) % squareSizePx
                gridOffsetX = wrap(gridOffsetX - s * dt)
            }
        }
    }

    // A canvass needed for a drawable surface
    // This will draw the grid pattern
    Canvas(
        modifier = modifier.graphicsLayer { clip = false }
    ) {

        val strokeWidth = 1.dp.toPx()
        val pad = strokeWidth / 2f
        val cell = squareSizePx

        // determine the x and y value the line should be drawn
        val startX = floor((gridOffsetX - pad) / cell) * cell - cell
        val startY = floor((gridOffsetY - pad) / cell) * cell - cell

        // knowing the width we know that where the next line should be to end the box
        val endW = size.width + cell * 2
        val endH = size.height + cell * 2

        var x = startX
        while (x < endW) {
            var y = startY
            while (y < endH) {
                val drawX = x - (gridOffsetX % cell) + pad
                val drawY = y - (gridOffsetY % cell) + pad

                // rectengle is drawn on the canvas
                drawRect(
                    color = borderColor,
                    topLeft = Offset(drawX, drawY),
                    size = Size(cell - strokeWidth, cell - strokeWidth),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Square)
                )
                y += cell
            }
            x += cell
        }
    }
}
