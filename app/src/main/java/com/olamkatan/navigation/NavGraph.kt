package com.olamkatan.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.olamkatan.ui.hub.HubScreen
import com.olamkatan.ui.animals.AnimalGardenScreen
import com.olamkatan.ui.colors.ColorSplashScreen
import com.olamkatan.ui.shapes.ShapePlaygroundScreen
import com.olamkatan.ui.counting.CountingGardenScreen
import com.olamkatan.ui.music.MusicMeadowScreen
import com.olamkatan.ui.words.WordGardenScreen
import com.olamkatan.ui.myday.MyDayScreen
import com.olamkatan.ui.canvas.FreeCanvasScreen
import com.olamkatan.ui.stickers.StickerAlbumScreen
import com.olamkatan.ui.settings.SettingsScreen

object Routes {
    const val HUB = "hub"
    const val ANIMALS = "animals"
    const val COLORS = "colors"
    const val SHAPES = "shapes"
    const val COUNTING = "counting"
    const val MUSIC = "music"
    const val WORDS = "words"
    const val MY_DAY = "myday"
    const val CANVAS = "canvas"
    const val STICKERS = "stickers"
    const val SETTINGS = "settings"
}

@Composable
fun OlamKatanNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.HUB
    ) {
        composable(Routes.HUB) {
            HubScreen(
                onIslandClick = { route -> navController.navigate(route) },
                onSettingsClick = { navController.navigate(Routes.SETTINGS) },
                onStickersClick = { navController.navigate(Routes.STICKERS) }
            )
        }
        composable(Routes.ANIMALS) {
            AnimalGardenScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.COLORS) {
            ColorSplashScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.SHAPES) {
            ShapePlaygroundScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.COUNTING) {
            CountingGardenScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.MUSIC) {
            MusicMeadowScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.WORDS) {
            WordGardenScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.MY_DAY) {
            MyDayScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.CANVAS) {
            FreeCanvasScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.STICKERS) {
            StickerAlbumScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}
