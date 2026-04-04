package com.olamkatan

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.olamkatan.navigation.OlamKatanNavGraph
import com.olamkatan.theme.OlamKatanTheme

@Composable
fun OlamKatanApp() {
    OlamKatanTheme {
        val navController = rememberNavController()
        OlamKatanNavGraph(navController = navController)
    }
}
