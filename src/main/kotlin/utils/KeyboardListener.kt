package utils

import java.awt.KeyboardFocusManager

object KeyboardListener {
    var isShiftDown = false
        private set

    init {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher { e ->
            isShiftDown = e.isShiftDown
            false
        }
    }
}