package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.EchoScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LuminaScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.StatsScreen
import com.example.ui.screens.ZenMergeScreen
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.ZenithTheme

class MainActivity : ComponentActivity() {
    private val viewModel: ZenithViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()
            val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
            val isSoundOn by viewModel.isSoundEnabled.collectAsStateWithLifecycle()
            val isHapticsOn by viewModel.isHapticsEnabled.collectAsStateWithLifecycle()

            ZenithTheme(darkTheme = isDarkTheme) {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("zenith_main_scaffold"),
                    bottomBar = {
                        // Show bottom navigation on main tabs: HOME, STATS, SETTINGS
                        if (currentScreen in listOf(Screen.HOME, Screen.STATS, Screen.SETTINGS)) {
                            ZenithBottomNav(
                                currentScreen = currentScreen,
                                onSelect = { viewModel.navigateTo(it) }
                            )
                        }
                    }
                ) { innerPadding ->
                    AnimatedContent(
                        targetState = currentScreen,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        label = "screen_transition"
                    ) { screen ->
                        when (screen) {
                            Screen.HOME -> {
                                val luminaHigh by viewModel.luminaHighScore.collectAsStateWithLifecycle()
                                val zenMergeHigh by viewModel.zenMergeHighScore.collectAsStateWithLifecycle()
                                val echoHigh by viewModel.echoHighScore.collectAsStateWithLifecycle()

                                HomeScreen(
                                    luminaHighScore = luminaHigh ?: 1,
                                    zenMergeHighScore = zenMergeHigh ?: 0,
                                    echoHighScore = echoHigh ?: 0,
                                    onNavigate = { viewModel.navigateTo(it) },
                                    onSoundToggle = { viewModel.toggleSound() },
                                    isSoundOn = isSoundOn
                                )
                            }
                            Screen.LUMINA -> {
                                val luminaState by viewModel.luminaState.collectAsStateWithLifecycle()
                                LuminaScreen(
                                    state = luminaState,
                                    onCellClick = { r, c -> viewModel.onLuminaCellClick(r, c) },
                                    onReset = { viewModel.resetLuminaLevel() },
                                    onNextLevel = { viewModel.nextLuminaLevel() },
                                    onSelectLevel = { viewModel.selectLuminaLevel(it) },
                                    onBack = { viewModel.navigateBack() }
                                )
                            }
                            Screen.ZEN_MERGE -> {
                                val zenMergeState by viewModel.zenMergeState.collectAsStateWithLifecycle()
                                ZenMergeScreen(
                                    state = zenMergeState,
                                    canUndo = true,
                                    onMove = { dir -> viewModel.onZenMergeMove(dir) },
                                    onUndo = { viewModel.onZenMergeUndo() },
                                    onReset = { viewModel.resetZenMerge() },
                                    onBack = { viewModel.navigateBack() }
                                )
                            }
                            Screen.ECHO -> {
                                val echoState by viewModel.echoState.collectAsStateWithLifecycle()
                                EchoScreen(
                                    state = echoState,
                                    onNodeTap = { nodeId -> viewModel.onEchoNodeTap(nodeId) },
                                    onStartGame = { isFreePlay -> viewModel.startEchoGame(isFreePlay) },
                                    onBack = { viewModel.navigateBack() }
                                )
                            }
                            Screen.STATS -> {
                                val records by viewModel.allRecords.collectAsStateWithLifecycle()
                                val totalGames by viewModel.totalGamesPlayed.collectAsStateWithLifecycle()
                                val luminaHigh by viewModel.luminaHighScore.collectAsStateWithLifecycle()
                                val zenMergeHigh by viewModel.zenMergeHighScore.collectAsStateWithLifecycle()
                                val echoHigh by viewModel.echoHighScore.collectAsStateWithLifecycle()

                                StatsScreen(
                                    records = records,
                                    totalGames = totalGames,
                                    luminaHigh = luminaHigh ?: 1,
                                    zenMergeHigh = zenMergeHigh ?: 0,
                                    echoHigh = echoHigh ?: 0,
                                    onBack = { viewModel.navigateBack() }
                                )
                            }
                            Screen.SETTINGS -> {
                                SettingsScreen(
                                    isDarkTheme = isDarkTheme,
                                    isSoundOn = isSoundOn,
                                    isHapticsOn = isHapticsOn,
                                    onToggleTheme = { viewModel.toggleTheme() },
                                    onToggleSound = { viewModel.toggleSound() },
                                    onToggleHaptics = { viewModel.toggleHaptics() },
                                    onClearData = { viewModel.clearAllData() },
                                    onBack = { viewModel.navigateBack() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ZenithBottomNav(
    currentScreen: Screen,
    onSelect: (Screen) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        modifier = Modifier
            .navigationBarsPadding()
            .testTag("zenith_bottom_nav")
    ) {
        NavigationBarItem(
            selected = currentScreen == Screen.HOME,
            onClick = { onSelect(Screen.HOME) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Arcade") },
            label = { Text("Arcade") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CyanNeon,
                selectedTextColor = CyanNeon,
                indicatorColor = CyanNeon.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_home")
        )
        NavigationBarItem(
            selected = currentScreen == Screen.STATS,
            onClick = { onSelect(Screen.STATS) },
            icon = { Icon(Icons.Default.Leaderboard, contentDescription = "Records") },
            label = { Text("Records") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CyanNeon,
                selectedTextColor = CyanNeon,
                indicatorColor = CyanNeon.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_records")
        )
        NavigationBarItem(
            selected = currentScreen == Screen.SETTINGS,
            onClick = { onSelect(Screen.SETTINGS) },
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CyanNeon,
                selectedTextColor = CyanNeon,
                indicatorColor = CyanNeon.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_settings")
        )
    }
}
