package view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.Keyboard
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import model.Log
import utils.KeyboardListener
import java.awt.KeyboardFocusManager

@Composable
fun LogList(logs: List<Log>, scrollToEnd: Boolean? = true, scrollPosition: Int = 0, onItemSelected: (Int) -> Unit = {}) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val selectedItemStart: MutableState<Int?> = remember { mutableStateOf(null) }
    val selectedItemEnd: MutableState<Int?> = remember { mutableStateOf(null) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
        ) {
            itemsIndexed(items = logs, itemContent = { index, dataItem ->
                LogRow(index, dataItem,
                    selected = (selectedItemStart.value?.let { index in it..(selectedItemEnd.value ?: it) }) ?: false,
                    onClick = { logPosition ->
                        if (KeyboardListener.isShiftDown) {
                            selectedItemStart.value?.let { if (logPosition < it) selectedItemStart.value = logPosition }
                            selectedItemEnd.value?.let { if (logPosition > it) selectedItemEnd.value = logPosition }
                        } else {
                            selectedItemStart.value = logPosition
                            selectedItemEnd.value = logPosition
                        }
                        onItemSelected(logPosition)
                    }
                )
            })
        }

        if (scrollToEnd == true) {
            coroutineScope.launch {
                listState.scrollToItem(logs.size)
//            listState.animateScrollToItem(logs.size)
            }
        }

        selectedItemStart.value?.let {
            FAB(
                counter = (selectedItemEnd.value?.minus(it) ?: 0) + 1,
                onClose = {
                    selectedItemStart.value = null
                    selectedItemEnd.value = null
                }
            )
        }
    }
}

@Composable
fun LogRow(index: Int, log: Log, selected: Boolean, onClick: (Int) -> Unit = {}) {
    log.apply {
        if (pid == 10441 || tid == 10441) { println("$date $pid-$tid/$packageName ${level.value}/$tag: $message") }

        val color: Color = when(selected) {
            true -> Color.Gray
            else -> Color.Unspecified
        }

        Text(
            "$date $pid-$tid/$packageName ${level.value}/$tag: $message",
            color = getLogColor(),
            modifier = Modifier.fillMaxWidth().background(color).padding(4.dp).clickable(onClick = {
                println("LogRow onClick -> $date $pid-$tid/$packageName ${level.value}/$tag: $message")
                onClick(index)
            })
        )
    }
}

@Composable
fun FAB(counter: Int, onClose: () -> Unit = {}) {
    Row(
        modifier = Modifier.padding(16.dp).wrapContentSize().background(Color.White).border(1.dp, Color.Gray).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier.then(Modifier.size(24.dp)),
            onClick = { onClose() }
        ) { Icon(Icons.Filled.Close, "Close") }
        Text("$counter")
    }
}