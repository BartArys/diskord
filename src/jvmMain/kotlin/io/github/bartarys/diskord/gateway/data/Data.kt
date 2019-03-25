package io.github.bartarys.diskord.gateway.data

import com.beust.klaxon.Json

actual data class HelloData actual constructor(
    actual val heartbeatInterval: Int,
    @Json(name = "_trace")
    actual val trace: List<String>
)

actual data class ReadyData actual constructor(
    @Json(name = "v")
    actual val gatewayProtocolVersion: Int,
    actual val user: UserData,
    actual val privateChannels: List<Any>,
    actual val guilds: List<UnavailableGuildData>,
    actual val sessionId: String,
    @Json(name = "_trace")
    actual val trace: List<String>,
    actual val shard: List<Int>?
)

actual data class UserData actual constructor(
    actual val id: String,
    actual val username: String,
    actual val discriminator: String,
    actual val avatar: String?,
    actual val bot: Boolean?,
    actual val twoFactorAuthenticationEnabled: Boolean?,
    actual val locale: String?,
    actual val verified: Boolean?,
    actual val email: String?,
    actual val flags: Int?,
    actual val premiumType: Int?
)

actual data class UnavailableGuildData actual constructor(
    actual val id: String,
    actual val unavailable: Boolean
)

actual data class ResumedData actual constructor(
    @Json(name = "_trace")
    actual val trace: List<String>
)

actual data class IdentifyData actual constructor(
    actual val token: String,
    actual val properties: IdentifyProperties,
    actual val compress: Boolean?,
    @Json(name = "large_threshold")
    actual val largeThreshold: Int?,
    actual val shard: List<Int>?,
    actual val presence: UpdateStatus?
)

actual data class IdentifyProperties actual constructor(
    @Json(name = "\$os")
    actual val os: String,
    @Json(name = "\$browser")
    actual val browser: String,
    @Json(name = "\$device")
    actual val device: String
)

actual data class UpdateStatus actual constructor(
    actual val status: String,
    actual val afk: Boolean,
    actual val since: Int?,
    actual val game: Activity?
)

actual data class Activity actual constructor(
    actual val name: String,
    actual val type: Int,
    actual val url: String?,
    actual val timestamps: ActivityTimeStamps?,
    actual val applicationId: String?,
    actual val details: String?,
    actual val state: String?,
    actual val party: ActivityParty?,
    actual val assets: ActivityAssets?,
    actual val secrets: ActivitySecrets?,
    actual val instance: Boolean?,
    actual val flags: Int?
)

actual data class ActivityTimeStamps actual constructor(
    actual val start: Int?,
    actual val end: Int?
)

actual data class ActivityParty actual constructor(
    actual val id: String?,
    actual val size: List<Int>?
)

actual data class ActivityAssets actual constructor(
    @Json(name = "large_image")
    actual val largeImage: String?,
    @Json(name = "large_text")
    actual val largeText: String?,
    @Json(name = "small_image")
    actual val smallImage: String?,
    @Json(name = "small_text")
    actual val smallText: String?
)

actual data class ActivitySecrets actual constructor(
    actual val join: String?,
    actual val spectate: String?,
    actual val match: String?
)

