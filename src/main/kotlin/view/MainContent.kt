package view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

@Composable
fun LogList(logs: List<Log>) {
    val listState = rememberLazyListState()
    val position = remember { mutableStateOf(0) }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
    ) {

        itemsIndexed(items = logs, itemContent = { index, dataItem ->
            LogRow("$index", dataItem)
        })
    }

    /*CoroutineScope(Dispatchers.Main).launch {
//        println("size ${logs.size}, position ${position.value}, state ${listState.firstVisibleItemIndex}, offset ${listState.firstVisibleItemScrollOffset}")
        if (position.value == listState.firstVisibleItemIndex || logs.size - listState.firstVisibleItemIndex < 50) {
            listState.animateScrollToItem(logs.size)
            position.value = listState.firstVisibleItemIndex
        }
    }*/
}

@Composable
fun LogRow(index: String, log: Log) {
    Row {
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
}