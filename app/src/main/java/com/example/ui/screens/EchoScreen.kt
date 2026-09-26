package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games.echo.EchoCrystals
import com.example.games.echo.EchoGameState
import com.example.games.echo.EchoPlayState
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.VioletNeon

@Composable
fun EchoScreen(
    state: EchoPlayState,
    onNodeTap: (Int) -> Unit,
    onStartGame: (isFreePlay: Boolean) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("echo_screen_container")
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
                    modifier = Modifier.testTag("echo_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "ECHO RESONANCE",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        ),
                        color = VioletNeon
                    )
                    Text(
                        text = if (state.isFreePlay) "Serene Free Play" else "Melodic Memory",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = { onStartGame(state.isFreePlay) },
                    modifier = Modifier.testTag("echo_restart_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Restart",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Mode Selector: Challenge vs Free Play
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                FilterChip(
                    selected = !state.isFreePlay,
                    onClick = { onStartGame(false) },
                    label = { Text("Memory Challenge") },
                    modifier = Modifier.padding(end = 8.dp)
                )
                FilterChip(
                    selected = state.isFreePlay,
                    onClick = { onStartGame(true) },
                    label = { Text("Zen Free Play") }
                )
            }

            // Status / Score Indicator
            if (!state.isFreePlay) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "SEQUENCE: ${state.score}",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = VioletNeon
                    )
                    Text(
                        text = "BEST: ${state.bestScore}",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = CyanNeon
                    )
                }

                // Phase Banner
                val bannerText = when (state.state) {
                    EchoGameState.IDLE -> "Press Start to begin harmonic sequence"
                    EchoGameState.DEMO_PLAYING -> "Listen to the celestial resonance..."
                    EchoGameState.PLAYER_TURN -> "Your turn: Repeat the sequence (${state.playerStep}/${state.sequence.size})"
                    EchoGameState.ROUND_SUCCESS -> "Harmonious resonance! Ascending..."
                    EchoGameState.GAME_OVER -> "Melody drifted away"
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ) {
                    Text(
                        text = bannerText,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = when (state.state) {
                            EchoGameState.ROUND_SUCCESS -> EmeraldNeon
                            EchoGameState.DEMO_PLAYING -> CyanNeon
                            else -> MaterialTheme.colorScheme.onSurface
                        },
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                    )
                }
            } else {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ) {
                    Text(
                        text = "Tap the crystal bells freely to compose peaceful meditative melodies.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 6 Sacred Crystal Bell Nodes (2 rows x 3 columns)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                // Row 1 (first 3 crystals)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (i in 0..2) {
                        val crystal = EchoCrystals[i]
                        val isGlowing = state.activeNodeGlow == crystal.id
                        CrystalBellButton(
                            crystal = crystal,
                            isGlowing = isGlowing,
                            onClick = { onNodeTap(crystal.id) }
                        )
                    }
                }

                // Row 2 (next 3 crystals)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (i in 3..5) {
                        val crystal = EchoCrystals[i]
                        val isGlowing = state.activeNodeGlow == crystal.id
                        CrystalBellButton(
                            crystal = crystal,
                            isGlowing = isGlowing,
                            onClick = { onNodeTap(crystal.id) }
                        )
                    }
                }
            }

            // Start / Action button if IDLE
            if (state.state == EchoGameState.IDLE && !state.isFreePlay) {
                Button(
                    onClick = { onStartGame(false) },
                    colors = ButtonDefaults.buttonColors(containerColor = VioletNeon),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .testTag("echo_start_challenge_button")
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Begin Memory Sequence", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Game Over Dialog
        if (state.state == EchoGameState.GAME_OVER && !state.isFreePlay) {
            AlertDialog(
                onDismissRequest = {},
                confirmButton = {
                    Button(
                        onClick = { onStartGame(false) },
                        colors = ButtonDefaults.buttonColors(containerColor = VioletNeon),
                        modifier = Modifier.testTag("echo_retry_button")
                    ) {
                        Text("Try Again", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = onBack) {
                        Text("Exit to Menu")
                    }
                },
                title = {
                    Text("Sequence Dissolved", fontWeight = FontWeight.Bold)
                },
                text = {
                    Column {
                        Text("You harmonized a streak of:")
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${state.score} Notes",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = VioletNeon
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun CrystalBellButton(
    crystal: com.example.games.echo.CrystalNode,
    isGlowing: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isGlowing) 1.15f else 1.0f,
        animationSpec = tween(150),
        label = "crystal_scale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.scale(scale)
    ) {
        Box(
            modifier = Modifier
                .size(86.dp)
                .clip(CircleShape)
                .background(
                    if (isGlowing) {
                        Brush.radialGradient(
                            colors = listOf(
                                Color.White,
                                crystal.color,
                                crystal.color.copy(alpha = 0.4f)
                            )
                        )
                    } else {
                        Brush.radialGradient(
                            colors = listOf(
                                crystal.color.copy(alpha = 0.35f),
                                crystal.color.copy(alpha = 0.12f),
                                Color.Transparent
                            )
                        )
                    }
                )
                .border(
                    width = if (isGlowing) 3.dp else 1.5.dp,
                    color = if (isGlowing) Color.White else crystal.color.copy(alpha = 0.6f),
                    shape = CircleShape
                )
                .clickable(onClick = onClick)
                .testTag("echo_crystal_${crystal.id}"),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = crystal.noteName,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                    color = if (isGlowing) Color.White else crystal.color
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = crystal.name,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
