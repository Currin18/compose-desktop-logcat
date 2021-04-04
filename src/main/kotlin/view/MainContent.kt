package view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import model.Log

@Composable
fun MainContent(logs: List<Log>) {
    Row (
        modifier = Modifier.fillMaxWidth().border(1.dp, Color.Gray)
    ) {
        LogMenu()
        LogList(logs)
    }
}

@Composable
fun LogMenu() {
    Column(
        modifier = Modifier.fillMaxHeight().background(Color.Red).padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            modifier = Modifier.then(Modifier.size(24.dp)),
            onClick = {}
        ) {
            Icon(
                Icons.Filled.ArrowBack,
                "Go to Bottom",
                tint = Color.White,
                modifier = Modifier.rotate(-90f)
            )
        }
    }
}

