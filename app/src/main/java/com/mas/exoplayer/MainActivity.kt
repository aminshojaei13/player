package com.mas.exoplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mas.exoplayer.ui.theme.PlayerTheme
import com.mas.exoplayer.ui.theme.Shapes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlayerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Gray
                ){
                Column() {
                    TopAppbar()

                    NowPlayingScreen()

                }
            }
        }
    }
}
}

@Composable
fun TopAppbar() {
    val searchValue = remember { mutableStateOf("") }

    TextField(
        modifier = Modifier
            .padding(all = 8.dp)
            .clip(Shapes.large)
            .border(width = 2.dp, shape = Shapes.large, color = Color.Gray),
        value = searchValue.value,
        onValueChange = {
            searchValue.value = it
        }
    )
}