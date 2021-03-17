import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import core.ADB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import model.Device
import view.FiltersMenu

val devices = listOf(
    Device("device 1"),
    Device("device 2"),
    Device("device 3"),
    Device("device 4"),
    Device("device 5"),
)

fun main() = Window {
    var deviceList by remember { mutableStateOf(listOf<Device>()) }
    var deviceFilter by remember { mutableStateOf(Device()) }

    MaterialTheme {
        Column {
            FiltersMenu(deviceList,
                onDeviceFilterChange = {
                    deviceFilter = it
                }
            )
            Text("Device: ${deviceFilter.id}")
        }
    }

    GlobalScope.launch(Dispatchers.IO) {
        val devicesString = ADB.devices()
        deviceList = devicesString.map { Device(it.split("\t")[0]) }
    }
}