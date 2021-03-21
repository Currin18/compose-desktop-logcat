package view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import model.Application
import model.Device
import model.Mode

val padding = 8.dp

@Composable
fun FiltersMenu(
    devices: List<Device>,
    applications: List<Application>,
    onDeviceFilterChanged: (Device?) -> Unit = {},
    onApplicationFilterChanged: (Application?) -> Unit,
    onModeFilterChanged: (Mode) -> Unit = {},
) {
    Row(
        modifier = Modifier.fillMaxWidth().border(1.dp, Color.Gray).padding(padding, padding * 2)
    ) {
        DeviceSelector(devices, onDeviceSelected = onDeviceFilterChanged)
        ApplicationSelector(applications, onApplicationSelected = onApplicationFilterChanged)
        ModeSelector(
            listOf(Mode.NO_FILTERS,Mode.SELECTED_APPLICATION),
            onModeSelected = onModeFilterChanged
        )
    }
}

@Composable
fun DeviceSelector(
    devices: List<Device>,
    onDeviceSelected: (Device?) -> Unit = {},
    modifier: Modifier = Modifier.padding(padding, 0.dp).width(300.dp)
) {
    Box(
        modifier = modifier
    ) {
        val deviceNotSelected = "Device not selected"

        DropDown(
            mutableListOf(deviceNotSelected) + devices.map { it.serial },
            onItemSelected = {
                if (it != 0) {
                    onDeviceSelected(devices[it - 1])
                } else {
                    onDeviceSelected(null)
                }
            },
            selectedItem = {
                if (it == 0) {
                    Text(deviceNotSelected, color = Color.Red)
                } else {
                    Text(devices[it-1].publicName)
                    Text(
                        devices[it-1].extraInfo,
                        color = Color.LightGray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(4.dp, 0.dp, 0.dp, 0.dp)
                    )
                }
            }
        ) {
            if (it == 0) {
                Text(deviceNotSelected, color = Color.Red)
            } else {
                Text(devices[it-1].publicName)
                Text(
                    devices[it-1].extraInfo,
                    color = Color.LightGray,
                    modifier = Modifier.padding(4.dp, 0.dp, 0.dp, 0.dp)
                )
            }
        }
    }
}

@Composable
fun ModeSelector(
    modes: List<Mode>,
    onModeSelected: (Mode) -> Unit = {},
    modifier: Modifier = Modifier.padding(padding, 0.dp).width(200.dp)
) {
    Box(
        modifier = modifier
    ) {
        DropDown(
            modes.map { it.value },
            onItemSelected = {
                onModeSelected(modes[it])
            },
            selectedItem = {
                Text(
                    modes[it].value,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        ) {
            Text(
                modes[it].value,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun ApplicationSelector(
    applications: List<Application>,
    onApplicationSelected: (Application?) -> Unit = {},
    modifier: Modifier = Modifier.padding(padding, 0.dp).width(300.dp)
) {
    Box(
        modifier = modifier
    ) {
        val applicationNotSelected = "Application not selected"
        DropDown(
            mutableListOf(applicationNotSelected) +applications.map { it.packageName },
            onItemSelected = {
                if (it != 0) {
                    onApplicationSelected(applications[it - 1])
                } else {
                    onApplicationSelected(null)
                }
            },
            selectedItem = {
                if (it == 0) {
                    Text(applicationNotSelected, color = Color.Red)
                } else {
                    Text(
                        applications[it-1].packageName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        ) {
            if (it == 0) {
                Text(applicationNotSelected, color = Color.Red)
            } else {
                Text(
                    applications[it-1].packageName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
fun DropDown(
    items: List<String>,
    onItemSelected: (index: Int) -> Unit,
    selectedItem: @Composable (index: Int) -> Unit,
    child: @Composable (index: Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }

    val corner = RoundedCornerShape(4.dp)
    Box(modifier = Modifier
        .wrapContentWidth()
        .wrapContentSize(Alignment.TopStart)
        .border(BorderStroke(1.dp, Color.Gray), corner)
        .clip(corner)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .clickable(onClick = { expanded = true })
                .padding(8.dp)
        ) {
            selectedItem(selectedIndex)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(400.dp)
//            offset = DpOffset(0.dp, 0.dp)
        ) {
            items.forEachIndexed { index, device ->
                DropdownMenuItem(
                    onClick = {
                        selectedIndex = index
                        expanded = false
                        onItemSelected(selectedIndex)
                    },
                ) {
                    Row(
                        modifier = Modifier.wrapContentWidth()
                    ) {
                        child(index)
                    }
                }
            }
        }
    }
}