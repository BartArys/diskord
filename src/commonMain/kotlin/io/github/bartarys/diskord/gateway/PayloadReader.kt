package io.github.bartarys.diskord.gateway

import io.github.bartarys.diskord.gateway.entity.Payload
import kotlinx.io.core.ByteReadPacket

interface PayloadReader {

    suspend fun read(packet: ByteReadPacket): Payload<*>

    companion object

}

expect fun PayloadReader.Companion.default(): PayloadReader