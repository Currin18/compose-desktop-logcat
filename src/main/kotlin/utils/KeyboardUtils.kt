package utils

import java.awt.KeyboardFocusManager
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard

import java.awt.datatransfer.StringSelection
import java.io.BufferedWriter
import java.io.FileWriter


object KeyboardUtils {
    var isShiftDown = false
        private set

    init {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher { e ->
            isShiftDown = e.isShiftDown
            false
        }
    }

    fun copyToClipboard(copy: String) {
        val stringSelection = StringSelection(copy)
        val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
        clipboard.setContents(stringSelection, null)
    }

    fun writeToFile(fileName: String, copy: String) {
        val writer = BufferedWriter(FileWriter(fileName))
        writer.write(copy)
        writer.close()
    }
}