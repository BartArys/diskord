package io.github.bartarys.diskord.gateway.data

annotation class JsonName(val value: String)

expect class HelloData(heartbeatInterval: Int, trace: List<String>) {
    val heartbeatInterval: Int
    @JsonName("_trace")
    val trace: List<String>
}

expect class ReadyData(
    gatewayProtocolVersion: Int,
    user: UserData,
    privateChannels: List<Any>,
    guilds: List<UnavailableGuildData>,
    sessionId: String,
    trace: List<String>,
    shard: List<Int>?
) {
    @JsonName("v")
    val gatewayProtocolVersion: Int
    val user: UserData
    val privateChannels: List<Any>
    val guilds: List<UnavailableGuildData>
    val sessionId: String
    @JsonName("_trace")
    val trace: List<String>
    val shard: List<Int>?
}

expect class UserData(
    id: String,
    username: String,
    discriminator: String,
    avatar: String? = null,
    bot: Boolean? = null,
    twoFactorAuthenticationEnabled: Boolean? = null,
    locale: String? = null,
    verified: Boolean? = null,
    email: String? = null,
    flags: Int? = null,
    premiumType: Int? = null
) {
    val id: String
    val username: String
    val discriminator: String
    val avatar: String?
    val bot: Boolean?
    @JsonName("mfa_enabled")
    val twoFactorAuthenticationEnabled: Boolean?
    val locale: String?
    val verified: Boolean?
    val email: String?
    val flags: Int?
    @JsonName("premium_type")
    val premiumType: Int?
}

expect class UnavailableGuildData(
    id: String,
    unavailable: Boolean
) {
    val id: String
    val unavailable: Boolean
}


expect class ResumedData(
    trace: List<String>
) {
    @JsonName("_trace")
    val trace: List<String>
}

expect class IdentifyData(
    token: String,
    properties: IdentifyProperties,
    compress: Boolean? = false,
    largeThreshold: Int? = 50,
    shard: List<Int>? = null,
    presence: UpdateStatus? = null
) {
    val token: String
    val properties: IdentifyProperties
    val compress: Boolean?
    @JsonName("large_threshold")
    val largeThreshold: Int?
    val shard: List<Int>?
    val presence: UpdateStatus?
}

expect class IdentifyProperties(
    os: String,
    browser: String,
    device: String
) {
    @JsonName("\$os")
    val os: String
    @JsonName("\$browser")
    val browser: String
    @JsonName("\$device")
    val device: String
}

expect class UpdateStatus(
    status: String,
    afk: Boolean,
    since: Int?,
    game: Activity?
) {
    val status: String
    val afk: Boolean
    val since: Int?
    val game: Activity?
}

expect class Activity(
    name: String,
    type: Int,
    url: String? = null,
    timestamps: ActivityTimeStamps? = null,
    applicationId: String? = null,
    details: String? = null,
    state: String? = null,
    party: ActivityParty? = null,
    assets: ActivityAssets? = null,
    secrets: ActivitySecrets? = null,
    instance: Boolean? = null,
    flags: Int? = null
) {
    val name: String
    val type: Int
    val url: String?
    val timestamps: ActivityTimeStamps?
    val applicationId: String?
    val details: String?
    val state: String?
    val party: ActivityParty?
    val assets: ActivityAssets?
    val secrets: ActivitySecrets?
    val instance: Boolean?
    val flags: Int?
}

expect class ActivityTimeStamps(
    start: Int? = null,
    end: Int? = null
) {
    val start: Int?
    val end: Int?
}

expect class ActivityParty(
    id: String? = null,
    size: List<Int>? = null
) {
    val id: String?
    val size: List<Int>?
}

expect class ActivityAssets(
    largeImage: String? = null,
    largeText: String? = null,
    smallImage: String? = null,
    smallText: String? = null
) {
    @JsonName("large_image")
    val largeImage: String?
    @JsonName("large_text")
    val largeText: String?
    @JsonName("small_image")
    val smallImage: String?
    @JsonName("small_text")
    val smallText: String?
}

expect class ActivitySecrets(
    join: String? = null,
    spectate: String? = null,
    match: String? = null
) {
    val join: String?
    val spectate: String?
    val match: String?
}
