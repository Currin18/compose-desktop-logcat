package core

import java.io.IOException
import java.io.BufferedReader
import java.io.InputStreamReader


object ADB {
    private const val binaryPath = "./src/main/resources/platform-tools/adb"

    fun getDevices(): List<String> {
        val devices: MutableList<String> = mutableListOf()

        processCommand("$binaryPath devices", true).forEach {
            if (it.contains(regex = Regex("(.*)\\tdevice"))) {
                devices.add(it)
            }
        }

        return devices.toList()
    }

    fun getDeviceInfo(serial: String): List<String> {
        return processCommand("$binaryPath -s $serial shell getprop | grep product", false)
    }

    fun getDebuggableApplications(serial: String): Map<String, List<String>> {
        val debuggableApplications: MutableMap<String, List<String>> = mutableMapOf()
        val applications = processCommand("$binaryPath -s $serial shell pm list packages | cut -d : -f 2")
        applications.forEach {
            val idList = processCommand("$binaryPath -s $serial shell run-as $it id", false)
            if (idList.isNotEmpty()) {
                println("$it: $idList")
                debuggableApplications[it] = idList
            }
        }
        return debuggableApplications.toMap()
//        return processCommand("for p in \$($binaryPath -s $serial shell pm list packages | cut -d : -f 2); " +
//                "do ($binaryPath -s $serial shell run-as \$p id >/dev/null 2>&1 && echo \$p); done", true)
    }

    fun startLogCat(serial: String, callback: (String) -> Unit = {}) {
        processCommand("$binaryPath -s $serial logcat -v threadtime > ./tmp/$serial.log", false, callback)
    }

    private fun processCommand(command: String, debugMode: Boolean = false, callback: (String) -> Unit = {}): List<String> {
        var s: String?
        val stringList: MutableList<String> = mutableListOf()

        try {

            val rt: Runtime = Runtime.getRuntime()
            val ps: Process = rt.exec(command)

            val stdInput = BufferedReader(InputStreamReader(ps.inputStream))

            val stdError = BufferedReader(InputStreamReader(ps.errorStream))

            // read the output from the command

            // read the output from the command
            if (debugMode)
                println("Here is the standard output of the command:\n")
            while (stdInput.readLine().also { s = it } != null) {
                s?.let {
                    if (it != "--------- beginning of system") {
                        stringList.add(it)
                        callback(it)
                    }
                    if (debugMode)
                        println(it)
                }
            }

            // read any errors from the attempted command

            // read any errors from the attempted command
            if (debugMode)
                println("Here is the standard error of the command (if any):\n")
            while (stdError.readLine().also { s = it } != null) {
                if (debugMode)
                    println(s)
            }
        } catch (e: IOException) {
            println("exception happened - here's what I know: ")
            e.printStackTrace()
        }

        return stringList.toList()
    }


}