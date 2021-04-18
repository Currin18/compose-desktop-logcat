package model

object Constants {
    const val resourcesFolder = "./src/main/resources"

    fun getProjectPath(): String = System.getProperty("user.dir")
}