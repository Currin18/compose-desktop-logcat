import androidx.compose.desktop.Window
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import core.ADB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import model.Application
import model.Device
import model.Mode
import view.FiltersMenu

fun main() = Window {
    var deviceList by remember { mutableStateOf(listOf<Device>()) }
    var applicationList by remember { mutableStateOf(listOf<Application>()) }
    var deviceFilter: Device? by remember { mutableStateOf(Device()) }
    var applicationFilter: Application? by remember { mutableStateOf(Application()) }
    var modeFilter by remember { mutableStateOf(Mode.NO_FILTERS) }

    var logs by remember { mutableStateOf(listOf<String>()) }

    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize()) {
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
                                logs = logs.toMutableList() + line
                            }
                        }
                    }
                },
                onApplicationFilterChanged = { applicationFilter = it },
                onModeFilterChanged = { modeFilter = it }
            )

            Text("Device: ${deviceFilter?.publicName ?: ""} ${deviceFilter?.extraInfo ?: ""}")
            Text("Application: ${applicationFilter?.packageName ?: ""}")
            Text("Mode: ${modeFilter.value}")

            LazyColumn() {
                items(logs) { log ->
                    Text(log)
                }
            }
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
    return ADB.getDebuggableApplications(serial).map { Application(it.key) }
}

fun startLogCat(serial: String, callback: (String) -> Unit) {
    ADB.startLogCat(serial, callback)
}