package model

import java.util.*

class Log(
    var date: String = "",
    var pid: Int = 0,
    var tid: Int = 0,
    var packageName: String = "?",
    var level: LogLevel = LogLevel.Assert,
    var tag: String = "?",
    var message: String = ""
) {
    private val pattern = Regex("(\\d\\d-\\d\\d)\\s+(\\d\\d:\\d\\d:\\d\\d.\\d\\d\\d)\\s+(\\d+)\\s+(\\d+)\\s+([a-zA-Z])\\s+([^\\s.]+)(\\s+)?:\\s+(.*)")

    constructor(stringLog: String) : this() {
        val matchResult = pattern.find(stringLog)

        matchResult?.let {
            val match = it.groupValues


                date = "2021-${match[1]} ${match[2]}"
                pid = match[3].toInt()
                tid = match[4].toInt()
                level = LogLevel.fromValue(match[5])
                tag = match[5]
                message = match[8]
            if (message.isBlank()) {

                println(stringLog)
                println(matchResult.groupValues)

                /*println("""
                    time: ${match[1]} ${match[2]}
                    pid: ${match[3]}
                    tid: ${match[4]}
                    level: ${match[5]}
                    tag: ${match[6]}
                    message: ${match[8]}
                """)*/
            }
        }
    }

    fun print() {
        println("""
                date: $date
                pid: $pid-$tid
                packageName: $packageName
                level: $level
                tag: $tag
                message: $message
            """)
    }

    enum class LogLevel(val value: String) {
        Assert("A"),
        Debug("D"),
        Info("I"),
        Warning("W"),
        Error("E");

        companion object {
            fun fromValue(stringValue: String): LogLevel = when(stringValue) {
                "D" -> Debug
                "I" -> Info
                "W" -> Warning
                "E" -> Error
                else -> Assert
            }
        }
    }
}