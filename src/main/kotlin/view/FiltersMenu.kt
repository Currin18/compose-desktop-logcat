package view

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import model.Device

@Composable
fun FiltersMenu(
    devices: List<Device>,
    onDeviceFilterChange: (Device) -> Unit = {}
) {
    Row(
        modifier = Modifier.padding(16.dp)
    ) {
        DeviceSelector(devices, onDeviceSelected = onDeviceFilterChange)
    }
}

@Composable
fun DeviceSelector(
    devices: List<Device>,
    onDeviceSelected: (Device) -> Unit = {},
    modifier: Modifier = Modifier.width(200.dp)
) {
    Box(
        modifier = modifier
    ) {
        val deviceNotSelected = "Device not selected"

        DropDown(
            mutableListOf(deviceNotSelected) + devices.map { it.id },
            onItemSelected = {
                if (it != 0)
                    onDeviceSelected(devices[it-1])
            }
        ) {
            if (it == 0) {
                Text(deviceNotSelected)
            } else {
                Text("$it: ${devices[it-1].id}")
            }
        }
    }
}

@Composable
fun DropDown(
    items: List<String>,
    onItemSelected: (index: Int) -> Unit,
    child: @Composable (index: Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }

    Box(modifier = Modifier
        .wrapContentWidth()
        .wrapContentSize(Alignment.TopStart)
        .clip(RoundedCornerShape(4.dp))
    ) {
        Text(
            text = items[selectedIndex],
            modifier = Modifier.fillMaxWidth()
                .clickable(onClick = { expanded = true })
                .background(Color.Gray)
                .padding(8.dp)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.wrapContentWidth().background(Color.Red),
//            offset = DpOffset(0.dp, 0.dp)
        ) {
            items.forEachIndexed { index, device ->
                DropdownMenuItem(
                    onClick = {
                        selectedIndex = index
                        expanded = false
                        onItemSelected(selectedIndex)
                    }
                ) {
//                    Text(text = device)
                    child(index)
                }
            }
        }
    }
}