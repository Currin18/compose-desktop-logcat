package model

import androidx.compose.ui.graphics.Color
import style.*
import java.util.*

class Log(
    var date: String = "",
    var pid: Int = 0,
    var tid: Int = 0,
    var packageName: String = "?",
    var level: LogLevel = LogLevel.Verbose,
    var tag: String = "?",
    var message: String = ""
) {
    private val pattern = Regex("(\\d\\d-\\d\\d)\\s+(\\d\\d:\\d\\d:\\d\\d.\\d\\d\\d)\\s+(\\d+)\\s+(\\d+)\\s+([a-zA-Z])\\s+([^\\s.]+)(\\s+)?:\\s+(.*)")

    constructor(stringLog: String, packageList: List<Application> = listOf()) : this() {
        val matchResult = pattern.find(stringLog)

        matchResult?.let { result ->
            val match = result.groupValues

            date = "2021-${match[1]} ${match[2]}"
            pid = match[3].toInt()
            tid = match[4].toInt()
            level = LogLevel.fromValue(match[5])
            tag = match[6]
            message = match[8]

            packageName = packageList.find { it.uid == pid.toString() }?.packageName ?: "?"

            if (message.isBlank()) {
                println(stringLog)
                println(result.groupValues)
            }
        }
    }

    fun getFormattedLog(): String {
        return "$date $pid-$tid/$packageName ${level.value}/$tag: $message"
    }

    fun getLogColor(): Color = when(level) {
        LogLevel.Debug -> LogDebug
        LogLevel.Info -> LogInfo
        LogLevel.Warning -> LogWarning
        LogLevel.Error -> LogError
        LogLevel.Assert -> LogAssert
        else -> Color.Unspecified
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
        Verbose("V"),
        Debug("D"),
        Info("I"),
        Warning("W"),
        Error("E"),
        Assert("A");

        companion object {
            fun fromValue(stringValue: String): LogLevel = when(stringValue) {
                "D" -> Debug
                "I" -> Info
                "W" -> Warning
                "E" -> Error
                "A" -> Assert
                else -> Verbose
            }
        }
    }
}