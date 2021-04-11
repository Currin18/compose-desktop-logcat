package view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import model.Log

@Composable
fun LogList(logs: List<Log>, scrollToEnd: Boolean? = true, onItemSelected: (Log) -> Unit = {}) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
    ) {
        itemsIndexed(items = logs, itemContent = { _, dataItem ->
            LogRow(dataItem, onClick = { log ->
                onItemSelected(log)
            })
        })
    }

    if (scrollToEnd == true) {
        coroutineScope.launch {
                listState.scrollToItem(logs.size)
//            listState.animateScrollToItem(logs.size)
        }
    }
}

@Composable
fun LogRow(log: Log, onClick: (Log) -> Unit = {}) {
    log.apply {
        if (pid == 10441 || tid == 10441) { println("$date $pid-$tid/$packageName ${level.value}/$tag: $message") }
        Text(
            "$date $pid-$tid/$packageName ${level.value}/$tag: $message",
            color = getLogColor(),
            modifier = Modifier.fillMaxWidth().padding(4.dp).clickable(onClick = {
                onClick(log)
            })
        )
    }
}