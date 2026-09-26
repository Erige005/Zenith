package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.LuminaGameState
import com.example.games.lumina.CellType
import com.example.games.lumina.Direction
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.IndigoNeon

@Composable
fun LuminaScreen(
    state: LuminaGameState,
    onCellClick: (Int, Int) -> Unit,
    onReset: () -> Unit,
    onNextLevel: () -> Unit,
    onSelectLevel: (Int) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("lumina_screen_container")
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("lumina_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "LUMINA PRISM",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        ),
                        color = CyanNeon
                    )
                    Text(
                        text = "Level ${state.levelIndex + 1}: ${state.level.name}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = onReset,
                    modifier = Modifier.testTag("lumina_reset_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset Level",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Level Selector Horizontal Bar
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(8) { idx ->
                    val isCurrent = idx == state.levelIndex
                    Surface(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .clickable { onSelectLevel(idx) }
                            .testTag("lumina_level_chip_$idx"),
                        shape = CircleShape,
                        color = if (isCurrent) CyanNeon else MaterialTheme.colorScheme.surfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isCurrent) CyanNeon else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                        )
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "${idx + 1}",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (isCurrent) Color.Black else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // Stats info bar (Targets / Moves)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "TARGETS: ${state.hitTargets.size} / ${state.totalTargets}",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = if (state.hitTargets.size == state.totalTargets && state.totalTargets > 0) EmeraldNeon else CyanNeon
                )
                Text(
                    text = "MOVES: ${state.moves}",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // The Laser Canvas Interactive Grid
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(
                        1.dp,
                        CyanNeon.copy(alpha = 0.3f),
                        RoundedCornerShape(20.dp)
                    )
                    .testTag("lumina_grid_canvas")
            ) {
                val boardSize = maxWidth
                val rows = state.level.rows
                val cols = state.level.cols
                val gridBgColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                val gridLineColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                val targetColor = EmeraldNeon
                val laserColor = CyanNeon
                val movableBorderColor = IndigoNeon

                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(rows, cols) {
                            detectTapGestures { offset ->
                                val cellW = size.width / cols
                                val cellH = size.height / rows
                                val c = (offset.x / cellW).toInt().coerceIn(0, cols - 1)
                                val r = (offset.y / cellH).toInt().coerceIn(0, rows - 1)
                                onCellClick(r, c)
                            }
                        }
                ) {
                    val cellWidth = size.width / cols
                    val cellHeight = size.height / rows

                    // 1. Draw Grid lines
                    for (r in 0..rows) {
                        drawLine(
                            color = gridLineColor,
                            start = Offset(0f, r * cellHeight),
                            end = Offset(size.width, r * cellHeight),
                            strokeWidth = 1f
                        )
                    }
                    for (c in 0..cols) {
                        drawLine(
                            color = gridLineColor,
                            start = Offset(c * cellWidth, 0f),
                            end = Offset(c * cellWidth, size.height),
                            strokeWidth = 1f
                        )
                    }

                    // 2. Draw Cells (Backgrounds, Blocks, Targets, Emitters, Mirrors)
                    state.cells.values.forEach { cell ->
                        val left = cell.col * cellWidth
                        val top = cell.row * cellHeight
                        val centerX = left + cellWidth / 2f
                        val centerY = top + cellHeight / 2f
                        val inset = 6.dp.toPx()

                        // Movable indicator glow
                        if (cell.isMovable) {
                            drawRoundRect(
                                color = movableBorderColor.copy(alpha = 0.15f),
                                topLeft = Offset(left + 2.dp.toPx(), top + 2.dp.toPx()),
                                size = androidx.compose.ui.geometry.Size(
                                    cellWidth - 4.dp.toPx(),
                                    cellHeight - 4.dp.toPx()
                                ),
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx())
                            )
                        }

                        when (cell.type) {
                            CellType.BLOCK -> {
                                drawRoundRect(
                                    color = Color(0xFF1E293B),
                                    topLeft = Offset(left + inset, top + inset),
                                    size = androidx.compose.ui.geometry.Size(
                                        cellWidth - 2 * inset,
                                        cellHeight - 2 * inset
                                    ),
                                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(8.dp.toPx())
                                )
                            }
                            CellType.TARGET -> {
                                val isHit = (cell.row to cell.col) in state.hitTargets
                                val fillColor = if (isHit) targetColor else Color(0xFF475569)

                                // Pulsing gem diamond
                                val path = Path().apply {
                                    moveTo(centerX, top + inset)
                                    lineTo(left + cellWidth - inset, centerY)
                                    lineTo(centerX, top + cellHeight - inset)
                                    lineTo(left + inset, centerY)
                                    close()
                                }
                                if (isHit) {
                                    // Outer radiance
                                    drawCircle(
                                        color = targetColor.copy(alpha = 0.35f),
                                        radius = cellWidth * 0.45f,
                                        center = Offset(centerX, centerY)
                                    )
                                }
                                drawPath(path, color = fillColor)
                                drawCircle(
                                    color = Color.White,
                                    radius = 3.dp.toPx(),
                                    center = Offset(centerX, centerY)
                                )
                            }
                            CellType.EMITTER -> {
                                // Source canon
                                drawCircle(
                                    color = laserColor.copy(alpha = 0.3f),
                                    radius = cellWidth * 0.4f,
                                    center = Offset(centerX, centerY)
                                )
                                drawCircle(
                                    color = laserColor,
                                    radius = cellWidth * 0.22f,
                                    center = Offset(centerX, centerY)
                                )
                                drawCircle(
                                    color = Color.White,
                                    radius = cellWidth * 0.1f,
                                    center = Offset(centerX, centerY)
                                )
                            }
                            CellType.MIRROR_FORWARD -> {
                                // '/' mirror from bottom-left to top-right
                                val mInset = 12.dp.toPx()
                                val start = Offset(left + mInset, top + cellHeight - mInset)
                                val end = Offset(left + cellWidth - mInset, top + mInset)

                                // Soft mirror glass line + silver rim
                                drawLine(
                                    color = Color.White.copy(alpha = 0.3f),
                                    start = start,
                                    end = end,
                                    strokeWidth = 9.dp.toPx(),
                                    cap = StrokeCap.Round
                                )
                                drawLine(
                                    color = CyanNeon,
                                    start = start,
                                    end = end,
                                    strokeWidth = 4.dp.toPx(),
                                    cap = StrokeCap.Round
                                )
                            }
                            CellType.MIRROR_BACKWARD -> {
                                // '\' mirror from top-left to bottom-right
                                val mInset = 12.dp.toPx()
                                val start = Offset(left + mInset, top + mInset)
                                val end = Offset(left + cellWidth - mInset, top + cellHeight - mInset)

                                drawLine(
                                    color = Color.White.copy(alpha = 0.3f),
                                    start = start,
                                    end = end,
                                    strokeWidth = 9.dp.toPx(),
                                    cap = StrokeCap.Round
                                )
                                drawLine(
                                    color = IndigoNeon,
                                    start = start,
                                    end = end,
                                    strokeWidth = 4.dp.toPx(),
                                    cap = StrokeCap.Round
                                )
                            }
                            CellType.SPLITTER -> {
                                // '+' prism splitter
                                val pInset = 14.dp.toPx()
                                drawCircle(
                                    color = Color.White.copy(alpha = 0.15f),
                                    radius = cellWidth * 0.35f,
                                    center = Offset(centerX, centerY)
                                )
                                drawLine(
                                    color = Color.White,
                                    start = Offset(centerX, top + pInset),
                                    end = Offset(centerX, top + cellHeight - pInset),
                                    strokeWidth = 3.dp.toPx(),
                                    cap = StrokeCap.Round
                                )
                                drawLine(
                                    color = Color.White,
                                    start = Offset(left + pInset, centerY),
                                    end = Offset(left + cellWidth - pInset, centerY),
                                    strokeWidth = 3.dp.toPx(),
                                    cap = StrokeCap.Round
                                )
                            }
                            CellType.EMPTY -> {
                                if (cell.isMovable) {
                                    // Soft dashed dot in center showing rotatable cell
                                    drawCircle(
                                        color = movableBorderColor.copy(alpha = 0.5f),
                                        radius = 3.5.dp.toPx(),
                                        center = Offset(centerX, centerY)
                                    )
                                }
                            }
                        }
                    }

                    // 3. Draw Laser Rays (with bloom effect)
                    state.laserSegments.forEach { seg ->
                        val start = Offset(seg.startX * cellWidth, seg.startY * cellHeight)
                        val end = Offset(seg.endX * cellWidth, seg.endY * cellHeight)

                        // Outer radiant bloom
                        drawLine(
                            color = CyanNeon.copy(alpha = 0.35f),
                            start = start,
                            end = end,
                            strokeWidth = 9.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                        // Core laser beam
                        drawLine(
                            color = Color.White,
                            start = start,
                            end = end,
                            strokeWidth = 2.8.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Instructions / Tips
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TIP: Tap highlighted cells with dots to rotate optical mirrors ( / and \\ ) and direct the light to all crystal gems.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Victory Dialog
        if (state.isSolved) {
            AlertDialog(
                onDismissRequest = {},
                confirmButton = {
                    Button(
                        onClick = onNextLevel,
                        colors = ButtonDefaults.buttonColors(containerColor = CyanNeon),
                        modifier = Modifier.testTag("lumina_next_level_button")
                    ) {
                        Text("Next Puzzle", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = onReset) {
                        Text("Replay")
                    }
                },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = EmeraldNeon,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Level Complete!",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                },
                text = {
                    Column {
                        Text(
                            text = "Harmony restored in ${state.moves} moves.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Score: ${maxOf(100, 1000 - state.moves * 40)} pts",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = CyanNeon
                        )
                    }
                }
            )
        }
    }
}
