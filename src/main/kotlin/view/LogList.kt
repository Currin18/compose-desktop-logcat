package view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorXmlResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import model.Log
import utils.KeyboardUtils
import java.text.SimpleDateFormat
import java.util.*
import javax.swing.JFileChooser
import javax.swing.filechooser.FileSystemView


@Composable
fun LogList(logs: List<Log>, scrollToEnd: Boolean? = true, wrapped: Boolean = false, onItemSelected: (Int) -> Unit = {}) {
    val listState = rememberLazyListState()
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val selectedItemStart: MutableState<Int?> = remember { mutableStateOf(null) }
    val selectedItemEnd: MutableState<Int?> = remember { mutableStateOf(null) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(), // TODO: add scroll
        ) {
            itemsIndexed(items = logs, itemContent = { index, dataItem ->
                LogRow(index, dataItem, wrapped = wrapped,
                    selected = (selectedItemStart.value?.let { index in it..(selectedItemEnd.value ?: it) }) ?: false,
                    onClick = { logPosition ->
                        if (KeyboardUtils.isShiftDown) {
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
            FloatingMenu(
                counter = (selectedItemEnd.value?.minus(it) ?: 0) + 1,
                onCopy = {
                    val copy: String = logsToCopy(logs, selectedItemStart.value, selectedItemEnd.value)
                    KeyboardUtils.copyToClipboard(copy)
                },
                onDownloadToFile = {
                    JFileChooser(FileSystemView.getFileSystemView().homeDirectory).apply {
                        dialogTitle = "Choose a directory to save your file: "
                        fileSelectionMode = JFileChooser.DIRECTORIES_ONLY

                        val returnVal = showSaveDialog(null)

                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            if (selectedFile.isDirectory) {
                                val copy = logsToCopy(logs, selectedItemStart.value, selectedItemEnd.value)

                                val formatter = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")
                                val date = Date(System.currentTimeMillis())

                                val fileName = "${formatter.format(date)}_logcat.log"

                                KeyboardUtils.writeToFile("$selectedFile/$fileName", copy)
                                println("Logs saved to: $selectedFile/$fileName")
                            } else {
                                println("Not directory selected")
                            }
                        } else {
                            println("Save command cancelled by user")
                        }
                    }
                },
                onCancel = {
                    selectedItemStart.value = null
                    selectedItemEnd.value = null
                }
            )
        }
    }
}

fun logsToCopy(logs: List<Log>, fromIndex: Int?, toIndex: Int?): String {
    var copy = ""
    fromIndex?.let { itemStart ->
        val itemEnd = toIndex ?: itemStart
        if (itemStart in 0..itemEnd) {
            logs.subList(itemStart, itemEnd + 1)
                .forEach { rowLog -> copy += "${rowLog.getFormattedLog()}\n" }
        }
    }
    return copy
}

@Composable
fun LogRow(index: Int, log: Log, wrapped: Boolean = false, selected: Boolean, onClick: (Int) -> Unit = {}) {
    log.apply {
        //if (pid == 10441 || tid == 10441) { println("$date $pid-$tid/$packageName ${level.value}/$tag: $message") }

        val color: Color = when(selected) {
            true -> Color.Gray
            else -> Color.Unspecified
        }

        val modifier = Modifier.fillMaxWidth()
        var maxLines = Int.MAX_VALUE
        if (!wrapped) {
            modifier.wrapContentWidth()
            maxLines = 1
        }

        Text(
            text = getFormattedLog(),
            color = getLogColor(),
            modifier = modifier.background(color).padding(4.dp).clickable(onClick = {
//                println("LogRow onClick -> $date $pid-$tid/$packageName ${level.value}/$tag: $message")
                onClick(index)
            }),
            maxLines = maxLines,
        )
    }
}

@Composable
fun FloatingMenu(counter: Int, onCopy: () -> Unit = {}, onDownloadToFile: () -> Unit = {}, onCancel: () -> Unit = {}) {
    Row(
        modifier = Modifier.padding(16.dp).wrapContentSize().background(Color.White).border(1.dp, Color.Gray).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {


//        Spacer(modifier = Modifier.size(8.dp))
        Text("$counter ${when(counter) {
            1 -> "line"
            else -> "lines"
        }}")

        Spacer(modifier = Modifier.size(16.dp))
        IconButton(
            modifier = Modifier.then(Modifier.size(24.dp)),
            onClick = { onCopy() }
        ) { Image(
            imageVector = vectorXmlResource("icons/copy.xml"),
            contentDescription = "Copy",
            modifier = Modifier.fillMaxSize()
        ) }

        Spacer(modifier = Modifier.size(8.dp))
        IconButton(
            modifier = Modifier.then(Modifier.size(24.dp)),
            onClick = { onDownloadToFile() }
        ) { Image(
            imageVector = vectorXmlResource("icons/file_download.xml"),
            contentDescription = "Download to file",
            modifier = Modifier.fillMaxSize()
        ) }

        Spacer(modifier = Modifier.size(8.dp))
        IconButton(
            modifier = Modifier.then(Modifier.size(24.dp)),
            onClick = { onCancel() }
        ) { Icon(Icons.Filled.Close, "Cancel") }

    }
}