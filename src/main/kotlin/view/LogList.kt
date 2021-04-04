package view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.Log
import style.*

@Composable
fun LogList(logs: List<Log>) {
    val listState = rememberLazyListState()
    val position = remember { mutableStateOf(0) }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
    ) {

        itemsIndexed(items = logs, itemContent = { index, dataItem ->
            LogRow(dataItem, index)
        })
    }

    CoroutineScope(Dispatchers.Main).launch {
//        println("size ${logs.size}, position ${position.value}, state ${listState.firstVisibleItemIndex}, offset ${listState.firstVisibleItemScrollOffset}")
        if (position.value == listState.firstVisibleItemIndex || logs.size - listState.firstVisibleItemIndex < 50) {
            listState.animateScrollToItem(logs.size)
            position.value = listState.firstVisibleItemIndex
        }
    }
}

@Composable
fun LogRow(log: Log, index: Int = 0) {

    log.apply {
        if (pid == 10441 || tid == 10441) { println("$date $pid-$tid/$packageName ${level.value}/$tag: $message") }
        Text(
            "$date $pid-$tid/$packageName ${level.value}/$tag: $message",
            color = getLogColor(),
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )
    }

    /*
    Row(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
        Text(index, Modifier.width(50.dp).background(Color.Red))
        log.apply {
            Text(date.toString(), Modifier.width(250.dp).background(Color.Blue))
            Text("${pid}-${tid}", Modifier.width(100.dp).background(Color.Red))
            Text(packageName, Modifier.width(50.dp).background(Color.Blue))
            Text(level.value, Modifier.width(50.dp).background(Color.Red))
            Text(tag, Modifier.width(100.dp).background(Color.Blue))
            Text(message, Modifier.fillMaxWidth().background(Color.Red))
        }
    }
    */
}