package view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import model.Log

@Composable
fun MainContent(logs: List<Log>, onClearLogs: () -> Unit = {}) {
    val scrollToEndState: MutableState<Boolean?> = remember { mutableStateOf(null) }
    if (logs.isNotEmpty() && scrollToEndState.value == null) scrollToEndState.value = true

    Row (
        modifier = Modifier.fillMaxWidth().border(1.dp, Color.Gray)
    ) {
        LogMenu(
            onClearLogs = {
                scrollToEndState.value = null
                onClearLogs()
            },
            onScrollToEndClicked = { scrollToEndState.value = true },
        )
        LogList(logs, scrollToEndState.value, onItemSelected = {
            scrollToEndState.value = false
        })
    }
}

@Composable
fun LogMenu(
    onClearLogs: () -> Unit = {},
    onScrollToEndClicked: () -> Unit = {},
) {

    Column(
        modifier = Modifier.fillMaxHeight().width(32.dp)
//            .background(Color.Red)
            .border(1.dp, Color.Gray)
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        ActionIcon(Icons.Filled.Delete, "Clear", isSpacerNeeded = false, onClick = { onClearLogs() })

        Spacer(Modifier.fillMaxWidth().height(17.dp).padding(0.dp, 8.dp).background(Color.Gray))

        ActionIcon(Icons.Filled.ArrowBack, "Scroll to end", modifier = Modifier.rotate(-90f),
            isSpacerNeeded = false, onClick = { onScrollToEndClicked() })
        ActionIcon(Icons.Filled.List, "Wrap content", onClick = {})

        Spacer(Modifier.fillMaxWidth().height(17.dp).padding(0.dp, 8.dp).background(Color.Gray))

        ActionIcon(Icons.Filled.Settings, "Settings", isSpacerNeeded = false, onClick = {})
    }
}

@Composable
fun ActionIcon(
    icon: ImageVector,
    contentDescription: String = "",
    modifier: Modifier = Modifier,
    isSpacerNeeded: Boolean = true,
    onClick: () -> Unit = {}
) {
    val spacerSize = 8.dp

    if (isSpacerNeeded) Spacer(Modifier.size(spacerSize))
    IconButton(
        modifier = Modifier.then(Modifier.size(24.dp)),
        onClick = { onClick() }
    ) { Icon(icon, contentDescription, modifier) }
}

