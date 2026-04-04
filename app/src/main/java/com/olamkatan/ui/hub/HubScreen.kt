package com.olamkatan.ui.hub

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.olamkatan.theme.SkyBlue

@Composable
fun HubScreen(
    onIslandClick: (String) -> Unit,
    onSettingsClick: () -> Unit,
    onStickersClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SkyBlue),
        contentAlignment = Alignment.Center
    ) {
        Text("Hub World")
    }
}
