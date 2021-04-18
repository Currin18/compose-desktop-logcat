package view

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.vectorXmlResource
import androidx.compose.ui.unit.dp
import model.Log

@Composable
fun MainContent(logs: List<Log>, onClearLogs: () -> Unit = {}) {
    val scrollToEndState: MutableState<Boolean?> = remember { mutableStateOf(null) }
    if (logs.isNotEmpty() && scrollToEndState.value == null) scrollToEndState.value = true

    val wrappedState = remember { mutableStateOf(false) }

    Row (
        modifier = Modifier.fillMaxWidth().border(1.dp, Color.Gray)
    ) {
        LogMenu(
            scrollToEndState = scrollToEndState.value,
            wrappedState = wrappedState.value,
            onClearLogs = {
                scrollToEndState.value = null
                onClearLogs()
            },
            onScrollToEndClicked = { scrollToEndState.value = true },
            onWrappedClicked = { wrappedState.value = !wrappedState.value}
        )
        LogList(logs, scrollToEndState.value, wrappedState.value, onItemSelected = {
            scrollToEndState.value = false
        })
    }
}

@Composable
fun LogMenu(
    scrollToEndState: Boolean? = false,
    wrappedState: Boolean = false,
    onClearLogs: () -> Unit = {},
    onScrollToEndClicked: () -> Unit = {},
    onWrappedClicked: () -> Unit = {},
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

        ActionIcon(
            resource = when(scrollToEndState) {
                true -> "icons/sort.xml"
                else -> "icons/sort_ascending.xml"
            },
            contentDescription = "Scroll to end",
            //modifier = Modifier.rotate(-90f),
            isSpacerNeeded = false, onClick = { onScrollToEndClicked() })
        ActionIcon(
            resource = when(wrappedState) {
                true -> "icons/wrap.xml"
                else -> "icons/wrap_disabled.xml"
            },
            contentDescription = "Wrap content",
            onClick = { onWrappedClicked() }
        )

        Spacer(Modifier.fillMaxWidth().height(17.dp).padding(0.dp, 8.dp).background(Color.Gray))

        ActionIcon(Icons.Filled.Settings, "Settings", isSpacerNeeded = false, onClick = {})
    }
}

@Composable
fun ActionIcon(
    icon: ImageVector,
    contentDescription: String = "",
    modifier: Modifier = Modifier.fillMaxSize(),
    isSpacerNeeded: Boolean = true,
    onClick: () -> Unit = {}
) {
    ActionIconBase(
        isSpacerNeeded = isSpacerNeeded,
        onClick = onClick
    ) { Icon(icon, contentDescription, modifier) }
}

@Composable
fun ActionIcon(
    resource: String,
    contentDescription: String = "",
    modifier: Modifier = Modifier,
    isSpacerNeeded: Boolean = true,
    onClick: () -> Unit = {}
) {
    ActionIconBase(
        isSpacerNeeded = isSpacerNeeded,
        onClick = onClick
    ) { Image(
        imageVector = vectorXmlResource(resource),
        contentDescription = contentDescription,
        modifier = modifier
    ) }
}

@Composable
fun ActionIconBase(
    isSpacerNeeded: Boolean = true,
    onClick: () -> Unit = {},
    child: @Composable () -> Unit = {}
) {
    val spacerSize = 8.dp

    if (isSpacerNeeded) Spacer(Modifier.size(spacerSize))
    IconButton(
        modifier = Modifier.then(Modifier.size(24.dp)),
        onClick = { onClick() }
    ) { child() }
}

