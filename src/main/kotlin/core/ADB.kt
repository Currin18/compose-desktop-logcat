package core

import java.io.IOException
import java.io.BufferedReader
import java.io.InputStreamReader


object ADB {
    private const val binaryPath = "./src/main/resources/platform-tools/adb"

    fun devices(): List<String> {
        var s: String?
        val devices: MutableList<String> = mutableListOf()

        try {

            val rt: Runtime = Runtime.getRuntime()
            val ps: Process = rt.exec("$binaryPath devices")

            val stdInput = BufferedReader(InputStreamReader(ps.inputStream))

            val stdError = BufferedReader(InputStreamReader(ps.errorStream))

            // read the output from the command

            // read the output from the command
            println("Here is the standard output of the command:\n")
            while (stdInput.readLine().also { s = it } != null) {
                println(s)
                s?.let {
                    if (it.contains(regex = Regex("(.*)\\tdevice"))) {
                        devices.add(it)
                    }
                }
            }

            // read any errors from the attempted command

            // read any errors from the attempted command
            println("Here is the standard error of the command (if any):\n")
            while (stdError.readLine().also { s = it } != null) {
                println(s)
            }
        } catch (e: IOException) {
            println("exception happened - here's what I know: ")
            e.printStackTrace()
        }

        return devices.toList()
    }
}