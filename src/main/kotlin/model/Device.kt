package model

class Device(
    var serial: String = "",
    var build_date: String? = null,
    var build_date_utc: String? = null,
    var build_fingerprint: String? = null,
    var build_id: String? = null,
    var build_tags: String? = null,
    var build_type: String? = null,
    var build_version_incremental: String? = null,
    var build_version_release: String? = null,
    var build_version_sdk: String? = null,
    var device: String? = null,
    var first_api_level: String? = null,
    var locale: String? = null,
    var manufacturer: String? = null,
    var model: String? = null,
    var name: String? = null,
) {
    val publicName: String
        get() = if (manufacturer != null) "$manufacturer $model" else serial
    val extraInfo: String
        get() = if (build_version_release != null) "Android $build_version_release, API $build_version_sdk" else ""

    fun putExtra(extra: List<String>) {
        extra.forEach { when {
            it.contains("[ro.product.build.date]") ->
                build_date = it.split(": [")[1].replace("]", "")
            it.contains("[ro.product.build.date.utc]") ->
                build_date_utc = it.split(": [")[1].replace("]", "")
            it.contains("[ro.product.build.fingerprint]") ->
                build_fingerprint = it.split(": [")[1].replace("]", "")
            it.contains("[ro.product.build.id]") ->
                build_id = it.split(": [")[1].replace("]", "")
            it.contains("[ro.product.build.tags]") ->
                build_tags = it.split(": [")[1].replace("]", "")
            it.contains("[ro.product.build.type]") ->
                build_type = it.split(": [")[1].replace("]", "")
            it.contains("[ro.product.build.version.incremental]") ->
                build_version_incremental = it.split(": [")[1].replace("]", "")
            it.contains("[ro.product.build.version.release]") ->
                build_version_release = it.split(": [")[1].replace("]", "")
            it.contains("[ro.product.build.version.sdk]") ->
                build_version_sdk = it.split(": [")[1].replace("]", "")
            it.contains("[ro.product.device]") ->
                device = it.split(": [")[1].replace("]", "")
            it.contains("[ro.product.first_api_level]") ->
                first_api_level = it.split(": [")[1].replace("]", "")
            it.contains("[ro.product.locale]") ->
                locale = it.split(": [")[1].replace("]", "")
            it.contains("[ro.product.manufacturer]") ->
                manufacturer = it.split(": [")[1].replace("]", "")
            it.contains("[ro.product.model]") ->
                model = it.split(": [")[1].replace("]", "")
            it.contains("[ro.product.name]") ->
                name = it.split(": [")[1].replace("]", "")
        }}
    }
}
