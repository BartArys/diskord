package io.github.bartarys.diskord.gateway.entity

import com.beust.klaxon.Json

actual open class Payload<T> actual constructor(
    @Json(name = "op")
    actual val opCode: OpCode,
    @Json(name = "d")
    actual val data: T
) {
    override fun equals(other: Any?): Boolean {
        return if (other is Payload<*>) data == other.data else false
    }

    override fun hashCode(): Int {
        return data.hashCode()
    }
}

actual open class DispatchPayload<T> actual constructor(
    opCode: OpCode,
    data: T,
    @Json(name = "s")
    actual val sequenceNumber: String?,
    @Json(name = "t")
    actual val eventName: String?
) : Payload<T>(opCode, data)
