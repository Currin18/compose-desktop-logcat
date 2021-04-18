import androidx.compose.desktop.Window
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import core.ADB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import model.Application
import model.Device
import model.Log
import model.Mode
import view.FiltersMenu
import view.MainContent
import view.padding

fun main() = Window {
    var deviceList by remember { mutableStateOf(listOf<Device>()) }
    var applicationList by remember { mutableStateOf(listOf<Application>()) }
    var deviceFilter: Device? by remember { mutableStateOf(Device()) }
    var applicationFilter: Application? by remember { mutableStateOf(Application()) }
    var modeFilter by remember { mutableStateOf(Mode.NO_FILTERS) }

    var logs by remember { mutableStateOf(listOf<Log>()) }

    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxWidth().border(1.dp, Color.Gray).padding(padding, padding * 2)) {
                Text("Device: ${deviceFilter?.publicName ?: ""} ${deviceFilter?.extraInfo ?: ""}")
                Text("Application: ${applicationFilter?.packageName ?: ""}")
                Text("Mode: ${modeFilter.value}")
            }

            FiltersMenu(
                devices = deviceList,
                applications = applicationList,
                onDeviceFilterChanged = { it ->
                    deviceFilter = it
                    deviceFilter?.let { device ->
                        GlobalScope.launch(Dispatchers.IO) {
                            applicationList = loadApplications(device.serial)
                        }

                        GlobalScope.launch(Dispatchers.IO) {
                            startLogCat(device.serial) { line ->
//                                println(line)
                                Log(line, applicationList).let {
                                    if (it.message.isNotBlank())
                                        logs = logs.toMutableList() + it
                                }
                            }
                        }
                    }
                },
                onApplicationFilterChanged = { applicationFilter = it },
                onModeFilterChanged = { modeFilter = it }
            )

            MainContent(logs, onClearLogs = { logs = listOf() })
        }
    }

    GlobalScope.launch(Dispatchers.IO) {
        deviceList = loadDevices()
    }
}

fun loadDevices(): List<Device> {
    val devices = ADB.getDevices().map { Device(it.split("\t")[0]) }

    devices.forEach {
        it.putExtra(ADB.getDeviceInfo(it.serial))
    }

    return devices
}

fun loadApplications(serial: String): List<Application> {
    return ADB.getDebuggableApplications(serial).map { app ->
        var uid = ""
        var gid = ""
        var groups = ""
        var context = ""
        app.value.forEach {
            when {
                it.contains("uid=") -> {
                    uid = it.replace(Regex("uid=|\\(*\\)"), "")
                }
                it.contains("gid=") -> {
                    gid = it.replace(Regex("gid=|\\(*\\)"), "")
                }
                it.contains("groups=") -> {
                    groups = it.replace(Regex("groups="), "")
                }
                it.contains("context=") -> {
                    context = it.replace(Regex("context="), "")
                }
            }
        }
        Application(app.key, uid, gid, groups, context)
    }
}

fun startLogCat(serial: String, callback: (String) -> Unit) {
    ADB.startLogCat(serial, callback)
}