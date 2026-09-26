package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games.zenmerge.Tile
import com.example.games.zenmerge.ZenMergeEngine
import com.example.games.zenmerge.ZenMergeState
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.IndigoNeon
import com.example.ui.theme.Tile1024
import com.example.ui.theme.Tile128
import com.example.ui.theme.Tile16
import com.example.ui.theme.Tile2
import com.example.ui.theme.Tile2048
import com.example.ui.theme.Tile256
import com.example.ui.theme.Tile32
import com.example.ui.theme.Tile4
import com.example.ui.theme.Tile512
import com.example.ui.theme.Tile64
import com.example.ui.theme.Tile8
import com.example.ui.theme.TileSuper
import com.example.ui.theme.VioletNeon
import kotlin.math.abs

@Composable
fun ZenMergeScreen(
    state: ZenMergeState,
    canUndo: Boolean,
    onMove: (ZenMergeEngine.Direction) -> Unit,
    onUndo: () -> Unit,
    onReset: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var totalDragX by remember { mutableFloatStateOf(0f) }
    var totalDragY by remember { mutableFloatStateOf(0f) }
    val dragThreshold = 55f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("zen_merge_screen_container")
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
                    modifier = Modifier.testTag("zen_merge_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "ZEN MERGE",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        ),
                        color = IndigoNeon
                    )
                    Text(
                        text = "Glide tiles to reach 2048",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row {
                    IconButton(
                        onClick = onUndo,
                        enabled = canUndo,
                        modifier = Modifier.testTag("zen_merge_undo_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Undo,
                            contentDescription = "Undo",
                            tint = if (canUndo) IndigoNeon else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                        )
                    }
                    IconButton(
                        onClick = onReset,
                        modifier = Modifier.testTag("zen_merge_reset_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "New Game",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Score and Best Score row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ScoreCard(
                    title = "SCORE",
                    value = "${state.score}",
                    accentColor = IndigoNeon,
                    modifier = Modifier.weight(1f)
                )
                ScoreCard(
                    title = "BEST",
                    value = "${state.bestScore}",
                    accentColor = CyanNeon,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // The 4x4 Grid Board with Swipe Gestures
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        RoundedCornerShape(20.dp)
                    )
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = {
                                totalDragX = 0f
                                totalDragY = 0f
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                totalDragX += dragAmount.x
                                totalDragY += dragAmount.y
                            },
                            onDragEnd = {
                                val absX = abs(totalDragX)
                                val absY = abs(totalDragY)
                                if (maxOf(absX, absY) > dragThreshold) {
                                    if (absX > absY) {
                                        if (totalDragX > 0) onMove(ZenMergeEngine.Direction.RIGHT)
                                        else onMove(ZenMergeEngine.Direction.LEFT)
                                    } else {
                                        if (totalDragY > 0) onMove(ZenMergeEngine.Direction.DOWN)
                                        else onMove(ZenMergeEngine.Direction.UP)
                                    }
                                }
                                totalDragX = 0f
                                totalDragY = 0f
                            }
                        )
                    }
                    .padding(10.dp)
                    .testTag("zen_merge_grid_board")
            ) {
                val gridSize = state.grid.size
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (r in 0 until gridSize) {
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            for (c in 0 until gridSize) {
                                val tile = state.grid[r][c]
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    TileView(tile = tile)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Accessible D-Pad Controls (for clicks or alternate navigation)
            Text(
                text = "SWIPE OR USE NAVIGATION KEYS",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                FilledTonalIconButton(
                    onClick = { onMove(ZenMergeEngine.Direction.UP) },
                    modifier = Modifier
                        .size(44.dp)
                        .testTag("zen_merge_up_button")
                ) {
                    Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "Move Up")
                }
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    FilledTonalIconButton(
                        onClick = { onMove(ZenMergeEngine.Direction.LEFT) },
                        modifier = Modifier
                            .size(44.dp)
                            .testTag("zen_merge_left_button")
                    ) {
                        Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Move Left")
                    }
                    FilledTonalIconButton(
                        onClick = { onMove(ZenMergeEngine.Direction.DOWN) },
                        modifier = Modifier
                            .size(44.dp)
                            .testTag("zen_merge_down_button")
                    ) {
                        Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "Move Down")
                    }
                    FilledTonalIconButton(
                        onClick = { onMove(ZenMergeEngine.Direction.RIGHT) },
                        modifier = Modifier
                            .size(44.dp)
                            .testTag("zen_merge_right_button")
                    ) {
                        Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "Move Right")
                    }
                }
            }
        }

        // Game Over Dialog
        if (state.isGameOver) {
            AlertDialog(
                onDismissRequest = {},
                confirmButton = {
                    Button(
                        onClick = onReset,
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoNeon),
                        modifier = Modifier.testTag("zen_merge_game_over_retry_button")
                    ) {
                        Text("Play Again", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = onBack) {
                        Text("Exit to Menu")
                    }
                },
                title = {
                    Text(
                        text = "Peaceful Journey Ended",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                text = {
                    Column {
                        Text(
                            text = "No more moves available. You achieved a score of:",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${state.score} pts (${state.moves} moves)",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = IndigoNeon
                        )
                    }
                }
            )
        }

        // Reached 2048 Celebration Dialog
        if (state.reachedZenith) {
            AlertDialog(
                onDismissRequest = {},
                confirmButton = {
                    Button(
                        onClick = { /* Continue playing */ },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldNeon)
                    ) {
                        Text("Keep Merging", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = EmeraldNeon
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Zenith 2048 Attained!")
                    }
                },
                text = {
                    Text("Brilliant harmony! You reached the 2048 tile. Can you ascend even higher to 4096?")
                }
            )
        }
    }
}

@Composable
fun TileView(tile: Tile?) {
    if (tile == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.4f))
        )
    } else {
        val (bg, textCol) = getTileColors(tile.value)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp))
                .background(bg)
                .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${tile.value}",
                style = when {
                    tile.value >= 1024 -> MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black)
                    tile.value >= 128 -> MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    else -> MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                },
                color = textCol
            )
        }
    }
}

fun getTileColors(value: Int): Pair<Color, Color> {
    return when (value) {
        2 -> Tile2 to Color(0xFF1E293B)
        4 -> Tile4 to Color(0xFF0F172A)
        8 -> Tile8 to Color(0xFF0F172A)
        16 -> Tile16 to Color.White
        32 -> Tile32 to Color.White
        64 -> Tile64 to Color.White
        128 -> Tile128 to Color.White
        256 -> Tile256 to Color.White
        512 -> Tile512 to Color.White
        1024 -> Tile1024 to Color.White
        2048 -> Tile2048 to Color(0xFF0F172A)
        else -> TileSuper to Color(0xFF0F172A)
    }
}

@Composable
fun ScoreCard(
    title: String,
    value: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = accentColor
            )
        }
    }
}
