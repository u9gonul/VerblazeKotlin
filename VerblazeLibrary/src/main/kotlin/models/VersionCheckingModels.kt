package models


internal data class CheckVersionRequest(
    val currentVersion:Int
)

internal data class CheckVersionResponse(
    val `data`: CheckVersionData,
    val message: String,
    val statusCode: Int
)
internal data class CheckVersionData(
    val latestVersion: Int,
    val needsUpdate: Boolean
)

